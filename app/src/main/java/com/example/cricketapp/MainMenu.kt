package com.example.cricketapp

import android.content.Context
import android.content.Intent
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.View
import androidx.annotation.IntegerRes
import androidx.annotation.RequiresApi
import androidx.coordinatorlayout.widget.CoordinatorLayout
import kotlinx.android.synthetic.main.content_main_menu.*
import java.text.NumberFormat
import android.content.Context.MODE_PRIVATE
import android.content.DialogInterface
import android.os.*
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.cricketapp.dataclasses.match
import com.google.android.gms.ads.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.IOException
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.contracts.Returns


class MainMenu : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {



    public var  ball:Int=0
    public var totaloverss:Int=0
     public var currentoverA:Int=0
     public var currentoverB:Int=0
     public var statusa:Boolean= false
     public var statusb:Boolean= false
    public var data:String=""
    public var teamatotalballs:Int=0
    public var teambtotalballs:Int=0
    lateinit var teamarr:TextView
    lateinit var teambrr:TextView
     lateinit var ref:DatabaseReference
     lateinit var matchstatus:String
     var savestatus:Boolean=false
     lateinit var matchidtodatabase:String
    private lateinit var mInterstitialAd: InterstitialAd

    lateinit var matchlist:MutableList<match>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        /////////get data from another activity///////////////////



         val adsHandler = object : Handler(Looper.getMainLooper()){
            override fun handleMessage(msg: Message?) {
               mInterstitialAd.show()
            }
        }

           fun scheduleAd(){
            adsHandler.sendEmptyMessageDelayed(0, 20_000)
        }
        MobileAds.initialize(this) {}

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-4777915220636073/4206388760"
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.setAdListener(object : AdListener() {
          override  fun onAdLoaded() {
              Toast.makeText(this@MainMenu,"Ad loded",Toast.LENGTH_LONG).show()
              scheduleAd()
            }

            override fun onAdFailedToLoad(p0: Int) {

                Toast.makeText(this@MainMenu,"Ad fail to load : $p0",Toast.LENGTH_LONG).show()
            }
           override fun onAdClosed() {
                // reschedule
               mInterstitialAd.loadAd(AdRequest.Builder().build())
                scheduleAd()
            }
        })



        ball=0
        matchstatus=""
        matchlist= mutableListOf()

        ref=FirebaseDatabase.getInstance().getReference("Matches")

        var key= ref.push().key
        matchidtodatabase=key!!
        val savetodatabase=findViewById<Button>(R.id.savetodatabase)
        val tAs=findViewById<Switch>(R.id.teamaswitch)
        val tBs=findViewById<Switch>(R.id.teambswitch)

        val clayout=findViewById<CoordinatorLayout>(R.id.clayout)
         teamarr=findViewById<TextView>(R.id.teamarr)
         teambrr=findViewById<TextView>(R.id.teambrr)
        val b:Bundle?=intent.extras
        val fteam=b?.getString("fteam")
        val steam=b?.getString("steam")
        val over=b?.getString("over")
         totaloverss=Integer.parseInt(over.toString())


        //////////////////////////////////////////
        teamaname.text=fteam
        teambname.text=steam
         totalovera.text=over
        totaloverb.text=over

        teamaout.text="0"
        teambout.text="0"
        teamaovers.text="0"
        teambover.text="0"
        teamaballs.text="0"
        teambball.text="0"
        teamascore.text="0"
        teambscore.text="0"
        teamarr.text="0.0"
        teambrr.text="0.0"


        /////////////////////////////////////////
        val toolbar: Toolbar = this.findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)





        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            if (mInterstitialAd.isLoaded) {
                mInterstitialAd.show()
            } else {
                Log.d("TAGi", "The interstitial wasn't loaded yet.")
            }
         var detail:String="Team A : ${teamaname.text} \n Score: ${teamascore.text}/${teamaout.text} \n Overs:${teamaovers.text}.${teamaballs.text}/${totalovera.text} \n\n" +
                 "Team B : ${teambname.text} \n" +
                 " Score: ${teambscore.text}${teambout.text} \n" +
                 " Overs:${teambover.text}.${teambball.text}/${totaloverb.text} \n\n Shared By Cricizz App\n Developed by Hannan Shahid "
             data=detail
            var i=Intent()
            i.action=Intent.ACTION_SEND
            i.putExtra(Intent.EXTRA_TEXT,data)
            i.setType("text/plain")
            startActivity(Intent.createChooser(i,"Share To: "))
            Snackbar.make(view, "Sharing ScoreCard...... ", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        tAs.setOnClickListener{
           statusa =if(tAs.isChecked)
            {
                ball=0
                true

            }
            else
            {

                ball=0
                false
            }
        }
        tBs.setOnClickListener {
            statusb = if (tBs.isChecked) {

                ball=0
                true
            } else {

                ball=0
                false
            }
        }
        btn1s.setOnClickListener {
                if (statusa == true && statusb == true) {
                    Snackbar.make(clayout, "One Team bat At a Time : Choose One", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                } else if (statusa==true)
                {
                    teamascores(1)
                    teamaball()
                }else if(statusb==true){
                    teambscores(1)
                    teambball()
                }else { }
            }
        btn2s.setOnClickListener{
             if (statusa == true && statusb == true) {
                 Snackbar.make(clayout, "One Team bat At a Time : Choose One", Snackbar.LENGTH_LONG)
                     .setAction("Action", null).show()
             } else if (statusa==true)
             {
                teamascores(2)
                 teamaball()
             }else if(statusb==true){
                 teambscores(2)
                 teambball()
             }else { }
         }
        btn4s.setOnClickListener{
    if (statusa == true && statusb == true) {
        Snackbar.make(clayout, "One Team bat At a Time : Choose One", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    } else if (statusa==true)
    {

        teamascores(4)
        teamaball()
    }else if(statusb==true){
        teambscores(4)
        teambball()
    }else { }
}
        btn6s.setOnClickListener{
    if (statusa == true && statusb == true) {
        Snackbar.make(clayout, "One Team bat At a Time : Choose One", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    } else if (statusa==true)
    {
        teamascores(6)
        teamaball()
    }else if(statusb==true){
        teambscores(6)
        teambball()
    }else { }
}
        btnout.setOnClickListener{
    if (statusa == true && statusb == true) {
        Snackbar.make(clayout, "One Team bat At a Time : Choose One", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    } else if (statusa==true)
    {

        var s=Integer.parseInt(teamaout.text.toString())
        if(s<10) {
           s=s+1
            teamaout.text = s.toString()
            teamaball()
        }
        else{Toast.makeText(this,"All Team Is Out",Toast.LENGTH_LONG).show()}
    }else if(statusb==true){
        var s=Integer.parseInt(teambout.text.toString())
        if(s<10)
        {   s=s+1
            teambout.text = s.toString()
            teambball()
        }
        else
        {
            Toast.makeText(this,"All Team Is Out",Toast.LENGTH_LONG).show()
        }
    }else { }

}
        btnmissball.setOnClickListener{
            if (statusa == true && statusb == true) {
                Snackbar.make(clayout, "One Team bat At a Time : Choose One", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            } else if (statusa==true)
            {

                teamaball()
            }else if(statusb==true){

                teambball()
            }else { }
        }
        btnwideball.setOnClickListener{
       if (statusa == true && statusb == true) {
           Snackbar.make(clayout, "One Team bat At a Time : Choose One", Snackbar.LENGTH_LONG)
               .setAction("Action", null).show()
       } else if (statusa==true)
       {

           teamascores(1)

       }else if(statusb==true){
           teambscores(1)

       }else { }
   }
        btnnoball.setOnClickListener{

            if (statusa == true && statusb == true) {
                Snackbar.make(clayout, "One Team bat At a Time : Choose One", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            } else if (statusa==true)
            {
                teamascores(1)

            }else if(statusb==true){
               teambscores(1)

            }else { }
        }
        btnrevision.setOnClickListener {
    var b =AlertDialog.Builder(this)
    val l=LayoutInflater.from(this)
    val v=l.inflate(R.layout.overthrow_run_layout,null)
    val textbox=v.findViewById<EditText>(R.id.textbox)
    b.setTitle("OverThrow")
    b.setView(v)
    b.setPositiveButton("OK",object :DialogInterface.OnClickListener
    {
        override fun onClick(p0: DialogInterface?, p1: Int) {


        }

    })
    b.setNegativeButton("Cancel",object :DialogInterface.OnClickListener
    {
        override fun onClick(p0: DialogInterface?, p1: Int) {
        }


    })
    val a=b.create()
    a.show()
   val pb =a.getButton(AlertDialog.BUTTON_POSITIVE)
   val bn=a.getButton(AlertDialog.BUTTON_NEGATIVE)
    pb.setOnClickListener {
        if(textbox.text.isNotEmpty()) {
            if (statusa == true && statusb == true) {
                Snackbar.make(clayout, "One Team bat At a Time : Choose One", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            } else if (statusa == true) {
                teamascores(Integer.parseInt(textbox.text.toString().trim()))
                a.dismiss()
            } else if (statusb == true) {
                teambscores(Integer.parseInt(textbox.text.toString().trim()))
               a.dismiss()
            } else {
            }
        }else
        {
            textbox.setError("Enter Runs")
            textbox.requestFocus()

        }

    }
    bn.setOnClickListener {
        a.dismiss()
    }


}
        btn3s.setOnClickListener {
             if (statusa == true && statusb == true) {
                 Snackbar.make(clayout, "One Team bat At a Time : Choose One", Snackbar.LENGTH_LONG)
                     .setAction("Action", null).show()
             } else if (statusa==true)
             {
                 teamascores(3)
                 teamaball()
             }else if(statusb==true){
                 teambscores(3)
                 teambball()
             }else { }
         }
        savetodatabase.setOnClickListener {
            var date=getcurrentdate()
           var matchid=matchidtodatabase
            if(savestatus==false)
            {

                if (matchstatus.length != 0) {
                    var tas = "${teamascore.text.toString()}/${teamaout.text.toString()}"
                    var tao =
                        "${teamaovers.text.toString()}.${teamaballs.text.toString()}/${totalovera.text.toString()}"
                    var tbs = "${teambscore.text.toString()}/${teambout.text.toString()}"
                    var tbo = "${teambover.text.toString()}.${teambball.text.toString()}/${totaloverb.text.toString()}"
                    var m = match(
                        matchid!!,
                        teamaname.text.toString(),
                        tas,
                        tao,
                        teambname.text.toString(),
                        tbs,
                        tbo,
                        matchstatus,
                        date
                    )

                    if (matchid != null) {
                        ref.child(matchid).setValue(m).addOnCompleteListener {
                            Toast.makeText(this, "Match Saved", Toast.LENGTH_LONG).show()
                            if (mInterstitialAd.isLoaded) {
                                mInterstitialAd.show()

                            } else {
                                Log.d("TAG", "The interstitial wasn't loaded yet.")
                            }
                        }
                        savestatus = true
                    }
                }
                else {
                    var builder = android.app.AlertDialog.Builder(this)
                    builder.setTitle("Uncomplete Match Save")
                    builder.setMessage("Are You Sure TO Save This Uncomplete Match ")

                    builder.setPositiveButton("save", object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int)
                        {
                            var tas = "${teamascore.text.toString()}/${teamaout.text.toString()}"
                            var tao =
                                "${teamaovers.text.toString()}.${teamaballs.text.toString()}/${totalovera.text.toString()}"
                            var tbs = "${teambscore.text.toString()}/${teambout.text.toString()}"
                            var tbo = "${teambover.text.toString()}.${teambball.text.toString()}/${totaloverb.text.toString()}"

                            var m = match(
                                matchid!!,
                                teamaname.text.toString(),
                                tas,
                                tao,
                                teambname.text.toString(),
                                tbs,
                                tbo,
                                matchstatus,date)


                            if (matchid != null) {
                                ref.child(matchid).setValue(m).addOnCompleteListener{
                                    Toast.makeText(this@MainMenu, "Match Saved", Toast.LENGTH_LONG).show()
                                    if (mInterstitialAd.isLoaded) {
                                        mInterstitialAd.show()

                                    } else {
                                        Log.d("TAG", "The interstitial wasn't loaded yet.")
                                    }
                                }
                                savestatus = true

                            }

                        }


                    })
                    builder.setNegativeButton("Dismiss", object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                        }

                    })
                    var a = builder.create()
                    a.show()
                   var b:Button=a.getButton(AlertDialog.BUTTON_NEGATIVE)
                    b.setOnClickListener {
                        a.dismiss()
                    }
                }
            }
            else
            {
                var builder = android.app.AlertDialog.Builder(this)
                builder.setTitle("Update MAtch Score")
                builder.setMessage("This Match Saved Already...\n Are You Want To Update Score ")

                builder.setPositiveButton("Update", object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int)
                    {
                        var tas = "${teamascore.text.toString()}/${teamaout.text.toString()}"
                        var tao =
                            "${teamaovers.text.toString()}.${teamaballs.text.toString()}/${totalovera.text.toString()}"
                        var tbs = "${teambscore.text.toString()}/${teambout.text.toString()}"
                        var tbo = "${teambover.text.toString()}.${teambball.text.toString()}/${totaloverb.text.toString()}"

                        var m = match(
                            matchidtodatabase!!,
                            teamaname.text.toString(),
                            tas,
                            tao,
                            teambname.text.toString(),
                            tbs,
                            tbo,
                            matchstatus,
                        date
                        )

                        if (matchid != null)
                        {
                            ref.child(matchidtodatabase!!).setValue(m).addOnCompleteListener{
                                Toast.makeText(this@MainMenu, "Match Score Update", Toast.LENGTH_LONG).show()
                                if (mInterstitialAd.isLoaded) {
                                    mInterstitialAd.show()

                                } else {
                                    Log.d("TAG", "The interstitial wasn't loaded yet.")
                                }
                            }
                            savestatus = true

                        }

                    }


                })
                builder.setNegativeButton("Dismiss", object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                    }

                })
                var a = builder.create()
                a.show()
                var b:Button=a.getButton(AlertDialog.BUTTON_NEGATIVE)
                b.setOnClickListener {
                    a.dismiss()
                }
            }



        }

    }

    private fun getcurrentdate(): String {
        val sdf=SimpleDateFormat("dd/M/yyyy hh:mm")
        val d=sdf.format(Date())
        return d
    }

    private fun teamascores( score:Int=0)
    {
         if(teamascore.text.toString().toInt() > teambscore.text.toString().toInt())
            { if(  teambscore.text.toString().toInt()!=0) {
                Toast.makeText(this, "${teamaname.text.toString()} Win's Match", Toast.LENGTH_LONG).show()
                matchstatus = "${teamaname.text.toString()} Win's the Match"
            }
                else
                  {
                      if (currentoverA < totaloverss) {
                          val s = Integer.parseInt(teamascore.text.toString()) + score
                          teamascore.text = s.toString()
                          teamarunrate()
                      } else
                      {
                      }

                  }
            }
            else
            {
                if (currentoverA < totaloverss) {
                    val s = Integer.parseInt(teamascore.text.toString()) + score
                    teamascore.text = s.toString()
                    teamarunrate()
                }
                else
                {
                    if(teambscore.text.toString().toInt()!=0) {
                        Toast.makeText(this, "${teambname.text.toString()} Win's Match", Toast.LENGTH_LONG).show()
                        matchstatus = "${teambname.text.toString()} Win's the Match"
                    }
                }

            }
    }
    private fun teambscores( score:Int=0) {
        if(teambscore.text.toString().toInt() > teamascore.text.toString().toInt())
        {
            if(teamascore.text.toString().toInt()!=0) {
                Toast.makeText(this, "${teambname.text.toString()} Win's Match", Toast.LENGTH_LONG).show()
                matchstatus = "${teambname.text.toString()} Win's the Match"
            }
           else
            {
                if (currentoverB < totaloverss) {
                    val s = Integer.parseInt(teambscore.text.toString()) + score
                    teambscore.text = s.toString()
                    teambrunrate()
                } else
                {

                }
            }
        }
        else
        {
            if (currentoverB < totaloverss) {
                val s = Integer.parseInt(teambscore.text.toString()) + score
                teambscore.text = s.toString()
                teambrunrate()
            } else
            {
                if(teamascore.text.toString().toInt()!=0) {
                    Toast.makeText(this, "${teamaname.text.toString()} Win's Match", Toast.LENGTH_LONG).show()
                    matchstatus = "${teamaname.text.toString()} Win's the Match"
                }
            }

        }
    }
    private fun teamaball() {
        if(teamascore.text.toString().toInt() > teambscore.text.toString().toInt())
        {
            if( teambscore.text.toString().toInt()!=0)
            {

            }
            else
            {    var ov = Integer.parseInt(teamaovers.text.toString())
                var ba = Integer.parseInt(teamaballs.text.toString())
                if (currentoverA < totaloverss)
                {
                    teamatotalballs = teamatotalballs + 1
                    if (ball == 5 || ba == 5) {
                        teamaovers.text = (ov + 1).toString()
                        currentoverA = currentoverA + 1
                        ball = 0
                        teamaballs.text = "0"
                    } else {
                        teamaballs.text = (ba + 1).toString()
                        ball = ball + 1
                    }
                    teamarunrate()
                }
                else
                {
                    if(  teambscore.text.toString().toInt()!=0)
                    {
                        Toast.makeText(this, "${teamaname.text.toString()} Win's Match", Toast.LENGTH_LONG).show()
                        matchstatus = "${teamaname.text.toString()} Win's the Match"
                    }
                    else
                    {
                    Toast.makeText(this, "Over's is complete this is ${teambname.text} Turn", Toast.LENGTH_LONG).show()
                    teambswitch.isChecked = false
                    statusa = false}
                }
            }



        }
        else {
            var ov = Integer.parseInt(teamaovers.text.toString())
            var ba = Integer.parseInt(teamaballs.text.toString())
            if (currentoverA < totaloverss) {
                teamatotalballs = teamatotalballs + 1
                if (ball == 5 || ba == 5) {
                    teamaovers.text = (ov + 1).toString()
                    currentoverA = currentoverA + 1
                    ball = 0
                    teamaballs.text = "0"
                } else {
                    teamaballs.text = (ba + 1).toString()
                    ball = ball + 1
                }
                teamarunrate()
            }
            else
            {
                if(  teambscore.text.toString().toInt()!=0)
                {
                    Toast.makeText(this, "${teambname.text.toString()} Win's Match", Toast.LENGTH_LONG).show()
                    matchstatus = "${teambname.text.toString()} Win's the Match"
                }
                else {
                    Toast.makeText(this, "Over's is complete this is ${teambname.text} Turn", Toast.LENGTH_LONG).show()
                    teambswitch.isChecked = false
                    statusa = false
                }
                }
        }

    }

    private fun teambball()
    {
        if(teambscore.text.toString().toInt() > teamascore.text.toString().toInt())
    {
        if( teamascore.text.toString().toInt()!=0){}
        else
        {var ov = Integer.parseInt(teambover.text.toString())
            var ba = Integer.parseInt(teambball.text.toString())
            if (currentoverB < totaloverss) {
                teambtotalballs = teambtotalballs + 1

                if (ball == 5 || ba == 5) {
                    teambover.text = (ov + 1).toString()
                    currentoverB = currentoverB + 1
                    ball = 0
                    teambball.text = "0"
                } else {
                    teambball.text = (ba + 1).toString()
                    ball = ball + 1
                }
                teambrunrate()
            } else {
                if(  teamascore.text.toString().toInt()!=0)
                {
                    Toast.makeText(this, "${teambname.text.toString()} Win's Match", Toast.LENGTH_LONG).show()
                    matchstatus = "${teambname.text.toString()} Win's the Match"
                }
                else{

                Toast.makeText(this, "Over's is complete this is ${teamaname.text} Turn", Toast.LENGTH_LONG).show()

                teamaswitch.isChecked = false
                statusb = false}
            }
        }

    }
        else {
        var ov = Integer.parseInt(teambover.text.toString())
        var ba = Integer.parseInt(teambball.text.toString())
        if (currentoverB < totaloverss) {
            teambtotalballs = teambtotalballs + 1

            if (ball == 5 || ba == 5) {
                teambover.text = (ov + 1).toString()
                currentoverB = currentoverB + 1
                ball = 0
                teambball.text = "0"
            } else {
                teambball.text = (ba + 1).toString()
                ball = ball + 1
            }
              teambrunrate()
        } else {
            if(  teamascore.text.toString().toInt()!=0)
            {
                Toast.makeText(this, "${teamaname.text.toString()} Win's Match", Toast.LENGTH_LONG).show()
                matchstatus = "${teambname.text.toString()} Win's the Match"
            }
            else {
                Toast.makeText(this, "Over's is complete this is ${teamaname.text} Turn", Toast.LENGTH_LONG).show()

                teamaswitch.isChecked = false
                statusb = false
            }
        }
    }
    }

    private fun teamarunrate()
    {
        var s:Double = teamascore.text.toString().toDouble()
       if(teamascore.text=="0" ||  teamaballs.text=="0")
       {
           teamarr.text="0.0"
       }
       else
       {
           var a:Double =s*6.0
           var c = a / teamatotalballs
           teamarr.text="%.1f".format(c)
       }
       }
    private fun teambrunrate()
    {

        val s = teambscore.text.toString().toDouble()
        if(teambscore.text=="0" ||  teambball.text=="0")
        {
            teambrr.text="0.0"
        }
        else
        {
            var a:Double =s*6.0
            var c = a / teambtotalballs
            teambrr.text="%.1f".format(c)
        }
    }
    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                var clayout=findViewById<CoordinatorLayout>(R.id.clayout)
                Snackbar.make(clayout, "This Feature is not available now", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_newmatch -> {
                startActivity(Intent(this,details::class.java))
                          }
            R.id.nav_feedback->
            {
               startActivity(Intent(this,matchhistory::class.java))

            }
            R.id.nav_rateus -> {
                startActivity(Intent(this,feedback::class.java))
            }
            R.id.nav_share-> {
          var detail:String="Cricizz App version 1.0" +
                  "\nDeveloped by : Hannan Shahid \nEmail:HannanShahid0@gmail.com " +
                  "\n Location : Panjab/Pakistan"
                var d=detail
                var i=Intent()
                i.action=Intent.ACTION_SEND
                i.putExtra(Intent.EXTRA_TEXT,d)
                i.setType("text/plain")
                startActivity(Intent.createChooser(i,"Share To: "))
            }
            R.id.nav_aboutus -> {

                startActivity(Intent(this,aboutus::class.java))
            }

        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
