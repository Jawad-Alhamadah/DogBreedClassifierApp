package com.example.helloworld

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton


class AnimalClassificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_classification)
        var myImage= findViewById(R.id.imageView) as ImageView
        var intent = intent
        var imageString = intent.getStringExtra("Image Uri")
        var wikipediaLink = "https://wikipedia.org/"+ intent.getStringExtra("link")
        var breed = intent.getStringExtra("breed")
        var floatingActionButton= findViewById(R.id.backButton) as FloatingActionButton

        System.out.println(wikipediaLink)

        var wikipediaTextView = findViewById(R.id.wikipediaLink) as Button
        var classificationTextView = findViewById(R.id.ClassificationText) as TextView
        var imageUri = Uri.parse(imageString)
        myImage.setImageURI(imageUri)
       // wikipediaTextView.text = wikipediaLink
        classificationTextView.text = breed

        classificationTextView.animate().apply{
            duration= 5000
            alpha(0.1f)
        }
        wikipediaTextView.setOnClickListener{
            System.out.println(wikipediaLink)
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(wikipediaLink)))
        }

        floatingActionButton.setOnClickListener{

            val intent = Intent(this@AnimalClassificationActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }


    }
}