package com.example.cricketapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_feedback.*

class feedback : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        button2.setOnClickListener{

            Toast.makeText(this,"Thanks For Feedback",Toast.LENGTH_LONG).show()

        }
    }
}
