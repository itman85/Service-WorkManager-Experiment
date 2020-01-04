package phannguyen.sample.serviceexperimental.helpers;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import phannguyen.sample.serviceexperimental.services.main.TestLongRunningService;
import phannguyen.sample.serviceexperimental.services.main.TestOldLongRunningService;
import phannguyen.sample.serviceexperimental.utils.Constant;
import phannguyen.sample.serviceexperimental.utils.FileLogs;
import phannguyen.sample.serviceexperimental.utils.SharePref;

import static phannguyen.sample.serviceexperimental.utils.Constant.APP_TAG;

public class ServiceHelper {
    private static final String TAG = "ServiceHelper";

    /**
     * depend on android version will start service appropriately
     * @param context
     * @param intent
     */
    public static void startLongRunningServiceInBackground(Context context, Intent intent){
        // check last done of main service, if longer than interval time elapsed then will start service right now
        long lastDone = SharePref.getLastTimeMainServiceDone(context);
        FileLogs.writeDayLogNoThread(context,APP_TAG,"I","Service last done time "+lastDone +"\n");
        // if last done of service is longer than interval time, will start service
        if(System.currentTimeMillis() - lastDone >= Constant.INTERVAL_PROCESS_DATA_IN_SECOND*1000) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //must call in this way from android 8(Oreo) or higher,
                TestLongRunningService.enqueueWork(context, intent);
            } else {
                // for android older (from 7,6,5-) will start service in old way (not allow in android 8+)
                context.startService(new Intent(context, TestOldLongRunningService.class));
                // if call in android 8+ will get java.lang.IllegalStateException: Not allowed to start service ... app is in background uid UidRecord
            }
        }else{
            FileLogs.writeDayLogNoThread(context,APP_TAG,"I","Service last done recently, will start later. \n");
        }
    }
}
