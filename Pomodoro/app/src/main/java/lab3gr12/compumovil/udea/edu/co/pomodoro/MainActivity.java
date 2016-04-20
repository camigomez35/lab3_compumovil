package lab3gr12.compumovil.udea.edu.co.pomodoro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {
    Preferencia preferencia;
    public static String TIEMPO = "tiempo";
    public static String TIPO = "tipo";
    public static int TIPO_DESCANSO = 0;
    public static int TIPO_TRABAJO = 1;
    public static int TIPO_ACTUAL = TIPO_TRABAJO;
    public static long tiempoActual = 0;
    private Intent intentPomodoro;
    private static CheckBox c1, c2, c3, c4;

    int debug;
    long tiempoTrabajo ;
    long tiempoDescansoLargo;
    long tiempoDescansoCorto;

    public BroadcastReceiver receiver;
    private TextView tvTiempo;
    private Button btPlay, btPause;
    private static int pomodoroCount=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        preferencia = PreferenciaDataManager.getInstance(this).obtenerPreferencia();
        debug = preferencia.getDebug();
        if(debug==1){
            tiempoTrabajo = 25*1000;
            tiempoDescansoLargo = (long)preferencia.tiempoLargo*1000;
            tiempoDescansoCorto = (long)preferencia.tiempoCorto*1000;
        }else{
            tiempoTrabajo = 25*1000*60;
            tiempoDescansoLargo = (long)preferencia.tiempoLargo*1000*60;
            tiempoDescansoCorto = (long)preferencia.tiempoCorto*1000*60;
        }


        btPlay = (Button) findViewById(R.id.bt_play);
        btPause = (Button) findViewById(R.id.bt_stop);
        btPause.setOnClickListener(this);
        btPlay.setOnClickListener(this);
        tvTiempo = (TextView) findViewById(R.id.tv_tiempo);
        c1=(CheckBox) findViewById(R.id.pomodoro1);
        c2=(CheckBox) findViewById(R.id.pomodoro2);
        c3=(CheckBox) findViewById(R.id.pomodoro3);
        c4=(CheckBox) findViewById(R.id.pomodoro4);

        //Iniciar servicio
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Bundle bunble = intent.getExtras();
                tiempoActual = bunble.getLong(MainActivity.TIEMPO);
                String s = Utilities.milliToString(bunble.getLong(MainActivity.TIEMPO));
                String[] t = s.split(":");
                if (Integer.parseInt(t[1]) < 10) {
                    t[1] = "0" + t[1];
                }
                if (Integer.parseInt(t[2]) < 10) {
                    t[2] = "0" + t[2];
                }
                s = t[1] + ":" + t[2];
                if(s.equals("00:00")){
                    if (TIPO_ACTUAL == TIPO_TRABAJO){
                        switch (pomodoroCount){
                            case 1:
                                c1.setChecked(true);
                                iniciarCronometro(tiempoDescansoCorto);
                                pomodoroCount=2;
                                break;
                            case 2:
                                c2.setChecked(true);
                                iniciarCronometro(tiempoDescansoCorto);
                                pomodoroCount=3;
                                break;
                            case 3:
                                c3.setChecked(true);
                                iniciarCronometro(tiempoDescansoCorto);
                                pomodoroCount=4;
                                break;
                            case 4:
                                c4.setChecked(true);
                                iniciarCronometro(tiempoDescansoLargo);
                                pomodoroCount=1;
                                break;
                        }
                        TIPO_ACTUAL =TIPO_DESCANSO;
                    }else{
                        if(pomodoroCount ==1){
                            c1.setChecked(false);
                            c2.setChecked(false);
                            c3.setChecked(false);
                            c4.setChecked(false);
                        }
                        iniciarCronometro(tiempoTrabajo);
                        TIPO_ACTUAL = TIPO_TRABAJO;
                    }

                }
                if (tvTiempo != null) {
                    tvTiempo.setText(s);
                }
            }
        };
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, PreferenciasVista.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter(MainActivity.TIEMPO)
        );
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_play:
                btPlay.setVisibility(View.GONE);
                btPause.setVisibility(View.VISIBLE);
                intentPomodoro = new Intent(
                        getApplicationContext(), Countdown.class);

                intentPomodoro.putExtra(TIPO, TIPO_ACTUAL);
                if (tiempoActual != 0) {
                    Log.e("tiempoActual", "" + tiempoActual);
                    intentPomodoro.putExtra(TIEMPO, tiempoActual);
                } else {
                    Log.e("tiempoActual", "" + tiempoTrabajo);
                    intentPomodoro.putExtra(TIEMPO, tiempoTrabajo);
                }

                startService(intentPomodoro);

                break;
            case R.id.bt_stop:
                btPause.setVisibility(View.GONE);
                btPlay.setVisibility(View.VISIBLE);
                Intent intentStop = new Intent(
                        getApplicationContext(), Countdown.class);
                stopService(intentStop);
        }

    }

    public void iniciarCronometro(long time){
        intentPomodoro = new Intent(
                getApplicationContext(), Countdown.class);
        intentPomodoro.putExtra(TIEMPO, time);

        startService(intentPomodoro);

    }
}
