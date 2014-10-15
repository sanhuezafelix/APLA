package Adaptadores;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luisgonzalez on 12-03-14.
 */
public class PagerViewAdapter extends FragmentPagerAdapter {

    List<Fragment> fragments;

    public PagerViewAdapter(FragmentManager fm) {
        super(fm);
        this.fragments = new ArrayList<Fragment>();
    }
    public  void  addFragment(Fragment fragment){this.fragments.add(fragment);}

    @Override
    public Fragment getItem(int position) { return this.fragments.get(position); }

    @Override
    public int getCount() { return  this.fragments.size(); }

    @Override
    public CharSequence getPageTitle(int position) {
        return "pagina "+position;
    }
}
