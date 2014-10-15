package Fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import DB.DBHelper;
import DB.mSpeaker;
import DB.mStand;
import mc.mCongressDemo.MainActivity;
import mc.mCongressDemo.R;
import mc.mCongressDemo.myApp;

/**
 * Created by luisgonzalez on 21-03-14.
 */
public class mStandFragmentView extends Fragment implements AdapterView.OnItemClickListener {

    private static final String CATEGORY = "category";

    private Cursor c;
    private mSpeakerAdapter adapter;
    private ListView listView;
    private MainActivity activity;
    private String _categoria;
    speakerTask task;
    private myApp app;


    public static mStandFragmentView newInstance(MainActivity activity, String category) {

        // Instantiate a new fragment

       mStandFragmentView fragment = new mStandFragmentView();

        // Save the parameters
        Bundle bundle = new Bundle();
        bundle.putString(CATEGORY, category);
        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);

        activity.UnSetBackButton();
        activity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

        return fragment;

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


        adapter.getCursor().moveToPosition(i);
        mSpeaker c = new mSpeaker(adapter.getCursor());
        adapter.set_cur_position(c._id);
        Fragment fragment =  mDetalleStandViewController.newInstance(activity,c._id);
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame,fragment);
        ft.addToBackStack(null);
        ft.commit();

    }
    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        this._categoria = getArguments().getString(CATEGORY);
        ((MainActivity)activity).setHideNavigationBar();
        ((MainActivity)activity).setTituloVista(getString(R.string.Stand));

        /*if (((MainActivity)activity).getSlidingMenu().isMenuShowing()){
            ((MainActivity)activity).getSlidingMenu().toggle();
        }*/


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View RootView = inflater.inflate(R.layout.common_list_layout, container , false);
        listView = (ListView)RootView.findViewById(R.id.commonListView);
        activity = (MainActivity)getActivity();
        activity.setTituloVista(getString(R.string.Stand));

        //c = activity.getDbHandler().getCursorSpeaker(this._id);

       // adapter = new mSpeakerAdapter(activity,c,R.layout.cell_speaker);
        //listView.setAdapter(adapter);

        task = new speakerTask(activity);
        task.execute(1);
        app = (myApp) getActivity().getApplicationContext();

        listView.setOnItemClickListener(this);

        /*RootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = RootView.getRootView().getHeight() - RootView.getHeight();
                if (heightDiff > 400) { // if more than 100 pixels, its probably a keyboard...
                   activity.hideBotomBAr();

                }
                else {
                    activity.unHideBotomBar();

                }
            }
        });*/



        return RootView;
    }

    public class mSpeakerAdapter extends CursorAdapter{
        private  int layout;
        private LayoutInflater inflater;
        private int currentPosition;
        private Cursor sp_cursor;
        private DBHelper bd;


        public mSpeakerAdapter(Context context, Cursor c, int flags, DBHelper db) {
            super(context, c, FLAG_REGISTER_CONTENT_OBSERVER);
            this.layout = flags;
            this.inflater = LayoutInflater.from(context);
            this.sp_cursor = c;
            this.bd = db;

        }



        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            View v = inflater.inflate(layout, viewGroup, false);
            ViewHolder holder  =   new ViewHolder();
            holder.Nombre  =   (TextView)v.findViewById(R.id.nombre_speaker);
            holder.pais    =   (TextView)v.findViewById(R.id.lugar_speaker);
            holder.layout    =   (RelativeLayout)v.findViewById(R.id.Content);

            v.setTag(holder);
            return v;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

           /* mSpeaker c = new mSpeaker(cursor);
            TextView Nombre = (TextView)view.findViewById(R.id.nombre_speaker);
            TextView pais = (TextView)view.findViewById(R.id.lugar_speaker);
            Nombre.setText(c.nombre);
            pais.setText(c.lugar);*/
            mStand c = new mStand(cursor);
            ViewHolder holder  =   (ViewHolder)    view.getTag();
           /* if (c._id==currentPosition){
                holder.layout.setBackgroundResource(R.drawable.card_background_border_selected);
            }
            else {
                holder.layout.setBackgroundResource(R.drawable.card_background_border);
            }*/

            holder.Nombre.setText(c.nombre);
            holder.pais.setText("STAND: "+c.descripcion);


        }

        private class   ViewHolder  {
            TextView Nombre ;
            TextView pais ;
            RelativeLayout layout;
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
            sp_cursor = bd.getCursorStand(1,_categoria);
            Log.v("tildes",app.remueveTildes(string)
            );
            // Notify
            changeCursor(sp_cursor);
            notifyDataSetChanged();
        }

    }

    public class speakerTask extends AsyncTask<Integer ,Void,Cursor> {
        MainActivity activity;
        public speakerTask(MainActivity a){
            this.activity = a;


        }

        @Override
        protected Cursor doInBackground(Integer... integers) {
            return this.activity.getDbHandler().getCursorStand(integers[0],_categoria);

        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            adapter = new mSpeakerAdapter(activity,cursor,R.layout.cell_speaker,activity.getDbHandler());
            int cur_id = app.fragment_speaker_position;
            if (cur_id >=0) {
                adapter.set_cur_position(cur_id);
                listView.setSelection(cur_id);
            }
            listView.setAdapter(adapter);
        }
    }


}
