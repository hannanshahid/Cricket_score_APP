package com.example.cricketapp.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.cricketapp.R
import com.example.cricketapp.dataclasses.match
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.match_history_layout.view.*

class macthadapter(val context:Context,val matches:List<match> ):RecyclerView.Adapter< macthadapter.myviewholder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myviewholder {
        val view=LayoutInflater.from(context).inflate(R.layout.match_history_layout,parent,false)
        return myviewholder(view)
    }

    override fun getItemCount(): Int {
        return matches.size

    }
    override fun onBindViewHolder(holder: macthadapter.myviewholder, position: Int) {
        val h=matches[position]
        holder.setdata(h)
    }

    inner class myviewholder(itemView: View):RecyclerView.ViewHolder(itemView) {
        fun setdata(ho: match?)
        {
            itemView.teamaname.text = ho!!.teamAname
            itemView.teambname.text = ho!!.teamBname
            itemView.teamascore.text = "Score:\n${ho!!.TeamAscore}"
            itemView.teambscore.text = "Score:\n${ho!!.TeamBscore}"
            itemView.teamaover.text = "Score:\n${ho!!.TeamAover}"
            itemView.teambover.text = "Score:\n${ho!!.TeamBover}"
            itemView.matchresult.text = ho!!.MatchResult
            itemView.matchdate.text = ho!!.Date
            itemView.imgdelet.setOnClickListener {
                var ref = FirebaseDatabase.getInstance().getReference("Matches")
                val buildr = AlertDialog.Builder(context)
                buildr.setTitle("Deleteing...")
                buildr.setMessage("Are You Sure To delete \n    ${ho.teamAname} vs ${ho.teamBname} \n data ? ")
                buildr.setPositiveButton("yes", object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {

                        ref.child(ho.Matchid).removeValue().addOnCompleteListener {

                            Toast.makeText(context, "Match data Deleted ", Toast.LENGTH_LONG).show()
                        }
                    }
                })
                buildr.setNegativeButton("No",object : DialogInterface.OnClickListener
                {
                    override fun onClick(p0: DialogInterface?, p1: Int) {

                    }
                })
              var a= buildr.create()
                a.show()
                var b=a.getButton(AlertDialog.BUTTON_NEGATIVE)
                b.setOnClickListener {
                    a.dismiss()
                }

            }
            itemView.imgShare.setOnClickListener {
                var detail:String="Team A : ${ho.teamAname} \nScore: ${ho.TeamAscore} \nOvers:${ho.TeamAover} \n\n" +
                        "Team B : ${ho.teamBname} \n" +
                        "Score: ${ho.TeamBscore} \n" +
                        "Overs:${ho.TeamBover} \n" +
                        "\nMatch Result:${ho.MatchResult}" +
                        "\n Match Date:${ho.Date}\n\nShared By Cricizz App\n Developed by Hannan Shahid "
                var data=detail
                var i= Intent()
                i.action=Intent.ACTION_SEND
                i.putExtra(Intent.EXTRA_TEXT,data)
                i.setType("text/plain")
                context.startActivity(Intent.createChooser(i,"Select to Share:"))
            }

        }
    }
}