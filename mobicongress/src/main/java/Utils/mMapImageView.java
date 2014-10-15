package Utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import mc.mCongressDemo.R;
import mc.mCongressDemo.myApp;

/**
 * Created by luisgonzalez on 24-05-14.
 */
public class mMapImageView extends ImageView {
    float x=0;
    float y =0;
    private myApp Myapp;
    public mMapImageView(Context context) {
        super(context);
    }

    public mMapImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public mMapImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Resources res = getResources();
       // Bitmap mBitmap = Bitmap.createScaledBitmap(Bitmap src, int dstWidth, int dstHeight, boolean filter);
        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.redpin);



        System.out.println("Painting content");
       // Paint paint  = new Paint(Paint.LINEAR_TEXT_FLAG);
        Paint paint = new Paint();
        //paint.setStyle(Paint.Style.FILL);

        //paint.setColor(Color.GREEN);
        //paint.setTextSize(12.0F);

       // canvas.drawCircle(getx(), gety(), 10, paint);
        if (getx()!=0&&gety()!=0){
            canvas.drawBitmap(bitmap, getx(),gety(), paint);
        }
        System.out.println("Drawing text");
        //canvas.drawText("Hello World in custom view", 100, 100, paint);
    }
    public void setCordenate(float a , float b){
        this.x=a;
        this.y = b;
    }
    public float getx(){
        return this.x;
    }
    public float gety(){
        return this.y;
    }
    public static float dipToPixel(Context context, float dip) {
        return (float) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dip, context.getResources().getDisplayMetrics());
    }

}
