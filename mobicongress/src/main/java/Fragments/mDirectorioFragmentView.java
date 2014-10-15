package Fragments;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import DB.mCongreso;
import mc.mCongressDemo.MainActivity;
import mc.mCongressDemo.R;

/**
 * Created by luisgonzalez on 11-03-14.
 */

public class mDirectorioFragmentView extends Fragment implements AdapterView.OnItemClickListener {

    private static final String INDEX = "index";
    private ListView Directorio;
    private Fragment fragment;
    private Cursor C;
    private View RootView;
    mDirectorioAdapter adapter;
    MainActivity activity;

    public static mDirectorioFragmentView newInstance(MainActivity activity, int index) {



        mDirectorioFragmentView fragment = new mDirectorioFragmentView();



        Bundle bundle = new Bundle();
        bundle.putInt(INDEX, index);
        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);
        activity.setHideNavigationBar();
       // activity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

        return fragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       this.RootView = inflater.inflate(R.layout.common_list_layout, container,false);

        Directorio = (ListView) RootView.findViewById(R.id.commonListView);

        activity = (MainActivity)getActivity();

        C = activity.getDbHandler().getCongress();


        adapter = new mDirectorioAdapter(activity.getApplicationContext(),C,R.layout.cell_directorio);
        Directorio.setAdapter(adapter);

        // Assign adapter to ListView

        Directorio.setOnItemClickListener(this);

        return  RootView;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


        adapter.getCursor().moveToPosition(i);
        mCongreso c = new mCongreso(adapter.getCursor());

        /*Toast toast1 =
                Toast.makeText(getActivity().getApplicationContext(),
                        "id "+ c.id+ " posicion "+IPVS , Toast.LENGTH_SHORT);

        toast1.show();*/
        this.Directorio.setVisibility(View.INVISIBLE);
        activity.hideBotomAndTopBAr();
       /* if (c.id == 1){
            this.RootView.setBackgroundResource(R.drawable.splash_neumologia);
        }
        else {
            this.RootView.setBackgroundResource(R.drawable.splash_cirugia);

        }*/





        MainActivity activity = (MainActivity)getActivity();
        activity.setCurrentCongress(c.descripcion);

        activity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        switch (activity.getSelectedSegmentIndex()){
            case R.id.segment_button_one:

                fragment = mAhoraFragmentView.newInstance(activity,c.id, true);
                break;
            case R.id.segment_button_two:

                if (c.descripcion.equals("abuelo")){
                    fragment = mProgramaABueloFragmentView.newInstance(activity,c.id,true);
                }
                else {
                    fragment = mProgramaFragmentView.newInstance(activity,c.id);
                }
                break;
            default:
                if (c.descripcion.equals("abuelo")){
                    fragment = mProgramaABueloFragmentView.newInstance(activity,c.id,true);
                }
                else {
                    fragment = mProgramaFragmentView.newInstance(activity,c.id);
                }
                break;
        }



        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(null);
        ft.commit();


    }



    /*
*    adaptador de datos
*/

    public class mDirectorioAdapter extends CursorAdapter{

        private LayoutInflater inflater;
        private int layout;
        private int cur_position;




        public mDirectorioAdapter(Context context, Cursor c, int layout) {
            super(context, c, layout);
            this.layout = layout;
            this.inflater = LayoutInflater.from(context);


        }


        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View v = inflater.inflate(layout, parent, false);
            return v;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            mCongreso c = new mCongreso(cursor);

            ImageView icono = (ImageView)view.findViewById(R.id.list_image);
            TextView fecha = (TextView) view.findViewById(R.id.DateCell);
            TextView titulo = (TextView) view.findViewById(R.id.TextContent);

           // time_start.setText(cursor.getString(cursor.getColumnIndex(DB.KEY_CONGRESO_TITULO)));
            titulo.setText(c.titulo);
            switch (c.id){
                case 1:
                    icono.setImageResource(R.drawable.ic_launcher);
                    break;
                case  2:
                    icono.setImageResource(R.drawable.ic_launcher);
            }

            fecha.setText(c.getDayevent());


        }









    }





}
