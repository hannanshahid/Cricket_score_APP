package com.example.cricketapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_details.*

class details : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)



        //texteditors
        val fteam=findViewById<EditText>(R.id.firstteam)
        val steam=findViewById<EditText>(R.id.secondteam)
        val over=findViewById<EditText>(R.id.overs)

        button.setOnClickListener {

         if(fteam.text.toString().isEmpty() )
         {
             Toast.makeText(this,"Enter First Team Name",Toast.LENGTH_SHORT).show()

         }
           else if(steam.text.toString().isEmpty())
            {
                Toast.makeText(this,"Enter Second Team Name",Toast.LENGTH_SHORT).show()

            }
         else if(over.text.toString().isEmpty())
         {
             Toast.makeText(this,"Enter Overs",Toast.LENGTH_SHORT).show()

         }
            else
         {
             val i=Intent(this,MainMenu::class.java)
             i.putExtra("fteam",fteam.text.toString())
             i.putExtra("steam",steam.text.toString())
             i.putExtra("over",over.text.toString())
             startActivity(i)
         }
        }
    }
}
