package lab3gr12.compumovil.udea.edu.co.pomodoro;

import android.app.Application;
import android.content.Context;

/**
 * Created by CamiGomez318 on 20/04/2016.
 */
public class Pomodoro extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
    public static Context getContext() {
        return context;
    }
}
