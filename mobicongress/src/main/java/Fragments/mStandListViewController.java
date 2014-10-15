package Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import Adaptadores.mStandAdapter;
import mc.mCongressDemo.MainActivity;
import mc.mCongressDemo.R;
import mc.mCongressDemo.myApp;

/**
 * Created by luisgonzalez on 13-03-14.
 */
public class mStandListViewController extends Fragment implements AdapterView.OnItemClickListener {

    private mStandAdapter adaptador;
    private ListView list;
    private myApp app;
    private Handler customHandler = new Handler();
    private ImageView iv_sponsor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.common_list_stand, container, false);
        app = (myApp) getActivity().getApplicationContext();
        String[] menuItems = getResources().getStringArray(R.array.stand_category);
        this.list = (ListView) RootView.findViewById(R.id.commonListView);

        adaptador = new mStandAdapter(getActivity().getApplicationContext(), menuItems);
        this.list.setAdapter(adaptador);
        this.list.setOnItemClickListener(this);
        return RootView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MainActivity activity = (MainActivity) getActivity();
        adaptador.set_cur_position(i);
        String[] stand = getResources().getStringArray(R.array.stand_category);
        activity.menuChangeView(mStandFragmentView.newInstance(activity,stand[i]),true);
        this.app.cleanPositionListview();
        activity.getSlidingMenu().toggle(true);

    }



}
