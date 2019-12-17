package phannguyen.sample.serviceexperimental;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import phannguyen.sample.serviceexperimental.utils.FileLogs;
import phannguyen.sample.serviceexperimental.utils.SbLog;
import phannguyen.sample.serviceexperimental.workers.LongProcessingWorker;
import phannguyen.sample.serviceexperimental.workers.StartServiceWorker;

import static phannguyen.sample.serviceexperimental.utils.Constant.APP_TAG;
import static phannguyen.sample.serviceexperimental.utils.Constant.TASK_DATA_INTERVAL_MAIN_WORKER_TAG;
import static phannguyen.sample.serviceexperimental.utils.Constant.TASK_DATA_INTERVAL_MAIN_WORKER_UNIQUE_NAME;
import static phannguyen.sample.serviceexperimental.utils.Constant.TASK_DATA_ONETIME_WORKER_TAG;

public class WorkManagerHelper {
    private static final String TAG = "WorkManagerHelper";

    public static void startOneTimeLongProcessWorker(Context context,int workPolicyVal, long delayInSecond){
        FileLogs.writeLog(context,TAG,"I","startOneTimeLongProcessWorker Start with delayInSecond = " + delayInSecond);
        FileLogs.writeLog(context,APP_TAG,"I","startOneTimeLongProcessWorker with delay In Mins = " + (delayInSecond/60));
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

    public static void startOneTimeWorker(Context context,long delayInSecond){
        FileLogs.writeLog(context,TAG,"I","startOneTimeWorker Start with delayInSecond = " + delayInSecond);
        FileLogs.writeLog(context,APP_TAG,"I","startOneTimeWorker with delay In Mins = " + (delayInSecond/60));

        OneTimeWorkRequest collectDataWork =
                new OneTimeWorkRequest.Builder(StartServiceWorker.class)
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

    public static boolean checkWorkIsStillOn(Context context, String uniqueWorkName){
        WorkInfo.State state = getStateOfWork(context,uniqueWorkName);
        FileLogs.writeLog(context,APP_TAG,"I","Work State "+ state.name());
        if(state == WorkInfo.State.ENQUEUED || state == WorkInfo.State.RUNNING)
            return true;
        else
            return false;
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
