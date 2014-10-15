package Fragments.chat.adaptadores;

import android.content.Context;
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

import com.quickblox.module.users.model.QBUser;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mc.mCongressDemo.R;

/**
 * Created by igorkhomenko on 9/12/14.
 */
public class UsersAdapter extends BaseAdapter implements Filterable {

    private List<QBUser> dataSource;
    private List<QBUser> basedataSource;
    private LayoutInflater inflater;
    private JSONObject json;
    private JSONObject jsonFilter;
    private List<QBUser> selected = new ArrayList<QBUser>();

    public UsersAdapter(List<QBUser> dataSource, Context ctx) {
        this.dataSource = dataSource;
        this.basedataSource =dataSource;
        this.inflater = LayoutInflater.from(ctx);

    }

    public List<QBUser> getSelected() {
        return selected;
    }

    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_user, null);
            holder = new ViewHolder();
            holder.username = (TextView) convertView.findViewById(R.id.userLogin);

            holder.Institution = (TextView) convertView.findViewById(R.id.userInstitution);
            holder.Cargo = (TextView) convertView.findViewById(R.id.userCargo);
            holder.content_cell = (RelativeLayout)convertView.findViewById(R.id.content_user_chat);
            holder.add = (CheckBox) convertView.findViewById(R.id.addCheckBox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final QBUser user = dataSource.get(position);
        if (user != null) {
            holder.username.setText(user.getFullName());
           holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((((CheckBox) v).isChecked())) {
                        selected.add(user);
                    } else {
                        selected.remove(user);
                    }
                }
            });
            holder.add.setChecked(selected.contains(user));

        }

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


                    for(QBUser a : dataSource){

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
                        if (a.getFullName().toLowerCase().startsWith(secuentcia)||filterCargo.toLowerCase().startsWith(secuentcia)||filterInst.toLowerCase().startsWith(secuentcia)){
                            listaTemporal.add(a);
                        }
                    }
                    filterResults.values = listaTemporal;
                    filterResults.count = listaTemporal.size();
                }
                else {
                    filterResults.values = dataSource;
                    filterResults.count = dataSource.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dataSource = (ArrayList<QBUser>)filterResults.values;
                if (filterResults.count >0){
                    notifyDataSetChanged();
                }
                /*else {
                    notifyDataSetInvalidated();
                }*/
            }
        };
    }

    private static class ViewHolder {
        TextView username;
        TextView Institution;
        TextView Cargo;

        RelativeLayout content_cell;
        CheckBox add;
    }
    public void resetResult(){
        this.dataSource=this.basedataSource;
    }
}
