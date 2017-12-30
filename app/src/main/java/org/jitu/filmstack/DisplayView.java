package org.jitu.filmstack;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jitesh on 6/19/2017.
 */
public class DisplayView extends Fragment {

    String URL ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.dview, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();

        final Bundle b = getArguments();
        URL = b.getString("url");
        ViewPager viewPager = (ViewPager) getView().findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getActivity().getSupportFragmentManager()
        ));
       // viewPager.setOffscreenPageLimit(3);
        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) getView().findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);


    }
    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 3;
        private String tabTitles[] = new String[] { "Description", "Cast", "Similar" };
        private Context context;

        public SampleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position)
            {
                case 0: return Display.newInstance(URL) ;

                case 2: Popular popular = new Popular() ;
                    return popular ;
                case 3: Rated rated = new Rated() ;
                    return rated ;

                default: return  null ;


            }

        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
    }
}
