package Fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import DB.mEvent;
import DB.mSpeaker;
import mc.mCongressDemo.MainActivity;
import mc.mCongressDemo.R;

/**
 * Created by luisgonzalez on 22-03-14.
 */
public class mDetalleSpeakerView extends Fragment implements AdapterView.OnItemClickListener {

    private static final String EVENTO_ID = "evento_id";
    private static final String WHIT_IMAGE = "whit_image";
    private Fragment fragment;

    TextView nombre;
    TextView lugar;
    TextView bio;
    Cursor c;
    Cursor c2;
    int _id;
    MainActivity activity;
    private ListView listView;
    mspeakerdetAdapter adapter;
    private View RootView;
    private ImageView foto;

    public static mDetalleSpeakerView newInstance(MainActivity activity, int index , boolean image) {

        // Instantiate a new fragment

        mDetalleSpeakerView fragment = new mDetalleSpeakerView();


        // Save the parameters
        Bundle bundle = new Bundle();
        bundle.putInt(EVENTO_ID, index);
        bundle.putBoolean(WHIT_IMAGE, image);
        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);
        activity.setBackbutton();
        activity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);


        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //View RootView = inflater.inflate(R.layout.detalle_speaker_layout,container,false);
        if (getArguments().getBoolean(WHIT_IMAGE)){
            RootView = inflater.inflate(R.layout.detalle_speaker_image_layout,container,false);


        }
        else {

            RootView = inflater.inflate(R.layout.detalle_speaker_layout,container,false);

        }
        activity = (MainActivity)getActivity();
        activity.setTituloVista(" ");
        nombre = (TextView)RootView.findViewById(R.id.nombre_detalle_speaker);
        lugar = (TextView)RootView.findViewById(R.id.lugar_detalle_speaker);
        bio = (TextView)RootView.findViewById(R.id.biografia_speaker_detalle);

        bio.setMovementMethod(new ScrollingMovementMethod());
        c = activity.getDbHandler().getCursorDetalleEventosSpeaker(this._id);
        mSpeaker speaker = new mSpeaker(c);
        nombre.setText(speaker.nombre);
        lugar.setText(speaker.lugar);
        bio.setText(speaker.bio);
        if (getArguments().getBoolean(WHIT_IMAGE)){
            this.foto = (ImageView)RootView.findViewById(R.id.image_speaker);
            if (speaker.imagen!=null){

                if (speaker.imagen.length > 0) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(speaker.imagen, 0, speaker.imagen.length);

                    if (bitmap!=null){
                        this.foto.setImageBitmap(bitmap);
                    }

                }
            }
        }

        c2 = activity.getDbHandler().getCursorAlleventoSpeaker(this._id);
        this.listView = (ListView)RootView.findViewById(R.id.detalle_speaker_listview);
        this.adapter = new mspeakerdetAdapter(activity,c2,R.layout.ahora_cell);
        this.listView.setAdapter(adapter);
        this.listView.setOnItemClickListener(this);



        return RootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this._id =getArguments().getInt(EVENTO_ID);
        ((MainActivity)activity).setHideNavigationBar();
        ((MainActivity)activity).setTituloVista(" ");

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        adapter.getCursor().moveToPosition(i);

        mEvent c = new mEvent(adapter.getCursor());
        adapter.set_cur_position(c.id);
        if (c.esUnico==1){
            fragment =  mDetalleExtraViewController.newInstance(activity,c.id);
        }
        else if (c.esUnico == 2){
            fragment = mDetalleModuloFragmentView.newInstance(activity,c.id);
        }
        else {
            fragment =  mDetalleEventoFragmentView.newInstance(activity,c.id, true);
        }


        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }


    public class mspeakerdetAdapter extends CursorAdapter{

        private  int layout;
        private LayoutInflater inflater;
        private int currentPosition;

        public mspeakerdetAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
            this.layout = flags;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            View v = inflater.inflate(layout, viewGroup, false);
            return v;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            mEvent c = new mEvent(cursor);
            RelativeLayout  layout = (RelativeLayout)view.findViewById(R.id.Content);
            TextView titulo_padre = (TextView)view.findViewById(R.id.title_padre);
            TextView hora_inicio = (TextView)view.findViewById(R.id.inicio_eem);
            TextView hora_fin = (TextView)view.findViewById(R.id.fin_eem);
            TextView titulo_nieto = (TextView)view.findViewById(R.id.titulo_nieto);
            TextView speaker = (TextView)view.findViewById(R.id.speaker_eem);
            TextView lugar = (TextView)view.findViewById(R.id.lugar_eem);
           // ImageView fav = (ImageView)view.findViewById(R.id.fav_cell);
            if (c.id==currentPosition){
                layout.setBackgroundResource(R.drawable.card_background_border_selected);
            }
            else {
                layout.setBackgroundResource(R.drawable.card_background_border);
            }

            if (!TextUtils.isEmpty(c.evento_padre)){
                if (c.esUnico == 2){
                    titulo_padre.setText(getString(R.string.tematic)+" "+c.evento_padre);
                }
                else {
                    if (c.tipo_evento != ""){
                        titulo_padre.setText(c.tipo_evento+": "+c.evento_padre);
                    }
                    else {
                        titulo_padre.setText(c.evento_padre);
                    }

                }

            }
            else {
                titulo_padre.setText("");
            }


            titulo_nieto.setText(c.titulo);
            if (c.speakerdos.equals(""))
            {
                speaker.setText(c.speakeruno);
            }
            else
            {
                speaker.setText(c.speakeruno+"\n"+c.speakerdos);
            }

            hora_inicio.setText(c.getStringStartTime());
            hora_fin.setText(c.getStringEndTime());
            lugar.setText(c.lugar);

        }
        public void set_cur_position(int position) {
            this.currentPosition = position;
            if (position >= 0) {
                this.notifyDataSetChanged();
            }
        }
    }

}
