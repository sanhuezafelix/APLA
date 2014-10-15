package Fragments.chat;

import android.app.Activity;
import android.app.AlertDialog;
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
import Fragments.chat.adaptadores.DialogsAdapterPrivateChat;
import mc.mCongressDemo.MainActivity;
import mc.mCongressDemo.R;
import mc.mCongressDemo.myApp;

/**
 * Created by luisgonzalez on 24-09-14.
 */
public class mDialogsUsersChat extends Fragment {

    private ListView dialogsListView;
    private ProgressBar progressBar;
    private Button new_chat;
    private ArrayList<QBDialog> filterDialog = new ArrayList<QBDialog>();
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialogs_activity, container,false);
        ((MainActivity)getActivity()).setHideNavigationBar();
        ((MainActivity)getActivity()).setTituloVista(getString(R.string.private_Chat));
        dialogsListView = (ListView) rootView.findViewById(R.id.roomsList);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        new_chat = (Button)rootView.findViewById(R.id.add_chat);
        new_chat.setVisibility(View.INVISIBLE);
        ((MainActivity)getActivity()).setEnablesRadioGruopChat(false);
        ((MainActivity)getActivity()).UnSetBackButton();
        // get dialogs
        //
        QBCustomObjectRequestBuilder customObjectRequestBuilder = new QBCustomObjectRequestBuilder();
        customObjectRequestBuilder.setPagesLimit(100);
        QBChatService.getChatDialogs(QBDialogType.GROUP , customObjectRequestBuilder, new QBEntityCallbackImpl<ArrayList<QBDialog>>() {
            @Override
            public void onSuccess(final ArrayList<QBDialog> dialogs, Bundle args) {

                // collect all occupants ids
                //
                filterDialog.clear();

                List<Integer> usersIDs = new ArrayList<Integer>();
                for (QBDialog dialog : dialogs) {
                    if (dialog.getOccupants().size()==2) {
                        filterDialog.add(dialog);
                    }
                }
                for (QBDialog dialog : filterDialog) {

                        usersIDs.addAll(dialog.getOccupants());

                }
                ((myApp)getActivity().getApplication()).setUsuarios_chat(usersIDs);

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
                        ((MainActivity)getActivity()).setEnablesRadioGruopChat(true);

                        buildListView(filterDialog);
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


        return rootView;
    }


    void buildListView(List<QBDialog> dialogs){



        final DialogsAdapterPrivateChat adapter = new DialogsAdapterPrivateChat(dialogs, (MainActivity)getActivity());
        dialogsListView.setAdapter(adapter);

        progressBar.setVisibility(View.GONE);

        // choose dialog
        //
        // choose dialog
        //
        dialogsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


               Bundle bundle = new Bundle();
                bundle.putSerializable(mNewChatFragmentView.EXTRA_DIALOG, (QBDialog)adapter.getItem(position));
                    bundle.putSerializable(mNewChatFragmentView.EXTRA_MODE, mNewChatFragmentView.Mode.GROUP);
                ((MainActivity)getActivity()).PushView(mNewChatFragmentView.getInstance(((MainActivity) getActivity()), bundle));
            }
        });

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity)getActivity()).setTituloVista(getString(R.string.private_Chat));
        ((MainActivity)getActivity()).setLastActionChat(MainActivity.ButtonAction.PRIVADOS);
        ((MainActivity)getActivity()).setVisibilityContentBotonNavbar(View.GONE);
        ((MainActivity)getActivity()).setVisibilityChangeViewChat(View.VISIBLE);
        ((MainActivity)getActivity()).UnSetBackButton();
    }
}
