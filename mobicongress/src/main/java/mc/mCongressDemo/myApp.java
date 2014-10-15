package mc.mCongressDemo;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;


import com.quickblox.module.chat.QBRoster;
import com.quickblox.module.chat.model.QBDialog;
import com.quickblox.module.users.QBUsers;
import com.quickblox.module.users.model.QBUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DB.Novedad;


/**
 * Created by luisgonzalez on 29-03-14.
 */
public class myApp extends Application {


    public int fragment_ahora_position = -1;
    public int fragment_jornada_position = -1;
    public int fragment_speaker_position = -1;
    public int fragment_favorite_position = -1;
    public int fragment_busqueda_position = -1;
    public int fragment_jornada_one_section = -1;
    public int fragment_menu_position = -1;
    private String speaker_search_text = "";
    private boolean pushView;
    public int last_resouce_id_menu;
    public long last_update_time_menu = 0L;
    public long total_update_time_menu = 0L;
    private boolean isFavoriteAlarm;
    private static Context mContext;
    private QBDialog dialog_to_chat;

    private ArrayList<QBDialog> allDialogs = new ArrayList<QBDialog>();
    // chat

    private QBUser currentUser;
    private List<QBUser> usuarios_registrados = new ArrayList<QBUser>();
    private List<Integer> usuarios_chat = new ArrayList<Integer>();

    private Map<Integer, QBUser> dialogsUsers = new HashMap<Integer, QBUser>();
    private QBRoster сhatRoster;
    private ArrayList<QBDialog> userDialogToChat = new ArrayList<QBDialog>();



    private static myApp singleton;
    public myApp getInstance(){
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        singleton = this;
        Parse.initialize(this, "JYUzhhPZOGy4W4imuXOVoGTAuowyTzFazedpqLaF", "28fKUcLsYVnnjQYgbxq2lVDQEMBhBl1cTCaNE01q");
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        Parse.enableLocalDatastore(getApplicationContext());
        ParseObject.registerSubclass(Novedad.class);
        // If you would like all objects to be private by default, remove this line.
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
        last_resouce_id_menu = 3;
    }

    public boolean saveFavoriteArray(ArrayList<Integer> array, String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("mCongress", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName +"_size", array.size());
        for(int i=0;i<array.size();i++)
            editor.putInt(arrayName + "_" + i, array.get(i));
        return editor.commit();
    }

    public Integer[] loadFavoriteArray(String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("mCongress", 0);
        int size = prefs.getInt(arrayName + "_size", 0);

        Integer array[] = new Integer[size];
        for(int i=0;i<size;i++)
            array[i] = prefs.getInt(arrayName + "_" + i, 0);
        return array;
    }

    public void setCurrentSpeakerPosition(int i){
        this.fragment_speaker_position = i;

    }
    public int getCurrentSpeakerPosition(){
        return this.fragment_speaker_position;

    }
    public void setCurrentFavoritePosition(int i){
        this.fragment_favorite_position = i;
    }
    public int getCurrentFavoritePosition(){
        return this.fragment_favorite_position;

    }
    public void cleanPositionListview(){
        this.fragment_speaker_position = -1;
    }
    public void setSpeakerSearchText(String s) {
        this.speaker_search_text = s;
    }

    public String getSpeakerSearchText() {
        return this.speaker_search_text;
    }

    public String remueveTildes(String input) {
        // Cadena de caracteres original a sustituir.
        String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
        // Cadena de caracteres ASCII que reemplazarán los originales.
        String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
        String output = input;
        for (int i=0; i<original.length(); i++) {
            // Reemplazamos los caracteres especiales.
            output = output.replace(original.charAt(i), ascii.charAt(i));
        }//for IPVS
        return output;
    }//remove1


    /*
            Metodos Notificaciones
     */

    public String[] loadArray(String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("mCongress", 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        String array[] = new String[size];
        for(int i=0;i<size;i++)
            array[i] = prefs.getString(arrayName + "_" + i, null);
        return array;
    }

    public boolean saveArray(ArrayList<String> array, String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("mCongress", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName +"_size", array.size());
        for(int i=0;i<array.size();i++)
            editor.putString(arrayName + "_" + i, array.get(i));
        return editor.commit();
    }

    public void setpushView(boolean a){
        this.pushView = a;

    }
    public void setFavoriteAlarm(boolean a){
        this.isFavoriteAlarm = a;
    }
    public boolean getFavoriteAlarm(){
        return this.isFavoriteAlarm;
    }

    public boolean getpushView(){
        return this.pushView;
    }

    public static Context getContext(){
        return mContext;
    }
    public boolean verificaConexion() {
        boolean bConectado = false;
        ConnectivityManager connec = (ConnectivityManager) this.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // No sólo wifi, también GPRS
        NetworkInfo[] redes = connec.getAllNetworkInfo();
        // este bucle debería no ser tan ñapa
        for (int i = 0; i < redes.length; i++) {
            // ¿Tenemos conexión? ponemos a true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                bConectado = true;
            }
        }
        return bConectado;
    }

     /*
            VARIABLES GLOBALES CHAT
     */

    public QBUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(QBUser currentUser) {
        this.currentUser = currentUser;
    }

    public Map<Integer, QBUser> getDialogsUsers() {
        return dialogsUsers;
    }

    public void setDialogsUsers(List<QBUser> setUsers) {
        dialogsUsers.clear();

        for (QBUser user : setUsers) {
            dialogsUsers.put(user.getId(), user);
        }
    }

    public void addDialogsUsers(List<QBUser> newUsers) {
        for (QBUser user : newUsers) {
            dialogsUsers.put(user.getId(), user);
        }
    }

    public Integer getOpponentIDForPrivateDialog(QBDialog dialog){
        Integer opponentID = -1;
        for(Integer userID : dialog.getOccupants()){
            if(userID != getCurrentUser().getId()){
                opponentID = userID;
                break;
            }
        }

        return opponentID;
    }

    public boolean setUserPreference(String user , String pass){
        SharedPreferences prefs = mContext.getSharedPreferences("mUserPreference", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("user" ,user);
        editor.putString("pass" ,pass);
        return editor.commit();

    }
    public Boolean isRegistred(){

        boolean registred=false;
        SharedPreferences prefs = mContext.getSharedPreferences("mUserPreference", 0);
        String pass = prefs.getString("user",null);
        String value = prefs.getString("pass",null);
        if (value == null || pass == null )
        {
           registred = false;
        } else {
            registred = true;
        }

        return registred;
    }
    public String[] loadRegistred( Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("mUserPreference", 0);
            String array[] = new String[2];
            array[0] = prefs.getString("user", null);
            array[1] = prefs.getString("pass", null);
        return array;
    }

    public void setUsuarios_registrados( List<QBUser> _usuerarios){
        this.usuarios_registrados.clear();
        this.usuarios_registrados.addAll(_usuerarios);
    }
    public List<QBUser> getUsuarios_registrados(){
        return this.usuarios_registrados;
    }

    public void setchatRoster(QBRoster roster){
        this.сhatRoster = roster;
    }
    public QBRoster getСhatRoster(){
        return this.сhatRoster;
    }



    public boolean setGroupUsersNames(String id , String names){
        SharedPreferences prefs = mContext.getSharedPreferences("mUsersGroupNames", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(id ,names);
        return editor.commit();

    }
    public String getGroupUsersNames( String id) {
        SharedPreferences prefs = mContext.getSharedPreferences("mUsersGroupNames", 0);

        return prefs.getString(id, null);
    }
    public void setUsuarios_chat(List<Integer> ids){
        this.usuarios_chat.clear();
        for (Integer getid : ids){
            if (!getid.equals(getCurrentUser().getId())){
                this.usuarios_chat.add(getid);
            }
        }




    }
    public boolean isUserCurrentChat(Integer id){
        boolean a= true;
        for (Integer saveIds:this.usuarios_chat){
            if (saveIds.equals(id)){
                a=true;
            }
            else {
                a=false;
            }

        }
        return a;
    }

    public void setDialog_to_chat(QBDialog dialog){
        this.dialog_to_chat= dialog;
    }

    public QBDialog getDialog_to_chat(){
        return this.dialog_to_chat;

    }

    public boolean setChatUsersIds(String id , String names){
        boolean request= true;
        SharedPreferences prefs = mContext.getSharedPreferences("mUsersChatNames", 0);
        if (prefs.getString(id,null)==null){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(id ,names);
            request= editor.commit();

        }
        else {
            request=false;
        }

        return request;

    }
    public void setChatUsersIds(List<Integer> ids){

        SharedPreferences prefs = mContext.getSharedPreferences("mUsersChatNames", 0);
        for (Integer id: ids){
            if (!id.equals(getCurrentUser().getId())){
                if (prefs.getString(id.toString(),null)==null){
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(id.toString() ,getUserNameById(id));
                    editor.commit();
                }
            }
        }
    }
    public String getUserNameById(Integer id){
        String name = null;
        for (QBUser usuarios:getUsuarios_registrados()){
            if (usuarios.getId().equals(id)){
                name= usuarios.getFullName();
                break;
            }
        }
        return name;
    }
    public String getChatUsersIds( String id) {
        SharedPreferences prefs = mContext.getSharedPreferences("mUsersChatNames", 0);
        return prefs.getString(id, null);
    }
    public boolean isChatUserCurrentDialogs(String id){
        boolean request= true;
        SharedPreferences prefs = mContext.getSharedPreferences("mUsersChatNames", 0);
        if (prefs.getString(id,null)!=null){

            request= true;

        }
        else {
            request=false;
        }
        return request;
    }
    public QBDialog getDialogTouserId(Integer id){
        QBDialog dialogo = null;
        for (QBDialog dialog : this.userDialogToChat) {
            for (Integer users:dialog.getOccupants()){
                if (users.equals(id)){
                    dialogo=dialog;
                    break;
                }
            }

        }
        return dialogo;
    }
    public void setDialogToChat(ArrayList<QBDialog> dialogs){
        this.userDialogToChat.clear();
        this.userDialogToChat.addAll(dialogs);


    }
    public void setAllDialog(ArrayList<QBDialog> dialogs){
        this.allDialogs.clear();
        this.allDialogs.addAll(dialogs);


    }
    public QBDialog getDialogToDialogId(String id){
        QBDialog dialogo = null;
        for (QBDialog dialog : this.userDialogToChat) {
            if (dialog.getDialogId().equals(id)){
                dialogo=dialog;
                break;
            }
        }
        return dialogo;
    }

    public String getDialogUserName(Integer id){
        String userName ="";
        for (QBUser usuario:getUsuarios_registrados()){
            if (usuario.getId().equals(id)){
                userName= usuario.getFullName();
                break;
            }
        }
        return userName;
    }




}
