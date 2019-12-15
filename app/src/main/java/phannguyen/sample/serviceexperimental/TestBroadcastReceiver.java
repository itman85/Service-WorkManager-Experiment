package phannguyen.sample.serviceexperimental;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import phannguyen.sample.serviceexperimental.utils.FileLogs;
import phannguyen.sample.serviceexperimental.utils.SbLog;

import static phannguyen.sample.serviceexperimental.utils.Constant.APP_TAG;

public class TestBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "TestBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        SbLog.i(TAG,"onReceive Fire");
        FileLogs.writeLog(context,TAG,"I","onReceive Fire");
        FileLogs.writeLog(context,APP_TAG,"I","Broadcast Receiver Fired");
        TestLongRunningService.enqueueWork(context,intent);
        //context.startService(new Intent(context,TestNormalService.class));
    }
}
