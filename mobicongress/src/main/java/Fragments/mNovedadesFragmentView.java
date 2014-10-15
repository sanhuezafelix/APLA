package Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import mc.mCongressDemo.MainActivity;
import mc.mCongressDemo.R;

/**
 * Created by luisgonzalez on 13-03-14.
 */
public class mNovedadesFragmentView extends Fragment {

    private static final String INDEX = "index";

    public static mNovedadesFragmentView newInstance(MainActivity activity, int index) {

        // Instantiate a new fragment

        mNovedadesFragmentView fragment = new mNovedadesFragmentView();


        // Save the parameters
        Bundle bundle = new Bundle();
        bundle.putInt(INDEX, index);
        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);
        activity.UnSetBackButton();
        activity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);


        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.common_list_layout,container,false);

        pushcustomadapter adaptador = new pushcustomadapter(this.getActivity(),loadArray("Notificaciones",this.getActivity()) );
        ListView NewList = (ListView) RootView.findViewById(R.id.commonListView);
        NewList.setAdapter(adaptador);


        return RootView;
    }
    public String[] loadArray(String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("mCongress", 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        String array[] = new String[size];
        for(int i=(size-1);i>=0;i--)
            array[(size-1)-i] = prefs.getString(arrayName + "_" + i, null);
        return array;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity)activity).setHideNavigationBar();
        ((MainActivity)activity).setTituloVista(getString(R.string.News));
    }

    /*
            pus adapter

     */

    public class pushcustomadapter extends BaseAdapter {

        private String[] lista;
        private LayoutInflater Linflater;
        public pushcustomadapter(Context context , String[] strings ){
            this.Linflater = LayoutInflater.from(context);
            this.lista = strings;

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return lista.length;
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return lista[arg0];
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            VistaContenedor contenedor = null;
            // TODO Auto-generated method stub
            if(arg1 == null)
            {
                arg1 = Linflater.inflate(R.layout.cell_news,	null);
                contenedor = new VistaContenedor();
                contenedor.fecha = (TextView) arg1.findViewById(R.id.fecha_news);
                contenedor.titulo = (TextView) arg1.findViewById(R.id.titulo_news);
                contenedor.Contenido = (TextView) arg1.findViewById(R.id.contenido_news);
                arg1.setTag(contenedor);
            }
            else{
                contenedor = (VistaContenedor)arg1.getTag();


            }
            String[] split = lista[arg0].split(":");
            contenedor.fecha.setText(split[0]);
            contenedor.titulo.setText(split[1]);
            contenedor.Contenido.setText(split[2]);

            return arg1;

        }

        class VistaContenedor{
            TextView fecha;
            TextView titulo;
            TextView Contenido;



        }
        public void actualizar(String[] array) {
            lista = array;
            //Triggers the list update
            notifyDataSetChanged();
        }


    }


    /*

     */
}
