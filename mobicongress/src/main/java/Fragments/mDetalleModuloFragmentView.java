package Fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import DB.mEvent;
import mc.mCongressDemo.MainActivity;
import mc.mCongressDemo.R;

/**
 * Created by luisgonzalez on 21-03-14.
 */
public class mDetalleModuloFragmentView extends Fragment implements AdapterView.OnItemClickListener {

    private static final String MODULO_ID = "modulo_id";
    private int _id;
    private Cursor c_actual;
    private Cursor c;
    private mEvent modulo_actual;

    private ListView listView;
    private TextView titulo_modulo;
    private TextView hora_modulo;
    private TextView lugar_modulo;
    private TextView fecha_modulo;
    private TextView Expositor_uno;
    private TextView Expositor_dos;
    private TextView Expositor_extra;
    private RelativeLayout expositor_content;

    MainActivity activity;
    private mDetalleModuloAdapter adapter;




    public  static mDetalleModuloFragmentView newInstance(MainActivity activity , int _id){

        mDetalleModuloFragmentView fragment = new mDetalleModuloFragmentView();
        Bundle bundle = new Bundle();
        bundle.putInt(MODULO_ID, _id);
        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);
        activity.setBackbutton();

        return fragment;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        adapter.getCursor().moveToPosition(i);
        mEvent c = new mEvent(adapter.getCursor());

       /* Toast toast1 =
                Toast.makeText(getActivity().getApplicationContext(),
                        "es unico =  "+ c.id, Toast.LENGTH_SHORT);

        toast1.show();
        */


            activity.PushView(mDetalleEventoFragmentView.newInstance(activity,c.id, false));




    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);this._id =  getArguments().getInt(MODULO_ID);
        ((MainActivity)activity).setHideNavigationBar();
        ((MainActivity)activity).setTituloVista(getString(R.string.event));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.simposios_fragment_layout , container, false);
        activity = (MainActivity)getActivity();
        activity.setTituloVista(getString(R.string.event));
        this.listView = (ListView)RootView.findViewById(R.id.modulo_listview);
        this.titulo_modulo = (TextView)RootView.findViewById(R.id.titulo_modulo);
        this.hora_modulo = (TextView)RootView.findViewById(R.id.hora_modulo);
        this.fecha_modulo = (TextView)RootView.findViewById(R.id.fecha_modulo);
        this.lugar_modulo = (TextView)RootView.findViewById(R.id.lugar_modulo);

        this.Expositor_uno= (TextView)RootView.findViewById(R.id.speaker_uno_dtm);
        this.Expositor_dos = (TextView)RootView.findViewById(R.id.speaker_dos_dtm);
        this.Expositor_extra = (TextView)RootView.findViewById(R.id.speaker_extra_dtm);
        this.expositor_content= (RelativeLayout)RootView.findViewById(R.id.speaker_content);


        c_actual = activity.getDbHandler().getModuloActual(this._id);
        c = activity.getDbHandler().getEventosNietos(this._id);


        modulo_actual= new mEvent(c_actual);
        if (modulo_actual.tipo_evento.length()>2){
            titulo_modulo.setText(modulo_actual.tipo_evento + ": "+modulo_actual.titulo);
        }
        else {
            titulo_modulo.setText(modulo_actual.titulo);
        }

        if (modulo_actual.speakeruno.equals("")){
            this.Expositor_uno.setVisibility(View.GONE);
        }
        else {
            this.expositor_content.setVisibility(View.VISIBLE);
            this.Expositor_uno.setText(modulo_actual.speakeruno +"\n"+getString(R.string.moderator));
        }
        if (modulo_actual.speakerdos.equals("")){

            this.Expositor_dos.setVisibility(View.GONE);
        }
        else {
            this.expositor_content.setVisibility(View.VISIBLE);
            this.Expositor_dos.setText(modulo_actual.speakerdos + "\n"+getString(R.string.moderator));
        }
        if (modulo_actual.lugar.equals("")){

            this.lugar_modulo.setVisibility(View.GONE);
        }
        else {

            this.lugar_modulo.setText(modulo_actual.lugar);
        }

        if (modulo_actual.speakeExtra.equals("")){
            this.Expositor_extra.setVisibility(View.GONE);
        }

        else {
            this.expositor_content.setVisibility(View.VISIBLE);
            this.Expositor_extra.setText(modulo_actual.speakeExtra);
        }

        this.hora_modulo.setText(getString(R.string.from)+" "+modulo_actual.getStringStartTime()+getString(R.string.to)+" "+ modulo_actual.getStringEndTime()+" hrs");
        this.fecha_modulo.setText(modulo_actual.getDayevent());

        this.adapter = new mDetalleModuloAdapter(activity,c,R.layout.programa_cell);
        this.listView.setAdapter(adapter);
        this.listView.setOnItemClickListener(this);



        return RootView;
    }

    public class mDetalleModuloAdapter extends CursorAdapter{

        private  int layout;
        private LayoutInflater inflater;



        public mDetalleModuloAdapter(Context context, Cursor c, int flags) {
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
            // TextView titulo_padre = (TextView)view.findViewById(R.id.title_padre);
            TextView hora = (TextView)view.findViewById(R.id.hora_eem);
            RelativeLayout background = (RelativeLayout)view.findViewById(R.id.Content);
            TextView titulo_nieto = (TextView)view.findViewById(R.id.titulo_nieto_pgr);
            TextView expositor_uno = (TextView)view.findViewById(R.id.speaker_uno_eem);
            TextView lugar = (TextView)view.findViewById(R.id.lugar_eem);
            TextView expositor_dos = (TextView)view.findViewById(R.id.speaker_dos_eem);
            TextView expositor_extra = (TextView)view.findViewById(R.id.speaker_extra_eem);
            ImageView fav = (ImageView)view.findViewById(R.id.fav_cell);
            background.setBackgroundResource(R.drawable.card_background_unicos);
            if (c.Favorito == 1)
            {
                fav.setVisibility(View.VISIBLE);
            }
            else {
                fav.setVisibility(View.GONE);
            }

            if (c.speakeruno.equals(""))
            {
                expositor_uno.setVisibility(View.GONE);
            }
            else
            {
                expositor_uno.setText(c.speakeruno);
            }
            if (c.speakerdos.equals(""))
            {
                expositor_dos.setVisibility(View.GONE);
            }
            else
            {
                expositor_dos.setText(c.speakerdos);
            }
            if (c.speakeExtra.equals(""))
            {
                expositor_extra.setVisibility(View.GONE);
            }
            else
            {
                expositor_extra.setText(c.speakeExtra);
            }

            titulo_nieto.setText(c.titulo);

            hora.setText(getString(R.string.from)+" "+c.getStringStartTime()+ getString(R.string.to)+" "+c.getStringEndTime());

            lugar.setText(c.lugar);


        }
    }
}