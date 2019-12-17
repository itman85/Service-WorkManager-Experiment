package phannguyen.sample.serviceexperimental;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.work.ExistingWorkPolicy;

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
        FileLogs.writeLog(context,TAG,"I","onReceive Fire");
        FileLogs.writeLog(context,APP_TAG,"I","Broadcast Receiver Fired");
        //will call start service after delay time instead start immediately
        if(intent.getAction() == Intent.ACTION_BOOT_COMPLETED) {
            FileLogs.writeLog(context,APP_TAG,"I","Device Reboot");
            boolean isStillOn = WorkManagerHelper.checkWorkIsStillOn(context,TASK_DATA_INTERVAL_MAIN_WORKER_UNIQUE_NAME);
            if(isStillOn){
                FileLogs.writeLog(context,APP_TAG,"I","Main Work Interval still ON, Will continue doing later...");
            }else{
                FileLogs.writeLog(context,APP_TAG,"I","Main Work was clear, start new one time work Now");
                WorkManagerHelper.startOneTimeWorker(context, Constant.DELAY_PROCESS_DATA);
            }

        }else {
            TestLongRunningService.enqueueWork(context,intent);
        }
    }
}
