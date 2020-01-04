package phannguyen.sample.serviceexperimental.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import phannguyen.sample.serviceexperimental.helpers.ServiceHelper;
import phannguyen.sample.serviceexperimental.utils.FileLogs;
import phannguyen.sample.serviceexperimental.utils.SbLog;

import static phannguyen.sample.serviceexperimental.utils.Constant.APP_TAG;

public class RebootBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "RebootBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        SbLog.i(TAG,"onReceive Fire");
        FileLogs.writeLogInThread(context,TAG,"I","onReceive Fire Just start service now");
        FileLogs.writeLogInThread(context,APP_TAG,"I","Broadcast Receiver Fired Just start service now");
        // as device reboot, so start service now, if service run util done then replace current schedule service
        ServiceHelper.startLongRunningServiceInBackground(context, intent);
    }
}
