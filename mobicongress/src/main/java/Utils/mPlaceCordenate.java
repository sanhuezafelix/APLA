package Utils;

import android.util.Log;

/**
 * Created by luisgonzalez on 24-05-14.
 */
public class mPlaceCordenate {

    private float cordx;
    private float cordy;


    public mPlaceCordenate(String place){
        if (place.equals("Camelias")){
            setCordy(190);
            setCordx(80);
            Log.v("cord", place);
        }
        else if (place.equals("Chapultepec A")){
            setCordy(160);
            setCordx(80);
            Log.v("cord", place);
        }
        else if (place.equals("Chapultepec B")){
            setCordy(130);
            setCordx(80);
            Log.v("cord", place);
        }
        else if (place.equals("Girasoles Jardín")){
            setCordy(100);
            setCordx(80);
        }
        else if (place.equals("Molino A")){
            setCordy(70);
            setCordx(80);
        }
        else if (place.equals("Molino B")){
            setCordy(70);
            setCordx(80);
        }
        else if (place.equals("Oaxaca")){
            setCordy(70);
            setCordx(80);
        }
        else if (place.equals("Terraza B")){
            setCordy(70);
            setCordx(80);
        }
        else if (place.equals("Terraza B-C")){
            setCordy(70);
            setCordx(80);
        }
        else if (place.equals("Terraza C")){
            setCordy(70);
            setCordx(80);
        }

        else {
            setCordy(0);
            setCordx(0);
            Log.v("cord", "noplace");
        }


    }

    public float getCordx() {
        return this.cordx;
    }

    public float getCordy() {
        return this.cordy;
    }

    public void setCordx(float cord) {

        this.cordx = cord;
    }

    public void setCordy(float cordy) {
        this.cordy = cordy;
    }
}
