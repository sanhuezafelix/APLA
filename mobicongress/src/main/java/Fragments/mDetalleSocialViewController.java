package Fragments;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import DB.mEvent;
import mc.mCongressDemo.MainActivity;
import mc.mCongressDemo.R;

/**
 * Created by luisgonzalez on 27-03-14.
 */
public class mDetalleSocialViewController extends Fragment implements View.OnClickListener {

    private static final String EVENTO_ID = "evento_id";
    private Integer _id;
    private TextView titulo_evento;
    private TextView hora_evento;
    private TextView fecha_evento;
    private TextView lugar_evento;
    private TextView descripcion_evento;


    private Cursor cursor_event;

    private mEvent evento;

    private ImageView fondo;

    private MainActivity activity;



    public static mDetalleSocialViewController newInstance(MainActivity activity , int _id){
        mDetalleSocialViewController fragment = new mDetalleSocialViewController();
        Bundle bundle = new Bundle();
        bundle.putInt(EVENTO_ID, _id);
        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);
        activity.setBackbutton();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.detalle_evento_social , container , false);
        activity = (MainActivity)getActivity();





        this.titulo_evento = (TextView)RootView.findViewById(R.id.Titulo_social_event);
        this.fecha_evento = (TextView)RootView.findViewById(R.id.fecha_social_event);
        this.hora_evento = (TextView)RootView.findViewById(R.id.hora_social_event);
        this.lugar_evento = (TextView)RootView.findViewById(R.id.lugar_social_event);
        this.fondo = (ImageView)RootView.findViewById(R.id.image_background);
        this.descripcion_evento = (TextView)RootView.findViewById(R.id.descripcion_social_event);




        activity.setBackbutton();

        cursor_event = activity.getDbHandler().getEventDetail(this._id);
        cursor_event.moveToFirst();

        evento = new mEvent(cursor_event);




        this.titulo_evento.setText(evento.titulo);
        this.hora_evento.setText(getString(R.string.from)+" " + evento.getStringStartTime()+" "+getString(R.string.to)+" " +evento.getStringEndTime()+" hrs");
        if (!evento.getDayevent().equals("")){
            this.fecha_evento.setText(evento.getDayevent());
        }
        else {
            fecha_evento.setVisibility(View.GONE);
        }

        if (!evento.lugar.equals("")){
            this.lugar_evento.setText(evento.lugar);
        }
        else {
            this.lugar_evento.setVisibility(View.GONE);
        }

        this.descripcion_evento.setText(evento.description);

        if (evento.imagen != null) {
            if (evento.imagen.length > 0) {



                Bitmap bitmap = BitmapFactory.decodeByteArray(evento.imagen, 0 , evento.imagen.length);


                if (bitmap!=null){
                    this.fondo.setImageBitmap(bitmap);
                }


            }
        }


        /*
        if (evento.lugar.equals("Hotel Viva Villahermosa")){
            RootView.setBackgroundResource(R.drawable.hotel);
        }
        else if (evento.lugar.equals("CIVE"))
        {
            RootView.setBackgroundResource(R.drawable.cive);
        }
        else if (evento.lugar.equals("Pantanos de Centla"))
        {
            RootView.setBackgroundResource(R.drawable.pantano);
        }
        else if (evento.lugar.equals("Universidad Intercultural"))
        {
            RootView.setBackgroundResource(R.drawable.intercultural);
        }
        else if (evento.lugar.equals("Casino Naval"))
        {
            RootView.setBackgroundResource(R.drawable.naval);
        }
        else if (evento.lugar.equals("Ciudad de Oxolotan"))
        {
            RootView.setBackgroundResource(R.drawable.oxolatan);
        }
        else if (evento.lugar.equals("Museo La Venta"))
        {
            RootView.setBackgroundResource(R.drawable.crown);
        }
        else if (evento.lugar.equals("Tapijulapa"))
        {
            RootView.setBackgroundResource(R.drawable.patijulapa);
        }
        else if (evento.lugar.equals("Casa Piedra"))
        {
            RootView.setBackgroundResource(R.drawable.casapiedra);
        }*/







        // RootView.setBackground(decodedByte);

        return RootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this._id = getArguments().getInt(EVENTO_ID);
    }

    @Override
    public void onClick(View view) {

    }

}

