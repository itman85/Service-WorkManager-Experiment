package phannguyen.sample.serviceexperimental;

import android.content.Context;

import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

import phannguyen.sample.serviceexperimental.utils.FileLogs;
import phannguyen.sample.serviceexperimental.workers.LongProcessingWorker;

import static phannguyen.sample.serviceexperimental.utils.Constant.APP_TAG;
import static phannguyen.sample.serviceexperimental.workers.LongProcessingWorker.TASK_DATA_INTERVAL_WORKER_UNIQUE_NAME;

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

        OneTimeWorkRequest collectDataWork=
                new OneTimeWorkRequest.Builder(LongProcessingWorker.class)
                        .setInitialDelay(delayInSecond, TimeUnit.SECONDS)
                        .addTag(LongProcessingWorker.TASK_DATA_INTERVAL_WORKER_TAG)// Use this when you want to add initial delay or schedule initial work to `OneTimeWorkRequest` e.g. setInitialDelay(2, TimeUnit.HOURS)
                        .build();
        WorkManager.getInstance(context).enqueueUniqueWork(TASK_DATA_INTERVAL_WORKER_UNIQUE_NAME,workPolicy,collectDataWork);
    }
}
