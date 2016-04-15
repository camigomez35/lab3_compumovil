package lab3gr12.compumovil.udea.edu.co.pomodoro;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.provider.SyncStateContract;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class Countdown extends Service {


    private static final String TAG = "tag";
    CountDownTimer timerTask;
    LocalBroadcastManager broadcaster;

    long actual;

    public Countdown() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
       broadcaster = LocalBroadcastManager.getInstance(this);
        Log.d(TAG, "Servicio creado...");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Servicio iniciado...");
        Bundle bundle = intent.getExtras();
        long tiempo = bundle.getLong(MainActivity.TIEMPO);

        timerTask = new CountDownTimer(tiempo,1000) {
            Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intentMain, 0);
            Notification notificacion = new Notification.Builder(getApplicationContext())
                    .setContentTitle("Terminado")
                    .setContentText("Tiempo terminado").setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pIntent).build();
            @Override
            public void onTick(long millisUntilFinished) {
                    notificacion = new Notification.Builder(getApplicationContext())
                            .setContentTitle("Contando")
                            .setContentText("Restan: "+Utilities.milliToString(millisUntilFinished))
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentIntent(pIntent).build();
                actual = millisUntilFinished;
                startForeground(1, notificacion);

                Intent intent = new Intent(MainActivity.TIEMPO);
                intent.putExtra(MainActivity.TIEMPO, millisUntilFinished);
                //intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                broadcaster.sendBroadcast(intent);
            }

            @Override
            public void onFinish() {
                Intent intentFin = new Intent(MainActivity.TIEMPO);
                intentFin.putExtra(MainActivity.TIEMPO, 0);
                broadcaster.sendBroadcast(intentFin);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(MainActivity.TIEMPO, 0);
                PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                Notification noti = new Notification.Builder(getApplicationContext())
                        .setContentTitle("Terminado")
                        .setContentText("Asunto_notificación").setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(pIntent)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setLights(Color.RED, 3000, 3000)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .build();

                NotificationManager notificationManager =
                        (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                //Se esconde la notificación tras ser seleccionada
                noti.flags |= Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(0, noti);

                stopForeground(true);

            }
        };

        timerTask.start();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        timerTask.cancel();

        Log.d(TAG, "Servicio destruido...");
    }

}
