package Fragments.chat.adaptadores;

/**
 * Created by igorkhomenko on 9/12/14.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quickblox.module.chat.model.QBDialog;
import com.quickblox.module.chat.model.QBDialogType;
import com.quickblox.module.users.model.QBUser;

import java.util.List;

import mc.mCongressDemo.R;
import mc.mCongressDemo.myApp;

public class DialogsAdapterPrivateChat extends BaseAdapter {
    private List<QBDialog> dataSource;
    private LayoutInflater inflater;
    private Activity ctx;

    public DialogsAdapterPrivateChat(List<QBDialog> dataSource, Activity ctx) {
        this.dataSource = dataSource;
        this.inflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
    }

    public List<QBDialog> getDataSource() {
        return dataSource;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return dataSource.get(position);
    }

    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // init view
        //
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_room, null);
            holder = new ViewHolder();
            holder.name = (TextView)convertView.findViewById(R.id.roomName);
            holder.lastMessage = (TextView)convertView.findViewById(R.id.lastMessage);
            holder.groupType = (TextView)convertView.findViewById(R.id.textViewGroupType);
            holder.counter = (TextView)convertView.findViewById(R.id.roomCount);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // set data
        //
        QBDialog dialog = dataSource.get(position);
        /*if(dialog.getType().equals(QBDialogType.GROUP)){
           holder.name.setText(dialog.getName());
        }else{
            // get opponent name for private dialog
            //
            Integer opponentID = ((myApp)ctx.getApplication()).getOpponentIDForPrivateDialog(dialog);
            QBUser user = ((myApp)ctx.getApplication()).getDialogsUsers().get(opponentID);
            if(user != null){
                holder.name.setText(user.getFullName() == null ? user.getLogin() : user.getFullName());


            }
        }*/
        Integer opponentID = ((myApp)ctx.getApplication()).getOpponentIDForPrivateDialog(dialog);
        QBUser user = ((myApp)ctx.getApplication()).getDialogsUsers().get(opponentID);
        if(user != null) {
            holder.name.setText(user.getFullName() == null ? user.getLogin() : user.getFullName());
            //holder.name.setText("user no  nulo");
        }
        else {
            holder.name.setText(dialog.getName());
        }


        holder.lastMessage.setText(dialog.getLastMessage());
        if (!dialog.getUnreadMessageCount().equals(0)){
            holder.counter.setText(ctx.getString(R.string.unread_msgs)+" "+ dialog.getUnreadMessageCount().toString());
            convertView.setBackgroundResource(R.drawable.card_background_border_loged);
        }
        else {
            holder.counter.setVisibility(View.GONE);
            convertView.setBackgroundResource(R.drawable.card_background_border);

        }

        holder.groupType.setText(myApp.getContext().getString(R.string.privado));

        return convertView;
    }

    private static class ViewHolder{
        TextView name;
        TextView lastMessage;
        TextView groupType;
        TextView counter;
    }
}