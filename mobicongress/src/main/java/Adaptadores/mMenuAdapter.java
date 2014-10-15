package Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mc.mCongressDemo.R;

/**
 * Created by luisgonzalez on 13-03-14.
 */
public class mMenuAdapter extends BaseAdapter {

    private String[] lista;
    private LayoutInflater Linflater;
    private int currentPosition;

    public mMenuAdapter(Context context , String[] strings ){
        this.Linflater = LayoutInflater.from(context);
        this.lista = strings;

    }
    @Override
    public int getCount() {
        return lista.length;
    }

    @Override
    public Object getItem(int i) {
        return lista[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {

        VistaContenedor contenedor = null;

        if(arg1 == null)
        {
            arg1 = Linflater.inflate(R.layout.cell_menu,	null);
            contenedor = new VistaContenedor();
            contenedor.titulo = (TextView) arg1.findViewById(R.id.item_title);
            contenedor.layout = (RelativeLayout) arg1.findViewById(R.id.content_menu);
            contenedor.icono = (ImageView)arg1.findViewById(R.id.item_image);

            arg1.setTag(contenedor);
        }
        else{
            contenedor = (VistaContenedor)arg1.getTag();

        }
       /* if (arg0 == currentPosition){
            contenedor.layout.setBackgroundResource(R.drawable.menu_background_selected);
        }else {
            contenedor.layout.setBackgroundColor(Color.BLACK);
        }*/
        contenedor.titulo.setText(lista[arg0]);
       switch (arg0){
            case 0:
                contenedor.icono.setImageResource(R.drawable.icon_ahora);
                break;
            case 1:
                contenedor.icono.setImageResource(R.drawable.icon_noticias);
                break;
            case 2:
                contenedor.icono.setImageResource(R.drawable.icon_expositor);
                break;
            case 3:
                contenedor.icono.setImageResource(R.drawable.icon_mapa);
                break;
            case 4:
                contenedor.icono.setImageResource(R.drawable.icon_hoteles);
                break;
           case 5:
               contenedor.icono.setImageResource(R.drawable.chat);
               break;
           case 6:
               contenedor.icono.setImageResource(R.drawable.info);
               break;
           case 7:
               contenedor.icono.setImageResource(R.drawable.bill);
               break;
        }


        return arg1;
    }
    public class VistaContenedor{

        TextView titulo;
        RelativeLayout layout;
        ImageView icono;

    }

    public void set_cur_position(int position) {
        this.currentPosition = position;
        if (position >= 0) {
            this.notifyDataSetChanged();
        }
    }
}
