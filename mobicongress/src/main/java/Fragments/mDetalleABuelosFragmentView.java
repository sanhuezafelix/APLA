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
import android.widget.ListView;
import android.widget.TextView;

import DB.mEvent;
import mc.mCongressDemo.MainActivity;
import mc.mCongressDemo.R;

/**
 * Created by luisgonzalez on 21-03-14.
 */
public class mDetalleABuelosFragmentView extends Fragment implements AdapterView.OnItemClickListener {

    private static final String MODULO_ID = "modulo_id";
    private int _id;
    private Cursor c_actual;
    private Cursor c;
    private mEvent modulo_actual;
    private ListView listView;
    private TextView titulo_modulo;
    private TextView hora_modulo;
    private TextView fecha_modulo;
    MainActivity activity;
    private mDetalleModuloAdapter adapter;




    public  static mDetalleABuelosFragmentView newInstance(MainActivity activity , int _id){

        mDetalleABuelosFragmentView fragment = new mDetalleABuelosFragmentView();
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


            activity.PushView(mDetalleModuloFragmentView.newInstance(activity,c.id));




    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);this._id =  getArguments().getInt(MODULO_ID);
        ((MainActivity)activity).setHideNavigationBar();
        ((MainActivity)activity).setTituloVista(getString(R.string.event));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.abuelos_detalles_fragment_layout , container, false);
        activity = (MainActivity)getActivity();
        activity.setTituloVista(getString(R.string.event));
        this.listView = (ListView)RootView.findViewById(R.id.modulo_listview);
        this.titulo_modulo = (TextView)RootView.findViewById(R.id.titulo_modulo);
        this.hora_modulo = (TextView)RootView.findViewById(R.id.hora_modulo);
        this.fecha_modulo = (TextView)RootView.findViewById(R.id.fecha_modulo);

        c_actual = activity.getDbHandler().getAbueloActual(this._id);
        c = activity.getDbHandler().getEventosPadres(this._id);


        modulo_actual= new mEvent(c_actual);
        titulo_modulo.setText(modulo_actual.titulo);
        this.hora_modulo.setText(getString(R.string.from)+" "+modulo_actual.getStringStartTime()+" "+getString(R.string.to)+" "+ modulo_actual.getStringEndTime()+" hrs");
       this.fecha_modulo.setText(modulo_actual.getDayevent());
        this.adapter = new mDetalleModuloAdapter(activity,c,R.layout.programa_cell_abuelo);
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
           TextView hora_inicio = (TextView)view.findViewById(R.id.hora_eem);
            TextView hora_fin = (TextView)view.findViewById(R.id.horafin_eem);
            TextView titulo_nieto = (TextView)view.findViewById(R.id.titulo_nieto_pgr);

           // speaker.setVisibility(View.GONE);

          /*  if (c.speakerdos.equals(""))
            {
                speaker.setText(c.speakeruno);
            }
            else
            {
                speaker.setText(c.speakeruno+"\n"+c.speakerdos);
            }*/
            titulo_nieto.setText(c.titulo);

          hora_inicio.setText(c.getStringStartTime());
          hora_fin.setText(c.getStringEndTime());




        }
    }
}