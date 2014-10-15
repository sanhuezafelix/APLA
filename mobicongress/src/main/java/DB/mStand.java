package DB;

import android.database.Cursor;
import android.text.TextUtils;

/**
 * Created by luisgonzalez on 17-03-14.
 */
public class mStand {

    public int id;



    public int estado;//ok
    public int congreso;//ok
    public String novedades;// ok

    public String descripcion;//ok
    public String telefono;//ok
    public String lugar;//ok
    public String mail;//ok
    public String contenido;//ok
    public String categoria;//ok
    public String nombre;

    public String web;
    public byte [] mapa;
    public byte [] fondo;







    public mStand(Cursor c) {
        try {
            this.id = c.getInt(c.getColumnIndex("_id"));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        try {
            this.estado = c.getInt(c.getColumnIndex("ZESTADO"));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        try {
            this.congreso = c.getInt(c.getColumnIndex("ZCONGRESO"));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        try {
            this.nombre = c.getString(c.getColumnIndex("ZNOMBRE"));
        } catch (Exception e2) {
            this.nombre = "";
            e2.printStackTrace();
        }


        try {
            this.contenido = c.getString(c.getColumnIndex("ZCONTENIDO"));

            if (TextUtils.isEmpty(this.contenido)) {
                this.contenido= "";
            }
        } catch (Exception e2) {
            this.contenido = "";
            e2.printStackTrace();
        }
        try {
            this.categoria = c.getString(c.getColumnIndex("ZCATEGORIA"));

            if (TextUtils.isEmpty(this.categoria)) {
                this.categoria= "";
            }
        } catch (Exception e2) {
            this.categoria = "";
            e2.printStackTrace();
        }

        try {
            this.lugar = c.getString(c.getColumnIndex(DB.KEY_EVENTO_NIETO_LUGAR));// ok
            if (TextUtils.isEmpty(this.lugar)) {
                this.lugar = "";
            }
        } catch (Exception e1) {
            this.lugar = "";
            e1.printStackTrace();
        }
        try {
            this.descripcion = c.getString(c.getColumnIndex("ZDESCRIPCION"));

            if (TextUtils.isEmpty(this.descripcion)) {
                this.descripcion= "";
            }
        } catch (Exception e2) {
            this.contenido = "";
            e2.printStackTrace();
        }
        try {
            this.telefono = c.getString(c.getColumnIndex("ZFONO"));

            if (TextUtils.isEmpty(this.telefono)) {
                this.telefono= "";
            }
        } catch (Exception e2) {
            this.telefono = "";
            e2.printStackTrace();
        }
        try {this.mail = c.getString(c.getColumnIndex("ZMAIL"));

            if (TextUtils.isEmpty(this.mail)) {
                this.mail = "";
            }
        } catch (Exception e2) {
            this.mail = "";
            e2.printStackTrace();
        }
        try {
            this.novedades = c.getString(c.getColumnIndex("ZNOVEDADES"));

            if (TextUtils.isEmpty(this.novedades)) {
                this.novedades= "";
            }
        } catch (Exception e2) {
            this.novedades = "";
            e2.printStackTrace();
        }
        try {
            this.web = c.getString(c.getColumnIndex("ZWEB"));

            if (TextUtils.isEmpty(this.web)) {
                this.web= "";
            }
        } catch (Exception e2) {
            this.web = "";
            e2.printStackTrace();
        }

        try {
            this.fondo = c.getBlob(c.getColumnIndex("ICONO"));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        try {
            this.mapa = c.getBlob(c.getColumnIndex("IMAGEN"));
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

}
