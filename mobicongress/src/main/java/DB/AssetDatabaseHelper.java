package DB;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

/**
 * A helper class the import and export db files. </ br> Example to import from
 * </ br> "{@code /assets/example.sqlite}" </ br>
 * {@code new AssetDAtabaseHelper(context,"example.sqlite").importIfNotExisit();}
 * </ br> Example to export to a speific locaiton in sd card: </ br>
 *
 * {@code new AssetDAtabaseHelper(context,"example.db").exportDatabase("example.db", "/data/theNewDb.sqlite");}
 *
 * @version 3
 * @author WillieTsang
 *
 */
public class AssetDatabaseHelper extends SQLiteOpenHelper {

    private String dbName;
    private String db_path;
    private Context context;

    /**
     * A helper class to import/export db files.
     *
     * @param context
     *            base/app context
     * @param dbName
     *            The name of the db in asset folder .
     */
    public AssetDatabaseHelper(Context context, String dbName) {
        super(context, dbName, null, 1);
        this.dbName = dbName;
        this.context = context;
        this.db_path = context.getDatabasePath(dbName).getAbsolutePath();
    }

    /**
     * Checks if the data base is in your app's database directory.
     *
     * @return true if it exists, false if it doesn't
     */
    public boolean checkExist() {
        File dbFile = new File(db_path);
        return dbFile.exists();
    }

    /**
     * Imports the database from your {@code /assets/YOUR_DB} to your app's
     * database directory. This method will not override your database if it
     * already exist.
     * */
    public void importIfNotExist() throws IOException {
        Log.e("import", "importIfNotExist-start");
        boolean dbExist = checkExist();
        dbExist = false;
        if (dbExist) {
            Log.e("import", "importIfNotExist-db existed");
            // do nothing - database already exist
        } else {
            Log.e("import", "importIfNotExist-db NOT existed");
            this.getReadableDatabase();
            try {
                copyDatabase();
            } catch (IOException e) {
                throw new IOException("Error copying database");
            }
        }
        Log.e("import", "importIfNotExist-end");
    }

    /**
     * Creates a copie of the database from asset to the app's database
     * directory..
     * */
    private void copyDatabase() throws IOException {
        Log.e("import", "copyDatabase-start");
        InputStream is = context.getAssets().open(dbName);
        OutputStream os = new FileOutputStream(db_path);
        byte[] buffer = new byte[4096];
        int length;
        while ((length = is.read(buffer)) > 0) {
            Log.e("importing", "1");
            os.write(buffer, 0, length);
        }
        os.flush();
        os.close();
        is.close();
        this.close();
        Log.e("import", "copyDatabase-end");
    }

    /**
     * Export a database for database directory of app to a destnation in sd
     * card. Ex.</br>
     * {@code this.exportDatabase(data.db, "/data/theNewDb.sqlite");} </br>
     * {@code this.exportDatabase(data.sqlite, null);}
     *
     * @param dbName
     *            name of database file including extension.
     * @param exportDestPath
     *            path in the device's external storage. Passing in {@code null}
     *            will be exported to root of external storage.
     */
    public void exportDatabase(String dbName, String exportDestSdPath) {
        if (exportDestSdPath == null) {
            exportDestSdPath = "/" + dbName;
        }
        try {
            File sd = Environment.getExternalStorageDirectory();
            if (sd.canWrite()) {
                String currentDBPath = context.getDatabasePath(dbName)
                        .getAbsolutePath();
                String backupDBPath = exportDestSdPath;
                File currentDB = new File(currentDBPath);
                File backupDB = new File(sd + backupDBPath);
                if (currentDB.exists()) {

                    FileInputStream src = new FileInputStream(currentDB);
                    FileChannel srcCh = src.getChannel();
                    FileOutputStream dest = null;
                    if (backupDB.exists()) {
                        dest = new FileOutputStream(backupDB);
                    } else {
                        if (backupDB.getParentFile().mkdirs()) {
                            dest = new FileOutputStream(backupDB);
                        } else {
                            src.close();
                            throw new IOException("Can't export db");
                        }
                    }

                    FileChannel destCh = dest.getChannel();
                    destCh.transferFrom(srcCh, 0, srcCh.size());
                    src.close();
                    dest.close();
                }
            }

        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    /**
     * Example usage of this method can be just:</ br>
     * {@code new AssetDAtabaseHelper(context,"example.db").exportDatabase(null);}
     * </ br> which will take your database file and export it to external
     * storage's root directory.
     *
     * @see #exportDatabase(String, String)
     * @param exportDestSdPath
     *            path in the device's external storage. Passing in {@code null}
     *            will be exported to root of external storage.
     */
    public void exportDatabase(String exportDestSdPath) {
        this.exportDatabase(this.dbName, exportDestSdPath);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}