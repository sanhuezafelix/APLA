package DB;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by alvaro on 24-09-14.
 */
@ParseClassName("Novedad")
public class Novedad extends ParseObject {

    public String getTitulo() {
        return getString("Titulo");
    }
    public String getContenido() {
        return getString("Contenido");
    }
    public String getFechaInicio() {
        return getString("Fecha_inicio");
    }

    public static ParseQuery<Novedad> getQuery() {
        return ParseQuery.getQuery(Novedad.class);
    }

}