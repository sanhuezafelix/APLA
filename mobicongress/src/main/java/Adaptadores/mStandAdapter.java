package Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import mc.mCongressDemo.R;

/**
 * Created by luisgonzalez on 13-03-14.
 */
public class mStandAdapter extends BaseAdapter {

    private String[] lista;
    private LayoutInflater Linflater;
    private int currentPosition;

    public mStandAdapter(Context context, String[] strings){
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
            arg1 = Linflater.inflate(R.layout.cell_stand,	null);
            contenedor = new VistaContenedor();
            contenedor.titulo = (TextView) arg1.findViewById(R.id.item_title);
            arg1.setTag(contenedor);
        }
        else{
            contenedor = (VistaContenedor)arg1.getTag();
        }

        contenedor.titulo.setText(lista[arg0]);
        return arg1;
    }
    public class VistaContenedor{

        TextView titulo;

    }

    public void set_cur_position(int position) {
        this.currentPosition = position;
        if (position >= 0) {
            this.notifyDataSetChanged();
        }
    }
}
