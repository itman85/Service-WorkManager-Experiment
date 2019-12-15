package phannguyen.sample.serviceexperimental;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.work.Configuration;

import phannguyen.sample.serviceexperimental.utils.FileLogs;
import phannguyen.sample.serviceexperimental.utils.SbLog;

import static phannguyen.sample.serviceexperimental.utils.Constant.APP_TAG;

public class MyTestApplication extends Application implements Configuration.Provider {
    private static final String TAG = "MyTestApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        SbLog.i(TAG,"onCreate Fire");
        FileLogs.writeLog(this,TAG,"I","onCreate Fire");
        FileLogs.writeLog(this,APP_TAG,"I","App created");
    }

    @NonNull
    @Override
    public Configuration getWorkManagerConfiguration() {
        return new Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.INFO)
                .build();
    }
}
