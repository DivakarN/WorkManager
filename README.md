# WorkManager

Work Manager:
-------------

The WorkManager API makes it easy to specify deferrable, asynchronous tasks and when they should run. These APIs let you create a task and hand it off to WorkManager to run immediately or at an appropriate time.
WorkManager is part of Android Jetpack.

Why WorkManager?
----------------

1) WorkManager chooses the appropriate way to run your task based on such factors as the device API level and the app state.
2) It provides us the clean interface, so no complex coding thing.
3)It guarantees the execution

WorkManager Features:
---------------------

1) It is fully backward compatible so in your code you do not need to write if-else for checking the android version.
2) With WorkManager we can check the status of the work.
3) Tasks can be chained as well, for example when one task is finished it can start another.
4) And it provides guaranteed execution with the constraints, we have many constrained available that we will see ahead.

Basic Classes:
--------------

1) Worker: The work needed to be done is defined here.
2) WorkRequest: It defines a work, like which worker class is going to be executed. The WorkRequest is an abstract class so we will use the direct subclasses, OneTimeWorkRequest or PeriodicWorkRequest.
3) WorkManager: It enqueues and managers the work request.
4) WorkInfo: It contains information about the work.
             For each WorkRequest we can get a LiveData using WorkManager.
             The LiveData holds the WorkInfo and by observing it we can determine the Work Informations.

Creating a Worker:
------------------

class MyOneTimeWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {

        //Work

        return Result.success() //Return status of the work
    }

}

Performing the Work:
--------------------

we have two subsclasses for WorkRequest

1) OneTimeWorkRequest: Used when we want to perform the work only once.
2) PeriodicWorkRequest: Used when we need to perform the task periodically.

//Initializing OneTimeWorkRequest
val workRequest = OneTimeWorkRequest.Builder(MyOneTimeWorker::class.java).build()

//Initializing PeriodicWorkRequest
val workRequest = PeriodicWorkRequest.Builder(MyPeriodicWorker::class.java,60000,TimeUnit.MILLISECONDS)

//Enqueuing work request into work manager
WorkManager.getInstance(applicationContext).enqueue(workRequest)

//Accessing work info of the work request through WorkManager LiveData
WorkManager.getInstance(applicationContext).getWorkInfoByIdLiveData(workRequest.id).observe(
    this, Observer {
        Toast.makeText(this,it.state.name, Toast.LENGTH_LONG).show()
    }
)

Sending And Receiving Data to/from WorkManager:
-----------------------------------------------

//Data object from work is used for data communication between work manager and application
val data = Data.Builder()
                .putString(KEY_TASK_IN, "The task data passed from DataSharingActivity")
                .build()

//specifying the input data in work request
val workRequest = OneTimeWorkRequest.Builder(MyDataSharingWorker::class.java)
                .setInputData(data)
                .build()

//Accessing output data from work manager into application
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

override fun doWork(): Result {

        //Accessing input data from application
        val inputData = inputData
        displayNotification("My Worker", inputData.getString(DataSharingActivity.KEY_TASK_IN).toString())

        //Sending output data to application
        val data = Data.Builder()
            .putString(KEY_TASK_OUT, "The conclusion of the task")
            .build()
        return Result.success(data)
}

Adding Constraints:
-------------------

Adding some constraint in work so that it will execute at a specific time.
WorkManager have many constraints available for example,

1) setRequiresCharging(boolean b): If it is set true the work will be only done when the device is charging.
2) setRequiresBatteryNotLow(boolean b): Work will be done only when the battery of the device is not low.
3) setRequiresDeviceIdle(boolean b): Work will be done only when the device is idle.

//Creating Constraints
val constraints = Constraints.Builder()
                .setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

//Assinging constraints to work request
val workRequest = OneTimeWorkRequest.Builder(MyConstraintWorker::class.java)
                            .setConstraints(constraints)
                            .build()

Canceling Work:
---------------

cancelAllWork(): To cancel all the work.
cancelAllWorkByTag(): To cancel all works of a specific tag.
cancelUniqueWork(): To cancel a unique work.
cancelWorkById(workId) : To cancel a work by its id

//Cancelling work request by its workId
WorkManager.getInstance(applicationContext).cancelWorkById(workId)

Chaining Works:
--------------

WorkManager allow works to be executed one by one using chaining the works in WorkManager instance.

WorkManager.getInstance().
    beginWith(workRequest)
    .then(workRequest1)
    .then(workRequest2)
    .enqueue()
    
AlarmManager vs JobScheduler vs WorkManager:
--------------------------------------------

The main drawback of Alarm Manager is that it solely works on the basis of time.

The main drawback of JobScheduler is that its run from API level 21 or higher.

1) WorkManager combines both JobScheduler and AlarmManager. If JobScheduler is available, it will use it. If not it will check for Firebase JobDispatcher availability and try to use it. Otherwise, it will fallback to AlarmManager and BroadcastReceivers.
2) Its ability to chain and combine tasks.
3) It integrates with LiveData from Architecture Components, so you can observe its status and output data in a simple manner
4) Exchanging data between app and Worker    
