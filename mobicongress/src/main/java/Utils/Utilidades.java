package Utils;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.Random;

import mc.mCongressDemo.MainActivity;
import mc.mCongressDemo.R;
import mc.mCongressDemo.myApp;

/**
 * Created by luisgonzalez on 15-03-14.
 */
public class Utilidades {

    public static String spliter_space = " ";
    public static String spliter_time = ":";
    public static String spliter_date = "-";

    /**
     * Parse /year/day/month from datetime string Eg: 2013-05-29 14:30:00 +0000
     * => 2013-05-29
     *
     * @param datetime
     * @return String[0] year String[1] month String[2] day
     */
    public static int[] getDate(String datetime) {
        int[] result = new int[3];
        String arr1[] = datetime.split(spliter_space);
        String dateString = arr1[0];
        String arr2[] = dateString.split(spliter_date);
        for (int i = 0; i < 3; i++) {
            result[i] = Integer.parseInt(arr2[i]);
        }
        return result;
    }

    /**
     * Parse /year/day/month from datetime string Eg: 2013-05-29 14:30:00 +0000
     * => 2013-05-29
     *
     * @param datetime
     * @return String[0] hour (0-24) String[1] minute(0-60) String[2] second
     *         (0-60)
     */
    public static int[] getTime(String datetime) {
        int[] result = new int[3];
        String arr1[] = datetime.split(spliter_space);
        String dateString = arr1[1];
        String arr2[] = dateString.split(spliter_time);
        for (int i = 0; i < 3; i++) {
            result[i] = Integer.parseInt(arr2[i]);
        }
        return result;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView,
                                                        int size) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        View childView = listAdapter.getView(0, null, listView);
        childView.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        int itemHeight = childView.getMeasuredHeight()
                + listView.getDividerHeight();
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = itemHeight * size - 1;
        Log.d("thanhnx", "list view height=" + params.height);
        listView.setLayoutParams(params);
    }

    public static void animate(final ImageView imageView, final int resource_id, int Visibility) {
        int fadeInDuration = 2000; // Configure time values here
        int timeBetween = 3000;
        int fadeOutDuration = 1000;
// Visible or invisible by default - this will apply when the animation
// ends

       // imageView.setVisibility(Visibility);
        imageView.setImageResource(resource_id);

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
        fadeIn.setDuration(fadeInDuration);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
        fadeOut.setStartOffset(fadeInDuration + timeBetween);
        fadeOut.setDuration(fadeOutDuration);

        AnimationSet animation = new AnimationSet(false); // change to false
        animation.addAnimation(fadeIn);
// animation.addAnimation(fadeOut);
        animation.setRepeatCount(1);
        imageView.setAnimation(animation);

// animation.start();
    }

    public static int randInt(int current) {
        int min = 1;
        int max = 3;
// Usually this can be a field rather than a method variable
        Random rand = new Random();
// nextInt is normally exclusive of the top value,
// so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;
        while (current == randomNum) {
            randomNum = rand.nextInt((max - min) + 1) + min;
        }
        Log.e("input = " + current, "output= " + randomNum);
        return randomNum;
    }


    public static String getWeekday(int a) {
        String dia = new String();


        switch (a){
            case 8:
                 dia = myApp.getContext().getString(R.string.Sabado) +" 8";
                break;
            case 9:
                dia = myApp.getContext().getString(R.string.Domingo) +" 9";
                break;
            case 10:
                dia = myApp.getContext().getString(R.string.Lunes) +" 10";
                break;
            case 11:
                dia = myApp.getContext().getString(R.string.Martes) +" 11";
                break;
           // case 14:
               // dia = myApp.getContext().getString(R.string.Jueves) +" 14";
               // break;
            //case 15:
                //dia = myApp.getContext().getString(R.string.Viernes) +" 15";
                //break;
        }
        return dia;
    }

    public static String getAlphanumericCustomId(){
        char[] chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }










}
