package com.example.analytics_homework

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details.view.*
import kotlinx.android.synthetic.main.activity_details.view.noteImage
import kotlinx.android.synthetic.main.card.view.*

class CategoryAdapter (var context: Context, var categories: ArrayList<CategoryModel>): BaseAdapter() {
    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    override fun getCount(): Int {
        return categories.size
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     * data set.
     * @return The data at the specified position.
     */
    override fun getItem(position: Int): Any {
        return categories[position]
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * [android.view.LayoutInflater.inflate]
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position The position of the item within the adapter's data set of the item whose view
     * we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     * is non-null and of an appropriate type before using. If it is not possible to convert
     * this view to display the correct data, this method can create a new view.
     * Heterogeneous lists can specify their number of view types, so that this View is
     * always of the right type (see [.getViewTypeCount] and
     * [.getItemViewType]).
     * @param parent The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val root = LayoutInflater.from(parent?.context).inflate(R.layout.card, parent, false)
        root.title.text = categories[position].name
        //image
        val imageRef = FirebaseStorage.getInstance().reference
        val storageRef = imageRef.child(categories[position].image!!)
        storageRef.downloadUrl.addOnSuccessListener { uri ->
            val imageUrl = uri.toString()
            Picasso.with(context).load(imageUrl).into(root.image)
            Log.d("getCatImage", "get image success")
        }
            .addOnFailureListener { exception ->
                Log.w("getCatImage", "get image failed", exception)
            }
        return root
    }
}