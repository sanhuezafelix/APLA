package Fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import DB.mEvent;
import mc.mCongressDemo.MainActivity;
import mc.mCongressDemo.R;
import mc.mCongressDemo.myApp;

/**
 * Created by luisgonzalez on 02-04-14.
 */
public class mDetalleAcompFragmentView extends Fragment implements AdapterView.OnItemClickListener {
    private static final String INDEX = "index";
    private MainActivity activity;
    ListView listView;
    private FavoriteAdapter adatador;
    private Cursor fv_cursor;
    private myApp app;
    public static mDetalleAcompFragmentView newInstance(MainActivity activity, int index) {



        mDetalleAcompFragmentView fragment = new mDetalleAcompFragmentView();



        Bundle bundle = new Bundle();
        bundle.putInt(INDEX, index);
        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);


        // activity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.common_list_layout,container,false);
        this.listView = (ListView)rootview.findViewById(R.id.commonListView);
        this.activity = (MainActivity)getActivity();
        app = (myApp)getActivity().getApplicationContext();
        activity.setTituloVista(getString(R.string.Accompanist));

        fv_cursor = activity.getDbHandler().getCursorAlleventoAcompanante(getArguments().getInt(INDEX));
        adatador= new FavoriteAdapter(activity,fv_cursor,R.layout.favorito_cell);
        if (adatador.getCount() == 0){

            rootview.setBackgroundResource(R.drawable.no_fav);
        }
        listView.setAdapter(adatador);
        listView.setOnItemClickListener(this);
        return rootview;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity)activity).setHideNavigationBar();
        ((MainActivity)activity).setTituloVista(getString(R.string.Accompanist));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {




        adatador.getCursor().moveToPosition(i);
        mEvent c = new mEvent(adatador.getCursor());
        this.app.setCurrentFavoritePosition(c.id);
        adatador.set_cur_position(c.id);
        /*if (c.esExtra ==1 || c.esUnico == 1){

            if (c.tipo_evento.equals("Evento Social"))
            {
                activity.PushView(mDetalleSocialViewController.newInstance(activity,c.id));
            }
            else {
                activity.PushView(mDetalleExtraViewController.newInstance(activity,c.id));
            }

        }
        else
        {
            if (c.tipo_evento.equals("Evento Social"))
            {
                activity.PushView(mDetalleSocialViewController.newInstance(activity,c.id));
            }
            else {
            activity.PushView(mDetalleEventoFragmentView.newInstance(activity,c.id,false));}
        }*/
        activity.PushView(mDetalleSocialViewController.newInstance(activity,c.id));


    }
    public class FavoriteAdapter extends CursorAdapter {

        private  int layout;
        private LayoutInflater inflater;
        private int currentPosition = -1;
        private ArrayList<String> visible_date;
        private ArrayList<Integer> visible_id;
        public int visible_count = 0;
        private String current_date = "";


        private int padding = 0;
        private int initialx = 0;
        private int currentx = 0;
        private boolean ismove = false;
        private  ViewHolder viewHolder;
        private Toast toast1;

        public FavoriteAdapter(Context context, Cursor c, int flags ) {
            super(context, c, FLAG_REGISTER_CONTENT_OBSERVER);
            this.layout = flags;

            this.inflater = LayoutInflater.from(context);
            visible_date = new ArrayList<String>();
            visible_id = new ArrayList<Integer>();
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            View v = inflater.inflate(layout, viewGroup, false);
            ViewHolder holder  =   new ViewHolder();
            holder.titulo_nieto  =   (TextView)v.findViewById(R.id.titulo_nieto_pgr);
            holder.hora_inicio = (TextView)v.findViewById(R.id.inicio_eem);
            holder.hora_fin = (TextView)v.findViewById(R.id.fin_eem);
            holder.layout    =   (RelativeLayout)v.findViewById(R.id.Content);
            holder.speaker= (TextView)v.findViewById(R.id.speaker_eem);
            holder.lugar = (TextView)v.findViewById(R.id.lugar_eem);
            holder.label = (TextView)v.findViewById(R.id.label_fav);
            // h//older.btn_dlt = (ImageButton)v.findViewById(R.id.btn_delete_fav);




            v.setTag(holder);
            // v.setOnTouchListener(this);

            return v;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            mEvent c = new mEvent(cursor);
            ViewHolder holder  =   (ViewHolder)    view.getTag();


            addToVisibleIds(c.getStringStartDate(),c.id);
            if (visible_id.size() >=2){
                if (visible_id.get(0)== c.id){
                    holder.label.setVisibility(view.VISIBLE);
                    holder.label.setText(c.getDayevent());
                }
                if (visible_id.get(1)== c.id){
                    holder.label.setVisibility(view.VISIBLE);
                    holder.label.setText(c.getDayevent());
                }
                if (c.id != visible_id.get(0)&&c.id != visible_id.get(1))
                {
                    holder.label.setVisibility(view.GONE);
                }
            }
            else {
                if (visible_id.get(0)== c.id){
                    holder.label.setVisibility(view.VISIBLE);
                    holder.label.setText(c.getDayevent());
                }

            }
         /*   if (c.id ==currentPosition){
                holder.layout.setBackgroundResource(R.drawable.card_background_border_selected);
            }
            else {
                holder.layout.setBackgroundResource(R.drawable.card_background_border);
            }*/
            holder.titulo_nieto.setText(c.titulo);
            if (c.speakerdos.equals(""))
            {
                holder.speaker.setText(c.speakeruno);
            }
            else
            {
                holder.speaker.setText(c.speakeruno+"\n"+c.speakerdos);
            }

            holder.hora_inicio.setText(c.getStringStartTime());
            holder.hora_fin.setText(c.getStringEndTime());
            holder.lugar.setText(c.lugar);
            holder.posicion =  c.id;




        }
        public void set_cur_position(int position) {
            this.currentPosition = position;
            if (position >= 0) {
                this.notifyDataSetChanged();
            }
        }

        private void addToVisibleIds(String date , int _id) {
            int size = visible_date.size();
           /* if (size <= 0){
                this.current_date = date;
                visible_date.add(date);
                Log.e("visible_size", " date uno = " + size);
            }
            else
            {
                for (int i = 0; i < visible_date.size() ; i++) {
                    if (!current_date.equals(date)){
                        visible_id.add(_id);
                        Log.e("visible_size", "= " + size + "id "+ visible_id.get(i));
                    }
                }

            }*/
            if (visible_id.size()<= 0)
            {

                this.visible_date.add(date) ;
                visible_id.add(_id);


            }
            else
            {
                if (visible_id.size()< 4)
                {
                    if (visible_id.size()<2){
                        if (!this.visible_date.get(0).equals(date))
                        {
                            this.visible_date.add(date) ;
                            visible_id.add(_id);
                        }
                    }
                    if (visible_id.size()<3&&visible_id.size()==2){
                        if (!this.visible_date.get(1).equals(date))
                        {
                            this.visible_date.add(date) ;
                            visible_id.add(_id);
                        }
                    }
                    if (visible_id.size()<4&&visible_id.size()==3){
                        if (!this.visible_date.get(2).equals(date))
                        {
                            this.visible_date.add(date) ;
                            visible_id.add(_id);
                        }
                    }




                }

            }

            for (int i = 0; i < visible_id.size(); i++) {
                Log.e("visible_id", "= " + visible_id.get(i));

            }
        }
    }


    public class ViewHolder{
        TextView hora_inicio ;
        TextView hora_fin ;
        TextView titulo_nieto ;
        TextView speaker ;
        TextView lugar ;
        ImageButton btn_dlt;
        TextView label;
        RelativeLayout layout;

        int posicion;
    }


}

