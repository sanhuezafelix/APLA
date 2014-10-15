package mc.mCongressDemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by luisgonzalez on 29-08-14.
 */
public class BroadcastAlarma extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d("action_", "got action " + action);
        if(action.equals("My.Action.Alarm")){
            String state = intent.getExtras().getString("alarma");
            Intent segundaActivity = new Intent(context, ServicioAlarma.class);
            segundaActivity.putExtra("mensaje",state);

            context.startService(segundaActivity);

        }

    }
}
