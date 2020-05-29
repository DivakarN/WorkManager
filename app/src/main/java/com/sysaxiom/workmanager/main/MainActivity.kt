package com.sysaxiom.workmanager.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sysaxiom.workmanager.R
import com.sysaxiom.workmanager.constraint.ConstraintActivity
import com.sysaxiom.workmanager.datasharing.DataSharingActivity
import com.sysaxiom.workmanager.onetimerequest.OneTimeRequestActivity
import com.sysaxiom.workmanager.periodicrequest.PeriodicRequestActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonOneTimeRequest.setOnClickListener {
            Intent(this, OneTimeRequestActivity::class.java).also {
                this.startActivity(it)
            }
        }

        buttonPeriodicRequest.setOnClickListener {
            Intent(this, PeriodicRequestActivity::class.java).also {
                this.startActivity(it)
            }
        }

        buttonDataSharing.setOnClickListener {
            Intent(this, DataSharingActivity::class.java).also {
                this.startActivity(it)
            }
        }

        buttonConstraint.setOnClickListener {
            Intent(this, ConstraintActivity::class.java).also {
                this.startActivity(it)
            }
        }
    }
}
