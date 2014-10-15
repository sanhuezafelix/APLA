package Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import Adaptadores.PagerViewAdapter;
import mc.mCongressDemo.MainActivity;
import mc.mCongressDemo.R;



/**
 * Created by luisgonzalez on 11-03-14.
 */
public class mProgramaFragmentView extends Fragment implements View.OnClickListener , ViewPager.OnPageChangeListener {

    private static final String INDEX = "index";

    private static final String DESCRIPCION = "descripcion";
    private ViewPager pager = null;
    private ImageButton BotonAvanzar;
    private ImageButton BotonRetroceder;
    private TextView TextViewDiaEvento;
    private PagerViewAdapter pg_adapter;
    private int congreso;
    private String descripcion;


    public static mProgramaFragmentView newInstance(MainActivity activity, int index ) {

        // Instantiate a new fragment

        mProgramaFragmentView fragment = new mProgramaFragmentView();
        // Save the parameters
        Bundle bundle = new Bundle();
        bundle.putInt(INDEX, index);

        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);
        activity.UnSetBackButton();
        activity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        activity.setSelectedSegmentIndex(R.id.segment_button_two);

        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  RootView = inflater.inflate(R.layout.programa_fragment_layout,container,false);
        MainActivity activity = (MainActivity)getActivity();
        activity.unHideNavigationBar();
        ((MainActivity)getActivity()).setVisibilityChangeViewChat(View.GONE);

        String[] congreso = getResources().getStringArray(R.array.date_congress);


        this.pager = (ViewPager)RootView.findViewById(R.id.pager);
            this.pager.setOnPageChangeListener(this);
            this.BotonAvanzar = (ImageButton)RootView.findViewById(R.id.btnNextDay);
            this.BotonAvanzar.setOnClickListener(this);
            this.BotonRetroceder = (ImageButton)RootView.findViewById(R.id.btnBackDay);
            this.BotonRetroceder.setOnClickListener(this);
            this.TextViewDiaEvento = (TextView)RootView.findViewById(R.id.TvTitleProgram);
           pg_adapter = new PagerViewAdapter(getChildFragmentManager());
            for (int i=0;i<congreso.length;i++){
                mPagerFragmentView page = mPagerFragmentView.newInstance(activity,congreso[i],getArguments().getInt(INDEX));
                pg_adapter.addFragment(page);
            }

            this.pager.setAdapter(pg_adapter);

            //this.TextViewDiaEvento.setText(pager.getAdapter().getPageTitle(this.pager.getCurrentItem()));
            this.TextViewDiaEvento.setText(getTitleDay());

            return RootView;

    }
    public String getTitleDay(){
        String titulo = new String();
        switch (this.pager.getCurrentItem()){

            case 0:
                titulo = getString(R.string.Sabado)+" 08";
                break;
            case 1:
                titulo = getString(R.string.Domingo)+" 09";
                break;
            case 2:
                titulo = getString(R.string.Lunes)+" 10";
                break;
            case 3:
                titulo = getString(R.string.Martes)+" 11";
                break;

        }
        return titulo;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.btnNextDay:
                pager.setCurrentItem(this.pager.getCurrentItem()+1);
                break;

            case  R.id.btnBackDay:
                pager.setCurrentItem(this.pager.getCurrentItem()-1);
                break;

        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //this.TextViewDiaEvento.setText(pager.getAdapter().getPageTitle(this.pager.getCurrentItem()));
        this.TextViewDiaEvento.setText(getTitleDay());
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).unHideBotomAndTopBar();
        ((MainActivity) activity).
                setCurrentCurrentId(
                        getArguments().getInt(INDEX));
        ((MainActivity)getActivity()).setVisibilityChangeViewChat(View.GONE);
    }


}
