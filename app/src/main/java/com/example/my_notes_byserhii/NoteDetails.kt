package com.example.my_notes_byserhii

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_note_details.*

class NoteDetails : AppCompatActivity() {

    var db:SQLiteDatabase? = null
    var noteId:Int = 0
    var cursor: Cursor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_details)

        val myNotesDatabaseHelper = MyNotesSQLiteOpenHelper(this)
        db = myNotesDatabaseHelper.writableDatabase

        noteId = intent.extras.get("NOTE_ID").toString().toInt()

        // Code that reads a Note Title and Description that its _id is equal to the noteId
        if (noteId != 0)
        {
            cursor = db!!.query("NOTES", arrayOf("TITLE", "DESCRIPTION"),"_id=?",
                arrayOf(noteId.toString()),
                null,null,null
            )

            if (cursor!!.moveToFirst() == true)
            {
                editTextTitle.setText(cursor!!.getString(0))
                editTextDescription.setText(cursor!!.getString(1))
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if(item!!.itemId == R.id.save_note) {
            // Inserting a new note
            val newNoteValues = ContentValues()

            if (editTextTitle.text.isEmpty() == true)
            {
                newNoteValues.put("TITLE", "Untitled")
            }
            else
            {
                newNoteValues.put("TITLE" , editTextTitle.text.toString())
            }

            newNoteValues.put("DESCRIPTION" , editTextDescription.text.toString())

            db!!.insert("NOTES",null,newNoteValues)

            Toast.makeText(this,"Note Saved!",Toast.LENGTH_SHORT).show()

            // Empty the Edit texts
            editTextTitle.setText("")
            editTextDescription.setText("")

            // Shifting focus to the editTextTitle
            editTextTitle.requestFocus()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.note_details_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onDestroy() {

        super.onDestroy()

        db!!.close()
    }
}
