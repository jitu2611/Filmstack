package org.jitu.filmstack;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jitu on 12/12/2017.
 */
public class FireMsgService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("Msg", "Message received [" + remoteMessage + "]");

        Intent intent ;
        // Create Notification

        intent = new Intent(this,FragmentMainActivity.class) ;
        Bundle b = new Bundle() ;
        b.putString("notificationid",remoteMessage.getData().get("ID"));
        Log.i("displayid",remoteMessage.getData().get("ID")) ;
        b.putInt("check",1) ;
        intent.putExtras(b);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1410,
                intent, PendingIntent.FLAG_UPDATE_CURRENT
                        | PendingIntent.FLAG_ONE_SHOT);



        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.iconmain);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.iconmain)) ;
        builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(getBitmapfromUrl(remoteMessage.getData().get("image")))) ;
        builder.setContentTitle("Filmstack") ;
        builder.setContentText(remoteMessage.getData().get("message")) ;
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent) ;


        NotificationManager notificationManager =
                (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1410, builder.build());
    }
    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }
}