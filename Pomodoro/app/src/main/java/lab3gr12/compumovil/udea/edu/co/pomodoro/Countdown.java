package lab3gr12.compumovil.udea.edu.co.pomodoro;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
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

    public Countdown() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        Log.d(TAG, "Servicio creado...");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Servicio iniciado...");


        timerTask = new CountDownTimer(30000,1000) {
            Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intentMain, 0);
            Notification notificacion = new Notification.Builder(getApplicationContext())
                    .setContentTitle("Terminado")
                    .setContentText("Asunto_notificación").setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pIntent).build();
            @Override
            public void onTick(long millisUntilFinished) {
                    notificacion = new Notification.Builder(getApplicationContext())
                            .setContentTitle("Contando")
                            .setContentText("millisUntilFinished: "+millisUntilFinished).setSmallIcon(R.mipmap.ic_launcher)
                            .setContentIntent(pIntent).build();
                startForeground(1,notificacion);
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,0);
                Notification noti = new Notification.Builder(getApplicationContext())
                        .setContentTitle("Terminado")
                        .setContentText("Asunto_notificación").setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(pIntent).build();
                NotificationManager notificationManager =
                        (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//Se esconde la notificación tras ser seleccionada
                noti.flags |= Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(0, noti);

            }
        };

        timerTask.start();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        timerTask.cancel();
        Intent localIntent = new Intent(Constants.ACTION_MEMORY_EXIT);

        // Emitir el intent a la actividad
        LocalBroadcastManager.
                getInstance(Countdown.this).sendBroadcast(localIntent);
        Log.d(TAG, "Servicio destruido...");
    }

}
