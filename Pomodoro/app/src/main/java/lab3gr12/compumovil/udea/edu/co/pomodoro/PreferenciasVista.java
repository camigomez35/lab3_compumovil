package lab3gr12.compumovil.udea.edu.co.pomodoro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;

/**
 * Created by Samuel on 15/04/2016.
 */
public class PreferenciasVista extends Activity implements View.OnClickListener{

    Preferencia preferencia;
    Button guardar;
    SeekBar volumen, tiempoCorto, tiempoLargo;
    CheckBox vibracion, debug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferencias);

        volumen = (SeekBar)findViewById(R.id.volumen);
        tiempoCorto = (SeekBar)findViewById(R.id.tiempoCorto);
        tiempoLargo = (SeekBar)findViewById(R.id.tiempoLargo);
        vibracion = (CheckBox)findViewById(R.id.vibracion);
        debug = (CheckBox)findViewById(R.id.debug);
        guardar = (Button)findViewById(R.id.guardar);
        guardar.setOnClickListener(this);
        preferencia = PreferenciaDataManager.getInstance(this).obtenerPreferencia();

        volumen.setProgress(preferencia.getVolumen());
        tiempoCorto.setProgress(preferencia.getTiempoCorto());
        tiempoLargo.setProgress(preferencia.getTiempoLargo());
        if(preferencia.getVibracion()==1){
            vibracion.setChecked(true);
        }
        if(preferencia.getDebug()==1){
            debug.setChecked(true);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.guardar:
                if(vibracion.isChecked()){
                    preferencia.setVibracion(1);
                }else{
                    preferencia.setVibracion(0);
                }
                if(debug.isChecked()){
                    preferencia.setDebug(1);
                }else{
                    preferencia.setDebug(0);
                }
                preferencia.setVolumen(volumen.getProgress());
                preferencia.setTiempoCorto(tiempoCorto.getProgress());
                preferencia.setTiempoLargo(tiempoLargo.getProgress());
                PreferenciaDataManager.getInstance(this).actualizarPreferencia(preferencia);
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                break;
        }
    }
}
