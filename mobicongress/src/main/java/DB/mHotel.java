package DB;

import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import Utils.Utilidades;

/**
 * Created by luisgonzalez on 17-03-14.
 */
public class mHotel {

    public int id;

    public String titulo;// ok
    public int congreso;//ok
    public int estado;//ok
    public String contenido;//ok
    public String direccion;//ok
    public String telefono;//ok
    public String lugar;//ok
    public String mail;//ok
    public String tipo;//ok
    public String web;
    public byte [] icono;
    public byte [] fondo;
    public byte [] mapa;








    public mHotel(Cursor c) {
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
            this.titulo = c.getString(c.getColumnIndex("ZNOMBRE"));
        } catch (Exception e2) {
            this.titulo = "";
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
            this.lugar = c.getString(c.getColumnIndex(DB.KEY_EVENTO_NIETO_LUGAR));// ok
            if (TextUtils.isEmpty(this.lugar)) {
                this.lugar = "";
            }
        } catch (Exception e1) {
            this.lugar = "";
            e1.printStackTrace();
        }
        try {
            this.direccion = c.getString(c.getColumnIndex("ZDIRECCION"));

            if (TextUtils.isEmpty(this.direccion)) {
                this.direccion= "";
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
            this.tipo = c.getString(c.getColumnIndex("ZTIPO"));

            if (TextUtils.isEmpty(this.tipo)) {
                this.tipo= "";
            }
        } catch (Exception e2) {
            this.tipo = "";
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
            this.icono = c.getBlob(c.getColumnIndex("ICONO"));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        try {
            this.fondo = c.getBlob(c.getColumnIndex("FONDO"));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        try {
            this.mapa = c.getBlob(c.getColumnIndex("MAP"));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

}
