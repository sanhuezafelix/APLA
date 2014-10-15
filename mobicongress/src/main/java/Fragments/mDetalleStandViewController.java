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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import DB.mStand;
import Utils.TouchImageView;
import Utils.mMapImageView;
import Utils.mPlaceCordenate;
import mc.mCongressDemo.MainActivity;
import mc.mCongressDemo.R;
import mc.mCongressDemo.myApp;

/**
 * Created by luisgonzalez on 27-03-14.
 */
public class mDetalleStandViewController extends Fragment implements View.OnClickListener {

    private static final String EVENTO_ID = "evento_id";
    private Integer _id;
    private TextView titulo_evento;
    private TextView local;
    private TextView tematica_stand;

    private  mMapImageView mapa;
    private RelativeLayout content;
    private Cursor cursor_event;
    private mStand stand;
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



    public static mDetalleStandViewController newInstance(MainActivity activity , int _id){
        mDetalleStandViewController fragment = new mDetalleStandViewController();
        Bundle bundle = new Bundle();
        bundle.putInt(EVENTO_ID, _id);
        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);
        activity.setBackbutton();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.detalle_stand , container , false);
        activity = (MainActivity)getActivity();
        app = (myApp)getActivity().getApplicationContext();



        this.mapa = (mMapImageView)RootView.findViewById(R.id.mapa_detalle_extra);
        this.content_map = (LinearLayout)RootView.findViewById(R.id.content_map);


        this.titulo_evento = (TextView)RootView.findViewById(R.id.titulo_stand);
        this.tematica_stand = (TextView)RootView.findViewById(R.id.tematica_stand);
        this.local = (TextView)RootView.findViewById(R.id.local_stand);
        activity.setBackbutton();
        cursor_event = activity.getDbHandler().getCursorDetalleStand(this._id);
        cursor_event.moveToFirst();
        stand = new mStand(cursor_event);
        this.titulo_evento.setText(stand.nombre);
        this.tematica_stand.setText(stand.categoria);
        this.local.setText("STAND: "+stand.descripcion);




        if (stand.mapa != null) {
            if (stand.mapa.length > 0) {


                Bitmap bitmap = BitmapFactory.decodeByteArray(stand.mapa, 0, stand.mapa.length);


                if (bitmap != null) {
                    mapa.setImageBitmap(bitmap);
                    this.pinMapa = new mPlaceCordenate(stand.lugar);
                    this.mapa.setCordenate(pinMapa.getCordx(),pinMapa.getCordy());
                }


            }
        }
        else {
            this.content_map.setVisibility(View.GONE);

        }



        this.mapa.setOnClickListener(this);

        return RootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this._id = getArguments().getInt(EVENTO_ID);
        ((MainActivity)activity).setHideNavigationBar();
        ((MainActivity)activity).setTituloVista(getString(R.string.Stand));
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

            if (stand.mapa != null) {
                if (stand.mapa.length > 0) {


                    Bitmap bitmap = BitmapFactory.decodeByteArray(stand.mapa, 0, stand.mapa.length);


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

