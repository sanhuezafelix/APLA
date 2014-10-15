package DB;

import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by luisgonzalez on 17-03-14.
 */
public class mImage {

    public int id;

    public String tipo_imagen;// ok

    public byte [] imagen_uno;//ok
    public byte[]  imagen_dos;//ok
    public byte [] imagen_tres;//ok


    public mImage(Cursor c) {
        try {
            this.id = c.getInt(c.getColumnIndex("_id"));
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        try {
            this.tipo_imagen = c.getString(c.getColumnIndex("ZTIPOIMAGEN"));
        } catch (Exception e2) {
            this.tipo_imagen = "";
            e2.printStackTrace();
        }


        try {
            this.imagen_uno =  c.getBlob(c.getColumnIndex("ZIMAGENUNO"));// ok



        } catch (Exception e2) {

            e2.printStackTrace();
        }

        try {
          this.imagen_dos=  c.getBlob(c.getColumnIndex("ZIMAGENDOS"));// ok

        } catch (Exception e1) {

            e1.printStackTrace();
        }
        try {
            this.imagen_tres = c.getBlob(c.getColumnIndex("ZIMAGENTRES"));


        } catch (Exception e2) {

            e2.printStackTrace();
        }
        Log.v("imagenes",tipo_imagen +" "+ imagen_uno);

    }

}
