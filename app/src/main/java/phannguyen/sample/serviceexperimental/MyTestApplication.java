package phannguyen.sample.serviceexperimental;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.work.Configuration;

import phannguyen.sample.serviceexperimental.utils.FileLogs;
import phannguyen.sample.serviceexperimental.utils.SbLog;

public class MyTestApplication extends Application implements Configuration.Provider {
    private static final String TAG = "MyTestApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        SbLog.i(TAG,"onCreate Fire");
        FileLogs.writeLog(this,TAG,"I","onCreate Fire");

    }

    @NonNull
    @Override
    public Configuration getWorkManagerConfiguration() {
        return new Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.INFO)
                .build();
    }
}
