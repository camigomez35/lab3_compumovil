package lab3gr12.compumovil.udea.edu.co.pomodoro;

/**
 * Created by Samuel on 15/04/2016.
 */
public class Utilities {
    public static String milliToString(Long milliseconds){
        int seconds = (int) (milliseconds / 1000) % 60 ;
        int minutes = (int) ((milliseconds / (1000*60)) % 60);
        int hours   = (int) ((milliseconds / (1000*60*60)) % 24);
        return ""+hours+":"+minutes+":"+seconds;
    }
}
