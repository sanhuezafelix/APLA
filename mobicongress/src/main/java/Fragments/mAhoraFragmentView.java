package Fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

import DB.mEvent;
import mc.mCongressDemo.MainActivity;
import mc.mCongressDemo.R;
import mc.mCongressDemo.myApp;

/**
 * Created by luisgonzalez on 10-03-14.
 */
public class mAhoraFragmentView extends Fragment implements AdapterView.OnItemClickListener , AbsListView.OnScrollListener{

    private static final String INDEX = "index";
    private static final String EVENTO_ID = "evento_id";
    private static final String SHOW_SPLASH = "show_splash";
    private int posicion;
    private mAHoraAdapter adapter;
    private  MainActivity activity;
    //TextView label;
    private myApp app;
    ListView listView ;
    Cursor c;
    private Fragment mDetalle;

    public static mAhoraFragmentView newInstance(MainActivity activity, int index , boolean splash) {

        // Instantiate a new fragment

        mAhoraFragmentView fragment = new mAhoraFragmentView();
        // Save the parameters
        Bundle bundle = new Bundle();
        bundle.putInt(INDEX, index);
        bundle.putBoolean(SHOW_SPLASH, splash);
        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);
        activity.UnSetBackButton();
        activity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);



        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.ahora_fragment_layout,container,false);
        // label = (TextView)RootView.findViewById(R.id.label_eem);
        activity = (MainActivity)getActivity();
        app = (myApp) getActivity().getApplicationContext();
        c = activity.getDbHandler().getCursorAhora(this.posicion);
        activity.unHideNavigationBar();
        activity.setVisibilityChangeViewChat(View.GONE);
        activity.UnSetBackButton();
        ((MainActivity)getActivity()).setVisibilityChangeViewChat(View.GONE);
        /*Toast toast1 =
                Toast.makeText(getActivity().getApplicationContext(),
                        "id "+ this.posicion+ "cantidad"+c.getCount(), Toast.LENGTH_SHORT);

        toast1.show();*/
        listView = (ListView) RootView.findViewById(R.id.commonListView);


        adapter = new mAHoraAdapter(activity.getApplicationContext(),c,R.layout.ahora_cell__whit_label);
        if (adapter.getCount() == 0){
            // label.setVisibility(View.GONE);
            if (getString(R.string.idioma).equals("es")) {
                RootView.setBackgroundResource(R.drawable.ahora_empty);
            }
            else if(getString(R.string.idioma).equals("pt")){
                RootView.setBackgroundResource(R.drawable.ahora_empty_portugues);
            }
            else {
                RootView.setBackgroundResource(R.drawable.ahora_empty_ingles);
            }
        }

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(this);
        if (adapter.visible_count==0){
            //label.setText("Próximas Actividades");
        }
        if (app.getpushView())
        {
            if (app.getFavoriteAlarm()){
                activity.menuChangeView(new mFavoritosFragmentView(),true);
            }
            else {
                activity.menuChangeView(mNovedadesFragmentView.newInstance(activity,1),true);
            }
            app.setpushView(false);

        }
        return RootView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        adapter.getCursor().moveToPosition(i);
        mEvent c = new mEvent(adapter.getCursor());

        if (c.esExtra ==1 || c.esUnico == 1){

            if (c.tipo_evento.equals("Programa De Acompañantes"))
            {
                activity.PushView(mDetalleSocialViewController.newInstance(activity,c.id));
            }
            else {
                activity.PushView(mDetalleExtraViewController.newInstance(activity,c.id));
            }

        }
        else {
            activity.PushView(mDetalleEventoFragmentView.newInstance(activity,c.id,true));
        }



    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (getArguments().getBoolean(SHOW_SPLASH))
        {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ((MainActivity)getActivity()).setVisibilityChangeViewChat(View.GONE);
        }


        this.posicion = getArguments().getInt(INDEX);

        ((MainActivity) activity).
                setCurrentCurrentId(
                        getArguments().getInt(INDEX));
        ((MainActivity) activity).unHideBotomAndTopBar();



    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {


    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i2, int i3) {



        if (adapter.visible_count == 0) {
            return;
        }


       /* if (i <= adapter.visible_count) {
            label.setVisibility(View.VISIBLE);
            label.setText("En Este Momento");
        } else {
            label.setText("Próximas Actividades");
        }*/



    }

    public class mAHoraAdapter extends CursorAdapter{
        private boolean viewlabel;

        private  int layout;
        private LayoutInflater inflater;
        public int visible_count = 0;
        private ArrayList<Integer> visible_ids;
        private ArrayList<Integer> visible_act;
        int total;

        public mAHoraAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
            this.layout = flags;
            this.inflater = LayoutInflater.from(context);
            visible_ids = new ArrayList<Integer>();
            visible_act = new ArrayList<Integer>();
            this.total = c.getCount();
            this.viewlabel = false;

        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View v = inflater.inflate(layout, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.titulo_padre = (TextView)v.findViewById(R.id.title_padre);
            holder.hora_inicio = (TextView)v.findViewById(R.id.inicio_eem);
            holder.hora_fin = (TextView)v.findViewById(R.id.fin_eem);
            holder.titulo_nieto = (TextView)v.findViewById(R.id.titulo_nieto);
            holder.speaker = (TextView)v.findViewById(R.id.speaker_eem);
            holder.lugar = (TextView)v.findViewById(R.id.lugar_eem);
            holder.label = (TextView)v.findViewById(R.id.label_card_eem);
            Log.v("_count", " es + posicion " +visible_count);

            v.setTag(holder);
            return v;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            mEvent c = new mEvent(cursor);
            ViewHolder holder  =   (ViewHolder)view.getTag();
            RelativeLayout layout = (RelativeLayout)view.findViewById(R.id.color_content);


            if (c.isOnGoing()) {
                addToVisibleIds(c.id);
            }
            else {
                addToVisibleIdsnextActivitys(c.id);
            }

            if (visible_ids.size()>0 && visible_act.size()==0){
                if ( c.id == visible_ids.get(0))
                {
                    holder.label.setVisibility(View.GONE);
                    holder.label.setText(getString(R.string.eem));
                }
                else {
                    holder.label.setVisibility(view.GONE);
                }

            }
            if (visible_ids.size()>0 && visible_act.size()>0)
            {


                if (c.id == visible_ids.get(0)) {
                    holder.label.setVisibility(View.GONE);
                    holder.label.setText(getString(R.string.eem));
                }
                if (c.id == visible_act.get(0))
                {
                    holder.label.setVisibility(View.VISIBLE);
                    holder.label.setText(getString(R.string.nextact));
                }
                if (c.id != visible_ids.get(0)&&c.id != visible_act.get(0))
                {
                    holder.label.setVisibility(view.GONE);
                }

            }
            if (visible_ids.size()<=0 && visible_act.size()>0)
            {
                if (c.id == visible_act.get(0))
                {
                    holder.label.setVisibility(View.VISIBLE);
                    holder.label.setText(getString(R.string.nextact));
                }
                else
                {
                    holder.label.setVisibility(view.GONE);
                }
            }

           /* if (visible_ids.size()>0){
                if ( c.id == visible_ids.get(0))
                {
                    holder.label.setVisibility(View.VISIBLE);
                    holder.label.setText("En este Momento");
                }
                else if(visible_act.size()>0)
                {
                    if ( c.id == visible_act.get(0))
                    {
                        holder.label.setVisibility(View.VISIBLE);
                        holder.label.setText("Proximas Actividades");
                    }
                }
                else
                {
                    holder.label.setVisibility(view.GONE);

                }


            }
            else
            {
                if (visible_act.size()>0){
                    if ( c.id == visible_act.get(0))
                    {
                        holder.label.setVisibility(View.VISIBLE);
                        holder.label.setText("Proximas Actividades");
                    }

                }
                else
                {
                    holder.label.setVisibility(view.GONE);

                }

            }*/

            if (!TextUtils.isEmpty(c.evento_padre)){

              if (c.tipo_evento_padre.length() > 1){

                    holder.titulo_padre.setText(c.tipo_evento_padre + ": " +c.evento_padre);
                }
                else {

                    holder.titulo_padre.setText(c.evento_padre);
                }



                layout.setBackgroundResource(R.drawable.card_background_padres);
                holder.titulo_padre.setVisibility(View.VISIBLE);

            }
            else  if (TextUtils.isEmpty(c.evento_padre)) {
                holder.titulo_padre.setVisibility(View.GONE);
                layout.setBackgroundResource(R.drawable.card_background_unicos);
            }

          /*  if (c.tipo_evento.equals("Evento Social")){
                layout.setBackgroundResource(R.drawable.card_background_extras);
            }
            else{

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
            holder.lugar.setVisibility(View.GONE);

        }

        /* private void addToVisibleIds(int id) {
             int size = visible_ids.size();
             for (int i = 0; i < size; i++) {
                 if (id == visible_ids.get(i)) {
                     return;
                 }
             }
             visible_ids.add(id);
             visible_count = visible_count + 1;
             Log.e("visible_count", "= " + visible_count);
         }*/
        private void addToVisibleIds(int id) {
            int size = visible_ids.size();
           /* for (int i = 0; i < size; i++) {
                if (id == visible_ids.get(i)) {
                    return;
                }
            }*/
            if (size==0){
                visible_ids.add(id);
            }

            visible_count = visible_count + 1;
            Log.e("visible_count", "= " + visible_count);
            Log.e("visible_size", "= " + size);
        }
        private void addToVisibleIdsnextActivitys(int id) {
            int size = visible_act.size();
            if (size==0){
                visible_act.add(id);
            }

        }

        public void clear() {
            this.visible_count = 0;
            this.visible_ids.clear();
        }

        private class   ViewHolder  {
            TextView titulo_padre;
            TextView hora_inicio;
            TextView hora_fin ;
            TextView titulo_nieto ;
            TextView speaker ;
            TextView lugar ;
            TextView label ;

        }
    }
}
