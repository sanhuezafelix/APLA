package DB;

import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import Utils.Utilidades;

/**
 * Created by luisgonzalez on 17-03-14.
 */
public class mEvent {

    public int id;
    //public static Date compare_date;
    // Imagen
    //public byte[] image;
    public long milisecondevent;
    public String time_start;
    public int[] time_start_date;
    public int[] time_start_time;
    public int[] time_alarm;
    public int[] date_alarm;
    public String time_end;
    public int[] time_end_date;
    public int[] time_end_time;
    // Titulo-Title
    public String titulo;
    // Description
    public String description;


    // Persona name
    public int Favorito;
    // Lugar-place
    public String lugar;
    // Event kind
    public String tipo_evento;
    public String tipo_evento_padre;
    // Person bio
    public String biografia;
    // Person instituction
    public String institucion;

    public String speakeruno;
    public int esExtra;
    public byte [] imagen;

    public String speakerdos;
    public String speakertres;
    public String speakercuatro;
    public String speakeExtra;
    public String rating;
    public int isRating;
    public int esUnico;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date start_;
    Date end_;
    Date alarma;


    public String pais;
    //public String event_place;

    public int evento_padre_id;
    public String evento_padre;

    public mEvent(Cursor c) {
        try {
            this.id = c.getInt(c.getColumnIndex("_id"));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        try {
            this.isRating = c.getInt(c.getColumnIndex("ZISRATING"));
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        try {
            this.Favorito = c.getInt(c.getColumnIndex("ZFAVORITO"));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        try {
            this.esUnico = c.getInt(c.getColumnIndex("ZESUNICO"));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        try {
            this.esExtra = c.getInt(c.getColumnIndex("ZESEXTRA"));
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        try {
            this.evento_padre_id = c.getInt(c.getColumnIndex(DB.KEY_EVENTO_NIETO_EVENTO_PADRE_ID));
        } catch (Exception e2) {

            e2.printStackTrace();
        }

        this.time_start = c.getString(c
                .getColumnIndex(DB.KEY_EVENTO_NIETO_FECHAINICIO));// ok
        this.time_end = c.getString(c.getColumnIndex(DB.KEY_EVENTO_NIETO_FECHAFIN));// ok
        try {
            this.titulo = c.getString(c.getColumnIndex(DB.KEY_EVENTO_NIETO_TITULO));// ok
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        try {
            this.tipo_evento = c.getString(c.getColumnIndex(DB.KEY_EVENTO_NIETO_TIPO_EVENTO));
            Log.e("event_kind", this.tipo_evento + " 00");
            if (TextUtils.isEmpty(this.tipo_evento)) {
                this.tipo_evento = "";
            }
        } catch (Exception e2) {
            this.tipo_evento = "";
            e2.printStackTrace();
        }
        try {
            this.tipo_evento_padre = c.getString(c.getColumnIndex("TIPOPADRE"));

            if (TextUtils.isEmpty(this.tipo_evento_padre)) {
                this.tipo_evento_padre = "";
            }
        } catch (Exception e2) {
            this.tipo_evento_padre = "";
            e2.printStackTrace();
        }
        try {
            this.rating = c.getString(c.getColumnIndex("ZRATING"));

            if (TextUtils.isEmpty(this.rating)) {
                this.rating = "";
            }
        } catch (Exception e2) {
            this.rating = "";
            e2.printStackTrace();
        }
        try {
            this.description = c.getString(c.getColumnIndex(DB.KEY_EVENTO_NIETO_DESCRIPCION));// ok
        } catch (Exception e1) {
            this.description = "";
            e1.printStackTrace();
        }

        try {
            this.speakeruno = c.getString(c.getColumnIndex(DB.KEY_EVENTO_NIETO_SPEAKER_UNO));// ok
            if (TextUtils.isEmpty(this.speakeruno)) {
                this.speakeruno = "";
            }
        } catch (Exception e1) {
            this.speakeruno = "";
            e1.printStackTrace();
        }
        try {
            this.speakerdos = c.getString(c.getColumnIndex(DB.KEY_EVENTO_NIETO_SPEAKER_DOS));// ok
            if (TextUtils.isEmpty(this.speakerdos)) {
                this.speakerdos = "";
            }
        } catch (Exception e1) {
            this.speakerdos = "";
            e1.printStackTrace();
        }
        try {
            this.speakertres = c.getString(c.getColumnIndex("PERSONATRES"));// ok
            if (TextUtils.isEmpty(this.speakertres)) {
                this.speakertres = "";
            }
        } catch (Exception e1) {
            this.speakertres = "";
            e1.printStackTrace();
        }
        try {
            this.speakercuatro = c.getString(c.getColumnIndex("PERSONACUATRO"));// ok
            if (TextUtils.isEmpty(this.speakercuatro)) {
                this.speakercuatro = "";
            }
        } catch (Exception e1) {
            this.speakercuatro = "";
            e1.printStackTrace();
        }


        try {
            this.speakeExtra = c.getString(c.getColumnIndex("PERSONAEXTRA"));
            if (TextUtils.isEmpty(this.speakeExtra)) {
                this.speakeExtra = "";
            }
        } catch (Exception e1) {
            this.speakeExtra = "";
            e1.printStackTrace();
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
            this.evento_padre = c.getString(c
                    .getColumnIndex(DB.KEY_EVENTO_NIETO_EVENTO_PADRE));// ok
        } catch (Exception e1) {
            this.evento_padre = "";
            e1.printStackTrace();
        }
        try {
            this.imagen = c.getBlob(c.getColumnIndex("IMAGEN"));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        // Log.e("start_time", this.getStringStartTime());
        // Log.e("end_time", this.getStringEndTime());
        // Log.e("start_date", this.getStringStartDate());
        // Log.e("end_date", this.getStringEndDate());
       try {
            // Log.e("time", "parsed");

            start_ = sdf.parse(this.time_start);
            end_ = sdf.parse(this.time_end);
            alarma = sdf.parse(this.time_start);
        } catch (ParseException e) {
            Log.e("time", "parsed failed");
            e.printStackTrace();
        }
        Log.e("Event1", "original==>"+get_end_time_mili_seconds()+" alarma==> "+ get_alarm_time_mili_seconds());
        Log.v("Event1","date=> "+getDateToMilliseconds(get_start_time_mili_seconds(),"yyyy-MM-dd HH:mm:ss")+"alarma => "+getDateToMilliseconds(get_alarm_time_mili_seconds(),"yyyy-MM-dd HH:mm:ss"));
        try {
            time_start_date = Utilidades.getDate(this.time_start);
            time_start_time = Utilidades.getTime(this.time_start);
            time_end_date = Utilidades.getDate(this.time_end);
            time_end_time = Utilidades.getTime(this.time_end);
            time_alarm = Utilidades.getTime(getDateToMilliseconds(get_alarm_time_mili_seconds(),"yyyy-MM-dd HH:mm:ss"));
            milisecondevent = get_alarm_time_mili_seconds();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }


    /**
     * Get time in format: start_time-endtime
     *
     * @return
     */
    public String getTime() {
        return this.time_start + "-" + time_end;
    }

    public String getStringStartDate() {
        String result = String.format("%04d-%02d-%02d", time_start_date[0],
                time_start_date[1], time_start_date[2]);
        return result;
        // time_start_date[0] + Utility.spliter_date + time_start_date[1]
        // + Utility.spliter_date + time_start_date[2];
    }

    public String getStringStartTime() {
        return String.format("%02d:%02d", time_start_time[0],
                time_start_time[1]);
    }

    public String getStringEndDate() {
        return time_end_date[0] + Utilidades.spliter_date + time_end_date[1]
                + Utilidades.spliter_date + time_end_date[2];
    }

    public String getStringEndTime() {
        return String.format("%02d:%02d", time_end_time[0], time_end_time[1]);
    }

    public String getDayevent() {
        return Utilidades.getWeekday(time_start_date[2]);

    }


    // Check it is ongoing event or not
    public boolean isOnGoing() {
        Date current = new Date();//get date now
//		if (compare_date != null) {
//			current = compare_date;
//		} else {
//			current = new Date();
//		}
        if (start_ == null || end_ == null) {
            // Log.e("isOnGoing", "NULL");
            return false;
        }
        if (current.compareTo(start_) > 0 && current.compareTo(end_) < 0) {
            // Log.e("isOnGoing", "TTT");
            return true;
        } else {
            // Log.e("isOnGoing", "FFF");
            return false;
        }
    }

    public long get_start_time_mili_seconds() {
        return start_.getTime();
    }
    public long get_alarm_time_mili_seconds() {
        return (start_.getTime()-600000);
    }


    public long get_end_time_mili_seconds() {
        return end_.getTime();
    }

    public String toString() {
        return "padre_id: "+this.evento_padre_id+"start:" + this.time_start + " end:" + this.time_end + " title:"
                + this.titulo + " des:" + this.description + "person name:"
                + this.speakeruno + this.speakerdos;
    }
    private String getDateToMilliseconds(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        DateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
