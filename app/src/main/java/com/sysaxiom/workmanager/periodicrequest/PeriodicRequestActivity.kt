package com.sysaxiom.workmanager.periodicrequest

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.*
import com.sysaxiom.workmanager.R
import com.sysaxiom.workmanager.constraint.ConstraintActivity
import kotlinx.android.synthetic.main.activity_periodic_request.*
import java.util.*
import java.util.concurrent.TimeUnit

class PeriodicRequestActivity : AppCompatActivity() {

    var workId : UUID? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_periodic_request)

        //A click listener for the button
        //inside the onClick method we will perform the work
        buttonStartEnqueue.setOnClickListener {

            val data = Data.Builder()
                .putString(ConstraintActivity.KEY_TASK_IN, "The task data passed from MainActivity")
                .build()

            val constraints = Constraints.Builder()
                .setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val workRequest = PeriodicWorkRequest.Builder(MyPeriodicWorker::class.java,60000,TimeUnit.MILLISECONDS)
                .setInputData(data)
                .setConstraints(constraints)
                .build()

            workId = workRequest.id
            WorkManager.getInstance(applicationContext).enqueue(workRequest)

            WorkManager.getInstance(applicationContext).getWorkInfoByIdLiveData(workId!!).observe(
                this, Observer {
                    if(it != null){
                        Toast.makeText(this,it.state.name, Toast.LENGTH_LONG).show()
                    }
                }
            )
        }

        buttonStopEnqueue.setOnClickListener {
            workId?.let { id -> WorkManager.getInstance(applicationContext).cancelWorkById(id) }
        }

    }

}
