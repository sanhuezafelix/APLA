package Fragments;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import DB.mHotel;
import Utils.TouchImageView;
import Utils.mMapImageView;
import mc.mCongressDemo.MainActivity;
import mc.mCongressDemo.R;

/**
 * Created by luisgonzalez on 12-03-14.
 */
public class mHotelesFragmentView extends Fragment  implements View.OnClickListener{

    private static final String EVENTO_ID = "evento_id";

    /*private Cursor cursor_event;

    private String nombre_evento;
    private TextView titulo_padre;
    private TextView speaker;
    private TextView titulo_evento;
    private TextView lugar;
    private TextView descripcion;
    private TextView hora_evento;
    private TextView fecha_evento;
    private mEvent evento;*/
    private TextView nombre_hotel;
    private TextView direccion_hotel;
    private TextView lugar_hotel;
    private TextView contenido_hotel;
    private TextView fono_hotel;
    private TextView mail_hotel;
    private TextView web_hotel;
    private TextView fecha_evento;
    private mHotel hotel;

    private int _id;
    private  Cursor cursor_hotel;
    private ImageView fondo_hotel;
    private mMapImageView mapa_hotel;
    private Button done;

    public static mHotelesFragmentView newInstance(MainActivity activity, int _id) {

        // Instantiate a new fragment

        mHotelesFragmentView fragment = new mHotelesFragmentView();

        // Save the parameters
        Bundle bundle = new Bundle();
        bundle.putInt(EVENTO_ID, _id);
        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);
        activity.setBackbutton();


        return fragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.hoteles_layout,container,false);
        MainActivity activity = (MainActivity)getActivity();
        /*activity.setBackbutton();

        this.titulo_padre = (TextView)RootView.findViewById(R.id.detalle_titulo_padre);
        this.titulo_evento = (TextView)RootView.findViewById(R.id.detalle_titulo_evento);
        this.speaker= (TextView)RootView.findViewById(R.id.detalle_speaker_name);
        this.lugar= (TextView)RootView.findViewById(R.id.detalle_lugar_evento);
        this.descripcion= (TextView)RootView.findViewById(R.id.detalle_descripcion);
        this.hora_evento= (TextView)RootView.findViewById(R.id.detalle_hora_evento);
        this.fecha_evento= (TextView)RootView.findViewById(R.id.detalle_fecha_evento);
        cursor_event = activity.getDbHandler().getEventDetail(this._id);
        cursor_event.moveToFirst();
        evento = new mEvent(cursor_event);
        this.titulo_padre.setText("Modulo: "+evento.evento_padre);
        this.titulo_evento.setText(evento.titulo);
        this.speaker.setText(evento.speakeruno+" "+evento.speakerdos);
        this.lugar.setText(evento.lugar);
        this.descripcion.setText(evento.description);
        this.hora_evento.setText("De " + evento.getStringStartTime()+" a " +evento.getStringEndTime()+" hrs");
        this.fecha_evento.setText(evento.getDayevent());*/
        this.nombre_hotel = (TextView)RootView.findViewById(R.id.nombre_hotel);
        this.direccion_hotel = (TextView)RootView.findViewById(R.id.direccion_hotel);
        this.lugar_hotel = (TextView)RootView.findViewById(R.id.lugar_hotel);
        this.contenido_hotel = (TextView)RootView.findViewById(R.id.content_descripcion_hotel);
        this.fono_hotel = (TextView)RootView.findViewById(R.id.telefono_hotel);
        this.web_hotel = (TextView)RootView.findViewById(R.id.web_hotel);
        this.fondo_hotel = (ImageView)RootView.findViewById(R.id.fondo_hotel);
        this.mapa_hotel = (mMapImageView )RootView.findViewById(R.id.map_hotel);
        this.mapa_hotel.setOnClickListener(this);

        this.cursor_hotel = activity.getDbHandler().getCursorDetalleHotel(this._id);
        hotel = new mHotel(cursor_hotel);
        this.nombre_hotel.setText(hotel.titulo);
        this.direccion_hotel.setText(hotel.direccion);
        this.lugar_hotel.setText(hotel.lugar);
        this.contenido_hotel.setText(hotel.contenido);
        this.fono_hotel.setText(hotel.telefono);
        this.web_hotel.setText(hotel.web);
        if (hotel.fondo != null) {
            if (hotel.fondo.length > 0) {


                Bitmap bitmap = BitmapFactory.decodeByteArray(hotel.fondo, 0, hotel.fondo.length);


                if (bitmap != null) {
                    fondo_hotel.setImageBitmap(bitmap);
                }


            }
        }
        if (hotel.mapa != null) {
            if (hotel.mapa.length > 0) {


                Bitmap bitmap = BitmapFactory.decodeByteArray(hotel.mapa, 0, hotel.mapa.length);


                if (bitmap != null) {
                    mapa_hotel.setImageBitmap(bitmap);
                }


            }
        }

        return RootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this._id = getArguments().getInt(EVENTO_ID);
        ((MainActivity)activity).setHideNavigationBar();
        ((MainActivity)activity).setTituloVista(" ");
    }



    @Override
    public void onClick(View view) {

        if (view.getId()== R.id.map_hotel) {
            final Dialog dialogo = new Dialog((MainActivity)getActivity());
            dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            dialogo.setContentView(R.layout.map_box_layout);


            done = (Button) dialogo.findViewById(R.id.btn_done_image_dialog);
            TouchImageView mapadialog = (TouchImageView) dialogo.findViewById(R.id.image_dialog);
            mapadialog.setMaxZoom(3f);
            mapadialog.setMinZoom(1f);
            if (hotel.mapa != null) {
                if (hotel.mapa.length > 0) {


                    Bitmap bitmap = BitmapFactory.decodeByteArray(hotel.mapa, 0, hotel.mapa.length);


                    if (bitmap != null) {
                        mapadialog.setImageBitmap(bitmap);
                    }

                }
            }
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

    }
}
