package Fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import DB.mEventoAbuelo;
import mc.mCongressDemo.MainActivity;
import mc.mCongressDemo.R;

/**
 * Created by luisgonzalez on 17-03-14.
 */
public class mProgramaABueloFragmentView extends Fragment implements AdapterView.OnItemClickListener{

    private static final String SHOW_SPLASH = "show_splash";
    private static final String INDEX = "index";
    private Cursor c;
    private MainActivity activity;
    private ListView listView;
    private mAbueloAdapter adapter;
    private int _id;
    Button done;

    public static mProgramaABueloFragmentView newInstance(MainActivity activity, int index , boolean splash) {

        mProgramaABueloFragmentView fragment = new mProgramaABueloFragmentView();

        Bundle bundle = new Bundle();
        bundle.putInt(INDEX, index);
        bundle.putBoolean(SHOW_SPLASH, splash);
        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);

        //activity.unHideNavigationBar();
        //activity.setHideNavigationBar();

        //activity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

        return fragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.common_list_layout,container,false);
        listView = (ListView)RootView.findViewById(R.id.commonListView);
        activity = (MainActivity)getActivity();
        activity.UnSetBackButton();

        c = activity.getDbHandler().getCursorEventoAbuelo(this._id);
        adapter = new mAbueloAdapter(activity.getApplicationContext(),c,R.layout.common_cell);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        return RootView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        adapter.getCursor().moveToPosition(i);
        mEventoAbuelo c = new mEventoAbuelo(adapter.getCursor());
        Fragment fragment =  mProgramaFragmentView.newInstance(activity,c.id);
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(null);
        ft.commit();

    }

    public class mAbueloAdapter extends CursorAdapter{
        private  int layout;
        private LayoutInflater inflater;

        public mAbueloAdapter(Context context, Cursor c, int flags) {
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

            mEventoAbuelo c = new mEventoAbuelo(cursor);
            TextView titulo = (TextView) view.findViewById(R.id.title_common);
            TextView descripcion =(TextView)view.findViewById(R.id.TextContent_common);
            titulo.setText(c.titulo);
            descripcion.setText(c.descripcion);



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
        }
        ((MainActivity) activity).unHideBotomAndTopBar();
        ((MainActivity) activity).
                setCurrentCurrentId(
                        getArguments().getInt(INDEX));
        this._id = getArguments().getInt(INDEX);

    }
}
