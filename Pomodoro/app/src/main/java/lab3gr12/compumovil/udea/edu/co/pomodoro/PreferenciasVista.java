package lab3gr12.compumovil.udea.edu.co.pomodoro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by Samuel on 15/04/2016.
 */
public class PreferenciasVista extends Activity implements View.OnClickListener {

    Preferencia preferencia;
    Button guardar;
    SeekBar volumen, tiempoCorto, tiempoLargo;
    CheckBox vibracion, debug;
    TextView vol, tvTiempoCorto, tvTiempoLargo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferencias);


        volumen = (SeekBar) findViewById(R.id.volumen);
        vol = (TextView) findViewById(R.id.txVolumen);

        tiempoCorto = (SeekBar) findViewById(R.id.tiempoCorto);
        tvTiempoCorto = (TextView) findViewById(R.id.txDescansoCorto);

        tiempoLargo = (SeekBar) findViewById(R.id.tiempoLargo);
        tvTiempoLargo = (TextView) findViewById(R.id.txDescansoLargo);

        vibracion = (CheckBox) findViewById(R.id.vibracion);
        debug = (CheckBox) findViewById(R.id.debug);
        guardar = (Button) findViewById(R.id.guardar);
        guardar.setOnClickListener(this);
        preferencia = PreferenciaDataManager.getInstance(this).obtenerPreferencia();

        volumen.setProgress(preferencia.getVolumen());
        tiempoCorto.setProgress(preferencia.getTiempoCorto());
        tiempoLargo.setProgress(preferencia.getTiempoLargo());
        if (preferencia.getVibracion() == 1) {
            vibracion.setChecked(true);
        }
        if (preferencia.getDebug() == 1) {
            debug.setChecked(true);
        }
        seekBarSeguir();
    }

    public void seekBarSeguir() {
        vol.setText(""+volumen.getProgress());
        volumen.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        vol.setText(""+progress);
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}

                }
        );

        tvTiempoCorto.setText(""+tiempoCorto.getProgress());
        tiempoCorto.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        tvTiempoCorto.setText(""+progress);
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}

                }
        );

        tvTiempoLargo.setText(""+tiempoLargo.getProgress());
        tiempoLargo.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        tvTiempoLargo.setText(""+progress);
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}

                }
        );
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.guardar:
                if (vibracion.isChecked()) {
                    preferencia.setVibracion(1);
                } else {
                    preferencia.setVibracion(0);
                }
                if (debug.isChecked()) {
                    preferencia.setDebug(1);
                } else {
                    preferencia.setDebug(0);
                }
                preferencia.setVolumen(volumen.getProgress());
                preferencia.setTiempoCorto(tiempoCorto.getProgress() + 5);
                preferencia.setTiempoLargo(tiempoLargo.getProgress() + 10);
                PreferenciaDataManager.getInstance(this).actualizarPreferencia(preferencia);
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                break;
        }
    }
}
