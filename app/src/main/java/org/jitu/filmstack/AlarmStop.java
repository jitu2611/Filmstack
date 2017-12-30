package org.jitu.filmstack;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class AlarmStop extends Activity {

	TextView tv;
	Button button;
	ImageView iv;
	MediaPlayer mediaPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarmstop);
		try {
		tv = (TextView) findViewById(R.id.alarmt);
		iv = (ImageView) findViewById(R.id.alarmimage);
		this.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		button = (Button) findViewById(R.id.alarmbutton);
		mediaPlayer = MediaPlayer.create(this, R.raw.wake);
		mediaPlayer.start();

			Bundle b = getIntent().getExtras();
			String tvi = b.getString("title") ;

			byte[] image = b.getByteArray("image");
			Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0,
					image.length);
			tv.setText(tvi);
			iv.setImageBitmap(bitmap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					mediaPlayer.release();
					finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
