package phannguyen.sample.serviceexperimental.helpers;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import phannguyen.sample.serviceexperimental.broadcast.MainBroadcastReceiver;
import phannguyen.sample.serviceexperimental.utils.Constant;

import static phannguyen.sample.serviceexperimental.utils.Constant.PENDING_INTENT_MAIN_BROADCAST_RECEIVER_REQUEST_CODE;

public class BroadcaseReceiverHelper {

    //get current pending intent, no create if not existed
    public static PendingIntent getPendingIntentForMainBroadcastReceiver(Context context) {
        Intent alarmIntent = new Intent(context, MainBroadcastReceiver.class);
        alarmIntent.setAction(Constant.BROADCAST_CUSTOM_ACTION);
        return PendingIntent.getBroadcast(context, PENDING_INTENT_MAIN_BROADCAST_RECEIVER_REQUEST_CODE, alarmIntent, PendingIntent.FLAG_NO_CREATE);// get current and no create
    }

    // create new one, update current if existed
    public static PendingIntent createPendingIntentForMainBroadcastReceiver(Context context) {
        Intent alarmIntent = new Intent(context, MainBroadcastReceiver.class);
        alarmIntent.setAction(Constant.BROADCAST_CUSTOM_ACTION);
        return PendingIntent.getBroadcast(context, PENDING_INTENT_MAIN_BROADCAST_RECEIVER_REQUEST_CODE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);// keep current alarm instead of creating new one
    }

    /**
     *
     * @param context
     * @return true if alarm set, otherwise false
     */
    public static boolean checkMainBroadcastReceiverIsWorking(Context context){
        Intent alarmIntent = new Intent(context, MainBroadcastReceiver.class); //same alarmIntent with getPendingIntentForMainBroadcastReceiver
        alarmIntent.setAction(Constant.BROADCAST_CUSTOM_ACTION);
        return (PendingIntent.getBroadcast(context, PENDING_INTENT_MAIN_BROADCAST_RECEIVER_REQUEST_CODE, alarmIntent, PendingIntent.FLAG_NO_CREATE) != null); // use FLAG_NO_CREATE flag
    }

}
