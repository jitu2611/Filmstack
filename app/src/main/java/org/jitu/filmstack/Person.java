package org.jitu.filmstack;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
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
import java.util.HashMap;

/**
 * Created by Jitesh on 1/2/2017.
 */
public class Person extends Fragment {


    ImageView pimage;
    TextView name, dob, knownfor, biogr, biography;
    String personid, namep, ico, dobirth = "N/A", bio = "N/A";
   ImageView like ;
    static final String TITLE = "title";
    static final String IMAGE = "image";
    static final String ID = "id";
    ArrayList<HashMap<String, String>> dataObject = new ArrayList<HashMap<String, String>>();
    HorizontalListView mlistview;
    static final String baseurl = "http://image.tmdb.org/t/p/w300";
    String base = "https://api.themoviedb.org/3/person/";
    String api_key = "?api_key=1b104f628c51adebff0570a1a27ae2af";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        return inflater.inflate(R.layout.person, container, false);

    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        try {
            dataObject.clear();

            pimage = (ImageView) getView().findViewById(R.id.icon);
            name = (TextView) getView().findViewById(R.id.personname);
            biogr = (TextView) getView().findViewById(R.id.biogr);
            biography = (TextView) getView().findViewById(R.id.biograpbhy);
            dob = (TextView) getView().findViewById(R.id.dob);
            knownfor = (TextView) getView().findViewById(R.id.knownfor);
            like=(ImageView)getView().findViewById(R.id.likeperson);
            like.setImageResource(R.drawable.bh);
            Bundle b = getArguments();
            mlistview = (HorizontalListView) getView().findViewById(R.id.movie);
            personid = b.getString("pid");

            if (!isOnline()) {
                NoInternet f = new NoInternet();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                ft.replace(R.id.mainfragment, f);
                ft.addToBackStack(null);


                ft.commit();


            }
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String, String> l = new HashMap<String, String>();
                    int ss ;
                    like.setImageResource(R.drawable.lh);
                    like.setEnabled(false);
                     ContentValues value = new ContentValues();
                    value.put(FavactDB.favact.CID, personid);
                    value.put(FavactDB.favact.CIURL,ico);

                    getActivity().getContentResolver().insert(FavactDB.Contenturi,value) ;


                    for(int i=0;i<dataObject.size();i++)
                    {
                        l = dataObject.get(i);

                        Cursor c = getActivity().managedQuery(AlgoDB.Contenturi,null,"id = "+l.get(ID),null,null) ;
                        if (!(c.moveToFirst()) || c.getCount() ==0){
                            //cursor is empty
                            ContentValues values = new ContentValues();
                            values.put(AlgoDB.algo.ID, l.get(ID));
                            values.put(AlgoDB.algo.TITLE,l.get(TITLE)) ;
                            values.put(AlgoDB.algo.COUNT, 5);
                            values.put(AlgoDB.algo.IURL, MainActivity.baseurl + l.get(IMAGE));
                            try {
                                getActivity().getContentResolver().insert(AlgoDB.Contenturi,
                                        values);
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            ContentValues values = new ContentValues();

                            values.put(AlgoDB.algo.COUNT, c.getInt(c.getColumnIndex(AlgoDB.algo.COUNT)) + 8);
                            getActivity().getContentResolver().update(AlgoDB.Contenturi,values,"id = "+l.get(ID),null) ;
                        }


                        //c.close();
                    }
                    Toast.makeText(getActivity(), "You Liked "+ namep, Toast.LENGTH_SHORT).show();
                }
            });
            mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                    // TODO Auto-generated method stub
                    HashMap<String, String> l = new HashMap<String, String>();
                    try {
                        l = dataObject.get(arg2);
                        String u = l.get(ID);

                        Bundle b = new Bundle();
                        b.putString("url", u);
                        b.putString("title",l.get(TITLE)) ;
                        Display f = new Display();
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        f.setArguments(b);
                        ft.hide(fm.findFragmentById(R.id.mainfragment));
                        ft.add(((ViewGroup) getView().getParent()).getId(), f);
                        ft.addToBackStack(null);
                        ft.commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            //Toast.makeText(getActivity(), base + personid + api_key, Toast.LENGTH_SHORT).show();
            new GetStringFromUrl().execute(base + personid + api_key);
            new GetJSONFrom1().execute(base + personid + "/movie_credits" + api_key);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class GetStringFromUrl extends AsyncTask<String, Void, String> {
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

            // @BadSkillz codes with same changes
            try {
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url
                        .openConnection();

                InputStream is = new BufferedInputStream(
                        urlConnection.getInputStream());

                String result = convertStreamToString(is);

                JSONObject reader = new JSONObject(result);


                ico = reader.optString("profile_path");
                namep = reader.optString("name");
                dobirth = reader.optString("birthday");
                bio = reader.optString("biography");


                String j = "j";
                return j;
            } catch (Exception e) {
                Log.e("Get Url", "Error in downloading: " + e.toString());
                String j = "jitu";
                return j;
            }

        }

        @Override
        protected void onPostExecute(String b) {
            if (bio.contentEquals("null")) {
                bio = "N/A";
            }
            if (dobirth.contentEquals("null")) {
                dobirth = "N/A";
            }
            try {


                Cursor cursor = getActivity().getContentResolver().query(FavactDB.Contenturi,null,"cid = "+personid,null,null) ;
                if(cursor.getCount()!=0) {
                    like.setImageResource(R.drawable.lh);
                    like.setEnabled(false);
                   // cursor.close();
                }

                name.setText(namep);
                dialog.cancel();
                dob.setText(dobirth);
                biogr.setText("Biography");
                biography.setText(bio);
                if (ico.contentEquals("null")) {
                    pimage.setImageResource(R.drawable.npii);
                } else {

                    ImageLoader imageLoader = ImageLoader.getInstance();
                    DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(false)
                            .cacheOnDisk(true).resetViewBeforeLoading(true)
                            .build();


                    imageLoader.displayImage(MainActivity.baseurl + ico, pimage, options) ;

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                    R.layout.hrowp, null);
            HashMap<String, String> list = new HashMap<String, String>();
            list = dataObject.get(position);

            ImageView img = (ImageView) retval.findViewById(R.id.hrow);
            // Bitmap l =
            // BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher)
            // ;



            final ProgressBar spinner = (ProgressBar) retval.findViewById(R.id.loading);


            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY).cacheInMemory(true)
                    .cacheOnDisk(true).resetViewBeforeLoading(true)
                    .build();


            imageLoader.displayImage(MainActivity.baseurl + list.get(IMAGE), img, options, new SimpleImageLoadingListener() {
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
                JSONArray jsonArray = reader.optJSONArray("cast");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    String adult = jsonObject.optString("adult").toString();
                    if (adult.contentEquals("false")) {
                        map.put(TITLE, jsonObject.optString("title").toString());
                        map.put(IMAGE, jsonObject.optString("poster_path")
                                .toString());
                        map.put(ID, jsonObject.optString("id").toString());

                        dataObject.add(map);
                    }
                }

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
            Log.d("pata  ----------nahi", "pata ------ nahi");
            knownfor.setText("Known For");

            try {
                mlistview.setAdapter(m1Adapter);
            } catch (Exception e) {
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


}
