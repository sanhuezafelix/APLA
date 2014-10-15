package DB;

import android.database.Cursor;
import android.util.Log;

import Utils.Utilidades;

/**
 * Created by luisgonzalez on 17-03-14.
 */
public class mEventoAbuelo {

    // variables

    public int id;

    public String titulo;
    public String descripcion;

    public mEventoAbuelo(Cursor c){

        try {
            this.id = c.getInt(c.getColumnIndex(DB.KEY_EVENTO_ABUELO_ID));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        try {
            this.descripcion = c.getString(c.getColumnIndex(DB.KEY_EVENTO_ABUELO_DESCRIPCION));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        try {
            this.titulo = c.getString(c.getColumnIndex(DB.KEY_EVENTO_ABUELO_TITULO));
        } catch (Exception e1) {
            e1.printStackTrace();
        }






    }

}
