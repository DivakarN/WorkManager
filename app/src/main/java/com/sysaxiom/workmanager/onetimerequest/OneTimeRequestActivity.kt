package com.sysaxiom.workmanager.onetimerequest

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.sysaxiom.workmanager.R
import kotlinx.android.synthetic.main.activity_one_time_request.*

class OneTimeRequestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_time_request)

        buttonEnqueue.setOnClickListener {

            val workRequest = OneTimeWorkRequest.Builder(MyOneTimeWorker::class.java).build()

            WorkManager.getInstance(applicationContext).enqueue(workRequest)

            WorkManager.getInstance(applicationContext).getWorkInfoByIdLiveData(workRequest.id).observe(
                this, Observer {
                    Toast.makeText(this,it.state.name, Toast.LENGTH_LONG).show()
                }
            )

        }

    }

}
