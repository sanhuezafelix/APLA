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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.applidium.headerlistview.HeaderListView;
import com.applidium.headerlistview.SectionAdapter;

import java.util.ArrayList;

import Utils.Utilidades;
import mc.mCongressDemo.MainActivity;
import mc.mCongressDemo.R;
import mc.mCongressDemo.myApp;

/**
 * Created by luisgonzalez on 02-04-14.
 */
public class mPostersFragmentView extends Fragment  {
    private MainActivity activity;
    ListView listView;
    private FavoriteHeaderAdaterView adapter;
    private HeaderListView list;
    private Cursor fv_cursor;
    private myApp app;
    private ArrayList<ArrayList<Integer>> secciones ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.common_list_fav,container,false);


        this.activity = (MainActivity)getActivity();
        app = (myApp)getActivity().getApplicationContext();
        activity.setTituloVista(getString(R.string.Favorites));

        fv_cursor = activity.getDbHandler().getCursorAlleventoFavorito(1);
        this.list = (HeaderListView)rootview.findViewById(R.id.lista_favoritos);

        String[] congreso = getResources().getStringArray(R.array.date_congress);
        this.secciones = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i <congreso.length ; i++) {
            fv_cursor.moveToFirst();
            ArrayList<Integer> posiciones = new ArrayList<Integer>();
            for (int j = 0; j < fv_cursor.getCount() ; j++) {
                if (fv_cursor.getString(fv_cursor.getColumnIndex("ZFECHAINICIO")).contains(congreso[i]))
                    posiciones.add(Integer.valueOf(fv_cursor.getPosition()));
                    fv_cursor.moveToNext();
            }
            if (posiciones.size()>0)
            {
                this.secciones.add(posiciones);
            }
        }



        this.adapter = new FavoriteHeaderAdaterView(fv_cursor, this.secciones);
        list.setAdapter(adapter);

        return rootview;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity)activity).setHideNavigationBar();
        ((MainActivity)activity).setTituloVista(getString(R.string.Favorites));
    }

    public class ViewHolder{
        TextView hora_inicio ;
        TextView hora_fin ;
        TextView titulo_nieto ;
        TextView speaker ;
        TextView lugar ;
        TextView label;
        RelativeLayout layout;

    }


    /*
        header class
     */

    private class FavoriteHeaderAdaterView extends SectionAdapter{


        private  int layout;
        private LayoutInflater inflater;
        private Cursor cursor;
        private ArrayList<ArrayList<Integer>> sections_list;
        public FavoriteHeaderAdaterView(Cursor c, ArrayList<ArrayList<Integer>> s) {
            this.cursor = c;
            this.sections_list = new ArrayList<ArrayList<Integer>>(s);
        }
       
        @Override
        public int numberOfSections() {
            return 4;
        }

        @Override
        public int numberOfRows(int section) {
            return 1;
        }

        @Override
        public Object getRowItem(int section, int row) {
            return null;
        }

        @Override
        public boolean hasSectionHeaderView(int section) {
            return true;
        }

        @Override
        public View getRowView(int section, int row, View v, ViewGroup parent) {
            ViewHolder holder ;
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) ((MainActivity)getActivity()).getSystemService( Context.LAYOUT_INFLATER_SERVICE );

                v = (RelativeLayout) inflater.inflate(getResources().getLayout(R.layout.favorito_cell), null);
                holder  =   new ViewHolder();
                holder.titulo_nieto  =   (TextView)v.findViewById(R.id.titulo_nieto_pgr);
                holder.hora_inicio = (TextView)v.findViewById(R.id.inicio_eem);
                holder.hora_fin = (TextView)v.findViewById(R.id.fin_eem);
                holder.layout    =   (RelativeLayout)v.findViewById(R.id.Content);
                holder.speaker= (TextView)v.findViewById(R.id.speaker_eem);
                holder.lugar = (TextView)v.findViewById(R.id.lugar_eem);
                holder.label = (TextView)v.findViewById(R.id.label_fav);
                v.setTag(holder);
            }
            else {
                holder = (ViewHolder) v.getTag();
            }

          /*  this.cursor.moveToPosition(this.sections_list.get(section).get(row));
            mEvent ev = new mEvent(this.cursor);
            holder.titulo_nieto.setText(ev.titulo);
            holder.hora_inicio.setText(ev.getStringStartTime());
            holder.hora_fin.setText(ev.getStringEndTime());
            holder.lugar.setText(ev.lugar);
            if (ev.speakerdos.equals(""))
            {
                holder.speaker.setText(ev.speakeruno);
            }
            else
            {
                holder.speaker.setText(ev.speakeruno+"\n"+ev.speakerdos);
            }*/
            switch (section){
                case 0:
                    holder.titulo_nieto.setText("Posters " +Utilidades.getWeekday(8));

                    break;
                case 1:
                    holder.titulo_nieto.setText("Posters " +Utilidades.getWeekday(9));
                    break;
                case 2:
                    holder.titulo_nieto.setText("Posters " +Utilidades.getWeekday(10));
                    break;
                case 3:
                    holder.titulo_nieto.setText("Posters " +Utilidades.getWeekday(11));
                    break;
            }
            holder.hora_inicio.setText("09:00");
            holder.hora_fin.setText("12:00");
            holder.lugar.setText("");
            holder.speaker.setText("");
            return v;
        }
        @Override
        public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) ((MainActivity)getActivity()).getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                convertView = (RelativeLayout) inflater.inflate(getResources().getLayout(R.layout.header_layout), null);
            }
//            this.cursor.moveToPosition(this.sections_list.get(section).get(0));
           // mEvent ev = new mEvent(this.cursor);
            switch (section){
                case 0:
                    ((TextView) convertView.findViewById(R.id.label_fav)).setText(Utilidades.getWeekday(8));
                    break;
                case 1:
                    ((TextView) convertView.findViewById(R.id.label_fav)).setText(Utilidades.getWeekday(9));

                    break;
                case 2:
                    ((TextView) convertView.findViewById(R.id.label_fav)).setText(Utilidades.getWeekday(10));
                    break;
                case 3:
                    ((TextView) convertView.findViewById(R.id.label_fav)).setText(Utilidades.getWeekday(11));

                    break;
            }

            return convertView;
        }

        @Override
        public void onRowItemClick(AdapterView<?> parent, View view, int section, int row, long id) {
            super.onRowItemClick(parent, view, section, row, id);
            String pdf = "https://docs.google.com/gview?embedded=true&url=";


            switch (section){
                case 0:
                    if (getString(R.string.idioma)=="es"){
                        activity.PushView(mDownloadContentViewController.newInstance(activity,pdf+"http://www.apla.com.ar/archivos/reuniones/base-programa-neg34-esp.pdf"));

                    }
                    else if(getString(R.string.idioma)=="pt")
                    {
                        activity.PushView(mDownloadContentViewController.newInstance(activity,pdf+"http://www.apla.com.ar/archivos/reuniones/programa-port-30-9.pdf"));

                    }
                    else {
                        activity.PushView(mDownloadContentViewController.newInstance(activity,pdf+"http://www.apla.com.ar/archivos/reuniones/program-ing-30-9-1.pdf"));

                    }
                    break;
                case 1:
                    if (getString(R.string.idioma)=="es"){
                        activity.PushView(mDownloadContentViewController.newInstance(activity,pdf+"http://www.apla.com.ar/archivos/reuniones/base-programa-neg34-esp.pdf"));

                    }
                    else if(getString(R.string.idioma)=="pt")
                    {
                        activity.PushView(mDownloadContentViewController.newInstance(activity,pdf+"http://www.apla.com.ar/archivos/reuniones/programa-port-30-9.pdf"));

                    }
                    else {
                        activity.PushView(mDownloadContentViewController.newInstance(activity,pdf+"http://www.apla.com.ar/archivos/reuniones/program-ing-30-9-1.pdf"));

                    }
                    break;
                case 2:
                    if (getString(R.string.idioma)=="es"){
                        activity.PushView(mDownloadContentViewController.newInstance(activity,pdf+"http://www.apla.com.ar/archivos/reuniones/base-programa-neg34-esp.pdf"));

                    }
                    else if(getString(R.string.idioma)=="pt")
                    {
                        activity.PushView(mDownloadContentViewController.newInstance(activity,pdf+"http://www.apla.com.ar/archivos/reuniones/programa-port-30-9.pdf"));

                    }
                    else {
                        activity.PushView(mDownloadContentViewController.newInstance(activity,pdf+"http://www.apla.com.ar/archivos/reuniones/program-ing-30-9-1.pdf"));

                    }
                    break;
                case 3:
                    if (getString(R.string.idioma)=="es"){
                        activity.PushView(mDownloadContentViewController.newInstance(activity,pdf+"http://www.apla.com.ar/archivos/reuniones/base-programa-neg34-esp.pdf"));

                    }
                    else if(getString(R.string.idioma)=="pt")
                    {
                        activity.PushView(mDownloadContentViewController.newInstance(activity,pdf+"http://www.apla.com.ar/archivos/reuniones/programa-port-30-9.pdf"));

                    }
                    else {
                        activity.PushView(mDownloadContentViewController.newInstance(activity,pdf+"http://www.apla.com.ar/archivos/reuniones/program-ing-30-9-1.pdf"));

                    }
                    break;
            }

        }

        private Cursor getCursor(){
            return this.cursor;
        }


    }


}

