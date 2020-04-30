package com.example.cricketapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cricketapp.adapters.macthadapter
import com.example.cricketapp.dataclasses.match
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_matchhistory.*

class matchhistory : AppCompatActivity() {

    lateinit var ref:DatabaseReference
    lateinit var matchlist:MutableList<match>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matchhistory)
       ref=FirebaseDatabase.getInstance().getReference("Matches")
       matchlist= mutableListOf()
        val  lm= LinearLayoutManager(this)
        lm.orientation = RecyclerView.VERTICAL
        matchrecyclerview.layoutManager=lm

        ref.addValueEventListener(object :com.google.firebase.database.ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p: DataSnapshot) {
                if (p!!.exists()) {
                    matchlist.clear()
                    for (h in p.children) {

                        val m = h.getValue(match::class.java)
                        matchlist.add(m!!)

                    }
                    val addapter = macthadapter(this@matchhistory, matchlist)
                    matchrecyclerview .adapter = addapter
                }}


        })

    }
}
