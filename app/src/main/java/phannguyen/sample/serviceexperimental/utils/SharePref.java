package phannguyen.sample.serviceexperimental.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePref {
    private static final String PREF_NAME = "TestSharePrefs";

    public static void setStartTaskTime(Context context, long time) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("startTaskTime", time);
        editor.apply();
    }

    public static long getStartTaskTime(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getLong("startTaskTime", 0);
    }

    public static void setEndTaskTime(Context context, long time) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("endTaskTime", time);
        editor.apply();
    }

    public static long getEndTaskTime(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getLong("endTaskTime", 0);
    }

    public static void setServiceNumber(Context context, long serviceNumber) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("serviceNo", serviceNumber);
        editor.apply();
    }

    public static long getServiceNumber(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getLong("serviceNo", 0);
    }

    public static void setTaskLoopNth(Context context, int loopNth) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("loopNth", loopNth);
        editor.apply();
    }

    public static int getTaskLoopNth(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("loopNth", 0);
    }
}
