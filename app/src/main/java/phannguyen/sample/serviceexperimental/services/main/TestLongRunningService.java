package phannguyen.sample.serviceexperimental.services.main;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.work.ExistingWorkPolicy;

import phannguyen.sample.serviceexperimental.helpers.WorkManagerHelper;
import phannguyen.sample.serviceexperimental.utils.FileLogs;
import phannguyen.sample.serviceexperimental.utils.SbLog;

import static phannguyen.sample.serviceexperimental.utils.Constant.APP_TAG;
import static phannguyen.sample.serviceexperimental.utils.Constant.INTERVAL_PROCESS_DATA;

/**
 * This user will be used in android 8+ for long running tasks
 */
public class TestLongRunningService extends JobIntentService {

    private static final int JOB_ID = 123;

    private static final int LOOP_NUMBER = 10;

    private static final String TAG = "TestLongRunningService";

    private long serviceNumber;//generated whenever service created

    private int serviceRunCount;// this help to prevent onHandleWork call multiple time while it running or enqueued

    public static void enqueueWork(Context context, Intent intent) {
        // if it already enqueued, so next one will be call onHandleWork as its turn and not call onCreate again.
        // so no matter how many enqueued, it only call onCreate and onDestroy one time and call onHandleWork multiple times
        enqueueWork(context, TestLongRunningService.class, JOB_ID, intent);
    }

    @Override
    public void onCreate() {
        serviceNumber = (long)(Math.random()*1000000) + 1;//random from 1 -> 1M
        serviceRunCount = 0;
        SbLog.i(TAG,"onCreate Fire");
        FileLogs.writeLogInThread(this,TAG,"I","onCreate Fire " + serviceNumber);
        FileLogs.writeLogNoThread(this,APP_TAG,"I","*** 1.Long Running Service(Android 8+) Created "+serviceNumber);
        super.onCreate();

    }
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        serviceRunCount++;
        if(serviceRunCount > 1)
            return;
        SbLog.i(TAG,"onHandleWork Start");
        FileLogs.writeLogInThread(this,TAG,"I","onHandleWork Start "+serviceNumber);
        FileLogs.writeLogNoThread(this,APP_TAG,"I","*** 2.Long Running Service Start In 5 Mins " + serviceNumber);
        try {
            //store current time and serviceNumber at start
            int loopNth = 1;
            // 3 mins to complete processing
            while (loopNth <= LOOP_NUMBER){
                Thread.sleep(10000); // sleep 10s for each loop
                loopNth++;
                //store loopNth for serviceNumber while working
            }
            //check if end work - start work < mins for this serviceNumber and loopNth = LOOP_NUMBER then success task and store log for this date
            SbLog.i(TAG,"onHandleWork Finish");
            FileLogs.writeLogInThread(this,TAG,"I","onHandleWork Finish " + serviceNumber);
            FileLogs.writeLogNoThread(this,APP_TAG,"I","*** 3.Long Running Service Finish "+ serviceNumber);
        } catch (InterruptedException e) {
            SbLog.e(TAG,e);
            FileLogs.writeLogInThread(this,TAG,"E",serviceNumber + " onHandleWork Error "+ Log.getStackTraceString(e));
            FileLogs.writeLogInThread(this,APP_TAG,"I",serviceNumber + "*** Long Running Service Error "+Log.getStackTraceString(e) );
        } finally {
            FileLogs.writeLogNoThread(this,APP_TAG,"I","*** 4.Long Running Service Finally "+serviceNumber);
            WorkManagerHelper.startOneTimeLongProcessWorker(this, ExistingWorkPolicy.KEEP.ordinal(),INTERVAL_PROCESS_DATA);
            stopSelf();
        }
    }

    @Override
    public void onDestroy() {
        serviceRunCount = 0;//reset
        SbLog.i(TAG,"onDestroy Fire");
        FileLogs.writeLogInThread(this,TAG,"I","onDestroy Fire "+serviceNumber);
        FileLogs.writeLogNoThread(this,APP_TAG,"I","*** 5.Long Running Service Destroy "+serviceNumber);
        super.onDestroy();
    }
}
