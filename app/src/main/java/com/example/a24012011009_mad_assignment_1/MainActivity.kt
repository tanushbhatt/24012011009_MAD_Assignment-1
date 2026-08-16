package com.example.a24012011009_mad_assignment_1

import android.content.Intent
import android.net.Uri
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

    private lateinit var tvDocumentName: TextView
    private lateinit var tvDocumentType: TextView

    private val documentPicker = registerForActivityResult(ActivityResultContracts.OpenDocument()){uri ->
        if (uri != null){
            try {
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: SecurityException){
                // Some file providers may not support persistent permission
            }

            val documentName = getFileName(uri)
            val documentType = contentResolver.getType(uri) ?: "Unknown File"

            tvDocumentName.text = documentName
            tvDocumentType.text = documentType

            val sharedPreferences = getSharedPreferences("VaultBoxData", MODE_PRIVATE)

            sharedPreferences.edit()
                .putString("documentUri", uri.toString())
                .putString("documentName", documentName)
                .putString("documentType", documentType)
                .apply()


            Toast.makeText(
                this,
                "Document Saved!",
                Toast.LENGTH_SHORT
            ).show()

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

        tvDocumentName = findViewById(R.id.tvDocumentName)
        tvDocumentType = findViewById(R.id.tvDocumentType)

        val btnAddDocument = findViewById<Button>(R.id.btnAddDocument) // Gets the "+ Add Document" button from XML.

        btnAddDocument.setOnClickListener { // Runs the code when the user clicks the button.
            documentPicker.launch(arrayOf("*/*")) // Opens the file picker and allows the user to choose a file
        }


        val btnViewDocument = findViewById<Button>(R.id.btnViewDocument)
        btnViewDocument.setOnClickListener {
            val sharedPreferences = getSharedPreferences("VaultBoxData", MODE_PRIVATE)
            val savedUri = sharedPreferences.getString("documentUri", null)

            if (savedUri != null){
                val uri = android.net.Uri.parse(savedUri)
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(
                        uri,
                        contentResolver.getType(uri) ?: "*/*"
                    )
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                startActivity(intent)
            }
            else{
                Toast.makeText(
                    this,
                    "No Document Saved!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        val sharedPreferences = getSharedPreferences("VaultBoxData", MODE_PRIVATE)

        val savedName = sharedPreferences.getString("documentName", null)
        val savedType = sharedPreferences.getString("documentType", null)

        if (savedName != null){
            tvDocumentName.text = savedName
        }

        if (savedType != null){
            tvDocumentType.text = savedType
        }
    }

    private fun getFileName(uri: android.net.Uri): String {

        var fileName = "Unknown Document"

        val cursor = contentResolver.query(
            uri,
            null,
            null,
            null,
            null
        )

        cursor?.use {
            val nameIndex = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)

            if (nameIndex != -1 && it.moveToFirst()) {
                fileName = it.getString(nameIndex)
            }
        }

        return fileName
    }
}