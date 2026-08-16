package com.example.a24012011009_mad_assignment_1

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {



    private val documentPicker = registerForActivityResult(ActivityResultContracts.OpenDocument()){uri ->
        if (uri != null){
            Toast.makeText(this, "Document Selected!", Toast.LENGTH_SHORT).show()
        }
    }

    /*
    * here registerForActivityResult creates a document picker that can return the file the user selects.
    * ActivityResultContracts.OpenDocument() tells the Android Studio to open the system document/file picker.
    * if (uri != null) Checks whether the user actually selected a file
    */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnAddDocument = findViewById<Button>(R.id.btnAddDocument) // Gets the "+ Add Document" button from XML.

        btnAddDocument.setOnClickListener { // Runs the code when the user clicks the button.
            documentPicker.launch(arrayOf("*/*")) // Opens the file picker and allows the user to choose a file
        }

    }
}