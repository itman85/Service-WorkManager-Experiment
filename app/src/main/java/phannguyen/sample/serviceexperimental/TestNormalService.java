package phannguyen.sample.serviceexperimental;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import phannguyen.sample.serviceexperimental.utils.FileLogs;
import phannguyen.sample.serviceexperimental.utils.SbLog;

import static phannguyen.sample.serviceexperimental.utils.Constant.APP_TAG;

/**
 * NOTE: from android 8 , cannot start service in background, must start a foreground service, so use JobIntentService instead
 */
public class TestNormalService extends Service {
    private static final String TAG = "TestNormalService";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SbLog.i(TAG,"onCreate Fire");
        FileLogs.writeLog(this,APP_TAG,"I","TestNormalService onCreate Fire");


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SbLog.i(TAG,"onStartCommand Start");
        FileLogs.writeLog(this,APP_TAG,"I","TestNormalService onStartCommand Start");
        try {
            Thread.sleep(30000);
            SbLog.i(TAG,"onStartCommand Finish");
            FileLogs.writeLog(this,APP_TAG,"I","TestNormalService onStartCommand Finish");
        } catch (InterruptedException e) {
            SbLog.e(TAG,e);
            FileLogs.writeLog(this,APP_TAG,"E","TestNormalService onStartCommand Error "+ Log.getStackTraceString(e));
        }finally {
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SbLog.i(TAG,"onDestroy Fire");
        FileLogs.writeLog(this,APP_TAG,"I","TestNormalService onDestroy Fire");
    }
}
