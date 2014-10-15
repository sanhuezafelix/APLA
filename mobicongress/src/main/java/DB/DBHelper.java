package DB;

import android.content.Context;
import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import mc.mCongressDemo.R;
import mc.mCongressDemo.myApp;

/**
 * Created by luisgonzalez on 14-03-14.
 */
public class DBHelper extends SQLiteOpenHelper {

    private Context context;

    public DBHelper(Context context){
        super(context, DB.DATABASE_NAME, null, DB.DATABASE_VERSION);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

        onCreate(sqLiteDatabase);

    }


    public String getDate() {
        String result = "";
        Date current_now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        result = sdf.format(current_now);
        return result;
    }
    public String getDateTime() {
        String result = "";
        Date current_now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        result = sdf.format(current_now);
        return result;
    }


    public Cursor getCongress(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT  Z_PK as _id ,ZTITULO , ZFECHAINICIO , ZFECHAFIN ,ZDESCRIPCION FROM ZCONGRESO  ORDER BY ZFECHAFIN DESC;";
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        return cursor;
    }




    public Cursor getCursorEventoAbuelo(int id){

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT  Z_PK as _id  ,ZCONGRESOPERTENEZCO  ,ZTITULO  ,ZDESCRIPCION FROM ZEVENTOABUELO WHERE  ZTITULO != ''  AND ZCONGRESOPERTENEZCO == "+id+" ORDER BY _id ASC";
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getCursorAhora(int congress){

        String date_time = getDateTime();
        String date = getDate();
        if (TextUtils.isEmpty(date_time + date)) {
            return null;
        }

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT A.Z_PK as _id, A.ZTITULO , A.ZTEMATICA AS ZTEMATICA, A.ZSPEAKERUNO , A.ZSPEAKERDOS, A.ZTIPOEVENTO, A.ZDESCRIPCION,A.ZFECHAINICIO , A.ZFECHAFIN , A.ZLUGAR ,A.ZEVENTOABUELO  ,A.ZESEXTRA,A.ZESUNICO,A.ZESTADO,A.ZEVENTOPADRE ," +
                "                B.ZTITULO PADRE ,B.ZTIPOEVENTO TIPOPADRE ,C.ZTRATAMIENTO ||' '||C.ZNOMBRE ||' '|| C.ZAPELLIDO as PERSONAUNO  ,D.ZTRATAMIENTO ||' '||D.ZNOMBRE || ' ' || D.ZAPELLIDO  as PERSONADOS  FROM ZEVENTONIETO A" +
                "                  LEFT JOIN ZEVENTOPADRE B ON A.ZEVENTOPADRE = B.Z_PK LEFT  JOIN ZPERSONA C ON C.Z_PK= A.ZSPEAKERUNO  LEFT JOIN ZPERSONA D ON  D.Z_PK == A.ZSPEAKERDOS  LEFT JOIN ZCONGRESO E ON A.ZFECHAINICIO  >= E.ZFECHAINICIO  AND A.ZFECHAFIN <= E.ZFECHAFIN WHERE (E.Z_PK == "+congress;
        query = query
                + " AND A.ZFECHAINICIO "
                + " LIKE '" + date + "%'"
                + " AND "
                + "A.ZFECHAFIN >= '" + date_time + "'  AND A.ZTIPOEVENTO NOT LIKE 'Accompanying Program%') "
        ;
        query = query + " AND  A.ZIDIOMA LIKE '"+ myApp.getContext().getString(R.string.idioma)+"%' order  by datetime(replace(A.ZFECHAINICIO,' +0000','')) ASC";

        Cursor cursor = db.rawQuery(query,null);
        Log.v("ahora",query);

        return cursor;

    }

    public Cursor getEventDetail(int _id){

        SQLiteDatabase db = this.getReadableDatabase();
        String query ="SELECT A.Z_PK as _id, A.ZTITULO , A.ZSPEAKERUNO ,A.ZRATING , A.ZISRATING, A.ZSPEAKERDOS,A.ZSPEAKERTRES,A.ZSPEAKERCUATRO,A.ZPERSONASEXTRAS as PERSONAEXTRA, A.ZTIPOEVENTO, A.ZDESCRIPCION,A.ZFECHAINICIO , A.ZFECHAFIN , A.ZLUGAR ,A.ZEVENTOABUELO ,A.ZESEXTRA,A.ZESUNICO,A.ZESTADO,A.ZEVENTOPADRE ," +
                " B.ZTITULO PADRE ,B.ZTIPOEVENTO TIPOPADRE ,C.ZTRATAMIENTO ||' '||C.ZNOMBRE ||' '|| C.ZAPELLIDO as PERSONAUNO  ,D.ZTRATAMIENTO ||' '||D.ZNOMBRE || ' ' || D.ZAPELLIDO  as PERSONADOS,X.ZTRATAMIENTO ||' '||X.ZNOMBRE || ' ' || X.ZAPELLIDO  as PERSONATRES,Y.ZTRATAMIENTO ||' '||Y.ZNOMBRE || ' ' || Y.ZAPELLIDO  as PERSONACUATRO, A.ZPERSONASEXTRAS AS PERSONAEXTRA ,E.ZIMAGENUNO as IMAGEN FROM ZEVENTONIETO A" +
                " LEFT JOIN ZEVENTOPADRE B ON A.ZEVENTOPADRE = B.Z_PK LEFT  JOIN ZPERSONA C ON C.Z_PK= A.ZSPEAKERUNO  LEFT JOIN ZPERSONA D ON  D.Z_PK == A.ZSPEAKERDOS " +
                " LEFT JOIN ZPERSONA X ON  X.Z_PK == A.ZSPEAKERTRES LEFT JOIN ZPERSONA Y ON  Y.Z_PK == A.ZSPEAKERCUATRO  LEFT JOIN ZIMAGEN E ON  E.Z_PK == A.ZIDIMAGEN  WHERE A.Z_PK == "+_id;
        Log.v("query-detail",query);
        Cursor cursor = db.rawQuery(query,null);
        return cursor;

    }






    public Cursor getAllEventPrograma(String date){

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM (SELECT A.Z_PK _id, A.ZTITULO ZTITULO ,A.ZTEMATICA AS ZTEMATICA, A.ZIDIOMA AS ZIDIOMA ,A.ZFECHAINICIO ZFECHAINICIO,A.ZFECHAFIN ZFECHAFIN, A.ZFAVORITO ZFAVORITO, A.ZESUNICO ZESUNICO ,A.ZESEXTRA ZESEXTRA  ,A.ZLUGAR ZLUGAR, A.ZTIPOEVENTO ZTIPOEVENTO, B.ZTRATAMIENTO ||' '||B.ZNOMBRE ||' '|| B.ZAPELLIDO as PERSONAUNO  ,C.ZTRATAMIENTO ||' '||C.ZNOMBRE || ' ' || C.ZAPELLIDO  as PERSONADOS ,A.ZPERSONASEXTRAS as PERSONAEXTRA FROM ZEVENTONIETO A LEFT JOIN ZPERSONA B ON  B.Z_PK == A.ZSPEAKERUNO LEFT JOIN ZPERSONA C ON  C.Z_PK == A.ZSPEAKERDOS   WHERE ZFECHAINICIO LIKE '"+date+"%'  AND (ZESEXTRA ==1 OR  ZESUNICO ==1)  " +
                //"UNION ALL SELECT H.Z_PK  _id ,H.ZTITULO,  H.ZFECHAINICIO ,H.ZFECHAFIN ,0 , 0 , 0 , H.ZLUGAR , H.ZTIPOEVENTO , E.ZTRATAMIENTO ||' '||E.ZNOMBRE ||' '|| E.ZAPELLIDO as PERSONAUNO  ,F.ZTRATAMIENTO ||' '||F.ZNOMBRE || ' ' || F.ZAPELLIDO  as PERSONADOS ,H.ZPERSONASEXTRAS as PERSONAEXTRA FROM ZEVENTOPADRE H LEFT JOIN ZPERSONA E ON  E.Z_PK == H.ZMODERADORUNO LEFT JOIN ZPERSONA F ON  F.Z_PK == H.ZMODERADORDOS   WHERE H.ZFECHAINICIO  LIKE '"+date+"%' AND H.ZTIPOEVENTO NOT LIKE 'Accompanying Program%'" +
                "UNION ALL SELECT H.Z_PK  _id ,H.ZTITULO,H.ZTEMATICA,H.ZIDIOMA , H.ZFECHAINICIO ,H.ZFECHAFIN ,0 , 0 , 0 , H.ZLUGAR , H.ZTIPOEVENTO , E.ZTRATAMIENTO ||' '||E.ZNOMBRE ||' '|| E.ZAPELLIDO as PERSONAUNO  ,F.ZTRATAMIENTO ||' '||F.ZNOMBRE || ' ' || F.ZAPELLIDO  as PERSONADOS ,H.ZPERSONASEXTRAS as PERSONAEXTRA FROM ZEVENTOPADRE H LEFT JOIN ZPERSONA E ON  E.Z_PK == H.ZMODERADORUNO LEFT JOIN ZPERSONA F ON  F.Z_PK == H.ZMODERADORDOS   WHERE (H.ZFECHAINICIO  LIKE '"+date+"%' AND H.ZEVENTOABUELO  LIKE 0) " +
                "UNION ALL SELECT X.Z_PK  _id ,X.ZTITULO,X.ZTEMATICA ,X.ZIDIOMA,  X.ZFECHAINICIO ,X.ZFECHAFIN ,0 , 4 , NULL , NULL, NULL , J.ZTRATAMIENTO ||' '||J.ZNOMBRE ||' '|| J.ZAPELLIDO as PERSONAUNO  ,K.ZTRATAMIENTO ||' '||K.ZNOMBRE || ' ' || K.ZAPELLIDO  as PERSONADOS, null FROM ZEVENTOABUELO X LEFT JOIN ZPERSONA J ON  J.Z_PK == X.ZPERSONAABUELOUNO LEFT JOIN ZPERSONA K ON  K.Z_PK == X.ZPERSONAABUELODOS WHERE ZFECHAINICIO LIKE '"+date+"%') WHERE ZIDIOMA LIKE '"+ myApp.getContext().getString(R.string.idioma)+"%' OR ZIDIOMA LIKE 'neutro'  ORDER BY ZFECHAINICIO ASC";
        Log.v("query_JORNI",query);
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        return cursor;
    }


    public Cursor getModuloActual(int _id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT A.Z_PK as _id ,A.ZTITULO , A.ZFECHAINICIO , A.ZFECHAFIN ,A.ZLUGAR , A.ZDESCRIPCION," +
                " B.ZTRATAMIENTO ||' '||B.ZNOMBRE ||' '|| B.ZAPELLIDO as PERSONAUNO,  C.ZTRATAMIENTO ||' '||C.ZNOMBRE ||' '|| C.ZAPELLIDO as PERSONADOS ,  A.ZPERSONASEXTRAS as PERSONAEXTRA " +
                " ,A.ZTIPOEVENTO FROM ZEVENTOPADRE A LEFT JOIN ZPERSONA B ON B.Z_PK= A.ZMODERADORUNO LEFT JOIN ZPERSONA C ON C.Z_PK= A.ZMODERADORDOS  WHERE _id == "+_id;

        Log.e("evento_actual",query);
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        return c;
    }

    public Cursor getEventosNietos(int padre){

        SQLiteDatabase db = this.getReadableDatabase();
        String query ="SELECT A.Z_PK as _id ,A.ZTITULO ,A.ZTIPOEVENTO , A.ZDESCRIPCION ,A.ZLUGAR, A.ZFAVORITO," +
                " A.ZFECHAINICIO , A.ZFECHAFIN  , B.ZTRATAMIENTO ||' '||B.ZNOMBRE ||' '|| B.ZAPELLIDO as PERSONAUNO," +
                "  C.ZTRATAMIENTO ||' '||C.ZNOMBRE ||' '|| C.ZAPELLIDO as PERSONAUNO ,A.ZPERSONASEXTRAS as PERSONAEXTRA FROM ZEVENTONIETO A " +
                "LEFT JOIN ZPERSONA B ON B.Z_PK= A.ZSPEAKERUNO " +
                "LEFT JOIN ZPERSONA C ON C.Z_PK= A.ZSPEAKERDOS LEFT JOIN ZPERSONA D ON D.Z_PK= A.ZPERSONASEXTRAS WHERE A.ZEVENTOPADRE== "+padre ;
        Log.e("query_nietos", query);

        Cursor c = db.rawQuery(query,null);

        c.moveToFirst();
        return c;
    }



    public Cursor getCursorSearchSpeaker(int _id ,String s){

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT A.Z_PK as _id, A.ZTRATAMIENTO ||' '||A.ZNOMBRE ||' '|| A.ZAPELLIDO as EXPOSITOR " +
                ",ZLUGAR, A.ZBIO ,B.ZIMAGENUNO as IMAGEN FROM ZPERSONA A LEFT JOIN ZIMAGEN B ON B.Z_PK LIKE A.ZIDIMAGEN ";

        if (TextUtils.isEmpty(s)) {
            query = query + " WHERE A.ZCONGRESO LIKE " + _id +" ORDER BY A.ZNOMBRE ASC";
            Log.v("speaker no text",query);
        }
        else{
            query = query + " WHERE replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace( replace( replace( replace( replace( replace( replace( replace( replace( replace( replace( replace( replace( replace( lower( EXPOSITOR)" +
                    ", '‡','a'), '¸','u'), '˚','u'), '˘','u'), 'Ò','n'), 'ˆ','o'), 'Ú','o'), 'Ô','IPVS'), 'Ó','IPVS'), 'Ï','IPVS'), 'Î','e'), '‰','a'), '‡','a'), '·','a'), '„','a'), '‚','a'), 'È','e'), 'Í','e'), 'Ì','IPVS'),'Û','o') ,'ı','o') ,'Ù','o'),'˙','u'), 'Á','c'),'á','a'),'é','e'),'í','IPVS'),'ó','o'),'ú','u')" +
                    "  LIKE '%"+s+"%' AND A.ZCONGRESO LIKE "+_id+" ORDER BY A.ZNOMBRE ASC";


        }
        Cursor c = db.rawQuery(query,null);



        Log.v("speaker",query);

        c.moveToFirst();
        return c;
    }


    public Cursor getCursorDetalleEventosSpeaker(int _id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT A.Z_PK as _id, A.ZTRATAMIENTO ||' '||A.ZNOMBRE ||' '|| A.ZAPELLIDO as EXPOSITOR " +
                ",ZLUGAR, A.ZBIO ,B.ZIMAGENUNO as IMAGEN FROM ZPERSONA A LEFT JOIN ZIMAGEN B ON B.Z_PK LIKE A.ZIDIMAGEN WHERE A.Z_PK == "+_id;


        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        return c;
    }

    public Cursor getCursorAlleventoSpeaker(int _id){
        SQLiteDatabase db = this.getReadableDatabase();
        /*String query = "SELECT A.Z_PK as _id, A.ZTITULO , A.ZSPEAKERUNO , A.ZSPEAKERDOS, A.ZTIPOEVENTO, A.ZDESCRIPCION,A.ZFECHAINICIO , A.ZFECHAFIN , A.ZLUGAR ,A.ZABUELOPERTENEZCO  ,A.ZESEXTRA,A.ZESUNICO,A.ZESTADO,A.ZEVENTOPADRE , B.ZTITULO PADRE  ,C.ZTRATAMIENTO ||' '||C.ZNOMBRE ||' '|| C.ZAPELLIDO as PERSONAUNO  ,D.ZTRATAMIENTO ||' '||D.ZNOMBRE || ' ' || D.ZAPELLIDO  as PERSONADOS  FROM ZEVENTONIETO A\n" +
                "LEFT JOIN ZEVENTOPADRE B ON A.ZEVENTOPADRE = B.Z_PK LEFT  JOIN ZPERSONA C ON C.Z_PK= A.ZSPEAKERUNO  LEFT JOIN ZPERSONA D ON  D.Z_PK == A.ZSPEAKERDOS   WHERE A.ZSPEAKERUNO == "+_id+ " OR A.ZSPEAKERDOS == "+_id+ " ORDER BY A.ZFECHAINICIO ASC";*/
        String query = "SELECT * FROM (SELECT A.Z_PK as _id, A.ZTITULO ZTITULO ,A.ZPERSONASEXTRAS PERSONAEXTRA,A.ZTEMATICA AS ZTEMATICA,A.ZIDIOMA AS ZIDIOMA, A.ZSPEAKERUNO ZSPEAKERUNO , A.ZSPEAKERDOS ZSPEAKERDOS, A.ZSPEAKERTRES ZSPEAKERTRES ,A.ZSPEAKERCUATRO ZSPEAKERCUATRO, A.ZFECHAINICIO ZFECHAINICIO , A.ZFECHAFIN ZFECHAFIN , A.ZLUGAR ZLUGAR ,A.ZEVENTOABUELO ZEVENTOABUELO  ,A.ZESEXTRA ZESEXTRA ,A.ZESUNICO ZESUNICO,A.ZEVENTOPADRE  ZEVENTOPADRE , B.ZTITULO PADRE  ,C.ZTRATAMIENTO ||' '||C.ZNOMBRE ||' '|| C.ZAPELLIDO as PERSONAUNO  ,D.ZTRATAMIENTO ||' '||D.ZNOMBRE || ' ' || D.ZAPELLIDO  as PERSONADOS ,J.ZTRATAMIENTO ||' '||J.ZNOMBRE || ' ' || J.ZAPELLIDO  as PERSONATRES,K.ZTRATAMIENTO ||' '||K.ZNOMBRE || ' ' || K.ZAPELLIDO  as PERSONACUATRO   FROM ZEVENTONIETO A " +
                "LEFT JOIN ZEVENTOPADRE B ON A.ZEVENTOPADRE = B.Z_PK LEFT  JOIN ZPERSONA C ON C.Z_PK= A.ZSPEAKERUNO  LEFT JOIN ZPERSONA D ON  D.Z_PK == A.ZSPEAKERDOS  LEFT JOIN ZPERSONA J ON  J.Z_PK == A.ZSPEAKERTRES  LEFT JOIN ZPERSONA K ON  K.Z_PK == A.ZSPEAKERCUATRO   WHERE A.ZSPEAKERUNO == "+_id+" OR A.ZSPEAKERDOS =="+_id+" OR A.ZSPEAKERTRES =="+_id+" OR A.ZSPEAKERCUATRO =="+_id;
        query = query + " UNION ALL ";
        query = query + "SELECT E.Z_PK as _id, E.ZTITULO  ,E.ZPERSONASEXTRAS, E.ZTEMATICA ,E.ZIDIOMA ,E.ZMODERADORUNO , E.ZMODERADORDOS,E.ZMODERADORTRES,0, E.ZFECHAINICIO INICIO, E.ZFECHAFIN , E.ZLUGAR , E.ZEVENTOABUELO , NULL , 2  , NULL , F.ZTITULO ,G.ZTRATAMIENTO ||' '||G.ZNOMBRE ||' '|| G.ZAPELLIDO as PERSONAUNO  ,H.ZTRATAMIENTO ||' '||H.ZNOMBRE || ' ' || H.ZAPELLIDO  as PERSONADOS  ,I.ZTRATAMIENTO ||' '||I.ZNOMBRE || ' ' || I.ZAPELLIDO  as PERSONATRES, NULL   FROM ZEVENTOPADRE E LEFT JOIN  ZEVENTOABUELO F ON E.ZEVENTOABUELO == F.Z_PK   LEFT  JOIN ZPERSONA G ON G.Z_PK= E.ZMODERADORUNO  LEFT JOIN ZPERSONA H ON  H.Z_PK == E.ZMODERADORDOS LEFT JOIN ZPERSONA I ON  I.Z_PK == E.ZMODERADORTRES  WHERE E.ZMODERADORUNO == " + _id +" OR E.ZMODERADORDOS == "+_id+" OR E.ZMODERADORTRES == "+_id+" ) WHERE ZIDIOMA LIKE '"+ myApp.getContext().getString(R.string.idioma)+"%' OR ZIDIOMA LIKE 'neutro'  ORDER BY ZFECHAINICIO ASC";


        Cursor c = db.rawQuery(query,null);
        Log.e("sqlite_error_SPEAKER",query);
        c.moveToFirst();
        return c;
    }
    public Cursor getCursorAlleventoFavorito(int _id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT A.Z_PK as _id, A.ZTITULO , A.ZSPEAKERUNO , A.ZSPEAKERDOS, A.ZTIPOEVENTO, A.ZDESCRIPCION,A.ZFECHAINICIO , A.ZFECHAFIN , A.ZLUGAR ,A.ZEVENTOABUELO  ,A.ZESEXTRA,A.ZESUNICO,A.ZESTADO,A.ZEVENTOPADRE , B.ZTITULO PADRE  ,C.ZTRATAMIENTO ||' '||C.ZNOMBRE ||' '|| C.ZAPELLIDO as PERSONAUNO  ,D.ZTRATAMIENTO ||' '||D.ZNOMBRE || ' ' || D.ZAPELLIDO  as PERSONADOS  FROM ZEVENTONIETO A " +
                "LEFT JOIN ZEVENTOPADRE B ON A.ZEVENTOPADRE = B.Z_PK LEFT  JOIN ZPERSONA C ON C.Z_PK= A.ZSPEAKERUNO  LEFT JOIN ZPERSONA D ON  D.Z_PK == A.ZSPEAKERDOS   WHERE A.ZFAVORITO LIKE 1 ORDER BY A.ZFECHAINICIO ASC";

        Cursor c = db.rawQuery(query,null);
        Log.e("sqlite_error",query);
        c.moveToFirst();
        return c;
    }
    public Cursor getCursorHotel(int _id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT A.Z_PK as _id ,A.ZESTADO ,A.ZCONGRESO,A.ZIDIOMA, A.ZNOMBRE ,A.ZCONTENIDO,A.ZFONO, A.ZDIRECCION ,A.ZLUGAR,A.ZTIPO , A.ZWEB , B.ZIMAGENUNO AS FONDO ,B.ZIMAGENDOS AS ICONO FROM ZHOTEL A LEFT JOIN ZIMAGEN B ON B.Z_PK == A.ZIDIMAGEN WHERE A.ZCONGRESO == "+_id +" AND  A.ZIDIOMA LIKE '"+ myApp.getContext().getString(R.string.idioma)+"%' ";

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        return c;
    }

    public Cursor getCursorDetalleHotel(int _id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT A.Z_PK as _id ,A.ZESTADO ,A.ZCONGRESO, A.ZIDIOMA, A.ZNOMBRE ,A.ZCONTENIDO,A.ZFONO, A.ZDIRECCION ,A.ZLUGAR,A.ZTIPO , A.ZWEB , B.ZIMAGENUNO AS FONDO ,B.ZIMAGENDOS AS ICONO , B.ZIMAGENTRES AS MAP FROM ZHOTEL A  LEFT JOIN ZIMAGEN B ON B.Z_PK == A.ZIDIMAGEN WHERE _id == "+_id +" AND  A.ZIDIOMA LIKE '"+ myApp.getContext().getString(R.string.idioma)+"%' ";;

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        return c;
    }
    public Cursor getCursorImage(String tipo){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT Z_PK as _id ,A.ZTIPOIMAGEN,A.ZIMAGENUNO,A.ZIMAGENDOS,A.ZIMAGENTRES FROM ZIMAGEN A WHERE A.ZTIPOIMAGEN LIKE '"+tipo +"%'";
        Cursor c = db.rawQuery(query,null);
        Log.v("imagen_query",query);
        c.moveToFirst();
        return c;
    }

    public void updateFavoriteList(int id , int b){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE ZEVENTONIETO SET ZFAVORITO = "+b+"  WHERE Z_PK="+id+";";
        db.execSQL(query);
    }
    public int isFavoriteCurrentEvent(int id ){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT ZFAVORITO FROM ZEVENTONIETO  WHERE ZFAVORITO = 1 AND  Z_PK = "+id+";";
        Cursor c = db.rawQuery(query,null);
        return c.getCount();
    }
    public String getTextNotePad(int id){
        String Texto = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT ZTEXTNOTEPAD FROM ZEVENTONIETO  WHERE ZNOTEPAD = 1 AND  Z_PK = "+id+";";
        Cursor c = db.rawQuery(query,null);
        if (c.getCount()<1){
            Texto = "";
        }
        else {
            c.moveToFirst();
            Texto = c.getString(c.getColumnIndex("ZTEXTNOTEPAD"));
        }

        return Texto;

    }

    public void updateNotePad(int id , int b,String note){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE ZEVENTONIETO SET ZNOTEPAD = "+b+" , ZTEXTNOTEPAD = '"+note+"'  WHERE Z_PK="+id+";";
        db.execSQL(query);
    }
    public int isNotePad(int id ){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT ZNOTEPAD FROM ZEVENTONIETO  WHERE ZNOTEPAD = 1 AND  Z_PK = "+id+";";
        Cursor c = db.rawQuery(query,null);
        return c.getCount();
    }

    public Cursor getCursorAlleventNotePad(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT A.Z_PK as _id, A.ZTITULO , A.ZSPEAKERUNO , A.ZSPEAKERDOS, A.ZTIPOEVENTO, A.ZDESCRIPCION,A.ZFECHAINICIO , A.ZFECHAFIN , A.ZLUGAR ,A.ZEVENTOABUELO  ,A.ZESEXTRA,A.ZESUNICO,A.ZESTADO,A.ZEVENTOPADRE , B.ZTITULO PADRE  ,C.ZTRATAMIENTO ||' '||C.ZNOMBRE ||' '|| C.ZAPELLIDO as PERSONAUNO  ,D.ZTRATAMIENTO ||' '||D.ZNOMBRE || ' ' || D.ZAPELLIDO  as PERSONADOS  FROM ZEVENTONIETO A " +
                "LEFT JOIN ZEVENTOPADRE B ON A.ZEVENTOPADRE = B.Z_PK LEFT  JOIN ZPERSONA C ON C.Z_PK= A.ZSPEAKERUNO  LEFT JOIN ZPERSONA D ON  D.Z_PK == A.ZSPEAKERDOS   WHERE A.ZNOTEPAD LIKE 1 ORDER BY A.ZFECHAINICIO ASC";

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        return c;
    }


    public Cursor getCursorGeneralSearch(int congress,String s){

        String[] columnas= new String[]{"A.ZTITULO","A.ZLUGAR","PERSONAUNO","PERSONADOS","PADRE"};

        String date_time = getDateTime();
        String date = getDate();
        if (TextUtils.isEmpty(date_time + date)) {
            return null;
        }
        if (s.equals("")){
            return null;
        }

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT A.Z_PK as _id, A.ZTITULO , A.ZTEMATICA ,A.ZIDIOMA, A.ZSPEAKERUNO , A.ZSPEAKERDOS, A.ZTIPOEVENTO, A.ZDESCRIPCION,A.ZFECHAINICIO , A.ZFECHAFIN , A.ZLUGAR ,A.ZEVENTOABUELO  ,A.ZESEXTRA,A.ZESUNICO,A.ZESTADO,A.ZEVENTOPADRE ," +
                "                B.ZTITULO PADRE ,B.ZTIPOEVENTO TIPOPADRE ,C.ZTRATAMIENTO ||' '||C.ZNOMBRE ||' '|| C.ZAPELLIDO as PERSONAUNO  ,D.ZTRATAMIENTO ||' '||D.ZNOMBRE || ' ' || D.ZAPELLIDO  as PERSONADOS  FROM ZEVENTONIETO A" +
                "                  LEFT JOIN ZEVENTOPADRE B ON A.ZEVENTOPADRE = B.Z_PK LEFT  JOIN ZPERSONA C ON C.Z_PK= A.ZSPEAKERUNO  LEFT JOIN ZPERSONA D ON  D.Z_PK == A.ZSPEAKERDOS  LEFT JOIN ZCONGRESO E ON A.ZFECHAINICIO  >= E.ZFECHAINICIO  AND A.ZFECHAFIN <= E.ZFECHAFIN ";

            query = query + " WHERE (A.ZTIPOEVENTO NOT LIKE 'Accompanying Program%' AND (";
            for (int i = 0; i < columnas.length ; i++) {
                query+= " replace(replace(replace(replace(replace(lower("+columnas[i]+") ,'á','a'),'é','e'),'í','i'),'ó','o'),'ú','u') LIKE '%"+s+"%'";
                if (i<columnas.length-1){
                    query += " OR ";
                }
            }
            query += "AND A.ZCONGRESO LIKE "+congress+" )) AND A.ZIDIOMA LIKE '"+ myApp.getContext().getString(R.string.idioma)+"%' OR A.ZIDIOMA LIKE 'neutro'  ORDER BY datetime(replace(A.ZFECHAINICIO,' +0000','')) ASC";


        Cursor cursor = db.rawQuery(query,null);
        Log.e("gral_search",query);

        return cursor;

    }

    public Cursor getCursorAcompanantes(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT A.Z_PK as _id, A.ZTITULO , A.ZMODERADORUNO , A.ZMODERADORDOS, A.ZTIPOEVENTO, A.ZDESCRIPCION,A.ZFECHAINICIO , A.ZFECHAFIN , A.ZLUGAR ,A.ZEVENTOABUELO ,A.ZESTADO,A.ZCONGRESOPERTENEZCO  FROM ZEVENTOPADRE A    WHERE A.ZTIPOEVENTO LIKE 'Accompanying Program%'";

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        return c;
    }
    public Cursor getCursorAlleventoAcompanante(int _id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT A.Z_PK as _id, A.ZTITULO , A.ZSPEAKERUNO , A.ZSPEAKERDOS, A.ZTIPOEVENTO, A.ZDESCRIPCION,A.ZFECHAINICIO , A.ZFECHAFIN , A.ZLUGAR ,A.ZEVENTOABUELO  ,A.ZESEXTRA,A.ZESUNICO,A.ZESTADO,A.ZEVENTOPADRE , B.ZTITULO PADRE  ,C.ZTRATAMIENTO ||' '||C.ZNOMBRE ||' '|| C.ZAPELLIDO as PERSONAUNO  ,D.ZTRATAMIENTO ||' '||D.ZNOMBRE || ' ' || D.ZAPELLIDO  as PERSONADOS  FROM ZEVENTONIETO A " +
                "LEFT JOIN ZEVENTOPADRE B ON A.ZEVENTOPADRE = B.Z_PK LEFT  JOIN ZPERSONA C ON C.Z_PK= A.ZSPEAKERUNO  LEFT JOIN ZPERSONA D ON  D.Z_PK == A.ZSPEAKERDOS   WHERE A.ZEVENTOPADRE LIKE "+_id +"  ORDER BY A.ZFECHAINICIO ASC";
        Log.v("acomp_search",query);
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        return c;
    }

    public Cursor getAbueloActual(int _id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = " SELECT A.Z_PK as _id ,A.ZTITULO , A.ZFECHAINICIO , A.ZFECHAFIN  FROM ZEVENTOABUELO A WHERE A.Z_PK ==  "+_id;
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        return c;
    }
    public Cursor getEventosPadres(int ABUELO){

        SQLiteDatabase db = this.getReadableDatabase();

        String query ="SELECT Z_PK AS _id,  ZTITULO ,ZTEMATICA, ZFECHAFIN , ZFECHAINICIO FROM ZEVENTOPADRE WHERE ZEVENTOABUELO LIKE  " +ABUELO+ " AND ZTEMATICA LIKE '"+ myApp.getContext().getString(R.string.idioma)+"%' OR ZTEMATICA LIKE 'neutro' ORDER BY ZFECHAINICIO ASC" ;
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        return c;
    }

    public void updateRating(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE ZEVENTONIETO SET ZISRATING = 1   WHERE Z_PK="+id+";";
        db.execSQL(query);
    }


    public Cursor getCursorStand(int _id, String _categoria){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT A.Z_PK as _id ,A.ZESTADO ,A.ZDESCRIPCION ,A.ZCATEGORIA,A.ZCONGRESO, A.ZNOMBRE ,A.ZFONO,A.ZLUGAR , A.ZWEB , B.ZIMAGENUNO AS IMAGEN ,B.ZIMAGENDOS AS ICONO FROM ZSTAND A LEFT JOIN ZIMAGEN B ON B.Z_PK == A.ZIDIMAGEN WHERE A.ZCONGRESO == "+_id +" AND A.ZCATEGORIA == '"+_categoria+"'";

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        return c;
    }

    public Cursor getCursorDetalleStand(int _id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT A.Z_PK as _id ,A.ZESTADO ,A.ZDESCRIPCION, A.ZCATEGORIA ,A.ZCONGRESO, A.ZNOMBRE ,A.ZFONO, A.ZLUGAR , A.ZWEB , B.ZIMAGENUNO AS IMAGEN ,B.ZIMAGENDOS AS ICONO FROM ZSTAND A  LEFT JOIN ZIMAGEN B ON B.Z_PK == A.ZIDIMAGEN WHERE _id == "+_id;

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        return c;
    }





}
