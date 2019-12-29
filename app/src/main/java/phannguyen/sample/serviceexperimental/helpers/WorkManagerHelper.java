package phannguyen.sample.serviceexperimental.helpers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import phannguyen.sample.serviceexperimental.utils.Constant;
import phannguyen.sample.serviceexperimental.utils.FileLogs;
import phannguyen.sample.serviceexperimental.utils.SbLog;
import phannguyen.sample.serviceexperimental.workers.LongProcessingWorker;
import phannguyen.sample.serviceexperimental.workers.OneTimeWorker;

import static android.content.Context.ALARM_SERVICE;
import static phannguyen.sample.serviceexperimental.utils.Constant.APP_TAG;
import static phannguyen.sample.serviceexperimental.utils.Constant.SDK_USE_WORK_MANAGER;
import static phannguyen.sample.serviceexperimental.utils.Constant.TASK_DATA_INTERVAL_MAIN_WORKER_TAG;
import static phannguyen.sample.serviceexperimental.utils.Constant.TASK_DATA_INTERVAL_MAIN_WORKER_UNIQUE_NAME;
import static phannguyen.sample.serviceexperimental.utils.Constant.TASK_DATA_ONETIME_WORKER_TAG;
import static phannguyen.sample.serviceexperimental.utils.Constant.TEST_USE_ALARM_MANAGER_FOR_ALL_SDK_VERSION;

public class WorkManagerHelper {
    private static final String TAG = "WorkManagerHelper";

    public static void scheduleNextWorking(Context context,int workPolicyVal, long delayInSecond){
        // todo check if alarm in on so skip schedule for next working (currently solution is to use KEEP in Workmanger and flag UPDATE in alarmmanager)
        if (Build.VERSION.SDK_INT >= SDK_USE_WORK_MANAGER && !TEST_USE_ALARM_MANAGER_FOR_ALL_SDK_VERSION) {
            // for android 6+
            startOneTimeLongProcessWorker(context,workPolicyVal,delayInSecond);

        }else{
            // for android 5- use alarm manager
            scheduleNextWorkingUsingAlarmManager(context,delayInSecond);
        }
    }

    public static void startWorkingInDelayTime(Context context,long delayInSecond){
        if (Build.VERSION.SDK_INT >= SDK_USE_WORK_MANAGER && !TEST_USE_ALARM_MANAGER_FOR_ALL_SDK_VERSION) {
            // for android 6+
            startOneTimeWorker(context,delayInSecond);

        }else{
            // for android 5- use alarm manager
            scheduleNextWorkingUsingAlarmManager(context,delayInSecond);
        }
    }

    public static boolean checkWorkIsStillOn(Context context, String uniqueWorkName){
        if (Build.VERSION.SDK_INT >= SDK_USE_WORK_MANAGER && !TEST_USE_ALARM_MANAGER_FOR_ALL_SDK_VERSION) {
            WorkInfo.State state = getStateOfWork(context, uniqueWorkName);
            FileLogs.writeLogNoThread(context, APP_TAG, "I", "Work State " + state.name());
            if (state == WorkInfo.State.ENQUEUED || state == WorkInfo.State.RUNNING)
                return true;
            else
                return false;
        }else{
            return false;// this version not use work manager so no worker on
        }
    }

    /////////////////////////PRIVATE METHODS////////////////////////////////////

    private static void scheduleNextWorkingUsingAlarmManager(Context context, long delayInSecond){
        FileLogs.writeLogInThread(context,TAG,"I","schedule next alarm with delayInSecond = " + delayInSecond);
        FileLogs.writeLogNoThread(context,APP_TAG,"I","schedule next alarm with delay In Mins = " + (delayInSecond/60));

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        long when = System.currentTimeMillis() + delayInSecond*1000;

        PendingIntent mainBroadcast = BroadcaseReceiverHelper.getPendingIntentForMainBroadcastReceiver(context);

        int SDK_INT = Build.VERSION.SDK_INT;

        if (SDK_INT < Build.VERSION_CODES.KITKAT) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, when, mainBroadcast);
        }else if (Build.VERSION_CODES.KITKAT <= SDK_INT && SDK_INT < Build.VERSION_CODES.M) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, when, mainBroadcast);
        }else if (SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, when, mainBroadcast);
        }
    }

    private static void startOneTimeLongProcessWorker(Context context,int workPolicyVal, long delayInSecond){
        FileLogs.writeLogInThread(context,TAG,"I","startLongProcessWorker Start with delayInSecond = " + delayInSecond);
        FileLogs.writeLogNoThread(context,APP_TAG,"I","startLongProcessWorker with delay In Mins = " + (delayInSecond/60));
        ExistingWorkPolicy workPolicy = ExistingWorkPolicy.REPLACE;//replace by new request
        switch (workPolicyVal){
            case 0:
                workPolicy = ExistingWorkPolicy.REPLACE; // replace worker going to execute
                break;
            case 1:
                workPolicy = ExistingWorkPolicy.KEEP;// keep current worker in progress
                break;
            case 2:
                workPolicy = ExistingWorkPolicy.APPEND;
                break;
        }

       /* Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(false)
                .build();*/

        OneTimeWorkRequest collectDataWork=
                new OneTimeWorkRequest.Builder(LongProcessingWorker.class)
                        .setInitialDelay(delayInSecond, TimeUnit.SECONDS)
                        //.setConstraints(constraints)
                        .addTag(TASK_DATA_INTERVAL_MAIN_WORKER_TAG)// Use this when you want to add initial delay or schedule initial work to `OneTimeWorkRequest` e.g. setInitialDelay(2, TimeUnit.HOURS)
                        .build();
        WorkManager.getInstance(context).enqueueUniqueWork(TASK_DATA_INTERVAL_MAIN_WORKER_UNIQUE_NAME,workPolicy,collectDataWork);
    }

    private static void startOneTimeWorker(Context context,long delayInSecond){
        FileLogs.writeLogInThread(context,TAG,"I","startOneTimeWorker Start with delayInSecond = " + delayInSecond);
        FileLogs.writeLogNoThread(context,APP_TAG,"I","startOneTimeWorker with delay In Mins = " + (delayInSecond/60));

        OneTimeWorkRequest collectDataWork =
                new OneTimeWorkRequest.Builder(OneTimeWorker.class)
                        .setInitialDelay(delayInSecond, TimeUnit.SECONDS)
                        .addTag(TASK_DATA_ONETIME_WORKER_TAG)// Use this when you want to add initial delay or schedule initial work to `OneTimeWorkRequest` e.g. setInitialDelay(2, TimeUnit.HOURS)
                        .build();
        WorkManager.getInstance(context).enqueue(collectDataWork);
    }

    private static WorkInfo.State getStateOfWork(Context context, String uniqueWorkName) {
        try {
            if (WorkManager.getInstance(context).getWorkInfosForUniqueWork(uniqueWorkName).get().size() > 0) {
                return WorkManager.getInstance(context).getWorkInfosForUniqueWork(uniqueWorkName)
                        .get().get(0).getState();
                // this can return WorkInfo.State.ENQUEUED or WorkInfo.State.RUNNING
                // you can check all of them in WorkInfo class.
            } else {
                return WorkInfo.State.CANCELLED;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
            SbLog.e(TAG,e);
            return WorkInfo.State.CANCELLED;
        } catch (InterruptedException e) {
            SbLog.e(TAG,e);
            e.printStackTrace();
            return WorkInfo.State.CANCELLED;
        }
    }


    // todo this way to check service running in api 21+ (Android 5+), below api 21 use other way
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static boolean isJobServiceOn(Context context,int JOB_ID ) {
        JobScheduler scheduler = (JobScheduler) context.getSystemService( Context.JOB_SCHEDULER_SERVICE ) ;

        boolean hasBeenScheduled = false ;

        for ( JobInfo jobInfo : scheduler.getAllPendingJobs() ) {
            if ( jobInfo.getId() == JOB_ID ) {
                hasBeenScheduled = true ;
                break ;
            }
        }

        return hasBeenScheduled ;
    }
}
