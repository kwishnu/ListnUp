package com.baked.listnup

import android.os.Bundle
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.list.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class List : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list)
        val listTitle =intent.getStringExtra("ListTitle")
        tv_another_activity.text = listTitle
        tv_another_activity.setOnClickListener {view ->
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            startActivity(Intent(this, MainActivity::class.java))
            super.onBackPressed()
        }

    }

}
