package com.sysaxiom.workmanager.constraint

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.*
import com.sysaxiom.workmanager.R
import kotlinx.android.synthetic.main.activity_constraint.*
import java.util.*

class ConstraintActivity : AppCompatActivity() {

    companion object {
        const val KEY_TASK_IN = "KEY_TASK_IN"
    }

    private lateinit var workId: UUID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint)

        //A click listener for the button
        //inside the onClick method we will perform the work
        buttonEnqueue.setOnClickListener {

            val data = Data.Builder()
                .putString(KEY_TASK_IN, "The task data passed from ConstraintActivity")
                .build()

            val constraints = Constraints.Builder()
                .setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            //This is the subclass of our WorkRequest
            val workRequest = OneTimeWorkRequest.Builder(MyConstraintWorker::class.java)
                .setInputData(data)
                .setConstraints(constraints)
                .build()

            workId = workRequest.id

            //Enqueuing the work request
            WorkManager.getInstance(applicationContext).enqueue(workRequest)

            WorkManager.getInstance(applicationContext).getWorkInfoByIdLiveData(workId).observe(
                this, Observer {
                    if(it != null){
                        if(it.state.isFinished){
                            Toast.makeText(this,it.outputData.getString(MyConstraintWorker.KEY_TASK_OUT), Toast.LENGTH_LONG).show()
                        }
                        Toast.makeText(this,it.state.name, Toast.LENGTH_LONG).show()
                    }
                }
            )
        }

    }

}
