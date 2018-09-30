package com.daftmobile.daftmobiletask

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View

import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    companion object {
        const val LIST_RESTORE_KEY = "ArrayListObjectsKey"
        const val SERVICE_STATE_RESTORE_KEY = "isTimerRunningKey"
    }

    private val items: ArrayList<Item> = ArrayList()
    private lateinit var adapter: ItemsAdapter

    private val timer: Timer = Timer()
    private var timerTask : TimerTask? = null
    var isTimerRunning = false
        private set(value) {
            field = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        savedInstanceState?.run{
            items.addAll(savedInstanceState.getParcelableArrayList(LIST_RESTORE_KEY) ?: emptyList())
            isTimerRunning = savedInstanceState.getBoolean(SERVICE_STATE_RESTORE_KEY)
        }

        adapter = ItemsAdapter(items, this)
        rv.run {
            layoutManager = LinearLayoutManager(context)
            adapter = this@MainActivity.adapter
            addItemDecoration(Divider(context))
        }

        btn_start.setOnClickListener {
            startTimer()
            isTimerRunning = true
        }
        btn_stop.setOnClickListener {
            stopTimer()
            isTimerRunning = false
         }
    }

    private fun runTask() = timer.schedule(0, 1000){ runOnUiThread { performRandomAction() } }

    private fun clearTask(){
        timerTask?.cancel()
        timer.purge()
    }

    private fun startTimer(){
        btn_start.visibility = View.GONE
        btn_stop.visibility = View.VISIBLE

        timerTask = runTask()
    }

    private fun stopTimer(){
        btn_stop.visibility = View.GONE
        btn_start.visibility = View.VISIBLE

        clearTask()
    }

    private fun performRandomAction() = adapter.run {
            if(itemCount < 5)
                addRandomElement()
            else
                with(Random().nextDouble()) {
                    when {
                        this < 0.5 -> incrementRandomElement()
                        this < 0.8 -> resetRandomElementCounter()
                        this < 0.9 -> removeRandomElement()
                        this < 1 -> addModelValue()
                    }
                }

        }

    override fun onResume() {
        super.onResume()
        if(isTimerRunning)
            startTimer()
    }

    override fun onPause() {
        super.onPause()
        clearTask()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.run {
            putParcelableArrayList(LIST_RESTORE_KEY, items)
            putBoolean(SERVICE_STATE_RESTORE_KEY, isTimerRunning)
        }
    }
}
