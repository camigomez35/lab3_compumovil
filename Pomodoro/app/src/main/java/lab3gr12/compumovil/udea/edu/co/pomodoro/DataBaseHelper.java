package lab3gr12.compumovil.udea.edu.co.pomodoro;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.util.Scanner;

public class DataBaseHelper extends SQLiteOpenHelper {
    /**
     * nombre del archivo de la base de datos
     */
    public static final String DB_NAME = "pomodoro.db";

    /**
     * extension de los assets con sentencias sql
     */
    public static final String FILE_EXTENSION = ".sql";

    /**
     * version actual de la base de datos, se incrementa en uno cada que se cree
     * un nuevo archivo assets de sentencias sql
     */
    public static final int DB_ACTUAL_VERSION = 1;

    /**
     * version inicial de la base de datos
     */
    public static final int DB_INITIAL_VERSION = 1;

    /**
     * contexto de la aplicacion
     */
    private Context context;

    /**
     * constructos del Helper
     *
     * @param context de la aplicacion
     * @param name    de la base de datos
     * @param version actual de la base de datos
     */
    public DataBaseHelper(Context context, String name, int version) {
        super(context, name, null, version);
        this.context = context;
    }

    /**
     * se ejecuta en la primera instalacion
     * y se aplican todos los scripts sql desde el inicial
     *
     * @param db base de datos de la aplicacion
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        onUpgrade(db, DB_INITIAL_VERSION - 1, DB_ACTUAL_VERSION);
    }

    /**
     * se ejecuta cada que la aplicacion esta instalada y tiene una version de la base de
     * datos menor a la actual
     * por cada version anterior a la nueva se ejecuta el correspondiente script
     *
     * @param db         base de datos de la aplicacion
     * @param oldVersion version actual de la base de datos
     * @param newVersion nueva version de la base de datos
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion < oldVersion) {
            return;
        }
        for (int v = oldVersion + 1; v <= newVersion; v++) {
            applyPatch(db, v);
        }
    }

    /**
     * construye el nombre del archivo de scripts a ejecutar segun la version
     *
     * @param db base de datos
     * @param v  version a ejecutar
     */
    private void applyPatch(SQLiteDatabase db, int v) {
        String filename = DB_NAME + "." + v + FILE_EXTENSION;
        runSqlFile(db, filename);
    }

    /**
     * ejecuta cada sentencia contenida en el archivo de scripts
     *
     * @param db       base de datos de la aplicacion
     * @param filename nombre del archivo que se debe ejecutar
     */
    private void runSqlFile(SQLiteDatabase db, String filename) {
        try {
            String sqlfileContent = readFileAsset(context, filename);
            String[] sentences = parseSQLSentences(sqlfileContent);
            for (String sql : sentences) {
                db.execSQL(sql);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * procesa todas las sentencias del archivo de scripts
     *
     * @param sqlfileContent nombre del archivo que debe procesar
     * @return arreglo que contiene todas las sentencias sql a ejecutar
     */
    private String[] parseSQLSentences(String sqlfileContent) {
        String otherThanQuote = " [^'] ";
        String quotedString = String.format(" ' %s* ' ", otherThanQuote);
        String regex = String.format("(?x) " + // enable comments, ignore white spaces
                        ";                         " + // match a semi colon
                        "(?=                       " + // start positive look ahead
                        "  (                       " + //   start group 1
                        "    %s*                   " + //     match 'otherThanQuote' zero or more times
                        "    %s                    " + //     match 'quotedString'
                        "  )*                      " + //   end group 1 and repeat it zero or more times
                        "  %s*                     " + //   match 'otherThanQuote'
                        "  $                       " + // match the end of the string
                        ")                         ", // stop positive look ahead
                otherThanQuote, quotedString, otherThanQuote);

        return sqlfileContent.split(regex);
    }

    /**
     * metodo que lee un archivo de sentencias SQL
     *
     * @param ctx      de la aplicacion
     * @param filename contiene las sentencias
     * @return string con todas las sentencias
     * @throws IOException
     */
    private String readFileAsset(Context ctx, String filename) throws IOException {
        StringBuilder builder = new StringBuilder();
        Scanner s = new Scanner(ctx.getAssets().open(filename));
        while (s.hasNextLine()) {
            builder.append(s.nextLine());
        }
        return builder.toString();
    }

}
