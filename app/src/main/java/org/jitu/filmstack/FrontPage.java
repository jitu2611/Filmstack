package org.jitu.filmstack;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jitesh on 6/16/2017.
 */
public class FrontPage extends Fragment {

   static int size = 9 ;
    ViewPager viewPager ;
    TabLayout tabLayout ;
    Bundle bundle ;
    String[] algoid= {"27205","550","72190","1402","155","1124","807","87101","263115"} ;

    String[] algoiurl={"https://image.tmdb.org/t/p/w154/qmDpIHrmpJINaRKAfWQfftjCdyi.jpg","https://image.tmdb.org/t/p/w154/adw6Lq9FiC9zjYEpOqfq03ituwp.jpg","https://image.tmdb.org/t/p/w154/Ha5t0J21eyiq6Az1EXzx0iwsGH.jpg","https://image.tmdb.org/t/p/w154/iMNp6gTeDBXbzjKRNYtorxZ76G2.jpg","https://image.tmdb.org/t/p/w154/1hRoyzDtpgMU7Dz4JF22RANzQO7.jpg","https://image.tmdb.org/t/p/w154/5MXyQfz8xUP3dIFPTubhTsbFY6N.jpg",
                       "https://image.tmdb.org/t/p/w154/8zw8IL4zEPjkh8Aysdcd0FwGMb0.jpg","https://image.tmdb.org/t/p/w154/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg",
                       "https://image.tmdb.org/t/p/w154/9EXnebqbb7dOhONLPV9Tg2oh2KD.jpg"} ;
    String[] title = {"Inception","Fight Club","World War Z","The Pursuit of Happyness","The Dark Knight","The Prestige","Se7en","Terminator Genisys","Logan"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View v = inflater.inflate(R.layout.frontpage, container, false);
         viewPager = (ViewPager)v.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) v.findViewById(R.id.sliding_tabs);
         try{
             bundle=getArguments() ;
             Log.i("displayid", "f" + bundle.getString("notificationid")) ;

         }catch (Exception e)
         {
             e.printStackTrace();
         }
        return v ;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!isOnline()) {
            /*NoInternet f = new NoInternet();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            ft.replace(R.id.mainfragment, f);
            ft.addToBackStack(null);
            ft.commit(); */
        }

        SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean isFirstRun = wmbPreference.getBoolean("FIRSTRUN", true);
        if (isFirstRun) {



            for(int i=0;i<size;i++) {


                Cursor c = getActivity().managedQuery(AlgoDB.Contenturi, null, "id = " + algoid[i], null, null) ;
                if (!(c.moveToFirst()) || c.getCount() ==0){
                    //cursor is empty
                    ContentValues values = new ContentValues();
                    values.put(AlgoDB.algo.ID, algoid[i]);
                    values.put(AlgoDB.algo.TITLE,title[i]) ;
                    values.put(AlgoDB.algo.COUNT, 1);
                    values.put(AlgoDB.algo.IURL, algoiurl[i]);

                    getActivity().getContentResolver().insert(AlgoDB.Contenturi,
                            values);
                }
                else
                {
                    ContentValues values = new ContentValues();

                    values.put(AlgoDB.algo.COUNT, c.getInt(c.getColumnIndex(AlgoDB.algo.COUNT)) + 5);
                    getActivity().getContentResolver().update(AlgoDB.Contenturi,values,"id = "+algoid[i],null) ;
                }
            }

            SharedPreferences.Editor editor = wmbPreference.edit();
            editor.putBoolean("FIRSTRUN", false);
            editor.apply();
        }



        viewPager.setAdapter(new SampleFragmentPagerAdapter(getActivity().getSupportFragmentManager()
                ));
        viewPager.setOffscreenPageLimit(4);
        // Give the TabLayout the ViewPager
        //TabLayout tabLayout = (TabLayout) getView().findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);


    }
    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 5;
        private String tabTitles[] = new String[] {"Recommended","Now Showing", "Upcoming", "Popular","Rated"};

        //private Context context;

        public SampleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position)
            {
                case 0:Algo f = new Algo() ;

                       f.setArguments(bundle);
                        return f ;
                case 2:
                        return new Upcoming() ;
                case 1:
                        return new Nowshow() ;
                case 3:
                        return new Popular() ;
                case 4:
                        return new Rated() ;


                default: return  null ;


            }

        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
