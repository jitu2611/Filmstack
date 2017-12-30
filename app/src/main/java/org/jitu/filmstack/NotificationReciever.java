package org.jitu.filmstack;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;



/**
 * Created by Jitesh on 1/2/2017.
 */
public class NotificationReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        try {

       // Toast.makeText(arg0, "HELLO", Toast.LENGTH_SHORT).show();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(arg0);
        builder.setSmallIcon(R.drawable.pp);
            builder.setLargeIcon(BitmapFactory.decodeResource(arg0.getResources(), R.drawable.iconmain)) ;
        Intent intent = new Intent(arg0, FragmentMainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(arg0, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setContentTitle("FilmStack");
        builder.setContentText("Check Out Latest Movies");
       // builder.setVibrate(new long[]{1000, 1000, 1000});
            builder.setAutoCancel(true) ;
        NotificationManager notificationManager = (NotificationManager) arg0.getSystemService(arg0.NOTIFICATION_SERVICE);

        // Will display the notification in the notification bar
        notificationManager.notify(1, builder.build());

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
