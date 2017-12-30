package org.jitu.filmstack;

import android.app.AlarmManager;
import android.support.v4.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;



/**
 * Created by JITU on 10/11/2016.
 */
public class AlarmInfo extends Fragment {

    ListView lv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.listsaved, container, false);
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        //setContentView(R.layout.listsaved);
        try {
            getActivity().getActionBar().setTitle("FilmStack");
        }catch (Exception e)
        {e.printStackTrace();}
        try {
            lv = (ListView) getView().findViewById(R.id.savedpage);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        registerForContextMenu(lv);

        try {
            Cursor cursor = getActivity().managedQuery(AlarmDatabase.Contenturi, null, null, null,
                    null);
            cursor.moveToFirst();
            if(cursor.getCount()==0)
            {
                Toast.makeText(getActivity(),"NO REMINDERS AVAILABLE",Toast.LENGTH_SHORT).show();
            }
            //Log.d("cursor init", cursor.getString(cursor
              //      .getColumnIndex(Database.bookmark.TITLE)));

            lv.setAdapter(new AlarmAdapter(getActivity(), cursor));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        super.onCreateContextMenu(menu, v, menuInfo);
        try {
            if (v.getId() == R.id.savedpage) {

                MenuInflater inflater = getActivity().getMenuInflater();
                inflater.inflate(R.menu.menu_list, menu);

            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        // return super.onContextItemSelected(item);
        try {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                    .getMenuInfo();

            int q = info.position;
            Cursor c = getActivity().managedQuery(AlarmDatabase.Contenturi, null, null, null, null);
            c.moveToPosition(q);
            int qw = c.getInt(c.getColumnIndex(AlarmDatabase.alarm._ID));

            switch (item.getItemId()) {

                case R.id.del:
                    // String[] whereArgs = new String[] {Long.toString(l)};
                    @SuppressWarnings("unused")
                    int d = getActivity().getContentResolver().delete(AlarmDatabase.Contenturi,
                            "_id = " + qw, null);
                    Intent intent = new Intent(getActivity(), AlarmReciever.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), Integer.parseInt(c.getString(c.getColumnIndex(AlarmDatabase.alarm.AID))), intent, 0);
                    AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                    alarmManager.cancel(pendingIntent);

                    try {
                        Cursor ci = getActivity().managedQuery(AlarmDatabase.Contenturi, null, null, null,
                                null);
                        ci.moveToFirst();
                        lv.setAdapter(new AlarmAdapter(getActivity(), ci));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getActivity().getBaseContext(), "Deleted Successfully ",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return super.onContextItemSelected(item);

    }
    public void onPause(){
        super.onPause();
        try {
            getActivity().getActionBar().setTitle("FilmStack");
        }catch (Exception e )
        {
            e.printStackTrace();
        }
    }
}
