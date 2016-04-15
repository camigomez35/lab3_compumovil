package lab3gr12.compumovil.udea.edu.co.pomodoro;

import android.content.Context;


public class DataManager {
    protected DataBaseHelper helper;


    public DataManager(Context context) {
        helper = new DataBaseHelper(context, DataBaseHelper.DB_NAME, DataBaseHelper.DB_ACTUAL_VERSION);
    }




}
