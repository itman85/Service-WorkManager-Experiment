package phannguyen.sample.serviceexperimental.services.main;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.work.ExistingWorkPolicy;

import phannguyen.sample.serviceexperimental.helpers.WorkManagerHelper;
import phannguyen.sample.serviceexperimental.utils.FileLogs;
import phannguyen.sample.serviceexperimental.utils.SbLog;

import static phannguyen.sample.serviceexperimental.utils.Constant.APP_TAG;
import static phannguyen.sample.serviceexperimental.utils.Constant.INTERVAL_PROCESS_DATA;
import static phannguyen.sample.serviceexperimental.utils.Constant.SLEEP_TIME_IN_MS;
import static phannguyen.sample.serviceexperimental.utils.Constant.SLEEP_TIME_LOOP_IN_MS;

/**
 * NOTE: this service use for android 5,6,7 for long running tasks, from android 8+ must use jobintentservice to start service in background
 */
public class TestOldLongRunningService extends IntentService {
    private static final String TAG = "TestOldLongRunningService";
    private long serviceNumber;//generated whenever service created
    private int serviceRunCount;// this help to prevent onHandleIntent call multiple time while it running

    public TestOldLongRunningService() {
        super("TestOldLongRunningService");
        serviceNumber = (long)(Math.random()*1000000) + 1;//random from 1 -> 1M
        serviceRunCount = 0;// a service must init to gain active status
        SbLog.i(TAG,"init Fire "+serviceNumber);
        // the context not init yet at here
        // in case this service start multiple times, first time will call init constructor -> onCreate -> onHandleIntent -> onDestroy
        // from second time, it only call onHandleIntent
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        serviceRunCount++;
        if(serviceRunCount > 1)// for startService while it running, then next call onHandleIntent will be skipped
            return;
        SbLog.i(TAG,"onHandleWork Start");
        FileLogs.writeLogInThread(this,TAG,"I","onHandleWork Start "+serviceNumber);
        FileLogs.writeLogInThread(this,APP_TAG,"I","*** 2.Long Running Service Start In "+(SLEEP_TIME_IN_MS/60000) + " Mins " + serviceNumber);
        try {
            FileLogs.writeDayLogNoThread(this,APP_TAG,"I","* 1. Long service start in  "+ (SLEEP_TIME_IN_MS/60000) + " Mins "+ serviceNumber);
            int loopNth = 1;
            long loopNumber = SLEEP_TIME_IN_MS/SLEEP_TIME_LOOP_IN_MS;
            // 3 mins to complete processing
            while (loopNth <= loopNumber){
                Thread.sleep(SLEEP_TIME_LOOP_IN_MS); // sleep 5s for each loop
                loopNth++;
            }
            FileLogs.writeDayLogNoThread(this,APP_TAG,"I","** 2. Long service End "+ serviceNumber + "\n");
            //check if end work - start work < mins for this serviceNumber and loopNth = LOOP_NUMBER then success task and store log for this date
            SbLog.i(TAG,"onHandleWork Finish");
            FileLogs.writeLogInThread(this,TAG,"I","onHandleWork Finish " + serviceNumber);
            FileLogs.writeLogInThread(this,APP_TAG,"I","*** 3.Long Running Service Finish "+ serviceNumber);
        } catch (InterruptedException e) {
            SbLog.e(TAG,e);
            FileLogs.writeLogInThread(this,TAG,"E",serviceNumber + " onHandleWork Error "+ Log.getStackTraceString(e));
            FileLogs.writeLogInThread(this,APP_TAG,"I",serviceNumber + "*** Long Running Service Error "+Log.getStackTraceString(e) );
        } finally {
            FileLogs.writeLogInThread(this,APP_TAG,"I","*** 4.Long Running Service Finally "+serviceNumber);
            WorkManagerHelper.scheduleNextWorking(this, ExistingWorkPolicy.KEEP.ordinal(),INTERVAL_PROCESS_DATA);
            //stopSelf(); intent service will stopSelf() as it done
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
       // serviceNumber = (long)(Math.random()*1000000) + 1;//random from 1 -> 1M
        SbLog.i(TAG,"onCreate Fire " + serviceNumber);
        FileLogs.writeLogInThread(this,TAG,"I","onCreate Fire " + serviceNumber);
        FileLogs.writeLogNoThread(this,APP_TAG,"I","*** 1.Long Running Service Created(Android 7-) "+serviceNumber);

    }



    @Override
    public void onDestroy() {
        // DO NOT reset serviceRunCount here because onDestroy will be called 1st time
        SbLog.i(TAG,"onDestroy Fire");
        FileLogs.writeLogInThread(this,TAG,"I","onDestroy Fire "+serviceNumber);
        FileLogs.writeLogNoThread(this,APP_TAG,"I","*** 5.Long Running Service Destroy "+serviceNumber);
        super.onDestroy();
    }
}
