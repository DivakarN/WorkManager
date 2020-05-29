package com.sysaxiom.workmanager.datasharing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Data
import kotlinx.android.synthetic.main.activity_data_sharing.*
import java.util.concurrent.TimeUnit

class DataSharingActivity : AppCompatActivity() {

    companion object {
        const val KEY_TASK_IN = "KEY_TASK_IN"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.sysaxiom.workmanager.R.layout.activity_data_sharing)

        //A click listener for the button
        //inside the onClick method we will perform the work
        buttonEnqueue.setOnClickListener {

            val data = Data.Builder()
                .putString(KEY_TASK_IN, "The task data passed from DataSharingActivity")
                .build()

            val diff = (System.currentTimeMillis()+60000) - System.currentTimeMillis()

            //This is the subclass of our WorkRequest
            val workRequest = OneTimeWorkRequest.Builder(MyDataSharingWorker::class.java)
                .setInitialDelay(diff,TimeUnit.MILLISECONDS)
                .setInputData(data)
                .build()

            //Enqueuing the work request
            WorkManager.getInstance(applicationContext).enqueue(workRequest)

            WorkManager.getInstance(applicationContext).getWorkInfoByIdLiveData(workRequest.id).observe(
                this, Observer {
                    if(it != null){
                        if(it.state.isFinished){
                            Toast.makeText(this,it.outputData.getString(MyDataSharingWorker.KEY_TASK_OUT), Toast.LENGTH_LONG).show()
                        }
                        Toast.makeText(this,it.state.name, Toast.LENGTH_LONG).show()
                    }
                }
            )
        }

    }
}
