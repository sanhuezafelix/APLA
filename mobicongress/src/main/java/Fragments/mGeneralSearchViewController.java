package Fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import DB.DBHelper;
import DB.mEvent;
import Utils.ClearableEditText;
import mc.mCongressDemo.MainActivity;
import mc.mCongressDemo.R;
import mc.mCongressDemo.myApp;

/**
 * Created by luisgonzalez on 03-06-14.
 */
public class mGeneralSearchViewController  extends Fragment implements AdapterView.OnItemClickListener , View.OnClickListener{

    private static final String INDEX_CONGRESS = "index_congress";

    private ListView listView;
    private MainActivity activity;
    private myApp app;
    private GeneralSearchTask task;
    private ClearableEditText  general_search;
    private GeneralSearchAdapter adapter;
    private Button Close_Search;
    private int id;

    public static mGeneralSearchViewController newInstance(int index){

        mGeneralSearchViewController fragment = new mGeneralSearchViewController();
        // Save the parameters
        Bundle bundle = new Bundle();
        bundle.putInt(INDEX_CONGRESS, index);
        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);
        return  fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.general_search_fragment_layout, container,false);
        listView = (ListView)view.findViewById(R.id.grl_search_listView);
        this.Close_Search = (Button)view.findViewById(R.id.close_search);
        this.Close_Search.setOnClickListener(this);

        activity = (MainActivity)getActivity();
        //activity.setTituloVista("Expositores");

        //c = activity.getDbHandler().getCursorSpeaker(this._id);

        // adapter = new mSpeakerAdapter(activity,c,R.layout.cell_speaker);
        //listView.setAdapter(adapter);

        task = new GeneralSearchTask(activity);
        task.execute(this.id);
        app = (myApp) getActivity().getApplicationContext();

        listView.setOnItemClickListener(this);
        general_search = (ClearableEditText) view.findViewById(R.id.general_search_textView);
        general_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO filter data here
                app.setSpeakerSearchText(s.toString().toLowerCase());
                adapter.filterData(s);
            }
        });
        if (!TextUtils.isEmpty(app.getSpeakerSearchText())) {
            general_search.setText(app.getSpeakerSearchText());
        }




        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        adapter.getCursor().moveToPosition(i);
        mEvent c = new mEvent(adapter.getCursor());

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
        else
        {
            activity.PushView(mDetalleEventoFragmentView.newInstance(activity,c.id,true));
        }


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.close_search){

            InputMethodManager inputManager = (InputMethodManager)
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            this.activity.isFavoriteViewVisible = false;
            //this.activity.onBackPressed();
        }
    }


    /*
            Adaptador
     */

    public class GeneralSearchAdapter extends CursorAdapter {
        private  int layout;
        private LayoutInflater inflater;
        private int currentPosition;
        private Cursor search_cursor;
        private DBHelper bd;


        public GeneralSearchAdapter(Context context, Cursor c, int flags, DBHelper db) {
            super(context, c, FLAG_REGISTER_CONTENT_OBSERVER);
            this.layout = flags;
            this.inflater = LayoutInflater.from(context);
            this.search_cursor = c;
            this.bd = db;

        }



        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            View v = inflater.inflate(layout, viewGroup, false);
            ViewHolder holder = new ViewHolder();
            holder.content = (RelativeLayout)v.findViewById(R.id.color_content);
            holder.titulo_padre = (TextView)v.findViewById(R.id.title_padre);
            holder.hora_inicio = (TextView)v.findViewById(R.id.inicio_eem);
            holder.hora_fin = (TextView)v.findViewById(R.id.fin_eem);
            holder.titulo_nieto = (TextView)v.findViewById(R.id.titulo_nieto);
            holder.speaker = (TextView)v.findViewById(R.id.speaker_eem);
            holder.lugar = (TextView)v.findViewById(R.id.lugar_eem);
            holder.label = (TextView)v.findViewById(R.id.label_card_eem);
            v.setTag(holder);
            v.setTag(holder);
            return v;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {


            mEvent c = new mEvent(cursor);
            ViewHolder holder  =   (ViewHolder)view.getTag();

            if (!TextUtils.isEmpty(c.evento_padre)){
                if (c.tipo_evento_padre.length() > 2){


                    holder.titulo_padre.setText(c.tipo_evento_padre + ": " +c.evento_padre);

                }
                else {

                    holder.titulo_padre.setText(c.evento_padre);

                }

            }
            else {
                holder.titulo_padre.setVisibility(View.GONE);

            }
            if (c.esExtra==1){
                holder.content.setBackgroundColor(Color.parseColor("#9acff1"));
            }
            else {
                holder.content.setBackgroundColor(Color.parseColor("#dd9580"));
            }
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


        }

        private class   ViewHolder  {
            RelativeLayout content;
            TextView titulo_padre;
            TextView hora_inicio;
            TextView hora_fin ;
            TextView titulo_nieto ;
            TextView speaker ;
            TextView lugar ;
            TextView label ;
        }
        public void set_cur_position(int position) {
            this.currentPosition = position;
            if (position >= 0) {
                this.notifyDataSetChanged();
            }
        }

        public void filterData(CharSequence s) {
            String string = s.toString().toLowerCase();

            // Filter
            search_cursor = bd.getCursorGeneralSearch(1, app.remueveTildes(string));
            Log.v("tildes", app.remueveTildes(string)
            );
            // Notify
            changeCursor(search_cursor);
            notifyDataSetChanged();
        }

    }

    /*
            Tarea Asincronica
     */
    public class GeneralSearchTask extends AsyncTask<Integer ,Void,Cursor> {
        MainActivity activity;
        public GeneralSearchTask(MainActivity a){
            this.activity = a;


        }

        @Override
        protected Cursor doInBackground(Integer... integers) {
            return this.activity.getDbHandler().getCursorGeneralSearch(integers[0], "");

        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            adapter = new GeneralSearchAdapter(activity,cursor,R.layout.ahora_cell,activity.getDbHandler());
            int cur_id = app.fragment_speaker_position;
            if (cur_id >=0) {
                adapter.set_cur_position(cur_id);
                listView.setSelection(cur_id);
            }
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.id = getArguments().getInt(INDEX_CONGRESS);
        //((MainActivity)activity).setHideNavigationBar();
        //((MainActivity)activity).setTituloVista("Expositores");
    }
}
