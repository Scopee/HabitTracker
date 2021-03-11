package com.example.habittracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.textView)
        Log.i(TAG, "onCreate")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putString(COUNTER, textView.text as String?)
        }
        Log.i(TAG, "onSaveInstanceState: text: ${textView.text}")
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.run {
            textView.text = (getString(COUNTER)?.toInt()?.plus(1)).toString()
        }
        Log.i(TAG, "onRestoreInstanceState: text: ${textView.text}")

    }

    companion object {
        const val COUNTER = "counter"
        const val TAG = "MainActivity"
    }

    fun onClick(view: View) {
        Log.i(TAG, "onClick: button pressed")
        val intent = Intent(this, SecondActivity::class.java).apply {
            val bundle = Bundle().apply {
                putInt(COUNTER, (textView.text as String).toInt())
            }
            putExtras(bundle)
        }
        startActivity(intent)
    }
}