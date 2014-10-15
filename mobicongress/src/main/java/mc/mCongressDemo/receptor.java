package mc.mCongressDemo;

/**
 * Created by luisgonzalez on 26-03-14.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by luisgonzalez on 25-03-14.
 */
public class receptor  extends BroadcastReceiver{
    private static final String TAG = "MyCustomReceiver";
    final ArrayList<String> elemento = new ArrayList<String>();
    private int api = android.os.Build.VERSION.SDK_INT;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent == null)
            {
                Log.d(TAG, "Receiver intent null");
            }
            else
            {
                String action = intent.getAction();
                Log.d(TAG, "got action " + action );
                if (action.equals("mc.mCongressDemo.UPDATE_STATUS"))
                {
                    //GaPush(context);
                    String channel = intent.getExtras().getString("com.parse.Channel");
                    JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

                    Log.d(TAG, "accion " + action + " en channel " + channel + " con:");
                    Iterator itr = json.keys();
                    while (itr.hasNext()) {
                        String key = (String) itr.next();
                        if (key.equals("cd"))
                        {



                            if(loadArray("Notificaciones",context).equals(""))
                            {
                                elemento.add(json.getString(key));
                                //Toast.makeText(context, "no hay notificaciones",Toast.LENGTH_SHORT).show();
                                saveArray(elemento, "Notificaciones", context);
                                //Toast.makeText(context, "se guardaron "+ elemento.size()+ "elementos",Toast.LENGTH_SHORT).show();
                                elemento.clear();

                            }
                            else{
                                //elemento.toArray(loadArray("Notificaciones",context));
                                for(int i =0;i< loadArray("Notificaciones",context).length;i++ )
                                    elemento.add(loadArray("Notificaciones",context)[i]);



                                //Toast.makeText(context, "hay "+ elemento.size()+ "elementos",Toast.LENGTH_SHORT).show();

                                elemento.add(json.getString(key));
                                saveArray(elemento, "Notificaciones", context);
                                //Toast.makeText(context, "se guardaron "+ elemento.size()+ "elementos",Toast.LENGTH_SHORT).show();
                                elemento.clear();



                            }
                            if(this.api >11){
                                Intent pupInt = new Intent(context, CustomAlertViewController.class);
                                String[] split = json.getString(key).split(":");
                                pupInt.putExtra("push", split[2]);
                                pupInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                                context.getApplicationContext().startActivity(pupInt);

                            }
                            else{
                                //Toast.makeText(context, "tu api es"+api, Toast.LENGTH_LONG).show();
                                Log.d(TAG, "tu api es " + api );

                            }
                            //Intent pupInt = new Intent(context, CustomAlert.class);
                            //pupInt.putExtra("push", json.getString(key));

                            //pupInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                            //context.getApplicationContext().startActivity(pupInt);


                        }

                        Log.d(TAG, "..." + key + " => " + json.getString(key));


                    }
                }
            }

        } catch (JSONException e) {
            Log.d(TAG, "JSONException: " + e.getMessage());
        }
    }
    /*
               Eventos novedades
        */
    public boolean saveArray(ArrayList<String> array, String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("mCongress", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName +"_size", array.size());
        for(int i=0;i<array.size();i++)
            editor.putString(arrayName + "_" + i, array.get(i));
        return editor.commit();
    }

    public String[] loadArray(String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("mCongress", 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        String array[] = new String[size];
        for(int i=0;i<size;i++)
            array[i] = prefs.getString(arrayName + "_" + i, null);
        return array;
    }

    /* CLASE ALERTA */
}
