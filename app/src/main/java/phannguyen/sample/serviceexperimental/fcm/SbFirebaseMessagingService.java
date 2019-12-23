package phannguyen.sample.serviceexperimental.fcm;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import phannguyen.sample.serviceexperimental.TestLongRunningService;
import phannguyen.sample.serviceexperimental.WorkManagerHelper;
import phannguyen.sample.serviceexperimental.utils.Constant;
import phannguyen.sample.serviceexperimental.utils.FileLogs;
import phannguyen.sample.serviceexperimental.utils.SbLog;

import static phannguyen.sample.serviceexperimental.utils.Constant.APP_TAG;
import static phannguyen.sample.serviceexperimental.utils.Constant.TASK_DATA_INTERVAL_MAIN_WORKER_UNIQUE_NAME;

public class SbFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FbMessageService";
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        FileLogs.writeLog(this,APP_TAG,"I","Fcm MessageReceived Data: " + remoteMessage.getData());
        /*boolean isStillOn = WorkManagerHelper.checkWorkIsStillOn(this,TASK_DATA_INTERVAL_MAIN_WORKER_UNIQUE_NAME);
        if(isStillOn){
            FileLogs.writeLog(this,APP_TAG,"I","Fcm MessageReceived - Main Work Interval still ON, Will continue doing later...");
        }else{
            FileLogs.writeLog(this,APP_TAG,"I","Fcm MessageReceived - Main Work was clear, start new one time work Now");
            WorkManagerHelper.startOneTimeWorker(this, Constant.QUICK_DELAY_PROCESS_DATA);
        }*/
        TestLongRunningService.enqueueWork(this.getApplicationContext(),new Intent(this.getApplicationContext(),TestLongRunningService.class));
        SbLog.i(TAG,"onMessageReceived Data: " + remoteMessage.getData());
        FileLogs.writeLog(this,TAG,"I","Fcm MessageReceived Data: " + remoteMessage.getData());

    }

    @Override
    public void onNewToken(@NonNull String refreshedToken) {
        super.onNewToken(refreshedToken);
        SbLog.i(TAG,"fcmToken "+ refreshedToken);
        FileLogs.writeLog(this,"fcmToken","I","fcmToken "+ refreshedToken);
    }
}

