package phannguyen.sample.serviceexperimental.workers;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import phannguyen.sample.serviceexperimental.services.main.TestLongRunningService;
import phannguyen.sample.serviceexperimental.helpers.ServiceHelper;
import phannguyen.sample.serviceexperimental.utils.FileLogs;
import phannguyen.sample.serviceexperimental.utils.SbLog;

import static phannguyen.sample.serviceexperimental.utils.Constant.APP_TAG;

public class StartServiceWorker extends Worker {

    private static final String TAG = "StartServiceWorker";

    public StartServiceWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        SbLog.i(TAG,"onCreate");
        FileLogs.writeLogInThread(context,TAG,"I","onCreate");
        FileLogs.writeLogNoThread(context,APP_TAG,"I","Start service worker created");
    }

    @NonNull
    @Override
    public Result doWork() {
        SbLog.i(TAG,"doWork");
        FileLogs.writeLogInThread(this.getApplicationContext(),TAG,"I","doWork");
        FileLogs.writeLogNoThread(this.getApplicationContext(),APP_TAG,"I","Start service worker DoWork...");

        //TestLongRunningService.enqueueWork(this.getApplicationContext(),new Intent(this.getApplicationContext(),TestLongRunningService.class));
        ServiceHelper.startLongRunningServiceInBackground(this.getApplicationContext(),new Intent(this.getApplicationContext(),TestLongRunningService.class));

        FileLogs.writeLogNoThread(this.getApplicationContext(),APP_TAG,"I","Start service worker Endwork...");
        return Result.success();
    }

    @Override
    public void onStopped() {
        FileLogs.writeLogInThread(this.getApplicationContext(),TAG,"I","onStopped");
        FileLogs.writeLogInThread(this.getApplicationContext(),APP_TAG,"I","Start service worker Stopped");
        super.onStopped();
    }
}
