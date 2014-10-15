package Fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import Adaptadores.PagerViewAdapter;
import DB.mImage;
import Utils.TouchImageView;
import mc.mCongressDemo.MainActivity;
import mc.mCongressDemo.R;

/**
 * Created by luisgonzalez on 08-08-14.
 */
public class mSponsorViewController extends Fragment {

    private ViewPager pager = null;
    private PagerViewAdapter pg_adapter;
    private mapsAdapter adaptador;
    private static final String INDEX = "index";
    private Cursor cursor_map;

    MainActivity activity ;
    int i;


    public static mSponsorViewController newInstance(MainActivity activity, int index) {

        // Instantiate a new fragment
        mSponsorViewController fragment = new mSponsorViewController();
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

        View RootView = inflater.inflate(R.layout.mapa_fragment_layout , container,false);
        this.activity = (MainActivity)getActivity();

        this.pager = (ViewPager)RootView.findViewById(R.id.pager_mapa);
        cursor_map = activity.getDbHandler().getCursorImage("mapa");
        this.adaptador = new mapsAdapter(cursor_map);
        this.pager.setAdapter(this.adaptador);
        return RootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity)activity).setHideNavigationBar();
        ((MainActivity)activity).setTituloVista(getString(R.string.Sponsor));

    }

    public class mapsAdapter extends PagerAdapter {

        private Cursor c;

        public mapsAdapter(Cursor cursor){
            super();
            c = cursor;

        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = (LayoutInflater) container.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View page = inflater.inflate(R.layout.mapa_image_layout, null);
            //ImageView mapa = (ImageView)page.findViewById(R.id.imageView_map);
            TouchImageView mapa =(TouchImageView)page.findViewById(R.id.imageView_map);

            mapa.setMaxZoom(3f);
            mapa.setMinZoom(1f);
            c.moveToPosition(position);


            mapa.setImageResource(R.drawable.sponsor);
            ((ViewPager)container).addView(page, 0);
            return page;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }
    }
}
