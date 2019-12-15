package phannguyen.sample.serviceexperimental;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import phannguyen.sample.serviceexperimental.utils.FileLogs;
import phannguyen.sample.serviceexperimental.utils.SbLog;

public class TestBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "TestBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        SbLog.i(TAG,"onReceive Fire");
        FileLogs.writeLog(context,TAG,"I","onReceive Fire");
        TestLongRunningService.enqueueWork(context,intent);
        //context.startService(new Intent(context,TestNormalService.class));
    }
}
