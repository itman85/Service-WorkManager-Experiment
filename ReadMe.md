# Những yêu cầu về cách hoạt động của broadcast receiver and service:
1. Broadcast receiver:
- Fire as device reboot completed
- Fire as call start broadcast receiver with specific custom action

2. Service
- Run to finish even app close, run on other process
- Run background
- Restart after killed by OS, sometime will restart as schedule no need restart immediately

# Những gì quan sát được:

# Some tech learned info
*** Android 8 việc run background service bị limit nên giải pháp là sài JobIntentService, nó sẽ tự tương thích với từng os version, nếu từ android
When running on Android O or later, the work will be dispatched as a job via JobScheduler.enqueue. When running on older versions of the platform, it will use Context.startService.
You do not need to use WakefulBroadcastReceiver when using this class. When running on Android O, the JobScheduler will take care of wake locks for you (holding a wake lock from the time you enqueue work until the job has been dispatched and while it is running). When running on previous versions of the platform, this wake lock handling is emulated in the class here by directly calling the PowerManager; this means the application must request the WAKE_LOCK permission.

JobIntentService:
- the test service will killed as app closed but then (after avg 30s) this service will schedule to re-create and handle of course application onCreate will be called first of all
- Android 8: in manifest service add android:stopWithTask="true" or "false", android:process=":testservice" not affect anything, behavior is same
- Android < 8: phải add permission WAKE_LOCK, Android 8+ thì ko cần.
- Android < 8 thì android:stopWithTask="true" kill service luon(khac voi android 8), android:stopWithTask="false" va android:process=":testservice" se restart lai service khi app closed, tat nhien application call oncreate again

---> Kết luận: Sài JobIntentService cho Android -8+ (với android -8 thì nhớ call stopSelf()) Thêm android:stopWithTask="false" va android:process=":testservice"
Thì service sẽ hoat động đôc lập in another process ,nếu app killed thì service sẽ dc reschedule ngay sau đó để start lại.

# Broadcase receiver whitelist android 8+
https://developer.android.com/guide/components/broadcast-exceptions.html

# Để nhận dc broadcast receiver cho event BOOT_COMPLETED cần thêm
"<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />"
- Android 8 : Khi device 1 thời gian dài user ko interact with app thì khi reboot có hiện tượng ko receive được event

# Work manager run in multiprocess application need to remove default workmangger initiallizer and use custom configuration instead
now use workmanager 2.2.0 +
https://developer.android.com/topic/libraries/architecture/workmanager/advanced/custom-configuration

# Checklist
 1. Broadcast receiver fire khi click button start -> Start service -> in background và cả khi app closed -> End service (after 3mins)
 2. Broadcast receiver fire khi reboot  -> Start service -> End service (after 3mins)
 3. Work manager call sau khi service finish -> 15p sau sẽ do work -> start service -> end service -> lặp lại quá trình này
 Xem nó ghi log trong bao nhiêu ngày.

# Report result for Android 7, 8, 9: ???
- Android 9 không start JobIntentService background từ broadcast receiver 
- Android 8,9 work ok with this approach, test with android 7?

# App Start|--(1)Running Service--|------------(2)Waiting for worker invoke-------|--Running Service--|------------Waiting for worker invoke-------\---------
(1): khi reboot at this moment thi work chua enqueued va no se start one time work khi reboot
(2): khi reboot at this moment thi work da enqueued, nen se waiting until main interval worker doWork  

# Note khi start JobIntentService
- Android 8 xaỷ ra hiện tượng khi worker doWork End nhưng service vẫn ko start, là do service đã enqueued rồi nhưng ko được schedule 
để start và nó phải mất 1 khoảng lâu sau mới start và start worker interval lại sau khi service destroy. Đây là vấn đề của android 8,9 
khi execute service trong background nó sẽ ko start liền. test xem tình huống này có được giải quyết nếu thêm permisson REQUEST_IGNORE_BATTERY_OPTIMIZATIONS

# FCM push Note 
- Khi send push data message thì thêm priority kết hợp với REQUEST_IGNORE_BATTERY_OPTIMIZATIONS để xem device có được wakeup dù đang trong sleep doze mode ko?

#######################################################################

# Chia lam 3 group về background schedule job interval:
*NOTE: kỹ thuật để tạo job interval hiện tại đang dùng work manager, khi device reboot , fcm push (khi job work manager died nhiều ngày)
- Android 5
- Android 6,7(Có Doze): xem nếu cần thiết sẽ viết lại interval job chỉ cho nhóm này
- Android 8,9,10+


# Chia lam 3 group về background service execute limitation
*Note: Kỹ thuật hiện tại đang dùng jobintentservice
- Android 5
- Android 6,7(Có Doze): xem lại nếu cần sẽ viết lại start service background chỉ cho nhóm này
- Android 8,9,10+

# Chia lam 3 group về background tracking: location, geo fencing, user activities
- Android 5,6,7: Sẽ viết lai background tracking cho nhóm này.
- Android 8,9
- Android 10+

# cases cần test cho các device
1. start service khi click button - kill app xem service có tự restart lại ko?
2. start service từ push fcm
3. start service từ reboot
4. start service tu push fcm khi device in doze mode (android 6+), có thể giả lập device goes into doze mode

- Android 5 : pass 1,2,3 ; 1: FAIL khi kill app ngay khi service đang running thì nó ko restart (do start service old way)
- Android 6 : pass 1,2,3 ; 1: FAIL khi kill app ngay khi service đang running thì nó ko restart (do start service old way)
- Android 7 : pass 1,2,3 ; 1: FAIL khi kill app ngay khi service đang running thì nó ko restart (do start service old way)
- Android 9 : pass 1,2,3 ; 1: OK khi kill app ngay khi service đang running thì nó có schedule để restart (do start service new way for android 8+)

# How service will run
- Service chỉ start khi cách lần done trước lâu hơn interval
- Service đang run thì nếu start tiếp sẽ phải skip ko xử lý nữa
- Khi service run xong done thì sẽ schedule cho lần start tiếp theo follow this rule:
  + Replace existing schedule để create new schedule
- Khi trigger từ reboot, fcm push, accessibility service thì start service ngay.

