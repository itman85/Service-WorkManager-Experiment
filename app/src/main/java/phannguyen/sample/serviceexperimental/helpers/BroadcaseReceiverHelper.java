package phannguyen.sample.serviceexperimental.helpers;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import phannguyen.sample.serviceexperimental.broadcast.MainBroadcastReceiver;
import phannguyen.sample.serviceexperimental.utils.Constant;

public class BroadcaseReceiverHelper {

    public static PendingIntent getPendingIntentForMainBroadcastReceiver(Context context) {
        Intent alarmIntent = new Intent(context, MainBroadcastReceiver.class);
        alarmIntent.setAction(Constant.BROADCAST_CUSTOM_ACTION);
        return PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    }
}
