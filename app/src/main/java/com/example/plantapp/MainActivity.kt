package com.example.plantapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val spinner=findViewById<Spinner>(R.id.modSpinner)
        val mods=ArrayList<String>()
        mods.add("Medical focus")
        mods.add("Cooking focus")
        mods.add("Botanic focus")
        val adapter : ArrayAdapter<String>
        adapter=ArrayAdapter(this,android.R.layout.simple_spinner_item,mods)
        spinner.adapter=adapter
    }
}