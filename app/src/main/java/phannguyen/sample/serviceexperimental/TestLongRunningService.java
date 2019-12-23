package phannguyen.sample.serviceexperimental;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.work.ExistingWorkPolicy;

import phannguyen.sample.serviceexperimental.utils.FileLogs;
import phannguyen.sample.serviceexperimental.utils.SbLog;

import static phannguyen.sample.serviceexperimental.utils.Constant.APP_TAG;
import static phannguyen.sample.serviceexperimental.utils.Constant.INTERVAL_PROCESS_DATA;

public class TestLongRunningService extends JobIntentService {

    private static final int JOB_ID = 123;

    private static final String TAG = "TestLongRunningService";

    public static void enqueueWork(Context context, Intent intent) {
        // todo check if service running, then skip start.
        enqueueWork(context, TestLongRunningService.class, JOB_ID, intent);
    }

    @Override
    public void onCreate() {
        SbLog.i(TAG,"onCreate Fire");
        FileLogs.writeLog(this,TAG,"I","onCreate Fire");
        FileLogs.writeLog(this,APP_TAG,"I","Long Running Service Created");
        super.onCreate();

    }
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        SbLog.i(TAG,"onHandleWork Start");
        FileLogs.writeLog(this,TAG,"I","onHandleWork Start");
        FileLogs.writeLog(this,APP_TAG,"I","Long Running Service Start In 5 Mins");
        try {
            Thread.sleep(300000);// 5 mins to complete processing
            SbLog.i(TAG,"onHandleWork Finish");
            FileLogs.writeLog(this,TAG,"I","onHandleWork Finish");
            FileLogs.writeLog(this,APP_TAG,"I","Long Running Service Finish");
        } catch (InterruptedException e) {
            SbLog.e(TAG,e);
            FileLogs.writeLog(this,TAG,"E","onHandleWork Error "+ Log.getStackTraceString(e));
            FileLogs.writeLog(this,APP_TAG,"I","Long Running Service Error "+Log.getStackTraceString(e) );
        } finally {
            FileLogs.writeLog(this,APP_TAG,"I","Long Running Service Finally");
            WorkManagerHelper.startOneTimeLongProcessWorker(this, ExistingWorkPolicy.KEEP.ordinal(),INTERVAL_PROCESS_DATA);
            stopSelf();
        }
    }

    @Override
    public void onDestroy() {
        SbLog.i(TAG,"onDestroy Fire");
        FileLogs.writeLog(this,TAG,"I","onDestroy Fire");
        FileLogs.writeLog(this,APP_TAG,"I","Long Running Service Destroy");
        super.onDestroy();
    }
}
