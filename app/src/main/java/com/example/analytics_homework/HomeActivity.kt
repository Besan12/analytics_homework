package com.example.analytics_homework

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class HomeActivity : AppCompatActivity() {
    val analytics = AnalyticsModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Firebase.firestore
        val categories = ArrayList<CategoryModel>()
        val categoryAdapter = CategoryAdapter( this, categories)
        list_home.adapter = categoryAdapter
        analytics.screenView("HomeActivity", "Home")

        db.collection("categories")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    categories.add(
                        CategoryModel(
                            document.getLong("id")!!.toInt(),
                            document.getString("name").toString(),
                            document.getString("image").toString(),
                        )
                    )
                    Log.d("getCategories", "${document.id} => ${document.data}")
                }
                categoryAdapter.notifyDataSetChanged()

                if (categories.isEmpty()) {
                    progressBar.isIndeterminate = true
                    progressBar.visibility = View.VISIBLE
                } else {
                    progressBar.isIndeterminate = false
                    progressBar.visibility = View.GONE
                }
            }
            .addOnFailureListener { exception ->
                Log.w("getCategories", "Error getting documents.", exception)
            }


    if (categories.isEmpty()) {
        progressBar.isIndeterminate = true
        progressBar.visibility = View.VISIBLE
    } else {
        progressBar.isIndeterminate = false
        progressBar.visibility = View.GONE
    }

    list_home.setOnItemClickListener { parent, view, position, id ->
        val catId = categories[position].id
        analytics.selectContent("${categories[position].id}","${categories[position].name}","CatCard")
        val intent = Intent(this, NotesActivity::class.java)
        intent.putExtra("catId", catId)
        startActivity(intent)
    }
    }

    override fun onPause() {
        super.onPause()
        analytics.screenView("HomeActivity", "Home")
    }

    override fun onResume() {
        super.onResume()
        analytics.trackScreenTime()
    }
}