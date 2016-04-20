package lab3gr12.compumovil.udea.edu.co.pomodoro;

/**
 * Created by Samuel on 15/04/2016.
 */
public class Preferencia {
    int id;
    int volumen;
    int vibracion;
    int tiempoLargo,tiempoCorto;
    int debug;

    public Preferencia() {
        id = 1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVolumen() {
        return volumen;
    }

    public void setVolumen(int volumen) {
        this.volumen = volumen;
    }

    public int getVibracion() {
        return vibracion;
    }

    public void setVibracion(int vibracion) {
        this.vibracion = vibracion;
    }

    public int getTiempoLargo() {
        return tiempoLargo;
    }

    public void setTiempoLargo(int tiempoLargo) {
        this.tiempoLargo = tiempoLargo;
    }

    public int getTiempoCorto() {
        return tiempoCorto;
    }

    public void setTiempoCorto(int tiempoCorto) {
        this.tiempoCorto = tiempoCorto;
    }

    public int getDebug() {
        return debug;
    }

    public void setDebug(int debug) {
        this.debug = debug;
    }
}
