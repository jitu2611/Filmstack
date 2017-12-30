package org.jitu.filmstack;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.picasso.Picasso;

import javax.net.ssl.HttpsURLConnection;

import info.guardianproject.netcipher.NetCipher;


public class Display extends Fragment {

    ImageView poster, icon;
    static final String NAME = "name";
    static final String CID = "cid";
    String rate;
    //String url ;
   String  videourl="" ;
    Animation mAnimation ;
    InterstitialAd mInterstitialAd;


    static final String IMAGE = "image";
    static final String TITLE = "title";

    static final String ID = "id";
    ArrayList<HashMap<String, String>> dataObject = new ArrayList<HashMap<String, String>>();

    static final String CPIC = "cpic";
    String key = "";
    String pos = "", ico = "", tit = "", rel = "", gen = "N/A";
    String imgurl = "http://image.tmdb.org/t/p/w154";
    String imgurlp = "http://image.tmdb.org/t/p/w780";

    String mlink; int rele=1;
    String des = "N/A";
    TextView title, release, genre, desc, sim, cas,watch,descc;
    TextView fetcht ;
    Button watchNow , trailer , alarm , bookmark,seen,down ;
    ImageView downb ;
    HorizontalListView c, similar;
    Button share;
    String base = "https://api.themoviedb.org/3/movie/";
    String api_key = "?api_key=1b104f628c51adebff0570a1a27ae2af";
    String guestsessionid = "";
    ArrayList<HashMap<String, String>> cast = new ArrayList<HashMap<String, String>>();
    static String URL = null;
    LinearLayout fetch ;
    Animation slideup ;



    private DatabaseReference id ;

    static Upcoming newInstance(String U)
    {
        Upcoming f = new Upcoming() ;
        URL = U ;
        return f ;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Log.i("HELLO", "hello") ;
        return inflater.inflate(R.layout.display, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        Log.i("JITESH", "ONACT") ;

        Log.i("JITESH","ONSTART") ;

        videourl="" ;
            cast.clear();
            dataObject.clear();
try {
    final Bundle b = getArguments();
    URL = b.getString("url");

    tit = b.getString("title") ;
    tit=tit.replaceAll(":", "") ;
    tit=tit.replaceAll("'","") ;
    tit=tit.replaceAll(",","") ;
    tit=tit.replaceAll("\\.","") ;
   // Toast.makeText(getActivity(),tit,Toast.LENGTH_SHORT).show() ;
}catch (Exception e)
{
    e.printStackTrace();
}


        fetch = (LinearLayout) getView().findViewById(R.id.fetch) ;
        fetcht = (TextView)getView().findViewById(R.id.fetchtext) ;
        slideup = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slideup);
        gen = "";
            poster = (ImageView) getView().findViewById(R.id.poster);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
           poster.getLayoutParams().height =(int) (width/1.75f) ;
            Log.d("A", "1");
            share = (Button) getView().findViewById(R.id.share);

            if (!isOnline()) {
                NoInternet f = new NoInternet();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                ft.add(R.id.mainfragment, f);
                ft.addToBackStack(null);


                ft.commit();


            }else {
                c = (HorizontalListView) getView().findViewById(R.id.cast);
                alarm = (Button) getView().findViewById(R.id.Alarm);
                //ratingBar = (RatingBar) getView().findViewById(R.id.ratingBar1);
                similar = (HorizontalListView) getView().findViewById(R.id.similar);
                watchNow = (Button) getView().findViewById(R.id.watchnow);

                icon = (ImageView) getView().findViewById(R.id.icon);
                sim = (TextView) getView().findViewById(R.id.Similar);
                down = (Button) getView().findViewById(R.id.usingseedr);
                cas = (TextView) getView().findViewById(R.id.CAST);
                seen = (Button) getView().findViewById(R.id.seen);
                title = (TextView) getView().findViewById(R.id.title);
                watch = (TextView) getView().findViewById(R.id.watch);
                title.setSelected(true);

                release = (TextView) getView().findViewById(R.id.release);
                genre = (TextView) getView().findViewById(R.id.genre);
                desc = (TextView) getView().findViewById(R.id.desc);
                descc = (TextView) getView().findViewById(R.id.des);

                //genre.setSelected(true);
                seen.setEnabled(false);

                trailer = (Button) getView().findViewById(R.id.trailer);
                trailer.setEnabled(false);

                downb = (ImageView) getView().findViewById(R.id.downb);
                mAnimation = new TranslateAnimation(
                        TranslateAnimation.ABSOLUTE, 0f,
                        TranslateAnimation.ABSOLUTE, 0f,
                        TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                        TranslateAnimation.RELATIVE_TO_PARENT, 0.05f);
                mAnimation.setDuration(500);
                mAnimation.setRepeatCount(-1);
                mAnimation.setRepeatMode(Animation.REVERSE);
                mAnimation.setInterpolator(new LinearInterpolator());


                bookmark = (Button) getView().findViewById(R.id.Bookmark);
                bookmark.setEnabled(false);
                Cursor x=null ;
                try {
                    x = getActivity().managedQuery(Database.Contenturi, null, "_id = " + URL, null, null);
                    if (x.getCount() == 0) {
                        bookmark.setEnabled(true);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {

                }
                mInterstitialAd = new InterstitialAd(getActivity());

                // set the ad unit ID
                mInterstitialAd.setAdUnitId("ca-app-pub-5961544513055122/5084292333");

                AdRequest adRequest = new AdRequest.Builder()//.addTestDevice("3B959FD02EE9C6EC1F533AE46958A019")
                        .build();

                // Load ads into Interstitial Ads
                mInterstitialAd.loadAd(adRequest);

                mInterstitialAd.setAdListener(new AdListener() {
                });
                registerForContextMenu(downb);
                downb.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showInterstitial();
                        getActivity().openContextMenu(v);
                      /*  Uri uri  = Uri.parse(videourl);
                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(i);
                        showInterstitial(); */
                    }
                });
                seen.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showInterstitial();
                        try {
                            // Toast.makeText(getActivity(), "You Have Seen " + tit, Toast.LENGTH_SHORT).show();
                            HashMap<String, String> l = new HashMap<String, String>();
                            int ss;
                            if (dataObject.size() > 10) {
                                ss = 10;
                            } else {
                                ss = dataObject.size();
                            }

                            ContentValues val = new ContentValues();
                            val.put(WatchedDB.watched.ID, URL);
                            val.put(WatchedDB.watched.TITL, tit);
                            val.put(WatchedDB.watched.IURL, ico);
                            getActivity().getContentResolver().insert(WatchedDB.Contenturi, val);

                            getActivity().getContentResolver().delete(AlgoDB.Contenturi, "id = " + URL, null);

                            seen.setEnabled(false);

                            for (int i = 0; i < ss; i++) {
                                l = dataObject.get(i);
                                Cursor c = null;
                                Cursor cc = getActivity().getContentResolver().query(WatchedDB.Contenturi, null, "id = " + l.get(ID), null, null);
                                if (!(cc.moveToFirst()) || cc.getCount() == 0) {
                                    c = getActivity().managedQuery(AlgoDB.Contenturi, null, "id = " + l.get(ID), null, null);
                                    if (!(c.moveToFirst()) || c.getCount() == 0) {
                                        //cursor is empty
                                        ContentValues values = new ContentValues();
                                        values.put(AlgoDB.algo.ID, l.get(ID));
                                        values.put(AlgoDB.algo.TITLE, l.get(TITLE));
                                        values.put(AlgoDB.algo.COUNT, 2);
                                        values.put(AlgoDB.algo.IURL, imgurl + l.get(IMAGE));
                                        try {
                                            getActivity().getContentResolver().insert(AlgoDB.Contenturi,
                                                    values);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        ContentValues values = new ContentValues();

                                        values.put(AlgoDB.algo.COUNT, c.getInt(c.getColumnIndex(AlgoDB.algo.COUNT)) + 2);
                                        getActivity().getContentResolver().update(AlgoDB.Contenturi, values, "id = " + l.get(ID), null);
                                    }

                                }
                                // cc.close();


                            }
                            Toast.makeText(getActivity(), "ADDED TO WATCHED LIST", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                registerForContextMenu(down);
                down.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showInterstitial();
                        getActivity().openContextMenu(v);
                      /*  try {
                            Uri uri = Uri.parse(videourl);
                            Intent i = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(i);
                            showInterstitial();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } */
                    }
                });

                watchNow.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // showInterstitial();
                        try {
                            Intent i = new Intent(getActivity(), VideoPlayer.class);
                            Bundle b = new Bundle();
                            b.putString("video", videourl);
                            i.putExtras(b);
                            startActivity(i);
                            showInterstitial();
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                       /* String watch = "http://fmovies.se/search?keyword="+title.getText().toString() ;



					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(watch));
					startActivity(intent);*/
                    }
                });
                share.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        try {
                            Uri bmpUri = getLocalBitmapUri(poster);
                            if (bmpUri != null) {
                                // Construct a ShareIntent with link to image
                                Intent shareIntent = new Intent();
                                shareIntent.setAction(Intent.ACTION_SEND);
                                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                                shareIntent.setType("image/*");
                                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                                        "Movie Time !! ");
                                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                                        title.getText().toString() + "\n"
                                                + genre.getText().toString() + "\n\n" + "FilmStack" + "\n" + "https://play.google.com/store/apps/details?id=org.jitu.filmstack&hl=en");

                                startActivity(Intent.createChooser(shareIntent,
                                        "Share Image"));
                            } else {

                                Intent shareIntent = new Intent();
                                shareIntent.setAction(Intent.ACTION_SEND);
                                Bitmap pop = BitmapFactory.decodeResource(getResources(),
                                        R.drawable.popcorn);
                                Uri popuri = null;
                                try {
                                    File file = new File(getActivity().getExternalFilesDir(
                                            Environment.DIRECTORY_PICTURES), "share_image_"
                                            + System.currentTimeMillis() + ".png");
                                    FileOutputStream out = new FileOutputStream(file);
                                    pop.compress(Bitmap.CompressFormat.PNG, 50, out);
                                    out.close();
                                    popuri = Uri.fromFile(file);
                                    shareIntent.putExtra(Intent.EXTRA_STREAM, popuri);
                                } catch (Exception e) {


                                    e.printStackTrace();
                                }

                                shareIntent.setType("image/*");
                                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                                        "Movie Time !! ");
                                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                                        title.getText().toString() + "\n"
                                                + genre.getText().toString());

                                startActivity(Intent.createChooser(shareIntent,
                                        "Share Image"));

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });


                bookmark.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        try {
                            ContentValues values = new ContentValues();
                            values.put(Database.bookmark.TITLE, tit);
                            values.put(Database.bookmark.RELEASE, rel);
                            values.put(Database.bookmark.GENRE, gen);
                            values.put(Database.bookmark.DESC, des);
                            poster.buildDrawingCache();
                            Bitmap mposter = poster.getDrawingCache();
                            ByteArrayOutputStream bo = new ByteArrayOutputStream();
                            mposter.compress(Bitmap.CompressFormat.PNG, 100, bo);
                            byte[] im = bo.toByteArray();
                            values.put(Database.bookmark.POSTER, im);
                            icon.buildDrawingCache();
                            Bitmap micon = icon.getDrawingCache();
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            micon.compress(Bitmap.CompressFormat.PNG, 100, bos);
                            byte[] img = bos.toByteArray();
                            values.put(Database.bookmark.ICON, img);

                            getActivity().getContentResolver().insert(Database.Contenturi,
                                    values);
                            Toast.makeText(getActivity(), "Bookmarked Successfully",
                                    Toast.LENGTH_SHORT).show();
                            showInterstitial();
                            bookmark.setEnabled(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
                // Toast.makeText(getBaseContext(), URL,Toast.LENGTH_LONG).show() ;
                trailer.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        //showInterstitial();
                        String url = "https://www.youtube.com/watch?v=" + key;
                        /*Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);*/
                        Intent i = new Intent(getActivity(), YoutubeAct.class);
                        Bundle b = new Bundle();
                        b.putString("yid", key);
                        i.putExtras(b);
                        startActivity(i);
                    }
                });

                similar.setOnItemClickListener(new OnItemClickListener() {

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
                            b.putString("title", l.get(TITLE));
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
                c.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                            long arg3) {
                        // TODO Auto-generated method stub
                        HashMap<String, String> l = new HashMap<String, String>();
                        try {
                            l = cast.get(arg2);
                            String u = l.get(CID);

                            Bundle b = new Bundle();
                            b.putString("pid", u);
                            Person f = new Person();
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
                id = FragmentMainActivity.mDatabase.child(URL) ;
                Log.i("TAGG",""+FragmentMainActivity.dlink.size()) ;
                videourl = FragmentMainActivity.dlink.get(URL) ;
                if(videourl==null)
                    videourl="";
                Log.i("TAGG", "Y3" + videourl);


                try {

                    GetStringFromUrl gg = new GetStringFromUrl() ;

                    gg.execute( base + URL + api_key);
                    String casturl = base + URL + "/casts" + api_key;

                    //	new GetGuestID().execute("https://api.themoviedb.org/3/authentication/guest_session/new?api_key=1b104f628c51adebff0570a1a27ae2af") ;
                    new trailer().execute( base + URL + "/videos" + api_key);
                    new GetCAST().execute( casturl);
                    new Similar().execute(base + URL + "/recommendations" + api_key);

                    //new Index().execute() ;


                    Log.i("Hello", "Hello") ;





                    //new openloadmovie().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,tit) ;
                    //new dl3enter().execute() ;
                   // new Add2().execute();
                   /* url = "https://thepiratebay.org/search/";
                    //Toast.makeText(getActivity(),"MAGNETIC LINK COPIED TO CLIPBOARD",Toast.LENGTH_LONG).show();
                    String tt = tit;
                    tt = tt.replaceAll(" ", "%20");
                    //tv = (TextView)getView().findViewById(R.id.tv) ;
                    url = url + tt + "/0/99/201";



*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
                alarm.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        showInterstitial();
                        Alarm f = new Alarm();
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();

                        Bundle b = new Bundle();
                        try {
                            b.putString("title", title.getText().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                            b.putString("title", tit);
                        }

                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                                R.drawable.popcorn);
                        ;
                        try {
                            bitmap = ((BitmapDrawable) icon.getDrawable()).getBitmap();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ByteArrayOutputStream _bs = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 50, _bs);
                        b.putByteArray("byteArray", _bs.toByteArray());
                        f.setArguments(b);
                        ft.hide(fm.findFragmentById(R.id.mainfragment));
                        ft.add(((ViewGroup) getView().getParent()).getId(), f);
                        ft.addToBackStack(null);
                        ft.commit();

                    }
                });
            }
    }


    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {

            File file = new File(getActivity().getExternalFilesDir(
                    Environment.DIRECTORY_PICTURES), "share_image_"
                    + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 25, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
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

                pos = reader.optString("backdrop_path");
                ico = reader.optString("poster_path");
                tit = reader.optString("title");
                rel = reader.optString("release_date");
                des = reader.optString("overview");

                JSONArray jsonArray = reader.optJSONArray("genres");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    gen = gen + jsonObject.optString("name") + " ";
                }
                tit=tit.replaceAll(":", "");
                tit=tit.replaceAll("'","") ;
                tit=tit.replaceAll(",","") ;
                tit=tit.replaceAll("\\.", "") ;

                String j = tit;





                return j;
            } catch (Exception e) {
                Log.e("Get Url", "Error in downloading: " + e.toString());
                String j = "jitu";
                return j;
            }

        }

        @Override
        protected void onPostExecute(String b) {
            fetch.setVisibility(View.VISIBLE);
            fetch.setAlpha(0.0f);
            fetch.animate()
                    .translationY(fetch.getHeight()).setDuration(1000)
                    .alpha(1.0f)
                    .setListener(null);


            Animation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(500); //You can manage the time of the blink with this parameter
            anim.setStartOffset(20);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(Animation.INFINITE);
            fetcht.startAnimation(anim);
            if (!b.contentEquals("")) {
                if (pos.contentEquals(""))
                    pos = ico;
                if (gen.contentEquals(""))
                    gen = "N/A";

                ImageLoader imageLoader = ImageLoader.getInstance();
                DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(false)
                        .cacheOnDisk(true).resetViewBeforeLoading(true)
                        .build();


                imageLoader.displayImage(imgurlp + pos, poster, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        //Picasso.with(getActivity()).load(Uri.parse(imgurlp+ico)).into(icon);


                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                    }
                });


                imageLoader = ImageLoader.getInstance();
                 options = new DisplayImageOptions.Builder().cacheInMemory(true)
                        .cacheOnDisk(true).resetViewBeforeLoading(true)
                        .build();


                imageLoader.displayImage(imgurl + ico, icon, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                    }
                });


                try {
                    if(des.contentEquals("null"))
                    {
                        des = "N/A" ;
                    }
                    if(gen.contentEquals("null"))
                    {
                        gen = "N/A" ;
                    }
                    title.setText(""+tit);
                    tit=tit.replaceAll(":", "");
                    tit=tit.replaceAll("'","") ;
                    tit=tit.replaceAll(",","") ;
                    tit=tit.replaceAll("\\.","") ;
                    //Toast.makeText(getActivity(),tit,Toast.LENGTH_SHORT).show();
                    release.setText(""+rel +" ( Initial Release )");
                    desc.setText(des);
                    descc.setText(" Description");
                    genre.setText(""+ gen);
                    watch.setText(" Watch Now");

                    try {Cursor c = null ;
                        Cursor cc = getActivity().getContentResolver().query(WatchedDB.Contenturi, null, "id = " + URL, null, null);
                        if (!(cc.moveToFirst()) || cc.getCount() == 0) {
                              c = getActivity().managedQuery(AlgoDB.Contenturi, null, "id = " + URL, null, null);
                            if (!(c.moveToFirst()) || c.getCount() == 0) {
                                //cursor is empty
                                ContentValues values = new ContentValues();
                                values.put(AlgoDB.algo.ID, URL);
                                values.put(AlgoDB.algo.TITLE,tit)  ;
                                values.put(AlgoDB.algo.COUNT, 1);
                                values.put(AlgoDB.algo.IURL, imgurl + ico);
                                try {
                                    getActivity().getContentResolver().insert(AlgoDB.Contenturi,
                                            values);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                ContentValues values = new ContentValues();

                                values.put(AlgoDB.algo.COUNT, c.getInt(c.getColumnIndex(AlgoDB.algo.COUNT)) + 2);
                                getActivity().getContentResolver().update(AlgoDB.Contenturi, values, "id = " + URL, null);
                            }

                        }

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }



                    //
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String release_date = rel;

                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DATE, -7);
                    String today = sdf.format(cal.getTime());

                    //Log.d("AAAA", release_date + "  "+ today) ;

                    Date date1 = sdf.parse(release_date);
                    Date date2 = sdf.parse(today);

                    if (date1.compareTo(date2) > 0) {

                        watchNow.setEnabled(false);
                        down.setEnabled(false) ;
                        rele =0 ;

                    }

                    dialog.dismiss();
                    Log.i("TAGG","V"+videourl) ;
                    if(videourl.contentEquals(""))
                    {   Log.i("TAGG","NAHI") ;
                        new Reza().execute() ;
                    }else
                    {   Log.i("TAGG","HA") ;
                        new checkurl().execute(videourl) ;
                    }






                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }

    }

    private BaseAdapter m2Adapter = new BaseAdapter() {

        @Override
        public int getCount() {
            return cast.size();
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
            View retval = null;
            try {
                retval = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.hroww, null);
                HashMap<String, String> list = new HashMap<String, String>();
                list = cast.get(position);

                ImageView img = (ImageView) retval.findViewById(R.id.hrow);
                TextView tv = (TextView )retval.findViewById(R.id.htext) ;
                tv.setText(list.get(NAME));
                if(list.get(CPIC).contentEquals("null")){

                    img.setImageResource(R.drawable.npii);
                }else {
                    ImageLoader imageLoader = ImageLoader.getInstance();
                    DisplayImageOptions options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY).cacheInMemory(true)
                            .cacheOnDisk(true).resetViewBeforeLoading(true)
                            .build();


                    imageLoader.displayImage(imgurl + list.get(CPIC), img, options) ;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return retval;
        }

    };
    private BaseAdapter mAdapter = new BaseAdapter() {

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
            View retval = null;
            try {
                retval = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.hrowp, null);
                HashMap<String, String> list = new HashMap<String, String>();
                list = dataObject.get(position);

                ImageView img = (ImageView) retval.findViewById(R.id.hrow);

                final ProgressBar spinner = (ProgressBar) retval.findViewById(R.id.loading);


                ImageLoader imageLoader = ImageLoader.getInstance();
                DisplayImageOptions options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY).cacheInMemory(true)
                        .cacheOnDisk(true).resetViewBeforeLoading(true)
                        .build();


                imageLoader.displayImage(MainActivity.baseurl + list.get(IMAGE), img,options, new SimpleImageLoadingListener() {
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

            } catch (Exception e) {
                e.printStackTrace();
            }

            return retval;
        }

    };
    private FragmentActivity context;

    private class trailer extends AsyncTask<String, Void, String> {

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

                JSONArray jsonArray = reader.optJSONArray("results");

                JSONObject jsonObject = jsonArray.getJSONObject(0);
                key = jsonObject.optString("key");

                return key;
            } catch (Exception e) {
                Log.e("Get Url", "Error in downloading: " + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String b) {
            trailer.setEnabled(true);
            if(key.contentEquals(""))
            {
                trailer.setEnabled(false);
            }
            // Toast.makeText(getBaseContext(), b, Toast.LENGTH_SHORT).show();
        }
    }

    private class GetCAST extends AsyncTask<String, Void, String> {

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
                Log.i("a", "yes");

                JSONArray jsonArray = reader.optJSONArray("cast");
                for (int i = 0; i < jsonArray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    map.put(NAME, jsonObject.optString("name").toString());
                    map.put(CID, jsonObject.optString("id").toString());
                    map.put(CPIC, jsonObject.optString("profile_path")
                            .toString());
                    // Toast.makeText(getBaseContext(),
                    // jsonObject.optString("name").toString(),
                    // Toast.LENGTH_SHORT).show() ;

                    cast.add(map);
                }
                return result;
            } catch (Exception e) {
                Log.e("Get Url", "Error in downloading: " + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String b) {
            try {
                c.setAdapter(m2Adapter);
                cas.setText(" Cast");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private class Similar extends AsyncTask<String, Void, String> {

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
                Log.i("a", "yes");

                JSONArray jsonArray = reader.optJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put(IMAGE, jsonObject.optString("poster_path")
                           );
                    map.put(TITLE, jsonObject.optString("title"));
                    map.put(ID, jsonObject.optString("id"));
                    dataObject.add(map);

                }

            } catch (Exception e) {
                Log.e("Get Url", "Error in downloading: " + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String b) {
            sim.setText(" Similar Movies");
            try {
                Cursor c = getActivity().getContentResolver().query(WatchedDB.Contenturi, null, "id = " + URL, null, null);
                if (c.getCount() == 0)
                    seen.setEnabled(true);

                c.close();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            try {
                similar.setAdapter(mAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

	/*private class GetGuestID extends AsyncTask<String, Void, String> {

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
				guestsessionid = reader.optString("guest_session_id");

				return guestsessionid;
			} catch (Exception e) {
				Log.e("Get Url", "Error in downloading: " + e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(String b) {

			Toast.makeText(getActivity(), b, Toast.LENGTH_SHORT).show();
		}
	}*/

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
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        super.onCreateContextMenu(menu, v, menuInfo);
        try {
            if (v.getId() == R.id.downb) {

                MenuInflater inflater = getActivity().getMenuInflater();
                inflater.inflate(R.menu.seed, menu);

            }
            if(v.getId()==R.id.usingseedr)
            {
                MenuInflater inflater = getActivity().getMenuInflater();
                inflater.inflate(R.menu.seed, menu);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.aseed :
                try {
                    Uri bmpUri = getLocalBitmapUri(poster);
                    if (bmpUri != null) {
                        // Construct a ShareIntent with link to image
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                        shareIntent.setType("image/*");

                        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,title.getText().toString()+"\n\n"+ videourl);
                        startActivity(Intent.createChooser(shareIntent, "Share using"));
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                break ;
            case R.id.seedr :
                Uri uri = Uri.parse(videourl);
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);

                break ;
        }
       /* String watch = null;
        int free=0 , autoseedr=0,seedr=0,vp=0 ;
        try {
            switch (item.getItemId()) {


                case R.id.aseed:

                    watch = "https://www.seedr.cc/" ;
                    autoseedr=1 ;
                    break ;
                case R.id.seedr:
                    watch = "https://www.seedr.cc/" ;
                    seedr= 1;
                    break ;


            }


                 if (autoseedr == 1) {
                    Bundle b = new Bundle();
                    b.putString("watch", watch);
                    b.putString("mlink", mlink);
                    Toast.makeText(getActivity(), "MAGNETIC LINK COPIED TO CLIPBOARD", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getActivity(), Web.class);
                    i.putExtras(b);
                    startActivity(i);
                } else {
                    Bundle b = new Bundle();
                    b.putString("watch", watch);
                    b.putString("mlink", mlink);
                    Toast.makeText(getActivity(), "MAGNETIC LINK COPIED TO CLIPBOARD", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getActivity(), WebJ.class);
                    i.putExtras(b);
                    startActivity(i);

            }






        } catch (Exception e) {
            e.printStackTrace();
        }
        */
        return true;
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
 /* public class Title extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Connect to the web site
                Document document = Jsoup.connect(url).get();
                // Get the html document title
                Elements links = document.select("a[href]");
                Elements ff = document.select("font[class]") ;
                int idx=0 ;
                for (Element link : ff) {

                    // System.out.println("link : " + link.attr("href"));
                    //
                    //String size = ff.attr("class") ;
                    //Log.d("size",size) ;
                    //Log.d("ss",ff.text()) ;
                    String txt = link.text() ;
                    Log.i("Value",txt) ;
                    if(txt.contains("MiB"))
                    {   mlink = mlink+txt ;
                        Log.i("Value",mlink) ;
                        break ;
                    }
                    else if(txt.contains("GiB"))
                    {
                          int idd = txt.indexOf("Size") ;
                          String size = Character.toString(txt.charAt(idd+5))+Character.toString(txt.charAt(idd+6))+Character.toString(txt.charAt(idd+7));
                          Log.i("Size",size) ;
                          float num = Float.valueOf(size) ;
                          if(num<2.0)
                        {
                            mlink = mlink+txt ;
                            break ;
                        }
                    }
                    idx++ ;
                   //if(magnet.contains("magnet:?")) {
                    //    title = title + link.attr("href") +"\n\n";

                   // }
                }
                int idy=0;
                for(Element link:links) {
                    String magnet = link.attr("href");
                    if (magnet.contains("magnet:?") ) {
                        Log.i("Value",magnet) ;
                        if(idy==idx)
                        {mlink = link.attr("href");break;}
                        else
                            idy++ ;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set title into TextView
            // tv.setText(title);

            if(getActivity()!=null){
try {ClipboardManager clipboard=null ;
    try {
        clipboard = (ClipboardManager) getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
    }catch (Exception e)
    {
        e.printStackTrace();
    }
    ClipData clip = ClipData.newPlainText("label", mlink);
    try {
        clipboard.setPrimaryClip(clip);
    }catch (Exception e)
    {
        e.printStackTrace();
    }
    if (!(rele == 0 && mlink.contentEquals(""))) {
        seedr.setEnabled(true);
    }
}catch (Exception e)
{
    e.printStackTrace();
}
            //Toast.makeText(getActivity(),"MAGNETIC LINK COPIED TO CLIPBOARD",Toast.LENGTH_LONG).show();

        }}
    }
*/
   public class Index extends AsyncTask<Void, Void, Void> {
        String[] titl = new String[5] ;
        int i = 0;


        @Override
        protected Void doInBackground(Void... params) {

              Document doc=null,document ;
            Elements links ;
            String jitu="";
            try {
                String googleurl = "http://www.google.com/search?q=intitle.indexof "+"%22"+tit.replaceAll(" ","%20")+" "+Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3)) +" " +"%22"+ "&num=15" ;
                Log.i("gu",googleurl) ;
                 try {
                     if(googleurl.contains("ipv4"))
                     { return null ;
                     }else {
                         doc = Jsoup
                                 .connect(googleurl)

                                 .get();
                     }


                 }catch(Exception e)
                 {e.printStackTrace();}
                  if(doc!=null) {
                      links = doc.select("a[href]");
                      for (Element link : links) {
                          titl[i] = link.attr("href");
                          if (titl[i].contains("http://") && (!titl[i].contains("webcache"))&&(!titl[i].contains("evonetbd"))) {
                              Log.i("JK", titl[i]);
                              i++;
                          }if (i == 4) {
                             break;
                          }

                      }
                  }
            } catch (Exception e) {
                e.printStackTrace();
            }
                // Connect to the web site
               //String ur = "http://dl2.mihanpix.com/Film/1900-2016/" ;
                 String mtitle =  tit.replaceAll(":","") ;
                 Log.i("FURL",mtitle) ;

               try {
                   if(titl[0]==null)
                       return null ;
                   if(titl[0].contains("ipv4"))
                   {
                       return null ;
                   }
                   document = Jsoup.connect(titl[0]).get();
                   // Get the html document title
                   links = document.select("a[href]");

                   Log.i("FURL", "hello");


                   for (Element link : links) {
                       String magnet = link.attr("href");
                       if ((magnet.startsWith(mtitle.replaceAll(" ", ".")) || magnet.startsWith(mtitle.replaceAll(" ", "%20"))) && (!magnet.contains("Dubbed")) && magnet.contains("mkv") && (!magnet.contains("sample"))) {
                           if ((magnet.startsWith(mtitle.replaceAll(" ", ".")) || magnet.startsWith(mtitle.replaceAll(" ", "%20"))) && (!magnet.contains("Dubbed")) && magnet.contains("mkv") && (!magnet.contains("sample"))) {
                               Log.i("FURL", magnet);
                               jitu = magnet;

                           }
                       }
                   }
                   if (!jitu.equals("")) {
                       videourl = videourl + titl[0] + jitu;
                       return null;

                   }
               } catch (IOException e) {
                   e.printStackTrace();
               }

            try{
                document = Jsoup.connect(titl[1]).get();
                // Get the html document title
                links = document.select("a[href]");

                Log.i("FURL","hello") ;

                 jitu="" ;
                for (Element link : links) {
                    String magnet = link.attr("href");
                    if ((magnet.startsWith(mtitle.replaceAll(" ","."))||magnet.startsWith(mtitle.replaceAll(" ","%20")))&&(!magnet.contains("Dubbed"))&&magnet.contains("mkv")&&(!magnet.contains("sample"))) {
                        Log.i("FURL", magnet);
                        jitu = magnet ;

                    }
                }
                if(!jitu.equals(""))
                {
                    videourl = titl[1]+jitu ;
                    return  null ;

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try{
                document = Jsoup.connect(titl[2]).get();
                // Get the html document title
                links = document.select("a[href]");

                Log.i("FURL","hello") ;

                jitu="" ;
                for (Element link : links) {
                    String magnet = link.attr("href");
                    if ((magnet.startsWith(mtitle.replaceAll(" ","."))||magnet.startsWith(mtitle.replaceAll(" ","%20")))&&(!magnet.contains("Dubbed"))&&magnet.contains("mkv")&&(!magnet.contains("sample"))) {
                        Log.i("FURL", magnet);
                        jitu = magnet ;

                    }
                }
                if(!jitu.equals(""))
                {
                    videourl =titl[2]+jitu ;
                    return  null ;

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            try{
                document = Jsoup.connect(titl[3]).get();
                // Get the html document title
                links = document.select("a[href]");

                Log.i("FURL","hello") ;

                jitu="" ;
                for (Element link : links) {
                    String magnet = link.attr("href");
                    if ((magnet.startsWith(mtitle.replaceAll(" ","."))||magnet.startsWith(mtitle.replaceAll(" ","%20")))&&(!magnet.contains("Dubbed"))&&magnet.contains("mkv")&&(!magnet.contains("sample"))) {
                        Log.i("FURL", magnet);
                        jitu = magnet ;

                    }
                }
                if(!jitu.equals(""))
                {
                    videourl =titl[3]+jitu ;
                    return  null ;

                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                Log.i("video", videourl);
                if (videourl.contentEquals("")||videourl==null) {
                  //  Toast.makeText(getActivity(), "SORRY NOT AVAILABLE", Toast.LENGTH_LONG).show();
                 // new Add().execute() ;
                    fetch.animate()
                            .translationY(0)
                            .alpha(0.0f)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    fetch.setVisibility(View.GONE);
                                }
                            });

                } else {
                    watchNow.setEnabled(true);
                    downb.setVisibility(View.VISIBLE);
                    downb.setAnimation(mAnimation);
                    down.setEnabled(true);
                    fetch.animate()
                            .translationY(0)
                            .alpha(0.0f)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    fetch.setVisibility(View.GONE);
                                }
                            });
                   // new Title().execute() ;
                   // Toast.makeText(getActivity(), videourl, Toast.LENGTH_LONG).show();
                }
            }catch (Exception e)
            {e.printStackTrace();}
        }
    }

    public class Add extends AsyncTask<Void, Void, Void> {

        String dh = "http://download.ihub.live/movies/Hindi/Archive/" ;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Connect to the web site
                String F = Character.toString(tit.charAt(0)) ;
                dh = dh+F+"/"+tit.replaceAll(" ","%20")+"%20"+"%28"+Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3))+"%29/" ;
                Log.i("dh", dh) ;
                Document document = Jsoup.connect(dh).get();
                // Get the html document title
                Elements links = document.select("a[href]");

                for (Element link :links) {

                    String magnet = link.attr("href");
                    if(magnet.contains("mkv")||magnet.contains("mp4"))
                    {

                                videourl = dh + magnet;
                        Log.i("dh",videourl) ;
                        break ;
                    }


                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                Log.i("video", videourl);
                if (videourl.contentEquals("")||videourl==null) {
                    //  Toast.makeText(getActivity(), "SORRY NOT AVAILABLE", Toast.LENGTH_LONG).show();
                    new Add1().execute() ;

                } else {
                    watchNow.setEnabled(true);
                    down.setEnabled(true) ;
                    downb.setVisibility(View.VISIBLE);
                    downb.setAnimation(mAnimation);
                   // new Title().execute() ;
                    //Toast.makeText(getActivity(), videourl, Toast.LENGTH_LONG).show();
                }
            }catch (Exception e)
            {e.printStackTrace();}
        }
    }
    public class Add1 extends AsyncTask<Void, Void, Void> {

        String dh = "http://download.ihub.live/movies/English/Archive/" ;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Connect to the web site
                String F = Character.toString(tit.charAt(0)) ;
                dh = dh+F+"/"+tit.replaceAll(" ","%20")+"%20"+"%28"+Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3))+"%29/" ;
                Log.i("dh", dh) ;
                Document document = Jsoup.connect(dh).get();
                // Get the html document title
                Elements links = document.select("a[href]");

                for (Element link :links) {

                    String magnet = link.attr("href");
                    if(magnet.contains("mkv")||magnet.contains("mp4"))
                    {

                        videourl = dh + magnet;
                        Log.i("dh",videourl) ;
                        break ;
                    }


                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                Log.i("video", videourl);
                if (videourl.contentEquals("")||videourl==null) {
                    //  Toast.makeText(getActivity(), "SORRY NOT AVAILABLE", Toast.LENGTH_LONG).show();
                    //new Add2().execute() ;
                    //new Title().execute() ;

                } else {
                    watchNow.setEnabled(true);
                    downb.setVisibility(View.VISIBLE);
                    downb.setAnimation(mAnimation);
                    down.setEnabled(true) ;
                    //Toast.makeText(getActivity(), videourl, Toast.LENGTH_LONG).show();
                    //new Title().execute() ;
                }
            }catch (Exception e)
            {e.printStackTrace();}
        }
    }

    public class Add2 extends AsyncTask<Void, Void, Void> {

        String dh ;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tit.replaceAll(":","");
            tit.replaceAll("'", "") ;

        }

        @Override
        protected Void doInBackground(Void... params) {


            try {
                dh="http://download.ihub.live/movies/English/New%20Releases/" ;
                dh = dh+tit.replaceAll(" ","%20")+"%20"+"%28"+Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3))+"%29/"+tit.replaceAll(" ","%20")+"%20"+"%28"+Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3))+"%29.mkv" ;
                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection con =  (HttpURLConnection) new URL(dh).openConnection();
                con.setRequestMethod("HEAD");
                System.out.println(con.getResponseCode());
                if(con.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    videourl = dh ;
                    return null ;
                }
            }
            catch (Exception e) {
                e.printStackTrace();

            }
            try {
                dh="http://download.ihub.live/movies/Hindi/New%20Releases/" ;
                dh = dh+tit.replaceAll(" ","%20")+"%20"+"%28"+Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3))+"%29/"+tit.replaceAll(" ","%20")+"%20"+"%28"+Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3))+"%29.mkv" ;
                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection con =  (HttpURLConnection) new URL(dh).openConnection();
                con.setRequestMethod("HEAD");
                System.out.println(con.getResponseCode());
                if(con.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    videourl = dh ;
                    return null ;
                }
            }
            catch (Exception e) {
                e.printStackTrace();

            }
            try {
                dh="http://download.ihub.live/movies/English/Archive/" ;
                String F = Character.toString(tit.charAt(0)) ;
                dh = dh+F+"/"+tit.replaceAll(" ","%20")+"%20"+"%28"+Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3))+"%29/"+tit.replaceAll(" ","%20")+"%20"+"%28"+Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3))+"%29.mkv" ;

                //dh = dh+tit.replaceAll(" ","%20")+"%20"+"%28"+Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3))+"%29/"+tit.replaceAll(" ","%20")+"%20"+"%28"+Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3))+"%29.mkv" ;
                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection con =  (HttpURLConnection) new URL(dh).openConnection();
                con.setRequestMethod("HEAD");
                System.out.println(con.getResponseCode());
                if(con.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    videourl = dh ;
                    return null ;
                }
            }
            catch (Exception e) {
                e.printStackTrace();

            }
            try {
                dh="http://download.ihub.live/movies/Hindi/Archive/" ;
                String F = Character.toString(tit.charAt(0)) ;
                dh = dh+F+"/"+tit.replaceAll(" ","%20")+"%20"+"%28"+Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3))+"%29/"+tit.replaceAll(" ","%20")+"%20"+"%28"+Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3))+"%29.mkv" ;
               // dh = dh+tit.replaceAll(" ","%20")+"%20"+"%28"+Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3))+"%29/"+tit.replaceAll(" ","%20")+"%20"+"%28"+Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3))+"%29.mkv" ;
                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection con =  (HttpURLConnection) new URL(dh).openConnection();
                con.setRequestMethod("HEAD");
                System.out.println(con.getResponseCode());
                if(con.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    videourl = dh ;
                    return null ;
                }
            }
            catch (Exception e) {
                e.printStackTrace();

            }

            try {
                dh="http://download.ihub.live/movies/English/New%20Releases/" ;
                dh = dh+tit.replaceAll(" ","%20")+"%20"+"%28"+Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3))+"%29/"+tit.replaceAll(" ","%20")+"%20"+"%28"+Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3))+"%29.mp4" ;
                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection con =  (HttpURLConnection) new URL(dh).openConnection();
                con.setRequestMethod("HEAD");
                System.out.println(con.getResponseCode());
                if(con.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    videourl = dh ;
                    return null ;
                }
            }
            catch (Exception e) {
                e.printStackTrace();

            }
            try {
                dh="http://download.ihub.live/movies/Hindi/New%20Releases/" ;
                dh = dh+tit.replaceAll(" ","%20")+"%20"+"%28"+Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3))+"%29/"+tit.replaceAll(" ","%20")+"%20"+"%28"+Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3))+"%29.mp4" ;
                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection con =  (HttpURLConnection) new URL(dh).openConnection();
                con.setRequestMethod("HEAD");
                System.out.println(con.getResponseCode());
                if(con.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    videourl = dh ;
                    return null ;
                }
            }
            catch (Exception e) {
                e.printStackTrace();

            }
            try {
                dh="http://download.ihub.live/movies/English/Archive/" ;
                String F = Character.toString(tit.charAt(0)) ;
                dh = dh+F+"/"+tit.replaceAll(" ","%20")+"%20"+"%28"+Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3))+"%29/"+tit.replaceAll(" ","%20")+"%20"+"%28"+Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3))+"%29.mp4" ;

                //dh = dh+tit.replaceAll(" ","%20")+"%20"+"%28"+Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3))+"%29/"+tit.replaceAll(" ","%20")+"%20"+"%28"+Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3))+"%29.mkv" ;
                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection con =  (HttpURLConnection) new URL(dh).openConnection();
                con.setRequestMethod("HEAD");
                System.out.println(con.getResponseCode());
                if(con.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    videourl = dh ;
                    return null ;
                }
            }
            catch (Exception e) {
                e.printStackTrace();

            }
            try {
                dh="http://download.ihub.live/movies/Hindi/Archive/" ;
                String F = Character.toString(tit.charAt(0)) ;
                dh = dh+F+"/"+tit.replaceAll(" ","%20")+"%20"+"%28"+Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3))+"%29/"+tit.replaceAll(" ","%20")+"%20"+"%28"+Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3))+"%29.mp4" ;
                // dh = dh+tit.replaceAll(" ","%20")+"%20"+"%28"+Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3))+"%29/"+tit.replaceAll(" ","%20")+"%20"+"%28"+Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3))+"%29.mkv" ;
                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection con =  (HttpURLConnection) new URL(dh).openConnection();
                con.setRequestMethod("HEAD");
                System.out.println(con.getResponseCode());
                if(con.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    videourl = dh ;
                    return null ;
                }
            }
            catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                Log.i("video", videourl);
                if (videourl.contentEquals("")||videourl==null) {
                    //  Toast.makeText(getActivity(), "SORRY NOT AVAILABLE", Toast.LENGTH_LONG).show();
                    //new Add3().execute() ;
                    try {
                        new Index().execute();
                      //  new Title().execute() ;
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                } else {
                    watchNow.setEnabled(true);
                    down.setEnabled(true) ;
                    downb.setVisibility(View.VISIBLE);
                    downb.setAnimation(mAnimation);
                    //Toast.makeText(getActivity(), videourl, Toast.LENGTH_LONG).show();

                }
            }catch (Exception e)
            {e.printStackTrace();}
        }
    }

    public class Indexof extends AsyncTask<Void,Void,Void>
    {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tit.replaceAll(":","");
            tit.replaceAll("'","") ;

        }

        @Override
        protected Void doInBackground(Void... params) {
            String indexofurl = "http://indexofmovies.me/full-movie-download/"+tit.replaceAll(" ","-")+"-"+Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3)) ;
          //  String l ="https://indexofmovies.me" ;
            Log.i("indexofurl", indexofurl) ;
            Document document ;
            try {
                document = Jsoup.connect(indexofurl).get();

                Elements links = document.select("tr");
                String magn="" ;
                String iurl ="" ;

                Element li = links.get(1) ;
               // Log.i("indexofurl", links.toString())
                magn = li.attr("id");
                    //Toast.makeText(getActivity(),magnet,Toast.LENGTH_SHORT).show();
                Log.i("indexofurl", magn) ;
                iurl = "http://indexofmovies.me/go/"+magn;

                Log.i("indexofurl",magn+"h") ;

               // if(!magnet.contentEquals("")) {
                    Log.i("indexofurl",iurl) ;
                    document = Jsoup.connect(iurl).get();

                    links = document.select("a");
                    for (Element link : links) {
                        magn = link.attr("href");
                        //Toast.makeText(getActivity(),magnet,Toast.LENGTH_SHORT).show();
                        //Log.i("indexofurl", magnet) ;
                       // break ;
                    }
                    Log.i("indexofurl",magn) ;
                    videourl = magn ;

                URL url = new URL(videourl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int code = connection.getResponseCode();
                if(code==200)
                {

                }else
                {
                    videourl="" ;
                }



            }catch (Exception e)
            {
               e.printStackTrace();
            }
            return null ;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                Log.i("indexofurll", videourl);

                if (videourl.contentEquals("")||videourl==null) {
                    //  Toast.makeText(getActivity(), "SORRY NOT AVAILABLE", Toast.LENGTH_LONG).show();
                    //new Add3().execute() ;
                      //new Index().execute() ;
                    fetch.animate()
                            .translationY(0)
                            .alpha(0.0f)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    fetch.setVisibility(View.GONE);
                                }
                            });

                } else {
                    fetch.animate()
                            .translationY(0)
                            .alpha(0.0f)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    fetch.setVisibility(View.GONE);
                                }
                            });
                    watchNow.setEnabled(true);
                    down.setEnabled(true) ;
                    downb.setVisibility(View.VISIBLE);
                    downb.setAnimation(mAnimation);
                    //Toast.makeText(getActivity(), videourl, Toast.LENGTH_LONG).show();

                }

            }catch (Exception e)
            {e.printStackTrace();}
        }

    }

    public class Reza extends AsyncTask<Void,Void,Void>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            tit.replaceAll(":","");
            tit.replaceAll("'", "") ;
        }

        @Override
        protected Void doInBackground(Void... params) {
            String rezaurl = "http://dl.my-film.in/reza/film/" ;
            String rezatitle = tit.replaceAll(" ",".")+"."+Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3)) ;
            Log.i("reza", rezatitle) ;
            Document document ;
            try {
                document = Jsoup.connect(rezaurl).get();

                Elements links = document.select("a");
                String rezau="" ;
                for(Element link :links)
                {
                    String find = link.attr("href") ;
                    if(!find.contentEquals("")&&find.contains(rezatitle))
                    {
                      rezau = rezaurl+find ;
                    }
                }
                Log.i("reza",rezau) ;
                videourl=rezau ;

            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //fetch.startAnimation(slideup);
            //fetch.setVisibility(View.INVISIBLE);

            try {

                Log.i("reza", videourl);

                if (videourl.contentEquals("") || videourl == null) {
                    //  Toast.makeText(getActivity(), "SORRY NOT AVAILABLE", Toast.LENGTH_LONG).show();
                    new s1().execute() ;


                } else {

                    id.setValue(videourl) ;
                    fetch.animate()
                            .translationY(0)
                            .alpha(0.0f)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    fetch.setVisibility(View.GONE);
                                }
                            });
                    watchNow.setEnabled(true);
                    down.setEnabled(true);
                    downb.setVisibility(View.VISIBLE);
                    downb.setAnimation(mAnimation);
                    //Toast.makeText(getActivity(), videourl, Toast.LENGTH_LONG).show();

                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    public class s1 extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute() {
        super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params) {
            String surl = "http://dl.my-film.in/s1/Movie/"+tit.replaceAll(" ",".")+"."+Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3)) ;
            Log.i("surl",surl) ;
            Document document ;
            try {
                document = Jsoup.connect(surl).get();
                Elements links = document.select("a");
                String fin="" ;
                for(Element link :links)
                {
                    String find = link.attr("href") ;
                    if(!find.contentEquals("")&&(find.contains("mkv")||find.contains("mp4")))
                    {
                        fin = surl+"/"+find ;

                    }
                }
                videourl=fin ;
                Log.i("surl",fin) ;
            }catch (Exception e)
            {

            }
                return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                Log.i("surl", videourl);

                if (videourl.contentEquals("") || videourl == null) {
                    //  Toast.makeText(getActivity(), "SORRY NOT AVAILABLE", Toast.LENGTH_LONG).show();
                   // new s1().execute() ;
                  // new Indexof().execute() ;
                     new filmbaroon().execute() ;

                } else {
                    id.setValue(videourl) ;
                    fetch.animate()
                            .translationY(0)
                            .alpha(0.0f)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    fetch.setVisibility(View.GONE);
                                }
                            });
                    watchNow.setEnabled(true);
                    down.setEnabled(true);
                    downb.setVisibility(View.VISIBLE);
                    downb.setAnimation(mAnimation);
                    //Toast.makeText(getActivity(), videourl, Toast.LENGTH_LONG).show();

                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }
    public class filmbaroon extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params) {
            String surl = Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3)) ;
            String filmurl = "http://dl.filmbaroon.com/" ;
            Log.i("filmurl",filmurl) ;
            Document document ;
            try {
                document = Jsoup.connect(filmurl).get();
                Elements links = document.select("a");
                String fin="" ;
                for(Element link :links)
                {
                    String find = link.attr("href") ;
                    if(find.contains(tit.replaceAll(" ", "."))&&find.contains(surl)&&!find.toLowerCase().contains("trailer")&&!find.toLowerCase().contains("dubbed"))
                    {
                        fin = filmurl+find  ;
                    }
                }
                videourl=fin ;
                Log.i("filmurl",fin) ;
            }catch (Exception e)
            {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                Log.i("filmurl", videourl);

                if (videourl.contentEquals("") || videourl == null) {
                    //  Toast.makeText(getActivity(), "SORRY NOT AVAILABLE", Toast.LENGTH_LONG).show();
                    // new s1().execute() ;
                    new dl3enter().execute() ;

                } else {
                    id.setValue(videourl) ;
                    fetch.animate()
                            .translationY(0)
                            .alpha(0.0f)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    fetch.setVisibility(View.GONE);
                                }
                            });
                    watchNow.setEnabled(true);
                    down.setEnabled(true);
                    downb.setVisibility(View.VISIBLE);
                    downb.setAnimation(mAnimation);
                    //Toast.makeText(getActivity(), videourl, Toast.LENGTH_LONG).show();

                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }

    public class dl3enter extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params) {
            //String surl = "http://dl.my-film.in/s1/Movie/"+tit.replaceAll(" ",".")+"."+Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3)) ;
            String durl = "http://mc1.dl3enter.in/files/d4/serial1/Movie/"+Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3))+"/" ;

            try{
                durl = durl + tit.replaceAll(" ","%20")+"/" ;
                Log.i("filmurl",durl) ;
                Document document ;

                Log.i("filmurl",durl) ;
                document = Jsoup.connect(durl).get();
                //Log.i("filmurl","f"+document.toString()) ;
                Elements links = document.select("a");
                String fin="" ;
                for(Element link :links)
                {
                    String find = link.attr("href") ;
                    Log.i("filmurl",find) ;
                    if(find.contains(tit.replaceAll(" ", ".")))
                    {
                        fin = durl+find  ;
                        Log.i("filmurl",find) ;
                        Log.i("filmurl",fin) ;
                    }
                }
                videourl=fin ;
                Log.i("filmurl",fin) ;
            }catch (Exception e)
            {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                Log.i("filmurl", videourl);

                if (videourl.contentEquals("") || videourl == null) {
                    //  Toast.makeText(getActivity(), "SORRY NOT AVAILABLE", Toast.LENGTH_LONG).show();
                    // new s1().execute() ;
                    new hasan().execute() ;


                } else {
                    id.setValue(videourl) ;
                    fetch.animate()
                            .translationY(0)
                            .alpha(0.0f)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    fetch.setVisibility(View.GONE);
                                }
                            });
                    watchNow.setEnabled(true);
                    down.setEnabled(true);
                    downb.setVisibility(View.VISIBLE);
                    downb.setAnimation(mAnimation);
                    //Toast.makeText(getActivity(), videourl, Toast.LENGTH_LONG).show();

                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }
    public class hasan extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params) {
            //String surl = "http://dl.my-film.in/s1/Movie/"+tit.replaceAll(" ",".")+"."+Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3)) ;
            String durl = "http://dl.my-film.in/hasan/film/" ;

            try{
                Log.i("filmurl",durl) ;
                Document document ;

                Log.i("filmurl",durl) ;
                document = Jsoup.connect(durl).get();
                //Log.i("filmurl","f"+document.toString()) ;
                Elements links = document.select("a");
                String fin="" ;
                for(Element link :links)
                {
                    String find = link.attr("href") ;
                    Log.i("filmurl",find) ;
                    if(find.contains(tit.replaceAll(" ",".")+"."+Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3)) ))
                    {
                        fin = durl+find  ;
                        Log.i("filmurl",find) ;

                    }
                }
                videourl=fin ;
                Log.i("filmurl",fin) ;
            }catch (Exception e)
            {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                Log.i("filmurl", videourl);

                if (videourl.contentEquals("") || videourl == null) {
                    //  Toast.makeText(getActivity(), "SORRY NOT AVAILABLE", Toast.LENGTH_LONG).show();
                    // new s1().execute() ;
                    //new Indexof().execute() ;
                   // new openload().execute() ;
                   // new openloadmovie().execute() ;

                    fetch.animate()
                            .translationY(0)
                            .alpha(0.0f)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    fetch.setVisibility(View.GONE);
                                }
                            });


                } else {
                    id.setValue(videourl) ;
                    fetch.animate()
                            .translationY(0)
                            .alpha(0.0f)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    fetch.setVisibility(View.GONE);
                                }
                            });

                    watchNow.setEnabled(true);
                    down.setEnabled(true);
                    downb.setVisibility(View.VISIBLE);
                    downb.setAnimation(mAnimation);
                    //Toast.makeText(getActivity(), videourl, Toast.LENGTH_LONG).show();

                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }

    public class openload extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String searchurl = "https://www.watch2movie.xyz/search_movies?s="+tit.replaceAll(" ","%20") ;
            String mainurl = "https://www.watch2movie.xyz/" ;

            Document document ;
            try {
                Log.i("openurl", searchurl);
                document = Jsoup.connect(searchurl).get();
                //Log.i("filmurl","f"+document.toString()) ;
                Elements links = document.select("a");
                String xyztitle = tit.toLowerCase()+" "+ Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3)) ;
                xyztitle = xyztitle.replaceAll(" ","-");
                Log.i("openurl",xyztitle) ;
                for(Element link :links)
                {
                    String find = link.attr("href") ;

                    if(find.contains(xyztitle))
                    {
                        mainurl = mainurl+find  ;
                        break ;

                    }
                }

                Log.i("openurl",mainurl) ;
                String openloadurl="" ;
                document = Jsoup.connect(mainurl).get();
                links = document.select("a");
                for(Element link :links)
                {
                    String find = link.attr("href") ;

                    if(find.contains("openload.co"))
                    {
                        openloadurl = find ;
                        break ;

                    }
                }
                Log.i("openurl",openloadurl) ;

                String filecode = openloadurl.substring(openloadurl.lastIndexOf('/') + 1);
                Log.i("openurl",filecode) ;
                if(!filecode.contentEquals(""))
                {
                    String tokenurl = "https://api.openload.co/1/file/dlticket?file=" + filecode + "&login=52de1f479109e3ca&key=FqM8YiWy";

                    URL url = new URL(tokenurl);
                    HttpURLConnection urlConnection = (HttpURLConnection) url
                            .openConnection();

                    InputStream is = new BufferedInputStream(
                            urlConnection.getInputStream());

                    String result = convertStreamToString(is);

                    JSONObject reader = new JSONObject(result);
                    JSONObject tic = reader.getJSONObject("result");

                    String ticket = tic.optString("ticket");

                    Log.i("openurl", ticket);

                    String fetchlink = "https://api.openload.co/1/file/dl?file=" + filecode + "&ticket=" + ticket + "";
                    Log.i("openurl", fetchlink);

                    url = new URL(fetchlink);
                    urlConnection = (HttpURLConnection) url
                            .openConnection();

                    is = new BufferedInputStream(
                            urlConnection.getInputStream());

                    result = convertStreamToString(is);

                    reader = new JSONObject(result);

                    JSONObject urlf = reader.getJSONObject("result");

                    String downloadurl = urlf.optString("url");

                    if (!downloadurl.contentEquals(""))
                        videourl = downloadurl;

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

            if (videourl.contentEquals("") || videourl == null) {
                //  Toast.makeText(getActivity(), "SORRY NOT AVAILABLE", Toast.LENGTH_LONG).show();
                // new s1().execute() ;

                fetch.animate()
                        .translationY(0)
                        .alpha(0.0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                fetch.setVisibility(View.GONE);
                            }
                        });

            } else {
                fetch.animate()
                        .translationY(0)
                        .alpha(0.0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                fetch.setVisibility(View.GONE);
                            }
                        });
                watchNow.setEnabled(true);
                down.setEnabled(true);
                downb.setVisibility(View.VISIBLE);
                downb.setAnimation(mAnimation);
                //Toast.makeText(getActivity(), videourl, Toast.LENGTH_LONG).show();

            }
        }
    }

    public class openloadmovie extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String searchurl = "https://openloadmovie.me/movies/" ;

            Log.i("openloadurl","1"+tit) ;
            String xyztitle = tit.toLowerCase()+" "+ Character.toString(rel.charAt(0))+Character.toString(rel.charAt(1))+Character.toString(rel.charAt(2))+Character.toString(rel.charAt(3)) ;
            xyztitle = xyztitle.replaceAll(" ","-");

            searchurl = searchurl+xyztitle ;



            Document document ;
            try {
                Log.i("openloadurl", searchurl);
                document = Jsoup.connect(searchurl).get();
                //Log.i("filmurl","f"+document.toString()) ;
                Elements links = document.select("iframe");

                Log.i("openloadurl",xyztitle) ;
                String openloadurl="" ;
                for(Element link :links)
                {
                    String find = link.attr("data-lazy-src") ;


                        if(find.contains("openload.co"))
                        {
                            openloadurl = find ;
                            break ;

                        }


                }
                Log.i("openloadurl","q"+openloadurl) ;
                String filecode = openloadurl.substring(openloadurl.lastIndexOf("embed/") + 6,openloadurl.lastIndexOf('/') );
                Log.i("openloadurl",filecode) ;


                if(!filecode.contentEquals(""))
                {
                    String tokenurl = "https://api.openload.co/1/file/dlticket?file=" + filecode + "&login=3c371f953f5c9c62&key=K8v9oiQX";

                    URL url = new URL(tokenurl);
                    HttpURLConnection urlConnection = (HttpURLConnection) url
                            .openConnection();

                    InputStream is = new BufferedInputStream(
                            urlConnection.getInputStream());

                    String result = convertStreamToString(is);

                    JSONObject reader = new JSONObject(result);
                    JSONObject tic = reader.getJSONObject("result");

                    String ticket="" ;
                    ticket= tic.optString("ticket");

                    if(!ticket.contentEquals("")) {
                        Log.i("openloadurl", ticket);

                        String fetchlink = "https://api.openload.co/1/file/dl?file=" + filecode + "&ticket=" + ticket + "";
                        Log.i("openloadurl", fetchlink);

                        url = new URL(fetchlink);
                        urlConnection = (HttpURLConnection) url
                                .openConnection();

                        is = new BufferedInputStream(
                                urlConnection.getInputStream());

                        result = convertStreamToString(is);

                        reader = new JSONObject(result);

                        JSONObject urlf = reader.getJSONObject("result");

                        String downloadurl = urlf.optString("url");

                        if (!downloadurl.contentEquals(""))
                            videourl = downloadurl;
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

            if (videourl.contentEquals("") || videourl == null) {
                //  Toast.makeText(getActivity(), "SORRY NOT AVAILABLE", Toast.LENGTH_LONG).show();
                // new s1().execute() ;
                fetcht.setText("NO LINK FOUND");

                fetch.animate()
                        .translationY(0)
                        .alpha(0.0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                fetch.setVisibility(View.GONE);
                            }
                        });
               // Toast.makeText(getActivity(), "No Link Found", Toast.LENGTH_SHORT).show();

            } else {
                fetch.animate()
                        .translationY(0)
                        .alpha(0.0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                fetch.setVisibility(View.GONE);
                            }
                        });

             //   Toast.makeText(getActivity(),"Link Fetched from openloadmovies.me",Toast.LENGTH_SHORT).show();

                watchNow.setEnabled(true);
                down.setEnabled(true);
                downb.setVisibility(View.VISIBLE);
                downb.setAnimation(mAnimation);
                //Toast.makeText(getActivity(), videourl, Toast.LENGTH_LONG).show();

            }
        }
    }

    public static class Post {

        public String link;


    }
    public class checkurl extends  AsyncTask<String, Void, Boolean>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection con =  (HttpURLConnection) new URL(params[0]).openConnection();
                con.setRequestMethod("HEAD");
                System.out.println(con.getResponseCode());
                return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
            }
            catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            boolean bResponse = result;
            if (bResponse==true)
            {
                Log.i("TAGG","VALID") ;
                fetch.animate()
                        .translationY(0)
                        .alpha(0.0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                fetch.setVisibility(View.GONE);
                            }
                        });
                watchNow.setEnabled(true);
                down.setEnabled(true);
                downb.setVisibility(View.VISIBLE);
                downb.setAnimation(mAnimation);
            }
            else
            {
                Log.i("TAGG", "NOVALID") ;
                new Reza().execute() ;
            }
        }
    }
}

