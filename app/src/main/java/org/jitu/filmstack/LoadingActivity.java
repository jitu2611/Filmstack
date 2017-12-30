package org.jitu.filmstack;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Jitu on 12/7/2017.
 */
public class LoadingActivity extends Activity {

    SparseBooleanArray spj ;

    final String[] arr ={"28","12","16","35","80","99","18","10751","14","36","27","9648","10749","878","53"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadingactivity);


        changeStatusBarColor();
        try {
            spj = Welcome2.glv.getCheckedItemPositions();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        Thread timer= new Thread()
        {
            public void run()
            {
                try
                {
                    //Display for 3 seconds
                    sleep(3000);
                }
                catch (InterruptedException e)
                {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                finally
                {
                    //Goes to Activity  StartingPoint.java(STARTINGPOINT)
                    Intent i = new Intent(LoadingActivity.this,FragmentMainActivity.class) ;
                    startActivity(i);
                }
            }
        };
        timer.start();
        new loading().execute() ;

    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#E53935"));
        }
    }
    public class loading extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                for (int i = 0; i < spj.size(); i++) {
                    String uu = "http://api.themoviedb.org/3/discover/movie?api_key=1b104f628c51adebff0570a1a27ae2af&with_genres=" + arr[spj.keyAt(i)] + "&page=1";
                    try {
                        URL url = new URL(uu);
                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                        InputStream is = new BufferedInputStream(
                                urlConnection.getInputStream());

                        String result = convertStreamToString(is);
                        // Toast.makeText(getBaseContext(), result,
                        // Toast.LENGTH_SHORT).show() ;
                        JSONObject reader = new JSONObject(result);

                        JSONArray jsonArray = reader.optJSONArray("results");

                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(j);
                            HashMap<String, String> map = new HashMap<String, String>();


                            String idd = jsonObject.optString("id");
                            Cursor c = managedQuery(AlgoDB.Contenturi, null, "id = " + idd, null, null);
                            if (!(c.moveToFirst()) || c.getCount() == 0) {
                                //cursor is empty
                                ContentValues values = new ContentValues();
                                values.put(AlgoDB.algo.ID, idd);
                                values.put(AlgoDB.algo.TITLE, jsonObject.optString("title"));
                                values.put(AlgoDB.algo.COUNT, 5);
                                values.put(AlgoDB.algo.IURL, MainActivity.baseurl + jsonObject.optString("poster_path"));
                                try {
                                    getContentResolver().insert(AlgoDB.Contenturi,
                                            values);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                ContentValues values = new ContentValues();

                                values.put(AlgoDB.algo.COUNT, c.getInt(c.getColumnIndex(AlgoDB.algo.COUNT)) + 5);
                                getContentResolver().update(AlgoDB.Contenturi, values, "id = " + idd, null);
                            }


                            // String name =
                            // reader.optString("original_title").toString() ;

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
           //


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

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
