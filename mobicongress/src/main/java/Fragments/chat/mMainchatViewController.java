package Fragments.chat;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.quickblox.core.QBEntityCallbackImpl;

import com.quickblox.core.request.QBCustomObjectRequestBuilder;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.module.chat.QBChatService;
import com.quickblox.module.chat.model.QBDialog;
import com.quickblox.module.chat.model.QBDialogType;
import com.quickblox.module.users.QBUsers;
import com.quickblox.module.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

import Fragments.chat.adaptadores.DialogsAdapterGroupChat;
import mc.mCongressDemo.MainActivity;
import mc.mCongressDemo.R;
import mc.mCongressDemo.myApp;

/**
 * Created by luisgonzalez on 24-09-14.
 */
public class mMainchatViewController extends Fragment {

    private ListView dialogsListView;
    private ProgressBar progressBar;
    private Button new_chat;
    private ArrayList<QBDialog> filterDialog = new ArrayList<QBDialog>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialogs_activity, container,false);
        ((MainActivity)getActivity()).setVisibilityChangeViewChat(View.VISIBLE);
        ((MainActivity)getActivity()).setHideNavigationBar();
        ((MainActivity)getActivity()).setTituloVista(getString(R.string.chat_Group));
        ((MainActivity)getActivity()).setEnablesRadioGruopChat(false);
        ((MainActivity)getActivity()).UnSetBackButton();
        dialogsListView = (ListView) rootView.findViewById(R.id.roomsList);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        new_chat = (Button)rootView.findViewById(R.id.add_chat);
        new_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).PushView(UsersFragment.getInstance());
            }
        });

        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {


                getDialogs();

            }
        });

        // get dialogs
        //
       /* QBCustomObjectRequestBuilder customObjectRequestBuilder = new QBCustomObjectRequestBuilder();
        customObjectRequestBuilder.setPagesLimit(100);

        QBChatService.getChatDialogs(QBDialogType.GROUP , customObjectRequestBuilder, new QBEntityCallbackImpl<ArrayList<QBDialog>>() {
            @Override
            public void onSuccess(final ArrayList<QBDialog> dialogs, Bundle args) {

                // collect all occupants ids
                //
                List<Integer> usersIDs = new ArrayList<Integer>();
                for (QBDialog dialog : dialogs) {
                    usersIDs.addAll(dialog.getOccupants());
                }

                // Get all occupants info
                //
                QBPagedRequestBuilder requestBuilder = new QBPagedRequestBuilder();
                requestBuilder.setPage(1);
                requestBuilder.setPerPage(usersIDs.size());
                QBUsers.getUsersByIDs(usersIDs, requestBuilder, new QBEntityCallbackImpl<ArrayList<QBUser>>() {
                    @Override
                    public void onSuccess(ArrayList<QBUser> users, Bundle params) {

                        // Save users
                        //
                        ((myApp)getActivity().getApplication()).setDialogsUsers(users);

                        buildListView(dialogs);
                    }

                    @Override
                    public void onError(List<String> errors) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder((MainActivity)getActivity());
                        dialog.setMessage("get occupants errors: " + errors).create().show();
                    }

                });

                //


            }
        });*/


        return rootView;
    }
    public void getDialogs(){

        QBCustomObjectRequestBuilder customObjectRequestBuilder = new QBCustomObjectRequestBuilder();
        customObjectRequestBuilder.setPagesLimit(100);

        QBChatService.getChatDialogs(QBDialogType.GROUP , customObjectRequestBuilder, new QBEntityCallbackImpl<ArrayList<QBDialog>>() {
            @Override
            public void onSuccess(final ArrayList<QBDialog> dialogs, Bundle args) {

                // collect all occupants ids
                //
                /*List<Integer> usersIDs = new ArrayList<Integer>();
                for (QBDialog dialog : dialogs) {
                    usersIDs.addAll(dialog.getOccupants());
                }*/
                filterDialog.clear();
                List<Integer> usersIDs = new ArrayList<Integer>();
                for (QBDialog dialog : dialogs) {
                    if (dialog.getOccupants().size()>2) {
                        filterDialog.add(dialog);
                    }
                }
                for (QBDialog dialog : filterDialog) {

                    usersIDs.addAll(dialog.getOccupants());

                }

                // Get all occupants info
                //
                QBPagedRequestBuilder requestBuilder = new QBPagedRequestBuilder();
                requestBuilder.setPage(1);
                requestBuilder.setPerPage(usersIDs.size());
                QBUsers.getUsersByIDs(usersIDs, requestBuilder, new QBEntityCallbackImpl<ArrayList<QBUser>>() {
                    @Override
                    public void onSuccess(ArrayList<QBUser> users, Bundle params) {

                        ((myApp)getActivity().getApplication()).setDialogsUsers(users);
                        buildListView(filterDialog);
                        ((MainActivity)getActivity()).setEnablesRadioGruopChat(true);
                    }

                    @Override
                    public void onError(List<String> errors) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder((MainActivity)getActivity());
                        dialog.setMessage("get occupants errors: " + errors).create().show();
                    }

                });

                //


            }
        });

    }


    void buildListView(List<QBDialog> dialogs){

        

        final DialogsAdapterGroupChat adapter = new DialogsAdapterGroupChat(dialogs, (MainActivity)getActivity());
        dialogsListView.setAdapter(adapter);

        progressBar.setVisibility(View.GONE);

        // choose dialog
        //
        // choose dialog
        //
        dialogsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QBDialog selectedDialog = (QBDialog)adapter.getItem(position);

               Bundle bundle = new Bundle();
                bundle.putSerializable(mNewChatFragmentView.EXTRA_DIALOG, (QBDialog)adapter.getItem(position));

                // group
                if(selectedDialog.getType().equals(QBDialogType.GROUP)){
                    bundle.putSerializable(mNewChatFragmentView.EXTRA_MODE, mNewChatFragmentView.Mode.GROUP);

                // private
                } else {
                    bundle.putSerializable(mNewChatFragmentView.EXTRA_MODE, mNewChatFragmentView.Mode.PRIVATE);


                }
                ((MainActivity)getActivity()).PushView(mNewChatFragmentView.getInstance(((MainActivity)getActivity()),bundle));

                // Open chat activity
                //
                //ChatActivity.start(DialogsActivity.this, bundle);*/
               // ((MainActivity)getActivity()).PushView(mNewChatFragmentView.getInstance(((MainActivity) getActivity()), bundle));
            }
        });

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity)getActivity()).setTituloVista(getString(R.string.chat_Group));
        ((MainActivity)getActivity()).setVisibilityChangeViewChat(View.VISIBLE);
        ((MainActivity)getActivity()).setVisibilityContentBotonNavbar(View.GONE);
        ((MainActivity)getActivity()).setLastActionChat(MainActivity.ButtonAction.GRUPOS);
        ((MainActivity)getActivity()).UnSetBackButton();

    }

    public class mGroupChatTask extends AsyncTask<Integer ,Void,Cursor> {
        MainActivity activity;
        public mGroupChatTask(MainActivity a){
            this.activity = a;


        }

        @Override
        protected Cursor doInBackground(Integer... integers) {
            return this.activity.getDbHandler().getCursorSearchSpeaker(integers[0],"");

        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
           /* adapter = new mSpeakerAdapter(activity,cursor,R.layout.cell_speaker,activity.getDbHandler());
            int cur_id = app.fragment_speaker_position;
            if (cur_id >=0) {
                adapter.set_cur_position(cur_id);
                listView.setSelection(cur_id);
            }
            listView.setAdapter(adapter);*/
        }
    }

}
