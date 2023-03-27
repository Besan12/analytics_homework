package com.example.analytics_homework

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.note_homework.NoteModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_note.*

class NotesActivity : AppCompatActivity() {
    val analytics = AnalyticsModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        val db = Firebase.firestore
        val notes = ArrayList<NoteModel>()
        val noteAdapter = NoteAdapter( this, notes)
        list_notes.adapter = noteAdapter
        val catId = intent.getIntExtra("catId", 0)
        analytics.screenView("NoteActivity", "Notes")

        if (catId != 0) {
            db.collection("notes")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if (document.getLong("cat_id")?.toInt() == catId)
                            notes.add(
                                NoteModel(
                                    document.getLong("id")!!.toInt(),
                                    document.getString("name").toString(),
                                    document.getString("content").toString(),
                                    document.getLong("letters")!!.toInt(),
                                    document.getString("image").toString(),
                                    document.getLong("cat_id")!!.toInt(),
                                )
                            )
                        Log.d("getNotes", "${document.id} => ${document.data}")
                    }
                    noteAdapter.notifyDataSetChanged()

                    if (notes.isEmpty()) {
                        progressBarNote.isIndeterminate = true
                        progressBarNote.visibility = View.VISIBLE
                    } else {
                        progressBarNote.isIndeterminate = false
                        progressBarNote.visibility = View.GONE
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("getNotes", "Error getting documents.", exception)
                }
        }


        if (notes.isEmpty()) {
            progressBarNote.isIndeterminate = true
            progressBarNote.visibility = View.VISIBLE
        } else {
            progressBarNote.isIndeterminate = false
            progressBarNote.visibility = View.GONE
        }

        list_notes.setOnItemClickListener { parent, view, position, id ->
            val note = notes[position]
            analytics.selectContent("${notes[position].id}","${notes[position].name}","NoteCard")
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("note", note)
            startActivity(intent)
        }
    }
    override fun onPause() {
        super.onPause()
        analytics.screenView("NoteActivity", "Notes")
    }

    override fun onResume() {
        super.onResume()
        analytics.trackScreenTime()
    }
}