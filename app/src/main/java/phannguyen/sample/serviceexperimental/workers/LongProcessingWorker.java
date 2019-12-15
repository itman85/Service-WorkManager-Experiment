package phannguyen.sample.serviceexperimental.workers;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import phannguyen.sample.serviceexperimental.TestLongRunningService;
import phannguyen.sample.serviceexperimental.utils.FileLogs;
import phannguyen.sample.serviceexperimental.utils.SbLog;

public class LongProcessingWorker extends Worker {

    public static final String TASK_DATA_INTERVAL_WORKER_TAG = "TASK_DATA_INTERVAL_WORKER_TAG";
    public static final String TASK_DATA_INTERVAL_WORKER_UNIQUE_NAME = "TASK_DATA_INTERVAL_WORKER_UNIQUE_NAME";
    private static final String TAG = "LongProcessingWorker";

    public LongProcessingWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        SbLog.i(TAG,"onCreate");
        FileLogs.writeLog(context,TAG,"I","onCreate");
    }

    @NonNull
    @Override
    public Result doWork() {
        SbLog.i(TAG,"doWork");
        FileLogs.writeLog(this.getApplicationContext(),TAG,"I","doWork");
        TestLongRunningService.enqueueWork(this.getApplicationContext(),new Intent(this.getApplicationContext(),TestLongRunningService.class));
        return Result.success();
    }

    @Override
    public void onStopped() {
        FileLogs.writeLog(this.getApplicationContext(),TAG,"I","onStopped");
        super.onStopped();
    }
}
