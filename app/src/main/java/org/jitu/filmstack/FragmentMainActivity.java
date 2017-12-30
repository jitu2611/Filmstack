package org.jitu.filmstack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;


/**
 * @author JITU
 */
public class FragmentMainActivity extends FragmentActivity {

    private static final int REQ_CODE_SPEECH_INPUT = 0;
    SearchView searchView;
    DrawerLayout dl;
     ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;
     ArrayAdapter<String> mAdapter;
    public static DatabaseReference mDatabase;

     AdView mAdView;
    Menu m;
    String iddi="0";
    private String BaseUrl = "https://api.themoviedb.org/3/search/movie?query=";
    private String apikey = "&api_key=1b104f628c51adebff0570a1a27ae2af";
    private RelativeLayout mDrawerRelativeLayout;
    ImageView iv;
    public static HashMap<String,String> dlink = new HashMap<>() ;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        try {
            mDrawerToggle.syncState();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        try {
            mDatabase =  FirebaseDatabase.getInstance().getReference();

            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                     dlink.put(ds.getKey(),(String)ds.getValue()) ;
                    }
                    Log.i("TAGG",""+dlink.size()) ;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            } ;

            mDatabase.addValueEventListener(valueEventListener);
            String tkn = FirebaseInstanceId.getInstance().getToken();
            Log.i("Nottt", "Token [" + tkn + "]");
            FirebaseMessaging.getInstance().subscribeToTopic("new");
           // Toast.makeText(FragmentMainActivity.this, "Current token ["+tkn+"]",
             //       Toast.LENGTH_LONG).show();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            setContentView(R.layout.fragmentactivity);
            dl = (DrawerLayout) findViewById(R.id.drawer_layout);
            iv = (ImageView) findViewById(R.id.image_view);
            mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder()//.addTestDevice("3B959FD02EE9C6EC1F533AE46958A019")
                    .build();
            mAdView.loadAd(adRequest);
            mDrawerList = (ListView) findViewById(R.id.navList);
            mDrawerRelativeLayout = (RelativeLayout) findViewById(R.id.left_drawer);
            String[] osArray = {"By Genre", "Watched Movies","Favourite Actors","Reminder Info", "Bookmarks","Help", "Exit"};
            mAdapter = new ArrayAdapter<String>(this,
                    R.layout.weltext, osArray);
            mDrawerList.setAdapter(mAdapter);


            mDrawerToggle = new ActionBarDrawerToggle(
                    this,
                    dl,
                    R.drawable.ic_dehaze_white1,
                    R.string.drawer_open,
                    R.string.drawer_close
            ) {

                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
                    // getActionBar().show();
                }


                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    //getActionBar().hide();

                }
            };
            dl.setDrawerListener(mDrawerToggle);
            try {
                getActionBar().setDisplayHomeAsUpEnabled(true);
                getActionBar().setHomeButtonEnabled(true);
            }catch(Exception e)
            {
                e.printStackTrace();

            }

                try {

                    FrontPage f = new FrontPage();
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    try{
                        f.setArguments(getIntent().getExtras());
                        Log.i("displayid","FM"+ getIntent().getExtras().getString("notificationid")) ;

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    FrameLayout contentView = (FrameLayout) findViewById(R.id.mainfragment);
                    ft.replace(contentView.getId(), f);
                    //ft.addToBackStack(null);
                    ft.commit();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            mDrawerList
                    .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {


                            switch (position) {
                                case 0:
                                    try {
                                        Genre f = new Genre();
                                        FragmentManager fm = getSupportFragmentManager();
                                        FragmentTransaction ft = fm.beginTransaction();

                                        FrameLayout contentView = (FrameLayout) findViewById(R.id.mainfragment);
                                        ft.hide(fm.findFragmentById(R.id.mainfragment));
                                        ft.add(contentView.getId(), f);
                                        ft.addToBackStack(null);
                                        ft.commit();
                                        dl.closeDrawer(mDrawerRelativeLayout);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    break;
                                case 4:
                                    try {

                                        Cursor cursor = getContentResolver().query(Database.Contenturi, null, null, null,
                                                null);

                                        if(cursor!=null&&cursor.getCount()==0)
                                        {
                                            Toast.makeText(getBaseContext(),"NO BOOKMARKS AVAILABLE",Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            SavePage f = new SavePage();
                                            FragmentManager fm = getSupportFragmentManager();
                                            FragmentTransaction ft = fm.beginTransaction();

                                            FrameLayout contentView = (FrameLayout) findViewById(R.id.mainfragment);
                                            ft.hide(fm.findFragmentById(R.id.mainfragment));
                                            ft.add(contentView.getId(), f);
                                            ft.addToBackStack(null);
                                            ft.commit();
                                        }

                                        dl.closeDrawer(mDrawerRelativeLayout);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case 3:
                                    try {
                                        Cursor cursor = getContentResolver().query(AlarmDatabase.Contenturi, null, null, null,
                                                null);

                                        if(cursor!=null&&cursor.getCount()==0)
                                        {
                                            Toast.makeText(getBaseContext(),"NO REMINDERS AVAILABLE",Toast.LENGTH_SHORT).show();
                                        }else {
                                            AlarmInfo a = new AlarmInfo();
                                            FragmentManager fm = getSupportFragmentManager();
                                            FragmentTransaction ft = fm.beginTransaction();

                                            FrameLayout contentView = (FrameLayout) findViewById(R.id.mainfragment);
                                            ft.hide(fm.findFragmentById(R.id.mainfragment));
                                            ft.add(contentView.getId(), a);
                                            ft.addToBackStack(null);
                                            ft.commit();
                                        }
                                        dl.closeDrawer(mDrawerRelativeLayout);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    break;
                                case 1:
                                      try{
                                          Cursor cursor = getContentResolver().query(WatchedDB.Contenturi, null, null, null,
                                                  null);
                                          if (cursor!=null&&cursor.getCount() == 0) {
                                              Toast.makeText(getBaseContext(), "YOU HAVE NOT WATCHED ANY MOVIE", Toast.LENGTH_SHORT).show();
                                          }else {
                                              Watched a = new Watched();
                                              FragmentManager fm = getSupportFragmentManager();
                                              FragmentTransaction ft = fm.beginTransaction();

                                              FrameLayout contentView = (FrameLayout) findViewById(R.id.mainfragment);
                                              ft.hide(fm.findFragmentById(R.id.mainfragment));
                                              ft.add(contentView.getId(), a);
                                              ft.addToBackStack(null);
                                              ft.commit();
                                          }
                                          dl.closeDrawer(mDrawerRelativeLayout);


                                      }catch(Exception e)
                                      {
                                          e.printStackTrace();
                                      }
                                    break ;
                                case 2:
                                    try {
                                        Cursor cursor = getContentResolver().query(FavactDB.Contenturi, null, null, null,
                                                null);
                                        if (cursor!=null &&cursor.getCount() == 0) {
                                            Toast.makeText(getBaseContext(), "YOU DONT LIKE ANYONE", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Favact aboutUs = new Favact();
                                            FragmentManager fm = getSupportFragmentManager();
                                            FragmentTransaction ft = fm.beginTransaction();

                                            FrameLayout contentView = (FrameLayout) findViewById(R.id.mainfragment);
                                            ft.hide(fm.findFragmentById(R.id.mainfragment));
                                            ft.add(contentView.getId(), aboutUs);
                                            ft.addToBackStack(null);
                                            ft.commit();
                                        }
                                        dl.closeDrawer(mDrawerRelativeLayout);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case 5:
                                    try {
                                         Help aboutUs = new Help();
                                        FragmentManager fm = getSupportFragmentManager();
                                        FragmentTransaction ft = fm.beginTransaction();

                                        FrameLayout contentView = (FrameLayout) findViewById(R.id.mainfragment);
                                        ft.hide(fm.findFragmentById(R.id.mainfragment));
                                        ft.add(contentView.getId(), aboutUs);
                                        ft.addToBackStack(null);
                                        ft.commit();
                                        dl.closeDrawer(mDrawerRelativeLayout);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break ;

                               /* case 6:
                                    try {
                                        AboutUs aboutUs = new AboutUs();
                                        FragmentManager fm = getSupportFragmentManager();
                                        FragmentTransaction ft = fm.beginTransaction();

                                        FrameLayout contentView = (FrameLayout) findViewById(R.id.mainfragment);
                                        ft.hide(fm.findFragmentById(R.id.mainfragment));
                                        ft.add(contentView.getId(), aboutUs);
                                        ft.addToBackStack(null);
                                        ft.commit();
                                        dl.closeDrawer(mDrawerRelativeLayout);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;*/
                                case 6:
                                    try {
                                        finish();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break ;

                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.book, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        try {  searchView = (SearchView) menu.findItem(R.id.searchView)
                .getActionView();
        m = menu;

            int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            TextView textView = (TextView) searchView.findViewById(id);
            textView.setTextColor(Color.WHITE);
            textView.setHintTextColor(Color.WHITE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast.makeText(getBaseContext(), query,
                // Toast.LENGTH_SHORT).show() ;
                Search f = new Search();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Bundle b = new Bundle();
                query = query.replaceAll(" ", "%20");
                query = BaseUrl + query + apikey;

                b.putString("query", query);

                f.setArguments(b);
                FrameLayout contentView = (FrameLayout) findViewById(R.id.mainfragment);
                ft.hide(fm.findFragmentById(R.id.mainfragment));
                ft.add(contentView.getId(), f);
                ft.addToBackStack(null);
                ft.commit() ;
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.voice:
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                        Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say A Movie Name");
                try {
                    startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(), "Sorry Not Available  ",
                            Toast.LENGTH_SHORT).show();
                }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //	searchView.setQuery(result.get(0), false);
                    Search f = new Search();
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    Bundle b = new Bundle();


                    String q = result.get(0);
                    q = q.replaceAll(" ", "%20");
                    q = BaseUrl + q + apikey;
                    b.putString("query", q);
                    f.setArguments(b);
                    FrameLayout contentView = (FrameLayout) findViewById(R.id.mainfragment);
                    ft.hide(fm.findFragmentById(R.id.mainfragment));
                    ft.add(contentView.getId(), f);
                    ft.addToBackStack(null);
                    ft.commit();
                    Toast.makeText(FragmentMainActivity.this, result.get(0),
                            Toast.LENGTH_SHORT).show();

                }
                break;
            }

        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        MenuItem menuSearch = m.findItem(R.id.searchView);
        try {
            getActionBar().setTitle("FilmStack");
        }catch(Exception e)
        {
            e.printStackTrace();
        }
//Don't use next line
//searchView.onActionViewCollapsed();
        menuSearch.collapseActionView();


        if (fm.getBackStackEntryCount() > 0) {

            fm.popBackStack();
        } else {

            super.onBackPressed();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onDestroy() {
        try {

            super.onDestroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
