package com.example.a24012011009_mad_assignment_1

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context):
    SQLiteOpenHelper(context, "VaultBox.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE documents (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "type TEXT," +
                    "date TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS documents")
        onCreate(db)
    }

    fun insertDocument(name: String, type: String, date: String) {
        val db = writableDatabase
        val values = android.content.ContentValues()

        values.put("name", name)
        values.put("type", type)
        values.put("date", date)

        db.insert("documents", null, values)
        db.close()
    }

    fun getDocuments(): ArrayList<String> {

        val documents = ArrayList<String>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT name FROM documents",
            null
        )

        while (cursor.moveToNext()) {
            documents.add(cursor.getString(0))
        }

        cursor.close()
        db.close()
        return documents
    }

}