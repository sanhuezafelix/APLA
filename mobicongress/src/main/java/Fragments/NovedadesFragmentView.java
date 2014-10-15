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
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.SaveCallback;

import java.util.List;

import DB.Novedad;
import Utils.ClearableEditText;
import mc.mCongressDemo.MainActivity;
import mc.mCongressDemo.R;
import mc.mCongressDemo.myApp;


/**
 * Created by luisgonzalez on 21-03-14.
 */

public class NovedadesFragmentView extends Fragment implements AdapterView.OnItemClickListener {

    private static final String INDEX = "index";

    private Cursor c;
    //private mNovedadAdapter adapter;
    private ParseQueryAdapter<Novedad> mNovedadAdapter;
    private ListView listView;
    private MainActivity activity;

    private int _id;
    //private LayoutInflater inflater;
    //NovedadTask task;

    private ClearableEditText Novedad_search;
    private boolean whitImage= false;


    public static NovedadesFragmentView newInstance(MainActivity activity, int index) {

        // Instantiate a new fragment

        NovedadesFragmentView fragment = new NovedadesFragmentView();

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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



    }
    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        this._id = getArguments().getInt(INDEX);
        ((MainActivity)activity).setHideNavigationBar();
        ((MainActivity)activity).setTituloVista(getString(R.string.News));

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View RootView = inflater.inflate(R.layout.common_list_layout, container , false);
        listView = (ListView)RootView.findViewById(R.id.commonListView);
        activity = (MainActivity)getActivity();
        activity.setTituloVista(getString(R.string.News));
        ParseQueryAdapter.QueryFactory<Novedad> factory = new ParseQueryAdapter.QueryFactory<Novedad>() {
            public ParseQuery<Novedad> create() {
                ParseQuery<Novedad> query = Novedad.getQuery();

                //query.addDescendingOrder("createdAt");
                query.fromLocalDatastore();
                query.addAscendingOrder("Prioridad");
                //query.include("nombre_ciudad");
                return query;
            }
        };



        //todoListAdapter = new mNovedadAdapter(getActivity(), factory);
        //inflater = (LayoutInflater) getActivity()
        //.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mNovedadAdapter = new mNovedadAdapter(getActivity(), factory);
        ListView NovedadListView = (ListView)RootView.findViewById(R.id.commonListView);
        NovedadListView.setAdapter(mNovedadAdapter);
        //ParseObject object;
/*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mNovedad Novedad = todoListAdapter.getItem(position);
                openEditView(Novedad);
            }
        });*/

        // S

        //c = activity.getDbHandler().getCursorNovedad(this._id);

        // adapter = new mNovedadAdapter(activity,c,R.layout.cell_Novedad);
        //listView.setAdapter(adapter);
        /*
        task = new NovedadTask(activity);
        task.execute(this._id);
        app = (myApp) getActivity().getApplicationContext();

        listView.setOnItemClickListener(this);
        Novedad_search = (ClearableEditText) RootView.findViewById(R.id.search_Novedad);
        Novedad_search.addTextChangedListener(new TextWatcher() {
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
                app.setNovedadSearchText(s.toString().toLowerCase());
                todoListAdapter.filterData(s);
            }
        });
        if (!TextUtils.isEmpty(app.getNovedadSearchText())) {
            Novedad_search.setText(app.getNovedadSearchText());
        }
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

    public void onResume() {
        super.onResume();
        // Check if we have a real user
        //loadCiudadfromParse();
        //loadFromParse();
        if((((myApp)getActivity().getApplicationContext()).verificaConexion())){
            RelationCiudadNovedad();

        }


        //FetchFromParse();
        // Sync data to Parse


        // Update the logged in label info


    }

    /*
    private void loadFromParse() {

        ParseQuery<Novedad> query = ParseQuery.getQuery(Novedad.class);
        query.setLimit(1);
        query.whereEqualTo("isDraft", true);
        query.include("id_ciudad");
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Novedad>() {
            public void done(List<Novedad> Novedads, ParseException e) {

                if (e == null) {
                    ParseObject.pinAllInBackground("Novedads",Novedads,
                            new SaveCallback() {
                                public void done(ParseException e) {
                                    if (e == null) {


                                    } else {
                                        Log.i("TodoListActivity",
                                                "Error pinning todos: "
                                                        + e.getMessage());
                                    }
                                }
                            });
                } else {
                    Log.i("TodoListActivity",
                            "loadFromParse: Error finding pinned todos: "
                                    + e.getMessage());
                }
            }
        });
    }


    private void loadCiudadfromParse() {
        ParseQuery<Ciudad> query = ParseQuery.getQuery(Ciudad.class);
        //query.fromPin(myApp.TODO_GROUP_NAME);
        //query.whereEqualTo("Draft", true);

        query.findInBackground(new FindCallback<Ciudad>() {
            @Override
            public void done(List<Ciudad> ciudades, ParseException e) {

                ParseObject.pinAllInBackground("Ciudades", ciudades,
                        new SaveCallback() {
                            public void done( ParseException e) {
                                if (e == null) {
                                   //refreshList();
                                } else {
                                    Log.i("TodoListActivity",
                                            "Error pinning todos: "
                                                    + e.getMessage());
                                }
                            }
                        });

            }
        });

    }*/

    private void RelationCiudadNovedad(){

        ParseQuery<Novedad> query = ParseQuery.getQuery(Novedad.class);
        query.addDescendingOrder("Prioridad");
        //query.whereEqualTo("isDraft", true);

        query.findInBackground(new FindCallback<Novedad>() {
            public void done(final List<Novedad> Novedads, ParseException e) {

                Novedad.unpinAllInBackground("Novedad", new DeleteCallback() {

                    public void done(ParseException e) {

                        for (Novedad novedad : Novedads) {
                            novedad.pinInBackground("Novedad", new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    mNovedadAdapter.loadObjects();
                                }
                            });

                        }
                        // Cache the new results.


                    }
                });

              /* Novedad.unpinAllInBackground("Novedad");


                for (Novedad novedad : Novedads) {


                    novedad.pinInBackground("Novedad",
                            new SaveCallback() {
                                public void done( ParseException e) {
                                    if (e == null) {

                                        mNovedadAdapter.loadObjects();

                                    } else {

                                    }
                                }
                            });





                    Log.d("post", "retrieved a related post");
                }*/
            }
        });

    }

    public class mNovedadAdapter extends ParseQueryAdapter<Novedad> {

        public mNovedadAdapter(Context context,
                               ParseQueryAdapter.QueryFactory<Novedad> queryFactory) {
            super(context, queryFactory);

        }


        @Override
        public View getItemView(Novedad Novedad, View view, ViewGroup parent) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            ViewHolder holder;
            if (view == null) {
                view = inflater.inflate(R.layout.cell_news, parent, false);
                holder = new ViewHolder();
                holder.titulo =   (TextView)view.findViewById(R.id.titulo_news);
                holder.contenido   =   (TextView)view.findViewById(R.id.contenido_news);
                holder.fecha  =   (TextView)view.findViewById(R.id.fecha_news);
//                holder.ciudad = (TextView)view.findViewById(R.id.ciudad_Novedad);
                //  holder.apellido = (TextView)view.findViewById(R.id.apellido_Novedad);
                //holder.layout    =   (RelativeLayout)view.findViewById(R.id.Content);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            TextView Titulo = holder.titulo;
            TextView Contenido = holder.contenido;
            TextView Fecha = holder.fecha;
            //TextView ciudad = holder.ciudad;
            //TextView apellido = holder.apellido;
            Titulo.setText(Novedad.getTitulo());
            Contenido.setText(Novedad.getContenido());
            Fecha.setText(Novedad.getFechaInicio());
//            ciudad.setText(Novedad.getNombreCiudad());
            //apellido.setText(Novedad.getApellido());

            return view;
        }





        private class   ViewHolder  {
            TextView titulo ;
            //TextView apellido;
            TextView fecha;
            TextView contenido;
            //  TextView ciudad;
            // TextView pais ;
            //RelativeLayout layout;
        }
        /*public void set_cur_position(int position) {
            this.currentPosition = position;
            if (position >= 0) {
                this.notifyDataSetChanged();
            }
        }

        public void filterData(CharSequence s) {
            String string = s.toString().toLowerCase();

            // Filter
            sp_cursor = bd.getCursorSearchNovedad(1,app.remueveTildes(string));
            Log.v("tildes",app.remueveTildes(string)
            );
            // Notify
            changeCursor(sp_cursor);
            notifyDataSetChanged();
        }

    }

    public class NovedadTask extends AsyncTask<Integer ,Void,Cursor> {
        MainActivity activity;
        public NovedadTask(MainActivity a){
            this.activity = a;


        }

        @Override
        protected Cursor doInBackground(Integer... integers) {
            return this.activity.getDbHandler().getCursorSearchNovedad(integers[0],"");

        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            adapter = new mNovedadAdapter(activity,cursor,R.layout.cell_Novedad,activity.getDbHandler());
            int cur_id = app.fragment_Novedad_position;
            if (cur_id >=0) {
                adapter.set_cur_position(cur_id);
                listView.setSelection(cur_id);
            }
            listView.setAdapter(adapter);
        }*/
    }


}