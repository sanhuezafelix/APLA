package mc.mCongressDemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.request.QBCustomObjectRequestBuilder;
import com.quickblox.module.chat.QBChatService;
import com.quickblox.module.chat.model.QBDialog;
import com.quickblox.module.chat.model.QBDialogType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class mReciverDialogsChat extends Service {
    private static final long _MILLIS_INTERVAL_SYNC = 5000;

    public static final String SYNC = "sync";
    private int result = MainActivity.RESULT_CANCELED;
    private  final IBinder mBinder = new myBinder();
    int count = 0;
    private ArrayList<QBDialog> filterDialog = new ArrayList<QBDialog>();

    private Timer timer = new Timer();
    private NotificationManager mManager;
    private Random r = new Random();








    @Override
    public void onCreate() {
        super.onCreate();
        pullSyncDialogChat();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Local Service", "Received start id " + startId + ": " + intent);
        //Queremos que el servicio continúe ejecutándose hasta que es explícitamente parado, así que devolvemos sticky
        return START_STICKY;
    }

    /*

         */
    public void pullSyncDialogChat(){
        long i1 = r.nextInt(1000 - 500) + 1000;
        Log.i("servicio","delay task ==> "+i1);

        timer.scheduleAtFixedRate(new TimerTask() {
            private Handler mHandler = new Handler(Looper.getMainLooper());

            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    public void run() {
                        Log.i("servicio","corriendo");
                        loasAllChatAlone();
                        count++;


                    }
                });
            }
        }, i1, _MILLIS_INTERVAL_SYNC);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    @Override
    public void onDestroy() {

        super.onDestroy();
        if (timer != null){
            timer.cancel();
            Log.i("servicio_timer","timer detenido");

        }

    }

    public  class myBinder extends Binder{
        public mReciverDialogsChat getService(){
            return mReciverDialogsChat.this;
        }
    }

    public void loasAllChatAlone(){

        Log.i("servicio_timer", "load all chat");

        QBCustomObjectRequestBuilder customObjectRequestBuilder = new QBCustomObjectRequestBuilder();
        customObjectRequestBuilder.setPagesLimit(100);
        QBChatService.getChatDialogs(QBDialogType.GROUP, customObjectRequestBuilder, new QBEntityCallbackImpl<ArrayList<QBDialog>>() {

            @Override
            public void onSuccess(ArrayList<QBDialog> dialogs, Bundle params) {
                ((myApp)getApplication()).setAllDialog(dialogs);
                for (QBDialog dialog : dialogs) {
                    if (dialog.getUnreadMessageCount() > 0) {
                        Integer userId = dialog.getLastMessageUserId();
                        if (dialog.getOccupants().size() <3) {
                            Log.i("servicio_timer", "ocupantes" + dialog.getOccupants().size());

                                showNotification(userId, ((myApp) getApplication()).getDialogUserName(userId), dialog.getLastMessage(), dialog.getUnreadMessageCount(), dialog.getDialogId(),"privado");



                        } else {

                                showNotification(userId, ((myApp) getApplication()).getDialogUserName(userId), dialog.getLastMessage(), dialog.getUnreadMessageCount(), dialog.getDialogId(), "grupal");

                        }

                        Log.i("servicio_timer", "mensajes sin leer" + dialog.getUnreadMessageCount());


                    } else {
                        Log.i("servicio_timer", "sin mensajes");


                    }
                }

            }

            @Override
            public void onError(List<String> errors) {
                Log.i("servicio_timer", "error " + errors);
            }
        });

    }


    private void showNotification(Integer userId ,String Username,String lastMessage , Integer counter, String dialogId,String type){
        mManager = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);

        Notification notification = new Notification(R.drawable.ic_launcher,"Le han enviado un mensaje", System.currentTimeMillis());
        Intent intent1 = new Intent(this.getApplicationContext(),MainActivity.class);
        intent1.putExtra("chat_view",true);
        intent1.putExtra("dialogId",dialogId);
        intent1.putExtra("typeDialog",type);

        //intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this.getApplicationContext(), userId, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_LIGHTS;

        notification.number= counter;
        notification.setLatestEventInfo(this.getApplicationContext(),Username, lastMessage, pendingNotificationIntent);
        mManager.notify(userId, notification);
    }
}
