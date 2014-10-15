package mc.mCongressDemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.quickblox.core.QBCallback;
import com.quickblox.core.QBCallbackImpl;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.QBSettings;
import com.quickblox.core.result.Result;
import com.quickblox.module.auth.QBAuth;
import com.quickblox.module.auth.model.QBSession;
import com.quickblox.module.chat.QBChatService;
import com.quickblox.module.users.QBUsers;
import com.quickblox.module.users.model.QBUser;
import com.quickblox.module.users.result.QBUserResult;

import org.jivesoftware.smack.SmackException;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by luisgonzalez on 27-03-14.
 */
public class SplashScreenActivity extends Activity {

    static final int AUTO_PRESENCE_INTERVAL_IN_SECONDS = 30;
    private QBChatService chatService;
    private static final long SPLASH_SCREEN_DELAY = 2000;
    private static final String APP_ID = "14221";
    private static final String AUTH_KEY = "DWqtA-Yp3YyZ-NN";
    private static final String AUTH_SECRET = "VMa7xEkVuPdPk-S";
    private static final String USER_LOGIN = "agustin";
    private static final String USER_PASSWORD = "prueba0199";
    //private static final String USER_LOGIN = "rositauss@gmail.com";
    //private static final String USER_PASSWORD = "rosirosi";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);



        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent mainIntent = new Intent().setClass(
                        SplashScreenActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        };


        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
        }
      /*  QBChatService.setDebugEnabled(true);


        QBSettings.getInstance().fastConfigInit(APP_ID, AUTH_KEY, AUTH_SECRET);
        if (!QBChatService.isInitialized()) {
            QBChatService.init(this);
        }
        chatService = QBChatService.getInstance();


        // create QB user
        //
       final QBUser user = new QBUser();

        user.setLogin(USER_LOGIN);
        user.setPassword(USER_PASSWORD);






        QBAuth.createSession(user, new QBEntityCallbackImpl<QBSession>(){
            @Override
            public void onSuccess(QBSession session, Bundle args) {

                // save current user
                //
                user.setId(session.getUserId());
                ((myApp)getApplication()).setCurrentUser(user);

                // login to Chat
                //
                loginToChat(user);
            }

            @Override
            public void onError(List<String> errors) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(SplashScreenActivity.this);
                dialog.setMessage("create session errors: " + errors).create().show();
            }
        });



    }

    private void loginToChat(final QBUser user){

        chatService.login(user, new QBEntityCallbackImpl() {
            @Override
            public void onSuccess() {

                // Start sending presences
                //
                try {
                    chatService.startAutoSendPresence(AUTO_PRESENCE_INTERVAL_IN_SECONDS);
                } catch (SmackException.NotLoggedInException e) {
                    e.printStackTrace();
                }

                // go to Dialogs screen
                //
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(List errors) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(SplashScreenActivity.this);
                dialog.setMessage("chat login errors: " + errors).create().show();
            }
        });
    }*/


}
