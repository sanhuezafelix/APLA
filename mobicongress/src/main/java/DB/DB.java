package DB;

/**
 * Created by luisgonzalez on 14-03-14.
 */
public class DB {
    // DB NAME AND VERSION
    public static final String DATABASE_NAME = "cnm_2014.sqlite";
    public static final int DATABASE_VERSION = 1;
    /*
     CONGRESO

     */
    public static final String KEY_TABLE_CONGRESO = "ZCONGRESO";
    public static final String KEY_CONGRESO_ID_CONGRESO = "_id";
    public static final String KEY_CONGRESO_DESCRIPCION = "ZDESCRIPCION";
    public static final String KEY_CONGRESO_TITULO = "ZTITULO";
    public static final String KEY_CONGRESO_FECHAINICIO = "ZFECHAINICIO";
    public static final String KEY_CONGRESO_FECHAFIN = "ZFECHAFIN";
    /*
                EVENTO ABUELO
     */
    public static final String KEY_TABLE_EVENTO_ABUELO = "ZEVENTOABUELO";
    public static final String KEY_EVENTO_ABUELO_ID = "_id";
    public static final String KEY_EVENTO_ABUELO_TITULO = "ZTITULO";
    public static final String KEY_EVENTO_ABUELO_CONGRESO = "ZCONGRESOPERTENEZCO";
    public static final String KEY_EVENTO_ABUELO_DESCRIPCION = "ZDESCRIPCION";
    public static final String KEY_EVENTO_ABUELO_ESTADO = "ZESTADO";

/*
            EVENTO PADRE
 */
    public static final String KEY_TABLE_EVENTO_PADRE = "ZEVENTOPADRE";
/*
        EVENTO NIETO
 */
    public static final String KEY_TABLE_EVENTO_NIETO = "ZEVENTONIETO";
    public static final String KEY_EVENTO_NIETO_TITULO = "ZTITULO";
    public static final String KEY_EVENTO_NIETO_EVENTO_PADRE_ID = "ZEVENTOPADRE";
    public static final String KEY_EVENTO_NIETO_EVENTO_PADRE = "PADRE";
    public static final String KEY_EVENTO_NIETO_FECHAFIN = "ZFECHAFIN";
    public static final String KEY_EVENTO_NIETO_FECHAINICIO = "ZFECHAINICIO";
    public static final String KEY_EVENTO_NIETO_DESCRIPCION = "ZDESCRIPCION";
    public static final String KEY_EVENTO_NIETO_LUGAR = "ZLUGAR";
    public static final String KEY_EVENTO_NIETO_SPEAKER_UNO = "PERSONAUNO";
    public static final String KEY_EVENTO_NIETO_SPEAKER_DOS = "PERSONADOS";
    public static final String KEY_EVENTO_NIETO_TIPO_EVENTO = "ZTIPOEVENTO";

    public static final String KEY_TABLE_IMAGEN = "ZIMAGEN";
    public static final String KEY_TABLE_NOVEDAD = "ZNOVEDAD";
    public static final String KEY_TABLE_TRABAJO_LIBRE = "ZTRABAJOLIBRE";

    /* DATOS CONGRESO */

}
