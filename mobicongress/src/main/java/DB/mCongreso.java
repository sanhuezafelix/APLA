package DB;

import android.database.Cursor;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import Utils.Utilidades;

/**
 * Created by luisgonzalez on 15-03-14.
 */
public class mCongreso {

    public int id;
    public String time_start;
    public int[] time_start_date;
    public int[] time_start_time;
    public String time_end;
    public int[] time_end_date;
    public int[] time_end_time;
    public String titulo;
    public String descripcion;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Date start_;
    private Date end_;

    public mCongreso(Cursor c){

        try {
            this.id = c.getInt(c.getColumnIndex(DB.KEY_CONGRESO_ID_CONGRESO));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        try {
            this.titulo = c.getString(c.getColumnIndex(DB.KEY_CONGRESO_TITULO));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        try {
            this.descripcion = c.getString(c.getColumnIndex(DB.KEY_CONGRESO_DESCRIPCION));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        this.time_start = c.getString(c
                .getColumnIndex("ZFECHAINICIO"));
        this.time_end = c.getString(c.getColumnIndex("ZFECHAFIN"));
        try {
            time_start_date = Utilidades.getDate(this.time_start);
            time_start_time = Utilidades.getTime(this.time_start);
            time_end_date = Utilidades.getDate(this.time_end);
            time_end_time = Utilidades.getTime(this.time_end);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        Log.v("id_congres", String.format("%d",this.id));
        try {
            // Log.e("time", "parsed");

            start_ = sdf.parse(this.time_start);
            end_ = sdf.parse(this.time_end);

        } catch (ParseException e) {
            Log.e("time", "parsed failed");
            e.printStackTrace();
        }





    }

    public String getDayevent() {
        String dia = new String();
        switch (time_start_date[2]){
            case 5:
                dia = "Desde el Miercoles 20 al Sabado 23 de MAgosto";
                break;
            case 20:
                dia = "Desde el jueves 20 al Sabado 22 de marzo";
                break;
        }
        return dia;
    }
    public Boolean isCurrentCongress(){
        Boolean isCurrent=false;
        Date current = new Date();//get date now
        if (start_ == null ) {
            return false;
        }
        if (current.compareTo(start_) > 0 && current.compareTo(end_) < 0){
            isCurrent=true;
        }
        else{
            isCurrent = false;
        }
        return isCurrent;
    }



}
