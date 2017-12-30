package org.jitu.filmstack;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Jitesh on 6/16/2017.
 */
public class Popular extends Fragment {

    static final String baseurl = "http://image.tmdb.org/t/p/w154" ;
    static final String baseurlpos = "http://image.tmdb.org/t/p/w300";
    static final String TITLE = "title";
    static final String IMAGE = "image";
    static final String ID = "id";
    private int pageCount=2 ;
    ArrayList<HashMap<String, String>> popObject = new ArrayList<HashMap<String, String>>();
    static final String POSTER = "poster" ;
    GridView gridview ;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        try {
            View v = inflater.inflate(R.layout.upcoming, container, false);
            gridview = (GridView) v.findViewById(R.id.gridview);
            return v ;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        popObject.clear();

        try {
            new GetJSONFrom4()
                    .execute("http://api.themoviedb.org/3/movie/popular?api_key=1b104f628c51adebff0570a1a27ae2af");
            gridview.setAdapter(adapter);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> ll = new HashMap<String, String>();
                ll = popObject.get(position);
                String u = ll.get(ID);


                Bundle b = new Bundle();
                b.putString("url", u);
                b.putString("title",ll.get(TITLE)) ;
try {
    Display f = new Display();
    FragmentManager fm = getActivity().getSupportFragmentManager();
    FragmentTransaction ft = fm.beginTransaction();
    f.setArguments(b);
    ft.hide(fm.findFragmentById(R.id.mainfragment));
    ft.add(R.id.mainfragment, f);
    ft.addToBackStack(null);
    ft.commit();
}catch (Exception e)
{
    e.printStackTrace();
}
            }
        });
       gridview.setOnScrollListener(new EndlessScrollListener());

    }

    private BaseAdapter adapter = new BaseAdapter() {

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
        public View getView(int position, View retval, ViewGroup parent) {
            if(retval==null) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                retval = inflater.inflate(R.layout.hrow, parent, false);
            }
            HashMap<String, String> list = new HashMap<String, String>();
            list = popObject.get(position);
            String url = baseurl + list.get(IMAGE) ;
            ImageView img = (ImageView) retval.findViewById(R.id.hrow);
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels;
            int width = displaymetrics.widthPixels;

            img.getLayoutParams().height = (int)((width/2) * 1.2f) ;

            final ProgressBar spinner = (ProgressBar) retval.findViewById(R.id.loading);


            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisk(true).resetViewBeforeLoading(true)
                    .build();


            imageLoader.displayImage(url, img, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    spinner.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    spinner.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    spinner.setVisibility(View.GONE);
                }
            });




            return retval;
        }

    };

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
                Collections.shuffle(popObject);
                gridview.setAdapter(adapter);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            // close progresses dialog
        }
    }

    private class extraLoading extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {

            // @BadSkillz codes with same changes
            try {
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url
                        .openConnection();

                InputStream is = new BufferedInputStream(
                        urlConnection.getInputStream());

                String result = convertStreamToString(is);
                // Toast.makeText(getBaseContext(), result,
                // Toast.LENGTH_SHORT).show() ;
                JSONObject reader = new JSONObject(result);

                JSONArray jsonArray = reader.optJSONArray("results");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(TITLE, jsonObject.optString("title").toString());
                    map.put(IMAGE, jsonObject.optString("poster_path")
                            .toString());
                    map.put(ID, jsonObject.optString("id").toString());
                    // String name =
                    // reader.optString("original_title").toString() ;
                    popObject.add(map);

                }


            } catch (Exception e) {
                Log.e("Get Url", "Error in downloading: " + e.toString());

            }
            return null;

        }

        @Override
        protected void onPostExecute(String b) {
            try {
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //	lv.setAdapter(adapter);

            // TODO change text view id for yourself
            // LazyAdapter adapter = new LazyAdapter(MainActivity.this,list)
            // ;
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

    public class EndlessScrollListener implements AbsListView.OnScrollListener {

        private int visibleThreshold = 20;
        private int currentPage = 0;
        private int previousTotal = 0;
        private boolean loading = true;

        public EndlessScrollListener() {
        }
        public EndlessScrollListener(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                    currentPage++;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold) && pageCount<=10) {
                // I load the next page of gigs using a background task,
                // but you can call any function here.
                String url = "https://api.themoviedb.org/3/movie/popular?api_key=1b104f628c51adebff0570a1a27ae2af" + "&page=" + pageCount;
                pageCount++;
                try {
                    new extraLoading().execute(url);
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
                loading = true;
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
    }





}
