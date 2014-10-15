package Fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import DB.mEvent;
import Utils.TouchImageView;
import Utils.mMapImageView;
import mc.mCongressDemo.BroadcastAlarma;
import mc.mCongressDemo.MainActivity;
import mc.mCongressDemo.R;
import mc.mCongressDemo.myApp;
import Utils.mPlaceCordenate;

/**
 * Created by luisgonzalez on 27-03-14.
 */
public class mDetalleExtraViewController extends Fragment implements View.OnClickListener {

    private static final String EVENTO_ID = "evento_id";
    private Integer _id;
    private TextView titulo_evento;
    private TextView hora_evento;
    private TextView fecha_evento;
    private TextView lugar_evento;

    private  mMapImageView mapa;
    private RelativeLayout content;
    private Cursor cursor_event;
    private mEvent evento;
    private Button done;
    private MainActivity activity;
    private ImageButton fvt;
    private myApp app;
    final ArrayList<Integer> favoriteArrayList = new ArrayList<Integer>();

    private mPlaceCordenate pinMapa;
    private ImageButton btn_note_pad;
    private LinearLayout content_map ;

    private TextView speaker;
    private TextView speaker_dos;
    private TextView speaker_extra;
    private PendingIntent pendingIntent;



    public static mDetalleExtraViewController newInstance(MainActivity activity , int _id){
        mDetalleExtraViewController  fragment = new mDetalleExtraViewController();
        Bundle bundle = new Bundle();
        bundle.putInt(EVENTO_ID, _id);
        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);
        activity.setBackbutton();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.detalle_evento_extra_layout , container , false);
        activity = (MainActivity)getActivity();
        app = (myApp)getActivity().getApplicationContext();



        this.mapa = (mMapImageView)RootView.findViewById(R.id.mapa_detalle_extra);
        this.content_map = (LinearLayout)RootView.findViewById(R.id.content_map);


        this.titulo_evento = (TextView)RootView.findViewById(R.id.titulo_evento_extra);
        this.fecha_evento = (TextView)RootView.findViewById(R.id.fecha_evento_extra);
        this.hora_evento = (TextView)RootView.findViewById(R.id.hora_evento_extra);
        this.lugar_evento = (TextView)RootView.findViewById(R.id.lugar_evento_extra);

        this.fvt = (ImageButton)RootView.findViewById(R.id.btn_fv_extra);
        this.btn_note_pad = (ImageButton)RootView.findViewById(R.id.btn_notepad);
        this.btn_note_pad.setOnClickListener(this);
        this.speaker= (TextView)RootView.findViewById(R.id.detalle_speaker_name);
        this.speaker_dos= (TextView)RootView.findViewById(R.id.detalle_speaker_name_dos);
        this.speaker_extra= (TextView)RootView.findViewById(R.id.detalle_speaker_name_extra);

       //esFavorito(this._id);





        activity.setBackbutton();

        cursor_event = activity.getDbHandler().getEventDetail(this._id);
        cursor_event.moveToFirst();
        evento = new mEvent(cursor_event);

        //this.mapa.setCordenate(100,100);

        this.titulo_evento.setText(evento.titulo);
        this.hora_evento.setText("De " + evento.getStringStartTime()+" a " +evento.getStringEndTime()+" hrs");
        this.fecha_evento.setText(evento.getDayevent());
        this.lugar_evento.setText(evento.lugar);


        if (evento.imagen != null) {
            if (evento.imagen.length > 0) {


                Bitmap bitmap = BitmapFactory.decodeByteArray(evento.imagen, 0, evento.imagen.length);


                if (bitmap != null) {
                    mapa.setImageBitmap(bitmap);
                    this.pinMapa = new mPlaceCordenate(evento.lugar);
                    this.mapa.setCordenate(pinMapa.getCordx(),pinMapa.getCordy());
                }


            }
        }
        else {
            this.content_map.setVisibility(View.GONE);

        }

        if (evento.speakeruno.equals("")){
            speaker.setVisibility(View.GONE);
        }
        else {
            speaker.setText(evento.speakeruno);
        }
        if (evento.speakerdos.equals("")){
            speaker_dos.setVisibility(View.GONE);
        }
        else {
            speaker_dos.setText(evento.speakerdos);
        }
        if (evento.speakeExtra.equals("")){
            speaker_extra.setVisibility(View.GONE);
        }
        else {
            speaker_extra.setText(evento.speakeExtra);
        }



        if (activity.getDbHandler().isFavoriteCurrentEvent(this._id)==1){
            fvt.setImageResource(R.drawable.btn_fav_0);

        }
        else {
            fvt.setImageResource(R.drawable.btn_fav_1);
        }
        this.mapa.setOnClickListener(this);
        this.fvt.setOnClickListener(this);

        return RootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this._id = getArguments().getInt(EVENTO_ID);
        ((MainActivity)activity).setHideNavigationBar();
        ((MainActivity)activity).setTituloVista(getString(R.string.event));
    }

    @Override
    public void onClick(View view) {
        if (view.getId()== R.id.mapa_detalle_extra) {
            final Dialog dialogo = new Dialog(this.activity);
            dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            dialogo.setContentView(R.layout.map_box_layout);


            done = (Button) dialogo.findViewById(R.id.btn_done_image_dialog);
            TouchImageView mapadialog = (TouchImageView) dialogo.findViewById(R.id.image_dialog);
            mapadialog.setMaxZoom(3f);
            mapadialog.setMinZoom(1f);

            if (evento.imagen != null) {
                if (evento.imagen.length > 0) {


                    Bitmap bitmap = BitmapFactory.decodeByteArray(evento.imagen, 0, evento.imagen.length);


                    if (bitmap != null) {
                        mapadialog.setImageBitmap(bitmap);
                    }


                }
            }


            /*switch (activity.getCurrentId()) {
                case 1:
                    mapa.setImageResource(R.drawable.mapa_1);
                    break;
                case 2:
                    mapa.setImageResource(R.drawable.mapa_3);
                    break;
            }*/
            mapadialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (done.getVisibility() == View.VISIBLE) {
                        done.setVisibility(View.INVISIBLE);
                    } else {
                        done.setVisibility(View.VISIBLE);
                    }


                }
            });

            dialogo.getWindow().getAttributes().width = RelativeLayout.LayoutParams.MATCH_PARENT;
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogo.dismiss();

                }
            });

            dialogo.show();
        }
        else if(view.getId() == R.id.btn_notepad){
            activity.PushView(mNotepadViewController.getInstance(activity,getArguments().getInt(EVENTO_ID),this.titulo_evento.getText().toString()));
        }
        else {
            if (activity.getDbHandler().isFavoriteCurrentEvent(this._id)==1){
                ///removeFavoriteArray("pruebaFavoritos",this.activity,this._id);
                activity.getDbHandler().updateFavoriteList(this._id,0);
                fvt.setImageResource(R.drawable.btn_fav_1);
            }
            else
            {

                activity.getDbHandler().updateFavoriteList(this._id,1);
                fvt.setImageResource(R.drawable.btn_fav_0);
                if (this.evento.milisecondevent <= System.currentTimeMillis()){
                    Toast toast1 =
                            Toast.makeText(activity,
                                    getString(R.string.finish), Toast.LENGTH_SHORT);

                    toast1.show();
                }
                else {
                    this.SetAlarmaEvento(this.evento.milisecondevent, evento.titulo);
                }

            }

        }

    }

    private void SetAlarmaEvento(long milis,String mensaje){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milis);



        Intent myIntent = new Intent(activity, BroadcastAlarma.class);
        myIntent.setAction("My.Action.Alarm");

        myIntent.putExtra("alarma",mensaje);
        pendingIntent = PendingIntent.getBroadcast(activity, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)activity.getApplicationContext().getSystemService(activity.getApplicationContext().ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Toast toast1 =
                Toast.makeText(activity,
                        getString(R.string.alarm), Toast.LENGTH_SHORT);

        toast1.show();


    }











}

