package DB;

import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by luisgonzalez on 21-03-14.
 */
public class mSpeaker {

    public int _id;
    public String nombre;
    public String institucion;
    public String tratamieto;
    public String bio;
    public String lugar;
    public String rol;
    public byte [] imagen;

    public mSpeaker(Cursor c) {
        try {
            this._id= c.getInt(c.getColumnIndex("_id"));
        } catch (Exception e) {
            this._id = -1;
            e.printStackTrace();
        }
        try {
            this.imagen = c.getBlob(c.getColumnIndex("IMAGEN"));
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        // this.image = c.getBlob(c.getColumnIndex(DB.KEY_IMAGEN_BINARINO));//
        // not use image now
        try {
            this.institucion = c.getString(c
                    .getColumnIndex("ZINSTITUCION"));// ok
        } catch (Exception e2) {
            this.institucion = "";
            e2.printStackTrace();
        }
        try {
            this.tratamieto = c.getString(c
                    .getColumnIndex("ZTRATAMIENTO"));
        } catch (Exception e1) {
            this.tratamieto = "";
            e1.printStackTrace();
        }
        try {
            this.nombre = c.getString(c.getColumnIndex("EXPOSITOR"));
        } catch (Exception e) {
            this.nombre = "";
            e.printStackTrace();
        }
        try {
            this.bio = c.getString(c.getColumnIndex("ZBIO"));// ok
        } catch (Exception e) {
            this.bio = "";
            e.printStackTrace();
        }
        try {
            this.lugar = c.getString(c.getColumnIndex("ZLUGAR"));// ok
            Log.e("GOT LUGAR", this.lugar);
        } catch (Exception e) {
            this.lugar = "";
            e.printStackTrace();
        }
        Log.e("Speaker", this.toString());
    }

    public String get_rol() {
        if (TextUtils.isEmpty(this.rol)) {
            return "";
        }
        return this.rol;
    }

    public String get_bio() {
        if (TextUtils.isEmpty(this.bio)) {
            return "";
        }
        return this.bio;
    }

    public String get_lugar() {
        if (TextUtils.isEmpty(this.lugar)) {
            return "";
        }
        return this.lugar;
    }

    public String get_instituction() {
        if (TextUtils.isEmpty(this.institucion)) {
            return "";
        } else {
            return this.institucion;
        }
    }

    public String getInstitucionOrCountry() {
        if (TextUtils.isEmpty(this.institucion)) {
            return this.lugar;
        }
        return this.institucion;
    }

    public String toString() {
        return this._id + this.nombre + this.bio + this.tratamieto + this.rol
                + "lugar: " + this.lugar + "institucion:" + this.institucion;
    }
}
