package mc.mCongressDemo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

import DB.AssetDatabaseHelper;
import DB.DBHelper;

import DB.mCongreso;
import Fragments.NovedadesFragmentView;
import Fragments.chat.mDialogsUsersChat;
import Fragments.chat.mListUsersChat;
import Fragments.chat.mLoginChat;
import Fragments.chat.mMainchatViewController;
import Fragments.chat.mNewChatFragmentView;
import Fragments.mAhoraFragmentView;
import Fragments.mFavoritosFragmentView;
import Fragments.mMenuLeftFragmentView;
import Fragments.mProgramaFragmentView;
import Utils.ClearableEditText;
import Fragments.mGeneralSearchViewController;
import Utils.Utilidades;

import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.PushService;
import com.google.analytics.tracking.android.EasyTracker;
import com.quickblox.core.QBCallback;
import com.quickblox.core.QBCallbackImpl;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.QBSettings;


import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.request.QBCustomObjectRequestBuilder;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.core.server.BaseService;
import com.quickblox.module.auth.QBAuth;
import com.quickblox.module.auth.model.QBSession;
import com.quickblox.module.chat.QBChatService;
import com.quickblox.module.chat.QBRoster;
import com.quickblox.module.chat.listeners.QBRosterListener;
import com.quickblox.module.chat.listeners.QBSubscriptionListener;
import com.quickblox.module.chat.model.QBDialog;
import com.quickblox.module.chat.model.QBDialogType;
import com.quickblox.module.chat.model.QBPresence;
import com.quickblox.module.users.QBUsers;
import com.quickblox.module.users.model.QBUser;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;


public class MainActivity extends SlidingFragmentActivity implements RadioGroup.OnCheckedChangeListener ,View.OnClickListener {


    private static final long SPLASH_SCREEN_DELAY = 3000;
    private ImageButton btn_menu;
    private ImageButton btn_search;
    private ImageButton btn_fav_view;
    private RadioGroup btn_Segment;
    private ImageButton btn_stand;
    private RadioGroup selected_chat;
    private ArrayList<QBDialog> filterDialog = new ArrayList<QBDialog>();

    private Fragment mContent;
    private Fragment mPrgram;
    private mMenuLeftFragmentView menuleft;
    private DBHelper dbHelper;
    private long lastBackPressTime = 0;
    private Toast toast;


    // current Congress
    private int current_id;
    private String current_congress;

    private RelativeLayout top_bar;
    private RelativeLayout botom_bar;
    private MainActivity actividad;
    private Cursor pagerCursor;
    private myApp app;
    final ArrayList<String> elemento = new ArrayList<String>();
    private TextView titulo;
    private boolean isGeneralSearchVisible;
    public boolean isFavoriteViewVisible;
    private int currentPage = 0;
    private int counter=0;
    private List<QBUser> users = new ArrayList<QBUser>();

    private Handler customHandler = new Handler();
    private ImageView iv_sponsor;
    private Action lastAction;
    private ButtonAction lastActionChat = ButtonAction.USURIAOS;
    private serviceAction lastActionService = serviceAction.NO_CHAT;

    // chat

    static final int AUTO_PRESENCE_INTERVAL_IN_SECONDS = 10;
    private QBChatService chatService;

    private static final String APP_ID = "14221";
    private static final String AUTH_KEY = "DWqtA-Yp3YyZ-NN";
    private static final String AUTH_SECRET = "VMa7xEkVuPdPk-S";
    /*private static final String APP_ID = "14816";
    private static final String AUTH_KEY = "UPFDVQuHgOOkVev";
    private static final String AUTH_SECRET = "6Y78TDkcfEhT4wW";*/



    // Roster
    //
    private QBRoster сhatRoster;
    private QBRosterListener rosterListener;
    private QBSubscriptionListener subscriptionListener;

    private String firtsnew = ":Estimados(as):Bienvenidos(as) a XLVIII Congreso Nacional de Nefrología 2014";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.actividad = this;
        this.app = (myApp)getApplicationContext();


        if(app.loadArray("Notificaciones", this).length==0)
        {
            elemento.add(this.firtsnew);
            app.saveArray(elemento, "Notificaciones", this);
            //Toast.makeText(context, "se guardaron "+ elemento.size()+ "elementos",Toast.LENGTH_SHORT).show();
            elemento.clear();
        }

        /*
                PARSE
         */


        // instalacion push
        ParseAnalytics.trackAppOpened(getIntent());
        PushService.setDefaultPushCallback(this, MainActivity.class);
        PushService.subscribe(this, "APLA2014", MainActivity.class);
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseInstallation installation = ParseInstallation
                .getCurrentInstallation();


        AssetDatabaseHelper dbAssestHelper = new AssetDatabaseHelper(
                getBaseContext(), "cnm_2014.sqlite");
        try {
            if (!dbAssestHelper.checkExist()){
                dbAssestHelper.importIfNotExist();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        // Iniciamos Base de datos
        dbHelper = new DBHelper(this);
        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.frame_menu);
        this.top_bar = (RelativeLayout)findViewById(R.id.navi_bar);
        this.iv_sponsor = (ImageView)findViewById(R.id.publicidad_main);
        this.iv_sponsor.setImageResource(R.drawable.publicidad_abajo1);
        this.botom_bar= (RelativeLayout)findViewById(R.id.bottom_bar);
        this.titulo = (TextView)findViewById(R.id.titulos_vistas);
        btn_menu = (ImageButton) findViewById(R.id.btn_menu);
        btn_menu.setOnClickListener(this);
        selected_chat = (RadioGroup) findViewById(R.id.radio_chat);
        selected_chat.setOnCheckedChangeListener(this);
        selected_chat.setVisibility(View.GONE);
        btn_search = (ImageButton) findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
        btn_fav_view = (ImageButton) findViewById(R.id.btn_fav_view);
        btn_stand = (ImageButton) findViewById(R.id.btn_stand_view);
        btn_stand.setOnClickListener(this);
        btn_fav_view.setOnClickListener(this);
        btn_Segment = (RadioGroup) findViewById(R.id.segment_btn);
        /* configuracion slidemenu */

        getSlidingMenu().setMode(SlidingMenu.LEFT);
        menuleft = new mMenuLeftFragmentView();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_menu, menuleft).commit();
      /*  getSlidingMenu().setSecondaryMenu(R.layout.frame_stand);
        getSlidingMenu().getSecondaryMenu().setActivated(false);
        getSlidingMenu().setSecondaryShadowDrawable(R.drawable.shadow_rigth);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_stand, new mStandListViewController()).commit();*/
        SlidingMenu sm = getSlidingMenu();
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setBehindScrollScale(0.25f);
        sm.setFadeDegree(0.0f);


        /* Eventos de los Botones de la Barra de Navegacion */

        btn_Segment.setOnCheckedChangeListener(this);
        // Utilidades.animate(iv_sponsor, getRandomResourceId(app.last_resouce_id_menu),getSponsorVisibility());
        app.last_update_time_menu = SystemClock.uptimeMillis();
        //  customHandler.postDelayed(sponsor_thread, 0);

        getSelectedSegmentIndex();
        Bundle reicieveParams = getIntent().getExtras();

        if (savedInstanceState == null) {
            if (reicieveParams != null){

                Log.d("Tag", "La actividad es push ");
                if (reicieveParams.getBoolean("fav_view")){
                    Log.d("Tag", "La actividad es alarma ");

                    app.setFavoriteAlarm(true);
                }
                else {
                    if (reicieveParams.getBoolean("chat_view")){
                        setServiceAction(serviceAction.CHAT);

                       // menuChangeView(mLoginChat.getInstance(this,true,reicieveParams.getString("dialogId"),reicieveParams.getString("typeDialog")),true);
                        menuChangeView(new mListUsersChat(),true);
                        stopService();
                        app.setFavoriteAlarm(false);


                    }
                    else {
                        menuChangeView(NovedadesFragmentView.newInstance(this, 0),true);
                        app.setFavoriteAlarm(false);
                        Log.d("Tag", "La actividad no es push ");
                    }

                }
                app.setpushView(true);

            }
            else {
                Log.d("Tag", "La actividad no es push ");
                app.setpushView(false);
                Cursor cursito = getDbHandler().getCongress();
                mCongreso congress = new mCongreso(cursito);
                if (congress.isCurrentCongress())
                {
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.content_frame, mAhoraFragmentView.newInstance(this, 1, false))
                            .commit();
                    setSelectedChat(R.id.segment_button_one);
                }
                else {
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.content_frame, mProgramaFragmentView.newInstance(this, 1))
                            .commit();
                    setSelectedChat(R.id.segment_button_two);
                }
            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        int pos = getSupportFragmentManager().getBackStackEntryCount();

        switch (pos){
            case 0:
                if (this.lastBackPressTime < System.currentTimeMillis() - 2000) {
                    toast = Toast.makeText(this, getString(R.string.back),Toast.LENGTH_SHORT);
                    toast.show();
                    this.lastBackPressTime = System.currentTimeMillis();
                } else {
                    if (toast != null) {
                        toast.cancel();
                    }
                    super.onBackPressed();
                }


                break;
            case 1:
                UnSetBackButton();
                super.onBackPressed();
                break;
           /* case 2:
                UnSetBackButton();
                super.onBackPressed();
                break;*/
            default:
                super.onBackPressed();
                break;
        }
    }

    /* Metodos de configuracion Barra de navegacion*/


    public void setBackbutton(){

        btn_menu.setImageResource(R.drawable.icon_back);
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFavoriteViewVisible){
                    isFavoriteViewVisible=false;
                }
                onBackPressed();
            }
        });
    }
    public void UnSetBackButton(){

        btn_menu.setImageResource(R.drawable.icon_menu);
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSlidingMenu().showMenu();
            }
        });
    }
    public void setHideNavigationBar(){
        //  btn_menu.setVisibility(View.INVISIBLE);
        btn_search.setVisibility(View.INVISIBLE);
        btn_Segment.setVisibility(View.INVISIBLE);

    }
    public void unHideNavigationBar(){
        // btn_menu.setVisibility(View.VISIBLE);
        // btn_stand.setVisibility(View.VISIBLE);
        btn_Segment.setVisibility(View.VISIBLE);
        btn_search.setVisibility(View.VISIBLE);
        this.titulo.setVisibility(View.GONE);
    }
    public void unSelectedSegmentedIndex(){

        this.btn_Segment.clearCheck();


    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

        switch (i){
            case R.id.segment_button_one:
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame,mAhoraFragmentView.newInstance(this,1,false)).addToBackStack(null)
                        .commit();
                this.lastAction = Action.AHORA;
                break;
            case R.id.segment_button_two:
                this.lastAction = Action.PROGRAMA;
                getSupportFragmentManager().popBackStack();

                /*if (this.getCurrentCongress().equals("abuelo")){
                    mPrgram = mProgramaABueloFragmentView.newInstance(this,this.getCurrentId(),false);

                }
                else {
                    mPrgram = mProgramaFragmentView.newInstance(this, this.getCurrentId());
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, mPrgram).addToBackStack(null)
                        .commit();*/
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, mProgramaFragmentView.newInstance(this, 1)).addToBackStack(null)
                        .commit();
                // mPrgram = mProgramaABueloFragmentView.newInstance(this,1,false);

                break;
            case R.id.rooms:

                changueChatView(new mMainchatViewController());
                break;



            case R.id.users:
                changueChatView(mListUsersChat.getInstance(this));
                break;
            case R.id.dialogs:
                changueChatView(new mDialogsUsersChat());
                break;
        }
    }

    public  void PushView(Fragment fragment){

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment).addToBackStack(null)
                .commit();
    }
    public void menuChangeView(Fragment fragment,boolean a){
        if (a == true){
            setVisibilityContentBotonNavbar(View.VISIBLE);
            setVisibilityChangeViewChat(View.GONE);
            Log.v("_sponsor","sponsor visible - chat visible");
            setServiceAction(serviceAction.NO_CHAT);
            initService();

        }
        else {
            setVisibilityContentBotonNavbar(View.GONE);
            stopService();
            //setVisibilityChangeViewChat(View.VISIBLE);
            Log.v("_sponsor","sponsor visible - chat invisible");
        }
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment).addToBackStack(null)
                .commit();
    }
    public void changueChatView(Fragment fragment){
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment).addToBackStack(null)
                .commit();
    }

    public void setCurrentCurrentId(int id ){

        this.current_id = id;
        //this.current_congress = congress;

    }
    public int getCurrentId(){
        return this.current_id;
    }
    public void setCurrentCongress(String congress){
        this.current_congress = congress;
    }

    public String getCurrentCongress(){
        return  this.current_congress;
    }

    public int getSelectedSegmentIndex(){

        return  this.btn_Segment.getCheckedRadioButtonId();

    }
    public void setSelectedSegmentIndex(int i){

        this.btn_Segment.check(i);

    }
    public void setSelectedchatIndex(int i){

        this.selected_chat.check(i);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_menu:
                if (getSlidingMenu().isMenuShowing()) {
                    getSlidingMenu().showContent();
                } else {
                    getSlidingMenu().showMenu();
                }
                break;
            case R.id.btn_search:
                if (!this.isGeneralSearchVisible){
                    this.isGeneralSearchVisible = true;


                    getSupportFragmentManager().popBackStack();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, mGeneralSearchViewController.newInstance(1)).addToBackStack(null)
                            .commit();

                }
                else {
                    this.isGeneralSearchVisible = false;
                    if (getSelectedSegmentIndex()==R.id.segment_button_one){
                        getSupportFragmentManager().popBackStack();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, mAhoraFragmentView.newInstance(actividad,1,false)).addToBackStack(null)
                                .commit();
                    }
                    else {
                        getSupportFragmentManager().popBackStack();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, mProgramaFragmentView.newInstance(actividad,1)).addToBackStack(null)
                                .commit();
                    }

                }
               /* if (this.searchView.getVisibility()== view.GONE){
                    //this.searchView.setVisibility(View.VISIBLE);
                    //this.btn_Segment.setVisibility(View.GONE);
                    setSelectedSegmentIndex(R.id.segment_button_two);
                    getSupportFragmentManager().popBackStack();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, mGeneralSearchViewController.newInstance(1)).addToBackStack(null)
                            .commit();
                }
                else {
                    //this.searchView.setVisibility(View.GONE);
                    this.btn_Segment.setVisibility(View.VISIBLE);
                }*/

                break;
            case R.id.btn_fav_view:
                if (!this.isFavoriteViewVisible){

                    this.isFavoriteViewVisible = true;
                    getSupportFragmentManager().popBackStack();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new mFavoritosFragmentView()).addToBackStack(null)
                            .commit();
                    this.setBackbutton();
                }
                else {
                    this.isFavoriteViewVisible = false;
                    super.onBackPressed();
                }
                break;
            case R.id.btn_stand_view:
                if (getSlidingMenu().isSecondaryMenuShowing()) {
                    getSlidingMenu().showContent();
                } else {
                    getSlidingMenu().showSecondaryMenu();
                }
                break;
        }
    }

    public DBHelper getDbHandler() {
        return this.dbHelper;
    }

    public void  hideBotomAndTopBAr(){
        this.botom_bar.setVisibility(View.GONE);
        this.top_bar.setVisibility(View.GONE);

    }
    public void  hideBotomBAr(){
        this.botom_bar.setVisibility(View.GONE);


    }
    public void  unHideBotomAndTopBar(){
        this.botom_bar.setVisibility(View.VISIBLE);
        this.top_bar.setVisibility(View.VISIBLE);

    }
    public void  unHideBotomBar(){
        this.botom_bar.setVisibility(View.VISIBLE);


    }

    public void setCursorPagerView(Cursor cursor){
        this.pagerCursor = cursor;

    }
    public Cursor getCursorPagerView(){

        return this.pagerCursor;

    }

    public void setTituloVista(String a){
        this.titulo.setText(a);
        this.titulo.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
    }

    /* public void onStartSponsor(){
        // Log.e("sponsor menu start", "start");
         customHandler.postDelayed(sponsor_thread, 0);
         app.last_update_time_menu = SystemClock.uptimeMillis();

     }
     public void onStopSponsor(){
         //Log.e("sponsor menu stop", "stop");
         customHandler.removeCallbacks(sponsor_thread);

     }
     public int getRandomResourceId(int current_id){
         int id = Utilidades.randInt(current_id);
         app.last_resouce_id_menu = id;
         switch (id) {
             case 1:
                 return R.drawable.publicidad_abajo1;
             case 2:
                 return R.drawable.publi_mobicon;

             default:
                 return R.drawable.publi_mobicon;
         }
     }
     //Runable for time counter sponsor
     private Runnable sponsor_thread = new Runnable() {
         public void run() {
             long delta = SystemClock.uptimeMillis() - app.last_update_time_menu;
             app.total_update_time_menu = app.total_update_time_menu + delta;
 //Log.e("run", "total = "+ myApp.total_update_time_main);
             if (app.total_update_time_menu > 3000) {
                 //Log.e("SPONSORED", "ON MENU");
                 app.total_update_time_menu = 0L;
 //Translate image
 //iv_sponsor.setImageResource(getRandomResourceId(myApp.last_resouce_id_menu));
                Utilidades.animate(iv_sponsor, getRandomResourceId(app.last_resouce_id_menu),getSponsorVisibility());
             }
             customHandler.postDelayed(this, 0);
             app.last_update_time_menu = SystemClock.uptimeMillis();
         }

     };*/
    public void onDestroy() {

        super.onDestroy();
    };


     /*
            CHAT

 */




    public int getSelectedChat(){

        return  this.btn_Segment.getCheckedRadioButtonId();

    }
    public void setSelectedChat(int i){

        this.btn_Segment.check(i);

    }
    public void setVisibilityChangeViewChat(int v){
        Log.v("_Visi","visible");
        this.selected_chat.setVisibility(v);
    }
    public void setVisibilityContentBotonNavbar(int v){
        Log.v("_Visi","visible");
        this.iv_sponsor.setVisibility(v);
        this.btn_fav_view.setVisibility(v);
    }
    public int getSponsorVisibility(){
        return this.iv_sponsor.getVisibility();
    }
    public int getChatButtonVisibility(){
        return this.selected_chat.getVisibility();
    }


    /*
    chat user login
     */

    public void initChat(String user_log, final String pass_log){
        // Init Chat
        //
        QBChatService.setDebugEnabled(true);
        QBSettings.getInstance().fastConfigInit(APP_ID, AUTH_KEY, AUTH_SECRET);
        if (!QBChatService.isInitialized()) {
            QBChatService.init(this);
        }
        chatService = QBChatService.getInstance();
        initRosterListener();
        initSubscriptionListener();
        // create QB user
        //
        final QBUser user = new QBUser();
        user.setLogin(user_log);
        user.setPassword(pass_log);





        QBAuth.createSession(user, new QBEntityCallbackImpl<QBSession>() {
            @Override
            public void onSuccess(QBSession session, Bundle args) {

                // save current user
                //
                user.setId(session.getUserId());
                ((myApp) getApplication()).setCurrentUser(user);


                // login to Chat
                try {
                    String token = BaseService.getBaseService().getToken();
                    if(token == null){
                        Log.i("_token"," token es nulo");
                    }
                    else {
                        Log.i("_token"," token es "+token);

                    }
                } catch (BaseServiceException e) {
                    e.printStackTrace();
                }
                //
                loginToChat(user);


            }

            @Override
            public void onError(List<String> errors) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setMessage("create session errors: " + errors).create().show();
            }
        });





    }

    private void loginToChat(final QBUser user) {

        loasAllChat();

        if(user.getExternalId()==null){

            user.setExternalId(user.getId().toString());

            user.setOldPassword(user.getPassword());
            QBUsers.updateUser(user,new QBEntityCallback<QBUser>() {
                @Override
                public void onSuccess(QBUser qbUser, Bundle bundle) {
                    Log.i("rooster_id",qbUser.getExternalId());


                }

                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(List<String> strings) {
                    Log.i("rooster","update error "+ strings);
                }
            });


        }


        chatService.login(user, new QBEntityCallbackImpl() {

            @Override
            public void onSuccess() {

                ((myApp)getApplication()).setUserPreference(user.getLogin(),user.getPassword());
                try {
                    chatService.startAutoSendPresence(AUTO_PRESENCE_INTERVAL_IN_SECONDS);

                    initRoster();



                    // createAlertDialog(user.getLogin() +" "+ user.getPassword());
                } catch (SmackException.NotLoggedInException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(List errors) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setMessage("chat login errors: " + errors).create().show();
            }
        });





    }

    public void createAlertDialog(String text){
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setMessage(text).create().show();

    }

    public static QBPagedRequestBuilder getQBPagedRequestBuilder(int page) {
        QBPagedRequestBuilder requestBuilder = new QBPagedRequestBuilder();
        requestBuilder.setPage(page);
        requestBuilder.setPerPage(100);


        return requestBuilder;
    }



    public enum Action{ AHORA,PROGRAMA}
    public enum ButtonAction{ USURIAOS,GRUPOS,PRIVADOS}
    public enum serviceAction{ CHAT,NO_CHAT}

    public void setLastAction(Action lastAction) {
        this.lastAction = lastAction;
    }
    public void setLastActionChat(ButtonAction lastActionchat) {
        this.lastActionChat = lastActionchat;
    }
    public void setServiceAction(serviceAction lastServiceAction) {
        this.lastActionService = lastServiceAction;
    }
    public Action getLastAction() { return this.lastAction ; }
    public ButtonAction getLastActionChat() { return this.lastActionChat ; }



    public void loadAllUsers(){

        Log.v("_registrados", "cargando usuraios");
        int i;
        for (i = 0; i<8;i++){
            ++currentPage;

            Log.v("_registrados", "for " +i);
            Log.v("_registrados", "currentpage  " +currentPage);

            QBUsers.getUsers(getQBPagedRequestBuilder(currentPage), new QBEntityCallback<ArrayList<QBUser>>() {
                @Override
                public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {

                    users.addAll(qbUsers);
                    counter ++;

                    if (counter == 8){

                        Collections.sort(users, new Comparator<QBUser>() {
                            @Override
                            public int compare(QBUser qbUser, QBUser qbUser2) {

                                int orden = qbUser.getFullName().compareToIgnoreCase(qbUser2.getFullName());


                                return  orden ;



                            }
                        });

                        Log.v("_registrados", "cantidad :"+users.size());
                        ((myApp)getApplication()).setUsuarios_registrados(users);
                        setLastActionChat(ButtonAction.USURIAOS);
                        PushView(new mListUsersChat());

                    }

                }

                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(List<String> strings) {
                    Log.v("_registrados", "error "+strings);

                }
            });


        }

    }

    private void initRoster() {
        this.сhatRoster = chatService.getRoster(QBRoster.SubscriptionMode.mutual, subscriptionListener);
        this.сhatRoster.addRosterListener(rosterListener);
        ((myApp)getApplication()).setchatRoster(this.сhatRoster);
        sendPrecesence();

    }

    private void initRosterListener(){
        rosterListener = new QBRosterListener() {
            @Override
            public void entriesDeleted(Collection<Integer> userIds) {
                Log.i("rooster","entriesDeleted: " + userIds);
            }

            @Override
            public void entriesAdded(Collection<Integer> userIds) {
                Log.i("rooster","entriesAdded: " + userIds);
            }

            @Override
            public void entriesUpdated(Collection<Integer> userIds) {
                Log.i("rooster","entriesUpdated: " + userIds);
            }

            @Override
            public void presenceChanged(QBPresence presence) {
                Log.i("rooster","presenceChanged: " + presence);
            }
        };
    }

    private void initSubscriptionListener(){
        subscriptionListener = new QBSubscriptionListener() {
            @Override
            public void subscriptionRequested(int userId) {
                Log.i("rooster","subscriptionRequested: " + userId);
            }
        };
    }

    public void sendPrecesence(){
        QBPresence presence = new QBPresence(QBPresence.Type.online);
        try {
            this.сhatRoster.sendPresence(presence);
            Log.i("rooster","presencia enviada " + presence.getType());
        } catch (SmackException.NotConnectedException e) {
            Log.i("rooster","error: " + e.getClass().getSimpleName());
        }
    }
    public void setEnablesRadioGruopChat(Boolean checked){

        for (int i = 0; i < selected_chat.getChildCount(); i++) {
            selected_chat.getChildAt(i).setEnabled(checked);
        }
    }
    public void loasAllChat(){
        QBCustomObjectRequestBuilder customObjectRequestBuilder = new QBCustomObjectRequestBuilder();
        customObjectRequestBuilder.setPagesLimit(100);
        QBChatService.getChatDialogs(QBDialogType.GROUP, customObjectRequestBuilder, new QBEntityCallbackImpl<ArrayList<QBDialog>>(){

            @Override
            public void onSuccess(ArrayList<QBDialog> dialogs, Bundle params) {
                List<Integer> usersIDs = new ArrayList<Integer>();
                for (QBDialog dialog : dialogs) {
                    if (dialog.getOccupants().size()==2) {
                        filterDialog.add(dialog);
                    }
                }
                for (QBDialog dialog : filterDialog) {

                    usersIDs.addAll(dialog.getOccupants());

                }
                ((myApp)getApplication()).setUsuarios_chat(usersIDs);
                loadAllUsers();
            }
        });

    }

    public void loasAllChatAlone(){
        QBCustomObjectRequestBuilder customObjectRequestBuilder = new QBCustomObjectRequestBuilder();
        customObjectRequestBuilder.setPagesLimit(100);
        QBChatService.getChatDialogs(QBDialogType.GROUP, customObjectRequestBuilder, new QBEntityCallbackImpl<ArrayList<QBDialog>>(){

            @Override
            public void onSuccess(ArrayList<QBDialog> dialogs, Bundle params) {
                List<Integer> usersIDs = new ArrayList<Integer>();
                for (QBDialog dialog : dialogs) {
                    if (dialog.getOccupants().size()==2) {
                        filterDialog.add(dialog);
                    }
                }
                for (QBDialog dialog : filterDialog) {
                    usersIDs.addAll(dialog.getOccupants());
                }
                ((myApp)getApplication()).setUsuarios_chat(usersIDs);
                Bundle bundle = new Bundle();
                bundle.putSerializable(mNewChatFragmentView.EXTRA_MODE, mNewChatFragmentView.Mode.GROUP);
                bundle.putSerializable(mNewChatFragmentView.EXTRA_DIALOG, ((myApp)getApplication()).getDialog_to_chat());
                PushView(mNewChatFragmentView.getInstance(MainActivity.this, bundle));


            }
        });

    }

    public void unSelectedRaioChat(){
        this.selected_chat.clearCheck();

    }

    public void initService(){
        startService(new Intent(MainActivity.this,
                mReciverDialogsChat.class));
    }
    public void stopService(){
        stopService(new Intent(MainActivity.this,
                mReciverDialogsChat.class));
    }




}