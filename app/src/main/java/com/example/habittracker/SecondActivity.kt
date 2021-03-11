package com.example.habittracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import java.lang.Math.pow
import kotlin.math.pow

class SecondActivity : AppCompatActivity() {

    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        textView = findViewById(R.id.squareTextView)
        intent.extras?.apply {
            val number =  getInt(MainActivity.COUNTER)
            textView.text = number.times(number).toString()
        }
        Log.i(TAG, "onCreate: number set")
    }

    companion object {
        const val TAG = "SecondActivity"
    }
}