package phannguyen.sample.serviceexperimental.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import phannguyen.sample.serviceexperimental.helpers.ServiceHelper;
import phannguyen.sample.serviceexperimental.helpers.WorkManagerHelper;
import phannguyen.sample.serviceexperimental.utils.Constant;
import phannguyen.sample.serviceexperimental.utils.FileLogs;
import phannguyen.sample.serviceexperimental.utils.SbLog;

import static phannguyen.sample.serviceexperimental.utils.Constant.APP_TAG;
import static phannguyen.sample.serviceexperimental.utils.Constant.TASK_DATA_INTERVAL_MAIN_WORKER_UNIQUE_NAME;

public class TestBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "TestBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        SbLog.i(TAG,"onReceive Fire");
        FileLogs.writeLogInThread(context,TAG,"I","onReceive Fire");
        FileLogs.writeLogInThread(context,APP_TAG,"I","Broadcast Receiver Fired");
        //will call start service after delay time instead start immediately
        if(intent.getAction() == Intent.ACTION_BOOT_COMPLETED) {
            FileLogs.writeLogInThread(context,APP_TAG,"I","Device Reboot");
            boolean isStillOn = WorkManagerHelper.checkWorkIsStillOn(context,TASK_DATA_INTERVAL_MAIN_WORKER_UNIQUE_NAME);
            if(isStillOn){
                FileLogs.writeLogInThread(context,APP_TAG,"I","Main Work Interval still ON, Will continue doing later...");
            }else{
                //FileLogs.writeLogNoThread(context,APP_TAG,"I","Main Work was clear, start new one time work Now");
                //WorkManagerHelper.startOneTimeWorker(context, Constant.DELAY_PROCESS_DATA);
                FileLogs.writeLogInThread(context,APP_TAG,"I","Main Work was clear, start long running service Now");
                ServiceHelper.startLongRunningServiceInBackground(context, intent);
            }

        }else {
            //TestLongRunningService.enqueueWork(context,intent);
            ServiceHelper.startLongRunningServiceInBackground(context, intent);
        }
    }
}
