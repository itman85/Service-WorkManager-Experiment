#Requirements for broadcast receiver and service:
1. Broadcast receiver:
- Fire as device reboot completed
- Fire as call start broadcast receiver with specific custom action

2. Service
- Run to finish even app close, run on other process
- Run background

#Some tech learned info
*** Android 8 việc run background service bị limit nên giải pháp là sài JobIntentService, nó sẽ tự tương thích với từng os version, nếu từ android
When running on Android O or later, the work will be dispatched as a job via JobScheduler.enqueue. When running on older versions of the platform, it will use Context.startService.
You do not need to use WakefulBroadcastReceiver when using this class. When running on Android O, the JobScheduler will take care of wake locks for you (holding a wake lock from the time you enqueue work until the job has been dispatched and while it is running). When running on previous versions of the platform, this wake lock handling is emulated in the class here by directly calling the PowerManager; this means the application must request the WAKE_LOCK permission.

JobIntentService:
- the test service will killed as app closed but then (after avg 30s) this service will schedule to re-create and handle of course application onCreate will be called first of all
- Android 8: in manifest service add android:stopWithTask="true" or "false", android:process=":testservice" not affect anything, behavior is same
- Android < 8: phải add permission WAKE_LOCK, Android 8+ thì ko cần.
- Android < 8 thì android:stopWithTask="true" kill service luon(khac voi android 8), android:stopWithTask="false" va android:process=":testservice" se restart lai service khi app closed, tat nhien application call oncreate again

---> Kết luận: Sài JobIntentService cho Android -8+ (với android -8 thì nhớ call stopSelf()) Thêm android:stopWithTask="false" va android:process=":testservice"
Thì service sẽ hoat động đôc lập ,nếu app killed thì service sẽ dc reschedule ngay sau đó để start lại.

#Broadcase receiver whitelist android 8+
https://developer.android.com/guide/components/broadcast-exceptions.html

# Để nhận dc broadcast receiver cho event BOOT_COMPLETED cần thêm
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
Work cho Android -8+



