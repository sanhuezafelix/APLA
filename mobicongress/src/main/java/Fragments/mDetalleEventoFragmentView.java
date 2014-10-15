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
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quickblox.core.QBCallback;
import com.quickblox.core.result.Result;
import com.quickblox.module.ratings.QBRatings;
import com.quickblox.module.ratings.model.QBScore;

import java.util.Calendar;

import Utils.DataHolder;
import Utils.TouchImageView;
import Utils.mMapImageView;

import DB.mEvent;
import Utils.mPlaceCordenate;
import mc.mCongressDemo.BroadcastAlarma;
import mc.mCongressDemo.MainActivity;
import mc.mCongressDemo.R;
import mc.mCongressDemo.myApp;

/**
 * Created by luisgonzalez on 12-03-14.
 */
public class mDetalleEventoFragmentView extends Fragment implements View.OnClickListener ,QBCallback{

    private static final String EVENTO_ID = "evento_id";
    private static final String VIEW_BTN = "view_btn";

    private Cursor cursor_event;
    private ImageButton fvt;
    private mMapImageView mapa;
    private TextView titulo_padre;
    private TextView speaker;
    private TextView speaker_dos;
    private TextView speaker_tres;
    private TextView speaker_cuatro;
    private TextView speaker_extra;
    private TextView titulo_evento;
    private TextView lugar;
    private TextView descripcion;
    private TextView hora_evento;
    private TextView fecha_evento;
    private TextView texto_fijo;
    private mEvent evento;
    private MainActivity activity;
    private Button done;
    private ImageButton ir_modulo;
    private ImageButton btn_note_pad;
    private mPlaceCordenate pinMapa;
    private int _id;
    private LinearLayout content_map ;
    private Button btn_rate;
    private Button btn_Ok;
    private Button btn_Cancel;
    private RatingBar ratingBar;
    private PendingIntent pendingIntent;

    public static mDetalleEventoFragmentView newInstance(MainActivity activity, int _id, boolean btnview) {

        // Instantiate a new fragment

        mDetalleEventoFragmentView fragment = new mDetalleEventoFragmentView();

        // Save the parameters
        Bundle bundle = new Bundle();
        bundle.putInt(EVENTO_ID, _id);
        bundle.putBoolean(VIEW_BTN, btnview);
        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);
        activity.setBackbutton();

        return fragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.evento_detalle_fragment_layout,container,false);
        activity = (MainActivity)getActivity();
        activity.setTituloVista(getString(R.string.event));
        activity.setBackbutton();

        this.titulo_padre = (TextView)RootView.findViewById(R.id.detalle_titulo_padre);
        this.titulo_evento = (TextView)RootView.findViewById(R.id.detalle_titulo_evento);
        this.mapa = (mMapImageView)RootView.findViewById(R.id.mapa_evento);
        this.content_map = (LinearLayout)RootView.findViewById(R.id.content_map);
        this.speaker= (TextView)RootView.findViewById(R.id.detalle_speaker_name);
        this.speaker_dos= (TextView)RootView.findViewById(R.id.detalle_speaker_name_dos);
        this.speaker_tres= (TextView)RootView.findViewById(R.id.detalle_speaker_name_tres);
        this.speaker_cuatro= (TextView)RootView.findViewById(R.id.detalle_speaker_name_cuatro);
        this.speaker_extra= (TextView)RootView.findViewById(R.id.detalle_speaker_name_extra);
        this.lugar= (TextView)RootView.findViewById(R.id.detalle_lugar_evento);
        this.descripcion= (TextView)RootView.findViewById(R.id.detalle_descripcion);
        this.hora_evento= (TextView)RootView.findViewById(R.id.detalle_hora_evento);
        this.fecha_evento= (TextView)RootView.findViewById(R.id.detalle_fecha_evento);
        this.fvt = (ImageButton)RootView.findViewById(R.id.favorito_evento);
        this.ir_modulo = (ImageButton)RootView.findViewById(R.id.ir_modulo_evento);
        this.texto_fijo =(TextView)RootView.findViewById(R.id.texto_fijo);
        this.btn_note_pad = (ImageButton)RootView.findViewById(R.id.btn_notepad);
        this.btn_rate = (Button)RootView.findViewById(R.id.btn_rate);

        this.btn_rate.setOnClickListener(this);
        this.btn_note_pad.setOnClickListener(this);
        if (getArguments().getBoolean(VIEW_BTN)){
            this.ir_modulo.setVisibility(View.VISIBLE);
            this.texto_fijo.setVisibility(View.VISIBLE);
            this.ir_modulo.setOnClickListener(this);
        }

        cursor_event = activity.getDbHandler().getEventDetail(this._id);
        cursor_event.moveToFirst();
        evento = new mEvent(cursor_event);

        if (!(((myApp)getActivity().getApplicationContext()).verificaConexion())){
            btn_rate.setVisibility(View.GONE);
        }
        else
        {
            if (evento.isRating!=0){
                btn_rate.setVisibility(View.GONE);
            }
        }
        if (evento.tipo_evento_padre.length()>2 ){
            this.titulo_padre.setText(evento.tipo_evento_padre+": "+evento.evento_padre);
        }
        else {
            this.titulo_padre.setText(evento.evento_padre);
        }

        this.titulo_evento.setText(evento.titulo);
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
       if (evento.speakertres.equals("")){
            speaker_tres.setVisibility(View.GONE);
        }
        else {
            speaker_tres.setText(evento.speakertres);
        }
        if (evento.speakercuatro.equals("")){
            speaker_cuatro.setVisibility(View.GONE);
        }
        else {
            speaker_cuatro.setText(evento.speakercuatro);
        }
        if (evento.speakeExtra.equals("")){
            speaker_extra.setVisibility(View.GONE);
        }
        else {
            speaker_extra.setText(evento.speakeExtra);
        }

        this.lugar.setText(evento.lugar);
        //this.lugar.setVisibility(View.GONE);
        this.descripcion.setText(evento.description);

        this.hora_evento.setText(getString(R.string.from)+" " + evento.getStringStartTime()+" "+getString(R.string.to)+" " +evento.getStringEndTime()+" hrs");
        this.fecha_evento.setText(evento.getDayevent());
        if (activity.getDbHandler().isFavoriteCurrentEvent(this._id)==1){
            fvt.setImageResource(R.drawable.btn_fav_0);

        }
        else {
            fvt.setImageResource(R.drawable.btn_fav_1);
        }
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

        if (view.getId()== R.id.mapa_evento) {
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
        else if (view.getId() == R.id.ir_modulo_evento)
        {
            this.activity.PushView(mDetalleModuloFragmentView.newInstance(this.activity,this.evento.evento_padre_id));

        }
        else if(view.getId() == R.id.btn_notepad){
            activity.PushView(mNotepadViewController.getInstance(this.activity,getArguments().getInt(EVENTO_ID),this.titulo_evento.getText().toString()));
        }
        else if (view.getId() == R.id.btn_rate)
        {
            final Dialog dialogo = new Dialog(this.activity);
            dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            dialogo.setContentView(R.layout.dialog_rate);
            btn_Ok = (Button) dialogo.findViewById(R.id.popOkRate);
            btn_Cancel = (Button)dialogo.findViewById(R.id.popCancelRate);
            this.ratingBar = (RatingBar) dialogo.findViewById(R.id.rating_bar);
            btn_Ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ((int) ratingBar.getRating()>0)
                    createScore(evento.rating,(int) ratingBar.getRating() );
                    dialogo.dismiss();
                }
            });
            this.btn_Cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogo.dismiss();
                }
            });

            dialogo.getWindow().getAttributes().width = RelativeLayout.LayoutParams.MATCH_PARENT;
            dialogo.show();




        }
        else {
            if (activity.getDbHandler().isFavoriteCurrentEvent(this._id)==1){



                ///removeFavoriteArray("pruebaFavoritos",this.activity,this._id);
                activity.getDbHandler().updateFavoriteList(this._id,0);
                fvt.setImageResource(R.drawable.btn_fav_1);

              /*  Toast toast1 =
                        Toast.makeText(getActivity().getApplicationContext(),
                                "favorito removido es  " + global.getFavoritoRemovido(), Toast.LENGTH_SHORT);

                toast1.show();*/

            }
            else
            {

                activity.getDbHandler().updateFavoriteList(this._id,1);
                fvt.setImageResource(R.drawable.btn_fav_0);
                //this.SetAlarmaEvento(evento.time_start_date,evento.time_alarm,evento.titulo);
                if (this.evento.milisecondevent <= System.currentTimeMillis()){
                    Toast toast1 =
                            Toast.makeText(activity,
                                    getString(R.string.finish), Toast.LENGTH_SHORT);

                    toast1.show();
                }
                else {
                    this.SetAlarmaEvento(this.evento.milisecondevent, evento.titulo);
                }

               /* if (loadFavoriteArray("pruebaFavoritos",this.activity).equals("")){

                    this.favoriteArrayList.add(this._id);
                    //Toast.makeText(context, "no hay notificaciones",Toast.LENGTH_SHORT).show();
                    saveFavoriteArray(this.favoriteArrayList, "pruebaFavoritos", this.activity);
                    //Toast.makeText(context, "se guardaron "+ elemento.size()+ "elementos",Toast.LENGTH_SHORT).show();
                    this.favoriteArrayList.clear();
                    fvt.setImageResource(R.drawable.btn_fav_1);


                }
                else {

                    for(int IPVS =0;IPVS< loadFavoriteArray("pruebaFavoritos",this.activity).length;IPVS++ )
                        this.favoriteArrayList.add(loadFavoriteArray("pruebaFavoritos",this.activity)[IPVS]);








                    this.favoriteArrayList.add(this._id);
                    saveFavoriteArray(this.favoriteArrayList, "pruebaFavoritos", this.activity);
                    //Toast.makeText(context, "se guardaron "+ elemento.size()+ "elementos",Toast.LENGTH_SHORT).show();
                    this.favoriteArrayList.clear();
                    fvt.setImageResource(R.drawable.btn_fav_1);

                }*/


            }

        }

    }

    private void createScore(String _id,int Score) {



        QBScore qbScore = new QBScore();
        qbScore.setGameModeId(Integer.parseInt(_id));
        //Toast.makeText(getActivity(), String.valueOf(id), Toast.LENGTH_SHORT).show();
        qbScore.setValue(Score);
        qbScore.setUserId(DataHolder.getDataHolder().getQbUserId());
        QBRatings.createScore(qbScore, this);

    }

    @Override
    public void onComplete(Result result) {
        if (result.isSuccess()){
            this.btn_rate.setVisibility(View.GONE);
            activity.getDbHandler().updateRating(this._id);
            Toast.makeText(getActivity(), getString(R.string.rate_send), Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onComplete(Result result, Object o) {

    }
   /* private void SetAlarmaEvento(int[] date , int []time,String mensaje){

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
        calendar.set(Calendar.YEAR, date[0]);
        calendar.set(Calendar.DAY_OF_MONTH, 3);
        calendar.set(Calendar.HOUR_OF_DAY, time[0]);
        calendar.set(Calendar.MINUTE, time[1]);
        calendar.set(Calendar.SECOND, time[2]);




        Intent myIntent = new Intent(activity, BroadcastAlarma.class);
        myIntent.setAction("My.Action.Alarm");

        myIntent.putExtra("alarma",mensaje);
        pendingIntent = PendingIntent.getBroadcast(activity, 0, myIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)activity.getApplicationContext().getSystemService(activity.getApplicationContext().ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }*/
   private void SetAlarmaEvento(long milis,String mensaje){

       Calendar calendar = Calendar.getInstance();
       calendar.setTimeInMillis(milis);



       Intent myIntent = new Intent(activity, BroadcastAlarma.class);
       myIntent.setAction("My.Action.Alarm");
       myIntent.putExtra("alarma",mensaje);
       pendingIntent = PendingIntent.getBroadcast(activity, 0, myIntent,PendingIntent.FLAG_UPDATE_CURRENT);

       AlarmManager alarmManager = (AlarmManager)activity.getApplicationContext().getSystemService(activity.getApplicationContext().ALARM_SERVICE);
       alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
       Toast toast1 =
               Toast.makeText(activity,
                       getString(R.string.alarm), Toast.LENGTH_SHORT);

       toast1.show();


   }

}
