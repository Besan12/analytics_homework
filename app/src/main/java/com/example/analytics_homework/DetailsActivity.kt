package com.example.analytics_homework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.note_homework.NoteModel
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {
    val analytics = AnalyticsModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        analytics.screenView("DetailsActivity", "Details")
        var note = intent.getParcelableExtra<NoteModel>("note")
        name.text = note!!.name
        content.text = note.content
        nofLetters.text = note.letters.toString()

        val imageRef = FirebaseStorage.getInstance().reference
        val storageRef = imageRef.child(note.image.toString())

        storageRef.downloadUrl.addOnSuccessListener { uri ->
            val imageUrl = uri.toString()
            Picasso.with(this).load(imageUrl).into(noteImage)
            Log.d("success", "get image success")
        }.addOnFailureListener { exception ->
            Log.w("error", "Error getting image.", exception)
        }
    }
    override fun onResume() {
        super.onResume()
        analytics.screenView("DetailsActivity", "Details")
    }

    override fun onPause() {
        super.onPause()
        analytics.trackScreenTime()
    }
}