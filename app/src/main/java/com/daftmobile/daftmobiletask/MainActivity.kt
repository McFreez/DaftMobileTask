package com.daftmobile.daftmobiletask

import android.app.Service
import android.content.*
import android.os.Binder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View

import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    companion object {
        // Defines a custom Intent action
        const val BROADCAST_ACTION = "actionTimerTick"

        const val LIST_RESTORE_KEY = "ArrayListObjects"
        const val SERVICE_STATE_RESTORE_KEY = "isServiceRunning"
    }

    private lateinit var mService: LocalService
    private var mBound = false
    private var isRunning = false

    private val items: ArrayList<Item> = ArrayList()
    private lateinit var adapter: ItemsAdapter

    private val mConnection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName,
                                        service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as LocalService.LocalBinder
            mService = binder.service
            mBound = true

            if(isRunning)
                runTimer()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    private val ticksReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            randomAction()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, LocalService::class.java)
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
        LocalBroadcastManager.getInstance(this).registerReceiver(ticksReceiver, IntentFilter(BROADCAST_ACTION))

        if(savedInstanceState != null) {
            items.addAll(savedInstanceState.getParcelableArrayList(LIST_RESTORE_KEY) ?: emptyList())
            isRunning = savedInstanceState.getBoolean(SERVICE_STATE_RESTORE_KEY)
        }

        rv.layoutManager = LinearLayoutManager(this)
        adapter = ItemsAdapter(items)

        rv.adapter = adapter
        rv.addItemDecoration(Divider(this))

        btn_start.setOnClickListener { runTimer() }
        btn_stop.setOnClickListener {
            if(mBound){
            showStart()
            mService.stopUpdates()
            isRunning = false
        } }
    }

    private fun runTimer(){
        if (mBound){
            showStop()
            mService.startUpdates()
            isRunning = true
        }
    }

    private fun showStop(){
        btn_start.visibility = View.GONE
        btn_stop.visibility = View.VISIBLE
    }

    private fun showStart(){
        btn_start.visibility = View.VISIBLE
        btn_stop.visibility = View.GONE
    }

    fun randomAction(){
        if(adapter.randomElementsCounter < 5)
            adapter.addRandomElement()
        else{
            val percent = Random().nextDouble()
            when{
                percent < 0.5 -> adapter.addRandomElement()
                percent < 0.8 -> adapter.resetRandomElementCounter()
                percent < 0.9 -> adapter.removeRandomElement()
                percent < 1 -> adapter.addModelValue()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        LocalBroadcastManager.getInstance(this).unregisterReceiver(ticksReceiver)
        if (mBound) {
            unbindService(mConnection)
            mBound = false
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelableArrayList(LIST_RESTORE_KEY, items)
        outState?.putBoolean(SERVICE_STATE_RESTORE_KEY, isRunning)
    }

}

class LocalService : Service() {
    // Binder given to clients
    private val mBinder = LocalBinder()

    private lateinit var timer: Timer

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        internal// Return this instance of LocalService so clients can call public methods
        val service: LocalService
            get() = this@LocalService
    }

    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    fun startUpdates(){
        timer = Timer()
        timer.schedule(0, 1000){
            val localIntent = Intent(MainActivity.BROADCAST_ACTION)
            LocalBroadcastManager.getInstance(baseContext).sendBroadcast(localIntent)
        }
    }

    fun stopUpdates() = timer.cancel()

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }
}
