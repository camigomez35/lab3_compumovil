package lab3gr12.compumovil.udea.edu.co.pomodoro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PreferenciaDataManager extends DataManager {
    private static PreferenciaDataManager instance;

    public static final String TABLE_NAME = "preferencia";

    public static final int COL_ID = 0,
            COL_VOLUMEN = 1,
            COL_TIEMPO_CORTO = 2,
            COL_TIEMPO_LARGO = 3,
            COL_VIBRACION = 4,
            COL_DEBUG = 5;


    public static final String[] COLUMNS = {
            "id",
            "volumen",
            "tiempocorto",
            "tiempolargo",
            "vibracion",
            "debug"
    };




    private PreferenciaDataManager(Context context) {
        super(context);
    }

    public static PreferenciaDataManager getInstance(Context context) {
        if (instance == null) {
            instance = new PreferenciaDataManager(context);
        }
        return instance;
    }




    public void insertarPreferencia(Preferencia alarma) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int id = (int) db.insert(TABLE_NAME, null, getContentValues(alarma));
        db.close();
        helper.close();
        alarma.setId(id);
    }

    public void actualizarPreferencia(Preferencia alarma) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.update(TABLE_NAME, getContentValues(alarma), COLUMNS[COL_ID] + " = ?", new String[]{String.valueOf(alarma.getId())});
        db.close();
        helper.close();
    }


    private Preferencia getPreferenciaFromCursor(Cursor cursor) {
        Preferencia alarma = new Preferencia();
        alarma.setId(cursor.getInt(COL_ID));
        alarma.setVolumen(cursor.getInt(COL_VOLUMEN));
        alarma.setTiempoCorto(cursor.getInt(COL_TIEMPO_CORTO));
        alarma.setTiempoLargo(cursor.getInt(COL_TIEMPO_LARGO));
        alarma.setDebug(cursor.getInt(COL_DEBUG));
        alarma.setVibracion(cursor.getInt(COL_VIBRACION));
        return alarma;
    }



    private ContentValues getContentValues(Preferencia alarma) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMNS[COL_ID], (alarma.getId() == 0) ? null : alarma.getId());
        cv.put(COLUMNS[COL_VOLUMEN], alarma.getVolumen());
        cv.put(COLUMNS[COL_TIEMPO_CORTO], alarma.getTiempoCorto());
        cv.put(COLUMNS[COL_TIEMPO_LARGO], alarma.getTiempoLargo());
        cv.put(COLUMNS[COL_VIBRACION], alarma.getVibracion());
        cv.put(COLUMNS[COL_DEBUG], alarma.getDebug());
        return cv;
    }

    public Preferencia obtenerPreferencia() {
        Preferencia alarma = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, COLUMNS, null,null, null, null, null);
        if (cursor.moveToFirst()) {
            alarma = getPreferenciaFromCursor(cursor);
        }
        db.close();
        cursor.close();
        helper.close();
        return alarma;
    }
    public Preferencia getPreferenciaById(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, COLUMNS,
                "id = ?", new String[]{String.valueOf(id)}, null, null, COLUMNS[COL_ID]);
        if (cursor.moveToNext()) {
            return getPreferenciaFromCursor(cursor);
        }
        cursor.close();
        helper.close();
        return null;
    }
}
