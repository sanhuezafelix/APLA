package Fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import DB.mEvent;
import mc.mCongressDemo.MainActivity;
import mc.mCongressDemo.R;
import mc.mCongressDemo.myApp;

/* clase del pagerview */
public class mPagerFragmentView extends Fragment implements AdapterView.OnItemClickListener {



    private static final String DATE = "date";
    private static final String INDEX_CONGRESS = "index_congress";
    private ListView listView ;

    private String date;
    private MainActivity activity;
    private Cursor c;
    private listTask task;
    private myApp app;
    private mPagerEventAdapter adapter;




    public static mPagerFragmentView newInstance(MainActivity activity, String date , int congress) {

        // Instantiate a new fragment

        mPagerFragmentView fragment = new mPagerFragmentView();


        // Save the parameters
        Bundle bundle = new Bundle();
        bundle.putString(DATE, date);
        bundle.putInt(INDEX_CONGRESS, congress);

        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);
        activity.UnSetBackButton();
        activity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);


        return fragment;

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.common_list_layout,container,false);
        app = (myApp) getActivity().getApplicationContext();

        activity = (MainActivity)getActivity();
        activity.unHideNavigationBar();
      //  c = activity.getDbHandler().getAllEventPrograma(this.date);
        listView = (ListView) RootView.findViewById(R.id.commonListView);
        listView.setOnItemClickListener(this);

        task = new listTask(activity);
        task.execute(date);

       // listView.setAdapter(adapter);


        return RootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.date =  getArguments().getString(DATE);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        adapter.getCursor().moveToPosition(i);
        mEvent c = new mEvent(adapter.getCursor());
        app.setCurrentSpeakerPosition(c.id);
        adapter.set_cur_position(c.id);

      /*  Toast toast1 =
                Toast.makeText(getActivity().getApplicationContext(),
                        "tipo =  "+ c.tipo_evento, Toast.LENGTH_SHORT);

        toast1.show();*/

        if (c.esExtra ==1 || c.esUnico == 1){

            if (c.tipo_evento.equals("Evento Social"))
            {
                activity.PushView(mDetalleSocialViewController.newInstance(activity,c.id));
            }
            else {
                activity.PushView(mDetalleExtraViewController.newInstance(activity,c.id));
            }

        }
        else if(c.esUnico == 4){

            activity.PushView(mDetalleABuelosFragmentView.newInstance(activity,c.id));
        }
        else
        {
            activity.PushView(mDetalleModuloFragmentView.newInstance(activity,c.id));
        }
    }

    public class mPagerEventAdapter extends CursorAdapter {
        private  int layout;
        private LayoutInflater inflater;
        private int currentPosition = -1;

        public mPagerEventAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
            this.layout = flags;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View v = inflater.inflate(layout, parent, false);
            return v;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            mEvent c = new mEvent(cursor);
            RelativeLayout layout = (RelativeLayout)view.findViewById(R.id.Content);
            TextView hora = (TextView)view.findViewById(R.id.hora_eem);
            TextView titulo_nieto = (TextView)view.findViewById(R.id.titulo_nieto_pgr);
            TextView lugar = (TextView)view.findViewById(R.id.lugar_eem);
            TextView expositor_uno = (TextView)view.findViewById(R.id.speaker_uno_eem);
            TextView expositor_dos = (TextView)view.findViewById(R.id.speaker_dos_eem);
            TextView expositor_extra = (TextView)view.findViewById(R.id.speaker_extra_eem);
            ImageView fav = (ImageView)view.findViewById(R.id.fav_cell);

            if (c.Favorito== 1){
                fav.setVisibility(View.VISIBLE);
            }
            else {
                fav.setVisibility(View.GONE);
            }
            if (c.id==currentPosition){
               layout.setBackgroundResource(R.drawable.card_background_journ_select);
            }
            else {
                if (c.esUnico ==1||c.esExtra ==1){
                   layout.setBackgroundResource(R.drawable.card_background_unicos);
                }
                else
                {
                    layout.setBackgroundResource(R.drawable.card_background_padres);
                }
            }
           // TextView titulo_padre = (TextView)view.findViewById(R.id.title_padre);
            if (c.speakeruno.equals("")){
                expositor_uno.setVisibility(View.GONE);
            }
            else {
                expositor_uno.setText(c.speakeruno);
            }
            if (c.speakerdos.equals("")){
                expositor_dos.setVisibility(View.GONE);
            }
            else {
                expositor_dos.setText(c.speakerdos);
            }
            if (c.speakeExtra.equals("")){
                expositor_extra.setVisibility(View.GONE);
            }
            else {
                expositor_extra.setText(c.speakeExtra);
            }





            hora.setText("De "+c.getStringStartTime()+" a "+c.getStringEndTime()+" Hrs.");

            /*if (c.lugar.equals("")){
                lugar.setVisibility(View.GONE);
                if (c.tipo_evento.length()>2){
                    titulo_nieto.setText(c.tipo_evento + ": "+c.titulo);
                }
                else {
                    titulo_nieto.setText( c.titulo);
                }
            }
            else {
                lugar.setVisibility(View.GONE);
                if (c.tipo_evento.length()>2){
                    titulo_nieto.setText("["+c.lugar+"] "+c.tipo_evento + ": "+c.titulo);
                }
                else {
                    titulo_nieto.setText("["+c.lugar+"] "+ c.titulo);
                }
            }*/
            if (c.tipo_evento.length()>2){
                titulo_nieto.setText(c.tipo_evento + ": "+c.titulo);
            }
            else {
                titulo_nieto.setText( c.titulo);
            }
            lugar.setText(c.lugar);

        }
        public void set_cur_position(int position) {
            this.currentPosition = position;
            if (position >= 0) {
                this.notifyDataSetChanged();
            }
        }
        private class   ViewHolder  {
            TextView hora_inicio ;
            TextView hora_fin ;
            TextView titulo_nieto ;
            TextView speaker ;
            TextView lugar ;
            RelativeLayout layout;
        }
    }


    public class  listTask extends AsyncTask<String,Void,Cursor>{

        MainActivity activity;
        public listTask(MainActivity c){
            activity=c;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Cursor doInBackground(String... strings) {

            //return  activity.getDbHandler().getCursorMergePrograma(strings[0],getArguments().getInt(INDEX_CONGRESS));
            return  activity.getDbHandler().getAllEventPrograma(strings[0]);

        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            adapter = new mPagerEventAdapter(activity.getApplicationContext(),cursor,R.layout.programa_cell);
            listView.setAdapter(adapter);
            int cur_id = app.fragment_speaker_position;
            if (cur_id >=0) {
                adapter.set_cur_position(cur_id);
            }

        }
    }
}
