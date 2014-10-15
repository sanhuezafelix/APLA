package Fragments.chat.adaptadores;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quickblox.module.chat.QBRoster;
import com.quickblox.module.chat.model.QBPresence;
import com.quickblox.module.users.model.QBUser;

import org.jivesoftware.smack.SmackException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mc.mCongressDemo.MainActivity;
import mc.mCongressDemo.R;
import mc.mCongressDemo.myApp;

/**
 * Created by igorkhomenko on 9/12/14.
 */
public class UsersAdapterChat extends BaseAdapter implements Filterable {

    private List<QBUser> datalist;
    private List<QBUser> basedatalist;
    private LayoutInflater inflater;
    private List<QBUser> selected = new ArrayList<QBUser>();

    private JSONObject json;
    private JSONObject jsonFilter;


    public UsersAdapterChat(List<QBUser> dataSource, Context ctx) {
        this.datalist = dataSource;
        this.basedatalist = dataSource;


        this.inflater = LayoutInflater.from(ctx);
    }

    public List<QBUser> getSelected() {
        return selected;
    }

    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int position) {
        return datalist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_user_chat, null);
            holder = new ViewHolder();
            holder.username = (TextView) convertView.findViewById(R.id.userLogin);
            holder.Institution = (TextView) convertView.findViewById(R.id.userInstitution);
            holder.Cargo = (TextView) convertView.findViewById(R.id.userCargo);
            holder.content_cell = (RelativeLayout)convertView.findViewById(R.id.content_user_chat);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final QBUser user = datalist.get(position);
        if (user != null) {

            holder.username.setText(user.getFullName());

           /* QBRoster roster = app.getСhatRoster();

            QBPresence presence = roster.getPresence(user.getId());
            if (presence == null) {
                Log.i("rooster","No user in your roster");

            }
            if (presence.getType() == QBPresence.Type.online) {
                Log.i("rooster", "User " + user.getId() + " is online");
                holder.conected.setText("is online");
            }else{
                Log.i("rooster", "User " + user.getId() + " is offline");
                holder.conected.setText("is offline");
            }*/

           // holder.conected.setText(user.);
            if (user.getExternalId()!=null) {
                holder.content_cell.setBackgroundResource(R.drawable.card_background_border_loged);
            }else{
                Log.i("rooster", "User " + user.getId() + " is offline");
                holder.content_cell.setBackgroundResource(R.drawable.card_background_border);
            }

            if (user.getCustomData() != null){

                try {
                    json = new JSONObject(user.getCustomData());
                    Log.v("json_string", json.toString()+" keys " +json.getString("cargo"));
                    holder.Cargo.setText(json.getString("cargo"));
                    holder.Institution.setText(json.getString("institucion"));
                } catch (JSONException e) {
                    Log.v("json_string", e.toString());
                    e.printStackTrace();
                }
            }
            else {

                holder.Institution.setText("");
                holder.Cargo.setText("");
            }
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                ArrayList<QBUser> listaTemporal = new ArrayList<QBUser>();
                String filterCargo ="" , filterInst="" ,secuentcia = charSequence.toString().toLowerCase();




                if (charSequence != null && charSequence.length()!= 0){

                    for(QBUser a : datalist){
                        if (a.getCustomData() != null){

                            try {
                                jsonFilter = new JSONObject(a.getCustomData());
                              //  Log.v("json_filter", jsonFilter.toString()+" keys " +jsonFilter.getString("cargo"));
                               filterCargo = jsonFilter.getString("cargo");
                                filterInst= jsonFilter.getString("institucion");

                            } catch (JSONException e) {
                                Log.v("json_filter", e.toString());
                                e.printStackTrace();
                               filterCargo= "";
                                filterInst="";
                            }
                        }



                      if (a.getFullName().toLowerCase().startsWith(secuentcia)||filterCargo.toLowerCase().startsWith(secuentcia)||filterInst.toLowerCase().startsWith(secuentcia)) {
                          Log.v("json_filter", filterCargo);


                            listaTemporal.add(a);
                        }



                    }

                }
                    filterResults.values = listaTemporal;
                    filterResults.count = listaTemporal.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                datalist = (ArrayList<QBUser>)filterResults.values;
                if (filterResults.count >0){
                    notifyDataSetChanged();
                }
            }
        };
    }

    private static class ViewHolder {
        TextView username;
        TextView Institution;
        TextView Cargo;

        RelativeLayout content_cell;
    }
    public void setSelecUser(int position){
        selected.clear();
        final QBUser user = datalist.get(position);
        selected.add(user);


    }
    public void resetResult(){
        this.datalist=this.basedatalist;
    }




}
