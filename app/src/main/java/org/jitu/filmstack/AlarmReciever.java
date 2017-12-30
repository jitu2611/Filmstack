package org.jitu.filmstack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class AlarmReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {

        String title = arg1.getStringExtra("title");
        byte[] img = arg1.getByteArrayExtra("image");
        int qw = arg1.getIntExtra("aid",0) ;


        try {
            Intent i = new Intent(arg0.getApplicationContext(), AlarmStop.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle b = new Bundle();
            b.putString("title", title);
            b.putByteArray("image", img);

            Toast.makeText(arg0.getApplicationContext(), title,
                    Toast.LENGTH_LONG).show();
            i.putExtras(b);
            arg0.getContentResolver().delete(AlarmDatabase.Contenturi,
                    "aid = " + qw, null);
            arg0.startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}