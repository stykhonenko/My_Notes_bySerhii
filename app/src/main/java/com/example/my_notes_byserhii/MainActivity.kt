package com.example.my_notes_byserhii

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.widget.SimpleCursorAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var db: SQLiteDatabase? = null
    var cursor:Cursor? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Open the NoteDetails activity
        fabAddNote.setOnClickListener {
            openNoteDetailsActivity(0)
        }

        listViewNotes.setOnItemClickListener { parent, view, position, id ->

            openNoteDetailsActivity(id)
        }
    }

    fun openNoteDetailsActivity(noteId:Long)
    {
        val intent = Intent(this, NoteDetails::class.java)
        intent.putExtra("NOTE_ID", noteId)
        startActivity(intent)
    }


    override fun onStart() {
        super.onStart()

        // Open the NoteDetails activity
        fabAddNote.setOnClickListener{
            val intent = Intent(this,NoteDetails::class.java)
            startActivity(intent)
        }

        // Creating the data source
        var objToCreateDB = MyNotesSQLiteOpenHelper(this)   // Create the database and its table
        db = objToCreateDB.readableDatabase  // Access to the database

        cursor = db!!.query("NOTES", arrayOf("_id","title"),  // Read data
            null,null,null,null,null)

        // Adapter
        val listAdapter = SimpleCursorAdapter(this,
            android.R.layout.simple_list_item_1,
            cursor,
            arrayOf("title"),
            intArrayOf(android.R.id.text1), 0
        )

        // Set adapter view
        listViewNotes.adapter = listAdapter
    }


    override fun onDestroy() {
        super.onDestroy()
        cursor!!.close()
        db!!.close()
    }
}

