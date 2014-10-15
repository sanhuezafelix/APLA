package Fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import DB.mHotel;
import mc.mCongressDemo.MainActivity;
import mc.mCongressDemo.R;

/**
 * Created by luisgonzalez on 11-03-14.
 */

public class mHotelesListView extends Fragment implements AdapterView.OnItemClickListener {

    private static final String INDEX = "index";
    private ListView Directorio;
    private Fragment fragment;
    private Cursor C;
    private View RootView;
    mDirectorioAdapter adapter;
    MainActivity activity;

    public static mHotelesListView newInstance(MainActivity activity, int index) {



        mHotelesListView fragment = new mHotelesListView();



        Bundle bundle = new Bundle();
        bundle.putInt(INDEX, index);
        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);


       // activity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

        return fragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       this.RootView = inflater.inflate(R.layout.hotel_list_layout, container,false);

        Directorio = (ListView) RootView.findViewById(R.id.commonListView);

        activity = (MainActivity)getActivity();
        activity.setTituloVista(getString(R.string.Hotels));

        C = activity.getDbHandler().getCursorHotel(1);


        adapter = new mDirectorioAdapter(activity.getApplicationContext(),C,R.layout.hotel_cell);
        Directorio.setAdapter(adapter);

        // Assign adapter to ListView

        Directorio.setOnItemClickListener(this);

        return  RootView;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


        adapter.getCursor().moveToPosition(i);
        mHotel c = new mHotel(adapter.getCursor());

        /*Toast toast1 =
                Toast.makeText(getActivity().getApplicationContext(),
                        "id "+ c.id+ " posicion "+IPVS , Toast.LENGTH_SHORT);

        toast1.show();*/






        MainActivity activity = (MainActivity)getActivity();



        fragment = mHotelesFragmentView.newInstance(activity,c.id);


        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(null);
        ft.commit();


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity)activity).setHideNavigationBar();
        ((MainActivity)activity).setTituloVista(getString(R.string.Hotels));
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

            mHotel c = new mHotel(cursor);

           // ImageView icono = (ImageView)view.findViewById(R.id.direccion_cell_hotel);
            TextView titulo = (TextView) view.findViewById(R.id.titulo_cell_hotel);
            TextView direccion = (TextView) view.findViewById(R.id.direccion_cell_hotel);
            TextView mail = (TextView) view.findViewById(R.id.mail_cell_hotel);
            TextView telefono = (TextView) view.findViewById(R.id.telefono_cell_hotel);
            ImageView icono  = (ImageView) view.findViewById(R.id.icono_hotel);
            direccion.setText(c.direccion);
            mail.setText(c.mail);
            titulo.setText(c.titulo);
            telefono.setText(c.telefono);
            if (c.icono != null) {
                if (c.icono.length > 0) {


                    Bitmap bitmap = BitmapFactory.decodeByteArray(c.icono, 0, c.icono.length);


                    if (bitmap!=null){
                        icono.setImageBitmap(bitmap);
                    }


                }
            }





        }









    }





}
