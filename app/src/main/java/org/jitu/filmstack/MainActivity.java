package org.jitu.filmstack;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.widget.FrameLayout;
import android.widget.LinearLayout;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;


import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Fragment {

    EditText search;

    static final String baseurl = "http://image.tmdb.org/t/p/w154" ;
    static final String baseurlpos = "http://image.tmdb.org/t/p/w780";
    static final String TITLE = "title";
    static final String IMAGE = "image";
    static final String ID = "id";
    static final String POSTER = "poster" ;
    ScrollView sv;
    TextView pop, thismonth, toprated, more1, more2, more3,more4,popular;

    ArrayList<HashMap<String, String>> dataObjects = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> dataObject = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> ratedObject = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> popObject = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> dataObject2 = new ArrayList<HashMap<String, String>>();

    HorizontalListView mlistview, plistview, rlistview,poplistview;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;

    ViewPager viewPager;
    MyPagerAdapter myPagerAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.activity_main, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            getActivity().getActionBar().show();
            getActivity().getActionBar().setTitle("FilmStack");

            dataObject.clear();
            dataObjects.clear();
            ratedObject.clear();
            dataObject2.clear() ;
            popObject.clear() ;
            viewPager = (ViewPager)getView().findViewById(R.id.myviewpager);
            myPagerAdapter = new MyPagerAdapter();
            search = (EditText) getView().findViewById(R.id.search);
           // sv = (ScrollView) getView().findViewById(R.id.scrolview);
            pop = (TextView) getView().findViewById(R.id.pop);
            popular = (TextView)getView().findViewById(R.id.popular) ;
            more1 = (TextView) getView().findViewById(R.id.more1);
            more2 = (TextView) getView().findViewById(R.id.more2);
            more4 = (TextView)getView().findViewById(R.id.more4) ;
            more3 = (TextView) getView().findViewById(R.id.more3);
            thismonth = (TextView) getView().findViewById(R.id.thismonth);
            toprated = (TextView) getView().findViewById(R.id.toprated);
            plistview = (HorizontalListView) getView().findViewById(R.id.plistview);
            poplistview = (HorizontalListView)getView().findViewById(R.id.poplistview);
            mlistview = (HorizontalListView) getView().findViewById(R.id.mlistview);

            rlistview = (HorizontalListView) getView().findViewById(R.id.rlistview);


            mlistview.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                    // TODO Auto-generated method stub
                    HashMap<String, String> l = new HashMap<String, String>();
                    l = dataObject.get(arg2);
                    String u = l.get(ID);

                    Bundle b = new Bundle();
                    b.putString("url", u);

                    Display f = new Display();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    f.setArguments(b);
                    ft.hide(fm.findFragmentById(R.id.mainfragment));
                    ft.add(((ViewGroup) getView().getParent()).getId(), f);
                    ft.addToBackStack(null);
                    ft.commit();

                }
            });
            more1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Search f = new Search();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    Bundle b = new Bundle();

                    String query = "https://api.themoviedb.org/3/movie/now_playing?api_key=1b104f628c51adebff0570a1a27ae2af&page=1";

                    b.putString("query", query);

                    f.setArguments(b);

                    ft.hide(fm.findFragmentById(R.id.mainfragment));
                    ft.add(((ViewGroup) getView().getParent()).getId(), f);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });
            rlistview.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                    // TODO Auto-generated method stub
                    HashMap<String, String> l = new HashMap<String, String>();
                    l = ratedObject.get(arg2);
                    String u = l.get(ID);

                    Bundle b = new Bundle();
                    b.putString("url", u);

                    Display f = new Display();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    f.setArguments(b);
                    ft.hide(fm.findFragmentById(R.id.mainfragment));
                    ft.add(((ViewGroup) getView().getParent()).getId(), f);
                    ft.addToBackStack(null);
                    ft.commit();

                }
            });
            more2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Search f = new Search();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    Bundle b = new Bundle();

                    String query = "http://api.themoviedb.org/3/movie/upcoming?api_key=1b104f628c51adebff0570a1a27ae2af&page=1";

                    b.putString("query", query);

                    f.setArguments(b);
                    ft.hide(fm.findFragmentById(R.id.mainfragment));
                    ft.add(((ViewGroup) getView().getParent()).getId(), f);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });
            plistview.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                    // TODO Auto-generated method stub
                    HashMap<String, String> l = new HashMap<String, String>();
                    l = dataObjects.get(arg2);
                    String u = l.get(ID);

                    Bundle b = new Bundle();
                    b.putString("url", u);

                    Display f = new Display();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    f.setArguments(b);
                    ft.hide(fm.findFragmentById(R.id.mainfragment));
                    ft.add(((ViewGroup) getView().getParent()).getId(), f);
                    ft.addToBackStack(null);
                    ft.commit();

                }
            });
            more3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Search f = new Search();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    Bundle b = new Bundle();

                    String query = "https://api.themoviedb.org/3/movie/top_rated?api_key=1b104f628c51adebff0570a1a27ae2af&page=1";

                    b.putString("query", query);

                    f.setArguments(b);

                    ft.hide(fm.findFragmentById(R.id.mainfragment));
                    ft.add(((ViewGroup) getView().getParent()).getId(), f);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });
            poplistview.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                    // TODO Auto-generated method stub
                    HashMap<String, String> l = new HashMap<String, String>();
                    l = popObject.get(arg2);
                    String u = l.get(ID);

                    Bundle b = new Bundle();
                    b.putString("url", u);

                    Display f = new Display();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    f.setArguments(b);
                    ft.hide(fm.findFragmentById(R.id.mainfragment));
                    ft.add(((ViewGroup) getView().getParent()).getId(), f);
                    ft.addToBackStack(null);
                    ft.commit();

                }
            });
            more4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Search f = new Search();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    Bundle b = new Bundle();

                    String query = "https://api.themoviedb.org/3/movie/popular?api_key=1b104f628c51adebff0570a1a27ae2af&page=1";

                    b.putString("query", query);

                    f.setArguments(b);

                    ft.hide(fm.findFragmentById(R.id.mainfragment));
                    ft.add(((ViewGroup) getView().getParent()).getId(), f);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });
            if (!isOnline()) {
                NoInternet f = new NoInternet();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                ft.replace(R.id.mainfragment, f);
                ft.addToBackStack(null) ;


                ft.commit();


            } else {

                new GetJSONFrom1()
                        .execute("https://api.themoviedb.org/3/movie/now_playing?api_key=1b104f628c51adebff0570a1a27ae2af");

                new GetJSONFrom2()
                        .execute("http://api.themoviedb.org/3/movie/upcoming?api_key=1b104f628c51adebff0570a1a27ae2af");
                new GetJSONFrom2more()
                        .execute("http://api.themoviedb.org/3/movie/upcoming?api_key=1b104f628c51adebff0570a1a27ae2af&page=2");
                new GetJSONFrom4().execute("https://api.themoviedb.org/3/movie/popular?api_key=1b104f628c51adebff0570a1a27ae2af&page=1") ;
                new GetJSONFrom3()
                .execute("https://api.themoviedb.org/3/movie/top_rated?api_key=1b104f628c51adebff0570a1a27ae2af&page=1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BaseAdapter m1Adapter = new BaseAdapter() {

        @Override
        public int getCount() {
            return dataObject.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View retval = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.hrow, null);
            HashMap<String, String> list = new HashMap<String, String>();
            list = dataObject.get(position);

            ImageView img = (ImageView) retval.findViewById(R.id.hrow);
            // Bitmap l =
            // BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher)
            // ;

            Uri u = Uri.parse(baseurl + list.get(IMAGE));

            Picasso.with(getActivity()).load(u).fit().into(img);

            return retval;
        }

    };
    private BaseAdapter m3Adapter = new BaseAdapter() {

        @Override
        public int getCount() {
            return ratedObject.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View retval = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.hrow, null);
            HashMap<String, String> list = new HashMap<String, String>();
            //Collections.shuffle(ratedObject);
            list = ratedObject.get(position);

            ImageView img = (ImageView) retval.findViewById(R.id.hrow);

            Picasso.with(getActivity())
                    .load(Uri.parse(baseurl + list.get(IMAGE))).fit().into(img);

            return retval;
        }

    };
    private BaseAdapter m2Adapter = new BaseAdapter() {

        @Override
        public int getCount() {
            return dataObjects.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View retval = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.hrow, null);
            HashMap<String, String> list = new HashMap<String, String>();
            list = dataObjects.get(position);

            ImageView img = (ImageView) retval.findViewById(R.id.hrow);

            Picasso.with(getActivity())
                    .load(Uri.parse(baseurl + list.get(IMAGE))).fit().into(img);

            return retval;
        }

    };
    private BaseAdapter m4Adapter = new BaseAdapter() {

        @Override
        public int getCount() {
            return popObject.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View retval = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.hrow, null);
            HashMap<String, String> list = new HashMap<String, String>();
            list = popObject.get(position);

            ImageView img = (ImageView) retval.findViewById(R.id.hrow);

            Picasso.with(getActivity())
                    .load(Uri.parse(baseurl + list.get(IMAGE))).fit().into(img);

            return retval;
        }

    };

    private class GetJSONFrom1 extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            dialog.setMessage("LOADING ");

            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url
                        .openConnection();

                InputStream is = new BufferedInputStream(
                        urlConnection.getInputStream());

                String result = convertStreamToString(is);
                JSONObject reader = new JSONObject(result);
                // String data = null;
                JSONArray jsonArray = reader.optJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String release_date = jsonObject.optString("release_date").toString() ;

                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DATE, -10);
                    String today = sdf.format(cal.getTime()) ;

                    Log.d("AAAA", release_date + "  "+ today) ;

                    Date date1 = sdf.parse(release_date);
                    Date date2 = sdf.parse(today);

                    if(date2.compareTo(date1)>0){
                    map.put(TITLE, jsonObject.optString("title").toString());
                    map.put(IMAGE, jsonObject.optString("poster_path")
                            .toString());
                        map.put(POSTER,jsonObject.optString("backdrop_path")) ;
                    map.put(ID, jsonObject.optString("id").toString());
                    dataObject.add(map);
                        dataObject2.add(map) ;

                }}

                String jitu = "j";
                return jitu;

            } catch (Exception e) {
                Log.e("Movies This Month",
                        "Error in downloading: " + e.toString());
                String no = "jitu";
                return no;

            }

        }

        @Override
        protected void onPostExecute(String b) {
            Log.d("pata nahi", "pata nahi");
            // TODO change text view id for yourself
            dialog.dismiss();
            if (b.contentEquals("jitu")) {
                try {
                    NoInternet f = new NoInternet();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    Toast.makeText(getActivity(), "No Internet Connection",
                            Toast.LENGTH_SHORT).show();
                    ft.replace(((ViewGroup) getView().getParent()).getId(), f);
                    ft.addToBackStack(null);
                    ft.commit();
                }catch(Exception e)
                {
                    e.printStackTrace();
                }


            } else if (b.contentEquals("j")) {
                Log.d("pata  ----------nahi", "pata ------ nahi");
                pop.setText("Now Showing");
                more1.setText("MORE");
                Collections.shuffle(dataObject2);
                try {
                    viewPager.setAdapter(myPagerAdapter);
                    Collections.shuffle(dataObject);
                    mlistview.setAdapter(m1Adapter);
                }catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
            // close progresses dialog

        }
    }

    private class GetJSONFrom2 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url
                        .openConnection();

                InputStream is = new BufferedInputStream(
                        urlConnection.getInputStream());

                String result = convertStreamToString(is);
                JSONObject reader = new JSONObject(result);
                // String data = null;
                JSONArray jsonArray = reader.optJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String release_date = jsonObject.optString("release_date").toString() ;

                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DATE, -10);
                    String today = sdf.format(cal.getTime()) ;

                    //Log.d("AAAA", release_date + "  "+ today) ;

                    Date date1 = sdf.parse(release_date);
                    Date date2 = sdf.parse(today);

                    if (date1.compareTo(date2) > 0 ) {

                        map.put(TITLE, jsonObject.optString("title").toString());
                        map.put(IMAGE, jsonObject.optString("poster_path")
                                .toString());
                        map.put(ID, jsonObject.optString("id").toString());
                        dataObjects.add(map);
                  }
                }

            } catch (Exception e) {
                Log.e("POPULAR", "Error in downloading: " + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String b) {

            // TODO change text view id for yourself

            // tv.setText("hi"+result) ;
            try {
                thismonth.setText("Upcoming");
                more2.setText("MORE");
               // plistview.setAdapter(m2Adapter);
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            // close progresses dialog
        }
    }
    private class GetJSONFrom2more extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url
                        .openConnection();

                InputStream is = new BufferedInputStream(
                        urlConnection.getInputStream());

                String result = convertStreamToString(is);
                JSONObject reader = new JSONObject(result);
                // String data = null;
                JSONArray jsonArray = reader.optJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String release_date = jsonObject.optString("release_date").toString() ;

                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DATE, -15);
                    String today = sdf.format(cal.getTime()) ;

                    //Log.d("AAAA", release_date + "  "+ today) ;

                    Date date1 = sdf.parse(release_date);
                    Date date2 = sdf.parse(today);

                    if (date1.compareTo(date2) > 0 ) {

                        map.put(TITLE, jsonObject.optString("title").toString());
                        map.put(IMAGE, jsonObject.optString("poster_path")
                                .toString());
                        map.put(ID, jsonObject.optString("id").toString());
                        dataObjects.add(map);
                    }
                }

            } catch (Exception e) {
                Log.e("POPULAR", "Error in downloading: " + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String b) {

            // TODO change text view id for yourself

            // tv.setText("hi"+result) ;
            try {
                Collections.shuffle(dataObjects);
                plistview.setAdapter(m2Adapter);
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            // close progresses dialog
        }
    }


    private class GetJSONFrom3 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url
                        .openConnection();

                InputStream is = new BufferedInputStream(
                        urlConnection.getInputStream());

                String result = convertStreamToString(is);
                JSONObject reader = new JSONObject(result);
                // String data = null;
                JSONArray jsonArray = reader.optJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(TITLE, jsonObject.optString("title").toString());
                    map.put(IMAGE, jsonObject.optString("poster_path")
                            .toString());
                    map.put(ID, jsonObject.optString("id").toString());
                    ratedObject.add(map);

                }

            } catch (Exception e) {
                Log.e("TOP RATED", "Error in downloading: " + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String b) {

            // TODO change text view id for yourself
try {
    // tv.setText("hi"+result) ;
    toprated.setText("Top Rated");
    more3.setText("MORE");
    Collections.shuffle(ratedObject);
    rlistview.setAdapter(m3Adapter);
}catch (Exception e)
{
    e.printStackTrace();
}
            // close progresses dialog
        }
    }

    private class GetJSONFrom4 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url
                        .openConnection();

                InputStream is = new BufferedInputStream(
                        urlConnection.getInputStream());

                String result = convertStreamToString(is);
                JSONObject reader = new JSONObject(result);
                // String data = null;
                JSONArray jsonArray = reader.optJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(TITLE, jsonObject.optString("title").toString());
                    map.put(IMAGE, jsonObject.optString("poster_path")
                            .toString());
                    map.put(ID, jsonObject.optString("id").toString());
                    popObject.add(map);

                }

            } catch (Exception e) {
                Log.e("TOP RATED", "Error in downloading: " + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String b) {

            // TODO change text view id for yourself
            try {
                // tv.setText("hi"+result) ;
                popular.setText("Popular");
                more4.setText("MORE");
                Collections.shuffle(popObject);
                poplistview.setAdapter(m4Adapter);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            // close progresses dialog
        }
    }
    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    private class MyPagerAdapter extends PagerAdapter {

        int NumberOfPages = 8 ;

        @Override
        public int getCount() {
            return NumberOfPages;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {



            HashMap<String, String> list = new HashMap<String, String>();
           list = dataObject2.get(position);

            TextView textView = new TextView(getActivity());
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(25);
            textView.setPadding(10, 0, 0, 10);
            textView.setMaxLines(2);
            textView.setGravity(Gravity.BOTTOM);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setText(list.get(TITLE));

            ImageView imageView = new ImageView(getActivity());
           // final ProgressDialog progress = new ProgressDialog(getActivity()) ;
            //progress.show() ;

           // imageView.setBackgroundResource(R.drawable.viewpager);
             imageView.setBackgroundColor(Color.rgb(211, 211, 211));
            LayoutParams imageParams = new LayoutParams(
                    LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(imageParams);

            FrameLayout layout = new FrameLayout(getActivity());
           // layout.setOrientation(LinearLayout.VERTICAL);
            LayoutParams layoutParams = new LayoutParams(
                    LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
           // layout.setBackgroundColor(backgroundcolor[position]);
            layout.setLayoutParams(layoutParams);
            layout.addView(imageView);
            layout.addView(textView);


            Uri u = Uri.parse(baseurlpos + list.get(POSTER));

            Picasso.with(getActivity()).load(u).fit().into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    //progress.cancel() ;
                }

                @Override
                public void onError() {

                }
            });
           final String uu = list.get(ID);

            final int page = position;
           imageView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {


                    Bundle b = new Bundle();
                    b.putString("url", uu);

                    Display f = new Display();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    f.setArguments(b);
                    ft.hide(fm.findFragmentById(R.id.mainfragment));
                    ft.add(((ViewGroup) getView().getParent()).getId(), f);
                    ft.addToBackStack(null);
                    ft.commit();
                }});

            container.addView(layout);
            return layout;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            try {
                container.removeView((LinearLayout) object);
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }}

}
