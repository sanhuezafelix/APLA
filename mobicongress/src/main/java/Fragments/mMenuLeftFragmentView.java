package Fragments;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import Fragments.chat.mLoginChat;
import Fragments.chat.mMainchatViewController;
import Utils.Utilidades;
import mc.mCongressDemo.MainActivity;
import mc.mCongressDemo.R;
import Adaptadores.mMenuAdapter;
import mc.mCongressDemo.myApp;

/**
 * Created by luisgonzalez on 13-03-14.
 */
public class mMenuLeftFragmentView extends Fragment implements AdapterView.OnItemClickListener {

    private mMenuAdapter adaptador;
    private ListView list;
    private myApp app;
    private Handler customHandler = new Handler();
    private ImageView iv_sponsor;
    private static final String pdf = "https://docs.google.com/gview?embedded=true&url=";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.menu_left_layout,container,false);
        app = (myApp) getActivity().getApplicationContext();
        String[] menuItems = getResources().getStringArray(R.array.menu);
        this.list = (ListView)RootView.findViewById(R.id.menu_list);
        this.iv_sponsor = (ImageView)RootView.findViewById(R.id.publicidad_menu);
        adaptador = new mMenuAdapter(getActivity().getApplicationContext(),menuItems);
        this.list.setAdapter(adaptador);
        this.list.setOnItemClickListener(this);
        iv_sponsor.setImageResource(R.drawable.publicidad_menu);
        // Utilidades.animate(iv_sponsor, getRandomResourceId(app.last_resouce_id_menu),View.VISIBLE);
        app.last_update_time_menu = SystemClock.uptimeMillis();
        // customHandler.postDelayed(sponsor_thread, 0);
        return RootView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


        MainActivity activity = (MainActivity)getActivity();

        adaptador.set_cur_position(i);
        switch (i){
            case 0:
                activity.getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                if (activity.getLastAction()== MainActivity.Action.AHORA){
                    activity.setSelectedSegmentIndex(R.id.segment_button_one);
                    activity.menuChangeView(mAhoraFragmentView.newInstance(activity,0,false),true);
                }
                else {
                    activity.setSelectedSegmentIndex(R.id.segment_button_two);
                    activity.menuChangeView(mProgramaFragmentView.newInstance(activity,0),true);
                }
                break;
            case 1:
                activity.menuChangeView(NovedadesFragmentView.newInstance(activity,0),true);
                break;
            case 2:
                activity.menuChangeView(mSpeakerFragmentView.newInstance(activity, 1),true);
                break;
            case 3:
                activity.menuChangeView(mMapasFragmentView.newInstance(activity,0),true);
                break;

            case 4:
                activity.menuChangeView(mHotelesListView.newInstance(activity,1),true);
                break;
            case 5:
                if (!((myApp)getActivity().getApplicationContext()).verificaConexion()){
                    createAlertCustomDialog();
                }else {
                    activity.menuChangeView(mLoginChat.getInstance(activity,false),false);
                }

                break;
            case 6:

                if (getString(R.string.idioma).equals("es")){
                    activity.PushView(mDownloadContentViewController.newInstance(activity,pdf+"http://croxz.com/mobi/infoES.pdf"));
                    Log.i("pdf","español");

                }
                else if(getString(R.string.idioma).equals("pt"))
                {
                    activity.PushView(mDownloadContentViewController.newInstance(activity,pdf+"http://croxz.com/mobi/infoEN.pdf"));
                    Log.i("pdf","portugues");
                }
                else {
                    activity.PushView(mDownloadContentViewController.newInstance(activity,pdf+"http://croxz.com/mobi/infoEN.pdf"));

                    Log.i("pdf","ingles");
                }

                break;

            case 7:
                activity.menuChangeView(new mSponsorViewController(),true);
                break;

            /*case 5:
                activity.menuChangeView(mMainFragmentChat.getIntance());
                break;
            case 4:
                activity.menuChangeView(mHotelesListView.newInstance(activity,1));
                break;

           case 6:
                activity.menuChangeView(mAcompañantesFragmentView.newInstance(activity,1));
                break;*/
          /*  case 5:
                activity.menuChangeView(new mListNotePadFragmentView());
                break;
            case 5:
                activity.menuChangeView(new mPostersFragmentView());

                break;*/
        /*  case 5:
                activity.menuChangeView(mDownloadContentViewController.newInstance(activity,"https://docs.google.com/gview?embedded=true&url=http://croxz.com/mobi/TrabajosyPoster.pdf"));
                break;
            case 6:
                activity.menuChangeView(new mSponsorViewController());
                break;*/
        }

        this.app.cleanPositionListview();
        activity.getSlidingMenu().toggle(true);
    }

    public void setFragment(Fragment fragment)
    {

        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(null);
        ft.commit();

    }

    /*public void onStartSponsor(){
       // Log.e("sponsor menu start", "start");
        customHandler.postDelayed(sponsor_thread, 0);
        app.last_update_time_menu = SystemClock.uptimeMillis();

    }
    public void onStopSponsor(){
       // Log.e("sponsor menu stop", "stop");
        customHandler.removeCallbacks(sponsor_thread);

    }
    public int getRandomResourceId(int current_id){
        int id = Utilidades.randInt(current_id);
        app.last_resouce_id_menu = id;
        switch (id) {
            case 1:
                return R.drawable.publicidad_menu;
            case 2:
                return R.drawable.publi_mobicon;
//case 3:
//return R.drawable.publicidad_sidebar_logo3;
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
               // Log.e("SPONSORED", "ON MENU");
                app.total_update_time_menu = 0L;
//Translate image
//iv_sponsor.setImageResource(getRandomResourceId(myApp.last_resouce_id_menu));
               Utilidades.animate(iv_sponsor, getRandomResourceId(app.last_resouce_id_menu),View.VISIBLE);
            }
            customHandler.postDelayed(this, 0);
            app.last_update_time_menu = SystemClock.uptimeMillis();
        }

    };*/
    public void onDestroy() {
        // customHandler.removeCallbacks(sponsor_thread);
        super.onDestroy();
    };

    public void createAlertCustomDialog(){


        final Dialog dialogo = new Dialog((MainActivity)getActivity());
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogo.setContentView(R.layout.box_conection_layout);
        dialogo.getWindow().getAttributes().width = RelativeLayout.LayoutParams.MATCH_PARENT;
        TextView mensaje = (TextView)dialogo.findViewById(R.id.contenido_box);
        mensaje.setText(getString(R.string.conection_string));
        dialogo.show();
        final Button close = (Button) dialogo.findViewById(R.id.btn_done_image_dialog);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogo.dismiss();
                ((MainActivity)getActivity()).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });


    }
}