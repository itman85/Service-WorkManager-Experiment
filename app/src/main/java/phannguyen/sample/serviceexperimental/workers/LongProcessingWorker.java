package phannguyen.sample.serviceexperimental.workers;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import phannguyen.sample.serviceexperimental.TestLongRunningService;
import phannguyen.sample.serviceexperimental.utils.FileLogs;
import phannguyen.sample.serviceexperimental.utils.SbLog;

import static phannguyen.sample.serviceexperimental.utils.Constant.APP_TAG;

public class LongProcessingWorker extends Worker {

    private static final String TAG = "LongProcessingWorker";

    public LongProcessingWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        SbLog.i(TAG,"onCreate");
        FileLogs.writeLog(context,TAG,"I","onCreate");
        FileLogs.writeLog(context,APP_TAG,"I","Long processing worker created");
    }

    @NonNull
    @Override
    public Result doWork() {
        SbLog.i(TAG,"doWork");
        FileLogs.writeLog(this.getApplicationContext(),TAG,"I","doWork");
        FileLogs.writeLog(this.getApplicationContext(),APP_TAG,"I","Long processing worker DoWork...");
        TestLongRunningService.enqueueWork(this.getApplicationContext(),new Intent(this.getApplicationContext(),TestLongRunningService.class));
        FileLogs.writeLog(this.getApplicationContext(),APP_TAG,"I","Long processing worker EndWork...");
        return Result.success();
    }

    @Override
    public void onStopped() {
        FileLogs.writeLog(this.getApplicationContext(),TAG,"I","onStopped");
        FileLogs.writeLog(this.getApplicationContext(),APP_TAG,"I","Long processing worker Stopped");
        super.onStopped();
    }
}
