package com.anwesh.uiprojects.looperdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.MotionEvent
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    var i : Float = 0f
    var evnHandler : EvenHandler = EvenHandler(this)
    var handler : Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        evnHandler.addOnInit { handler1 ->
            handler = handler1
        }
        evnHandler.start()

    }

    fun showOddToast() {
        Toast.makeText(this, "odd times clicked", Toast.LENGTH_SHORT).show()
    }

    fun showEventToast() {
        Toast.makeText(this, "even times clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                i++
                var message : Message = Message()
                message.obj = i
                handler?.sendMessage(message)
            }
        }
        return true
    }
}

class EvenHandler(var activity: MainActivity) : Thread(){
    private var looper : Looper? = null
    private lateinit var initCb : (Handler) -> Unit
    override fun run() {
        Looper.prepare()
        var handler : Handler = Handler(Looper.myLooper()) { message ->
            var data = message.obj as Float
            if (data == 6f) {
                Looper.myLooper().quit()
            }
            when (data % 2) {
                0f -> activity.showEventToast()
                1f -> activity.showOddToast()
            }
            true
        }
        initCb(handler)
        Looper.loop()
    }

    fun addOnInit(cb : (Handler) -> Unit) {
        initCb = cb
    }
}