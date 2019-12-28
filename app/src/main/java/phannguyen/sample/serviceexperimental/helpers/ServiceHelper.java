package phannguyen.sample.serviceexperimental.helpers;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import phannguyen.sample.serviceexperimental.services.main.TestLongRunningService;
import phannguyen.sample.serviceexperimental.services.main.TestOldLongRunningService;

public class ServiceHelper {
    private static final String TAG = "ServiceHelper";

    /**
     * depend on android version will start service appropriately
     * @param context
     * @param intent
     */
    public static void startLongRunningServiceInBackground(Context context, Intent intent){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //must call in this way from android 8(Oreo) or higher,
            TestLongRunningService.enqueueWork(context, intent);
        }else{
            // for android older (from 7,6,5-) will start service in old way (not allow in android 8+)
            context.startService(new Intent(context, TestOldLongRunningService.class));
            // if call in android 8+ will get java.lang.IllegalStateException: Not allowed to start service ... app is in background uid UidRecord
        }
    }
}
