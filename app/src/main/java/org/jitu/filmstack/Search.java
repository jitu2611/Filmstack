package org.jitu.filmstack;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;



import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


import org.json.JSONArray;
import org.json.JSONObject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class Search extends Fragment {

    String search;
    static final String TITLE = "title";
    static final String IMAGE = "image";
    static final String ID = "id";
    String Tag = "Search";
    String query;
    EditText tv;
    private String BaseUrl = "https://api.themoviedb.org/3/search/movie?query=";
    private String apikey = "&api_key=1b104f628c51adebff0570a1a27ae2af";

    ListView lv;
    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    LazyAdapter adapter;
    ImageView back;
    private int pageCount = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        try {
            return inflater.inflate(R.layout.search, container, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        try {


            list.clear();
            lv = (ListView) getView().findViewById(R.id.listview);

            Bundle extras = getArguments();

            BaseUrl = extras.getString("query");


            if (!isOnline()) {
                NoInternet f = new NoInternet();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Toast.makeText(getActivity(), "No Internet Connection",
                        Toast.LENGTH_SHORT).show();
                ft.replace(R.id.mainfragment, f);
                ft.addToBackStack(null);


                ft.commit();


            }


            new GetStringFromUrl().execute(BaseUrl);


            lv.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                                        long arg3) { // TODO Auto-generated method stub
                    HashMap<String, String> l = new HashMap<String, String>();
                    l = list.get(pos);
                    String u = l.get(ID);

                    Bundle b = new Bundle();
                    b.putString("url", u);
                    b.putString("title",l.get(TITLE));
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
            lv.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    final int lastItem = firstVisibleItem + visibleItemCount;
                    if (lastItem == totalItemCount) {
                        // you have reached end of list, load more data
                        String url = BaseUrl + "&page=" + pageCount;
                        pageCount++;
                        if(pageCount<=3)
                        new extraLoading().execute(url);
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private class GetStringFromUrl extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            dialog.setCancelable(false);
            dialog.setMessage("LOADING ");
            dialog.show();

        }

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
                if (jsonArray.length() == 0) {
                    String l = "no";
                    return l;

                } else {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();

                        map.put(TITLE, jsonObject.optString("title").toString());
                        map.put(IMAGE, jsonObject.optString("poster_path")
                                .toString());
                        map.put(ID, jsonObject.optString("id").toString());
                        // String name =
                        // reader.optString("original_title").toString() ;
                        list.add(map);
                    }
                }

            } catch (Exception e) {
                Log.e("Get Url", "Error in downloading: " + e.toString());

            }
            String ji = "jitu";
            return ji;
        }

        @Override
        protected void onPostExecute(String b) {
            dialog.dismiss();


            if (b.contentEquals("no")) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    try {
                        fm.popBackStack();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(getActivity(), "No Result Found  ", Toast.LENGTH_LONG).show();
            } else {
                try {
                    adapter = new LazyAdapter(getActivity(), list);
                    lv.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // TODO change text view id for yourself
                // LazyAdapter adapter = new LazyAdapter(MainActivity.this,list)
                // ;
            }
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
                    list.add(map);

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

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}
