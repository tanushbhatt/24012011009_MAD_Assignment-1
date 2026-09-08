package com.example.a24012011009_mad_assignment_1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val recyclerDocuments = findViewById<RecyclerView>(R.id.recyclerDocuments)

        val dbHelper = DatabaseHelper(this)

        if (dbHelper.getDocuments().isEmpty()) {
            dbHelper.insertDocument(
                "Receipt.jpg",
                "Image",
                "16-09-2026"
            )
        }

        val documents = dbHelper.getDocuments()

        recyclerDocuments.layoutManager = LinearLayoutManager(this)
        recyclerDocuments.adapter = DocumentAdapter(documents)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}