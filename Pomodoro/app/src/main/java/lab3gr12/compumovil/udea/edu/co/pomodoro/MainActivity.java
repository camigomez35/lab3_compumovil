package lab3gr12.compumovil.udea.edu.co.pomodoro;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.renderscript.ScriptGroup;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    Preferencia preferencia;
    public static String TIEMPO = "tiempo";
    public static String TIPO = "tipo";
    public static int TIPO_DESCANSO = 0;
    public static int TIPO_TRABAJO = 1;
    public static int TIPO_ACTUAL = TIPO_TRABAJO;
    public static long tiempoActual = 0;
    private Intent intentPomodoro;
    long tiempo = 30000;
    public BroadcastReceiver receiver;
    private TextView tvTiempo;
    private SeekBar sbVolumen, sbDescanso, sbTrabajo;
    private CheckBox cbVibracion, cbDebug;
    private Button btPlay, btPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        preferencia = PreferenciaDataManager.getInstance(this).obtenerPreferencia();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        btPlay = (Button) findViewById(R.id.bt_play);
        btPause = (Button) findViewById(R.id.bt_stop);
        btPause.setOnClickListener(this);
        btPlay.setOnClickListener(this);
        tvTiempo = (TextView) findViewById(R.id.tv_tiempo);
        //Iniciar servicio
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Bundle bunble = intent.getExtras();
                tiempoActual = bunble.getLong(MainActivity.TIEMPO);
                String s = Utilities.milliToString(bunble.getLong(MainActivity.TIEMPO));
                if (tvTiempo != null) {
                    tvTiempo.setText(s);
                }
            }
        };
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                intentPomodoro = new Intent(
                        getApplicationContext(), Countdown.class);

                intentPomodoro.putExtra(TIPO, TIPO_ACTUAL);
                if (tiempoActual != 0) {
                    Log.e("tiempoActual", "" + tiempoActual);
                    intentPomodoro.putExtra(TIEMPO, tiempoActual);
                } else {
                    Log.e("tiempoActual", "" + tiempo);
                    intentPomodoro.putExtra(TIEMPO, tiempo);
                }

                startService(intentPomodoro);

                break;
            case R.id.bt_stop:
                Intent intentStop = new Intent(
                        getApplicationContext(), Countdown.class);
                stopService(intentStop);
        }

    }
}
