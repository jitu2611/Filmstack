package org.jitu.filmstack;
import android.support.v4.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;



public class Alarm extends Fragment implements OnClickListener {

    private static final int RQS_1 = 0;
    Button btnDatePicker, btnTimePicker, set;


    private int mYear, mMonth, mDay, mHour, mMinute, t1, t2;
    long AlarmTime = 0;
    ImageView img;
    TextView textView;
    String text;
    byte[] a;
    Bundle b;
    String title;
    byte[] im;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub


        return inflater.inflate(R.layout.alarm, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            try {
                btnDatePicker = (Button) getView().findViewById(R.id.btn_date);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            btnTimePicker = (Button) getView().findViewById(R.id.btn_time);
            img = (ImageView) getView().findViewById(R.id.aimage);
            textView = (TextView) getView().findViewById(R.id.atext);
            set = (Button) getView().findViewById(R.id.set);


            b = getArguments();
            title = b.getString("title");
            im = b.getByteArray("byteArray");
            textView.setText(title);
            Bitmap bitmap = BitmapFactory.decodeByteArray(im, 0,
                    im.length);
            img.setImageBitmap(bitmap);
            btnDatePicker.setOnClickListener(this);
            btnTimePicker.setOnClickListener(this);
            set.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
  try {
      if (v == btnDatePicker) {

          // Get Current Date
          final Calendar c = Calendar.getInstance();
          mYear = c.get(Calendar.YEAR);
          mMonth = c.get(Calendar.MONTH);
          mDay = c.get(Calendar.DAY_OF_MONTH);

          DatePickerDialog datePickerDialog = new DatePickerDialog(
                  getActivity(), new DatePickerDialog.OnDateSetListener() {

              @Override
              public void onDateSet(DatePicker view, int year,
                                    int monthOfYear, int dayOfMonth) {
                  SimpleDateFormat myFormat = new SimpleDateFormat(
                          "dd MM yyyy");
                  String inputString1 = mDay + " " + mMonth + " "
                          + mYear;
                  String inputString2 = dayOfMonth + " "
                          + monthOfYear + " " + year;
                  long diff = 0;
                  try {
                      Date date1 = myFormat.parse(inputString1);
                      Date date2 = myFormat.parse(inputString2);
                      diff = date2.getTime() - date1.getTime();


                      AlarmTime = TimeUnit.DAYS.convert(diff,
                              TimeUnit.MILLISECONDS) * 24 * 60 * 60;
                      btnDatePicker.setText(dayOfMonth + "/" + monthOfYear+1 + "/" + year);

                  } catch (Exception e) {
                      e.printStackTrace();
                  }

              }
          }, mYear, mMonth, mDay);
          datePickerDialog.show();

      }
      if (v == btnTimePicker) {

          // Get Current Time
          final Calendar c = Calendar.getInstance();
          mHour = c.get(Calendar.HOUR_OF_DAY);
          mMinute = c.get(Calendar.MINUTE);

          t1 = mHour * 60 + mMinute;
          // Launch Time Picker Dialog
          final TimePickerDialog timePickerDialog = new TimePickerDialog(
                  getActivity(), new TimePickerDialog.OnTimeSetListener() {

              @Override
              public void onTimeSet(TimePicker view, int hourOfDay,
                                    int minute) {
                  t2 = hourOfDay * 60 + minute;
                  //t2 = t2 - t1;
                  t2 = (t2 - t1) * 60;
                  if (minute < 10) {
                      btnTimePicker.setText(hourOfDay + ":0" + minute);
                  } else {
                      btnTimePicker.setText(hourOfDay + ":" + minute);
                  }

              }
          }, mHour, mMinute, false);
          timePickerDialog.show();


      }
      if (v == set) {
          if (AlarmTime + t2 <= 0) {
              Toast.makeText(getActivity(), "Reminder Can not Be Set For This Time.", Toast.LENGTH_SHORT).show();
          } else {
              setAlarm(AlarmTime + t2 - 40);
          }
      }
  }catch(Exception e)
  {
      e.printStackTrace();
  }
    }

    private void setAlarm(long t) {

        Toast.makeText(getActivity(), "Reminder Set ", Toast.LENGTH_SHORT).show();
        try {
        Intent intent = new Intent(getActivity(), AlarmReciever.class);

        final int _ide = (int) System.currentTimeMillis();

        intent.putExtra("title", b.getString("title"));
        intent.putExtra("image", b.getByteArray("byteArray"));
        intent.putExtra("aid", _ide);

            ContentValues values = new ContentValues();
            values.put(AlarmDatabase.alarm.TITLE, title);
            values.put(AlarmDatabase.alarm.AID, _ide);
            values.put(AlarmDatabase.alarm.ICON, im);
            values.put(AlarmDatabase.alarm.TIME, btnTimePicker.getText().toString());
            values.put(AlarmDatabase.alarm.DATE, btnDatePicker.getText().toString());
            Uri u = getActivity().getContentResolver().insert(AlarmDatabase.Contenturi,
                    values);
            //     Toast.makeText(getActivity(), "g" + u, Toast.LENGTH_SHORT).show();

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), _ide, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getActivity()
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + t* 1000, pendingIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}