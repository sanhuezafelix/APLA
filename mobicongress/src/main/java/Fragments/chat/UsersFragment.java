package Fragments.chat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import com.quickblox.core.QBEntityCallbackImpl;

import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.module.chat.QBChatService;
import com.quickblox.module.chat.model.QBDialog;
import com.quickblox.module.chat.model.QBDialogType;

import com.quickblox.module.users.model.QBUser;


import java.util.ArrayList;
import java.util.List;

import Fragments.chat.adaptadores.UsersAdapter;
import Utils.ClearableEditText;
import mc.mCongressDemo.MainActivity;
import mc.mCongressDemo.R;
import mc.mCongressDemo.myApp;

public class UsersFragment extends Fragment implements View.OnClickListener {

    private static final int PAGE_SIZE = 10;
    private PullToRefreshListView usersList;
    private Button createChatButton;
    private int listViewIndex;
    private int listViewTop;
    private ProgressBar progressBar;
    private UsersAdapter usersAdapter;
    private EditText searchBar;
    private Button Close_Search;
    private String GROUP_CHAT_NAME;

    private int currentPage = 0;
    private List<QBUser> users = new ArrayList<QBUser>();

    public static UsersFragment getInstance() {
        return new UsersFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.users_new_chat, container, false);
        usersList = (PullToRefreshListView) v.findViewById(R.id.usersList_newchat);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        createChatButton = (Button) v.findViewById(R.id.createChatButton);
        this.Close_Search = (Button)v.findViewById(R.id.search2);
        this.Close_Search.setOnClickListener(this);
        searchBar = (EditText) v.findViewById(R.id.search_list_user);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (i3==0){
                    usersAdapter.resetResult();
                    usersAdapter.notifyDataSetChanged();

                }
                else {
                    usersAdapter.getFilter().filter(charSequence);
                    usersAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                usersAdapter.notifyDataSetChanged();

            }
        });

        createChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



               /* ((myApp)getActivity().getApplication()).addDialogsUsers(usersAdapter.getSelected());

                // Create new group dialog
                //
                QBDialog dialogToCreate = new QBDialog();
                dialogToCreate.setName(usersListToChatName());
                if(usersAdapter.getSelected().size() == 1){
                    dialogToCreate.setType(QBDialogType.PRIVATE);
                }else {
                    dialogToCreate.setType(QBDialogType.GROUP);
                }
                dialogToCreate.setOccupantsIds(getUserIds(usersAdapter.getSelected()));


                QBChatService.getInstance().getGroupChatManager().createDialog(dialogToCreate, new QBEntityCallbackImpl<QBDialog>() {
                    @Override
                    public void onSuccess(QBDialog dialog, Bundle args) {
                        if(usersAdapter.getSelected().size() == 1){
                            startSingleChat(dialog);
                        } else {
                            startGroupChat(dialog);
                        }
                    }

                    @Override
                    public void onError(List<String> errors) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                        dialog.setMessage("dialog creation errors: " + errors).create().show();
                    }
                });*/
                if(usersAdapter.getSelected().size() >= 2) {
                    showAuthenticateDialog();
                }
                else {
                    Toast.makeText(((MainActivity)getActivity()),
                            getString(R.string.select), Toast.LENGTH_SHORT)
                            .show();
                }


            }

        });

        usersList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                // Do work to refresh the list here.
                //loadNextPage();
                listViewIndex = usersList.getRefreshableView().getFirstVisiblePosition();
                View v = usersList.getRefreshableView().getChildAt(0);
                listViewTop = (v == null) ? 0 : v.getTop();
                usersAdapter.notifyDataSetChanged();

            }
        });

       // loadNextPage();
        this.users=((myApp)getActivity().getApplication()).getUsuarios_registrados();
        loadAllUsers();
        return v;

    }


    public static QBPagedRequestBuilder getQBPagedRequestBuilder(int page) {
        QBPagedRequestBuilder pagedRequestBuilder = new QBPagedRequestBuilder();
        pagedRequestBuilder.setPage(page);
        pagedRequestBuilder.setPerPage(PAGE_SIZE);

        return pagedRequestBuilder;
    }


    /*@Override
    public void onSuccess(ArrayList<QBUser> newUsers, Bundle bundle){

        // save users
        //
        users.addAll(newUsers);

        // Prepare users list for simple adapter.
        //
        usersAdapter = new UsersAdapter(users, getActivity());
        usersList.setAdapter(usersAdapter);
        usersList.onRefreshComplete();
        usersList.getRefreshableView().setSelectionFromTop(listViewIndex, listViewTop);

        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onSuccess(){

    }

    @Override
    public void onError(List<String> errors){
        AlertDialog.Builder dialog = new AlertDialog.Builder(UsersFragment.getInstance().getActivity());
        dialog.setMessage("get users errors: " + errors).create().show();
    }


    private String usersListToChatName(){
        String chatName = "";
        for(QBUser user : usersAdapter.getSelected()){
            String prefix = chatName.equals("") ? "" : ", ";
            chatName = chatName + prefix + user.getLogin();
        }
        return chatName;
    }*/

    public void startSingleChat(QBDialog dialog) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(mNewChatFragmentView.EXTRA_MODE, mNewChatFragmentView.Mode.PRIVATE);
        bundle.putSerializable(mNewChatFragmentView.EXTRA_DIALOG, dialog);

        ((MainActivity)getActivity()).menuChangeView(mNewChatFragmentView.getInstance(((MainActivity) getActivity()), bundle),false);

    }

    private void startGroupChat(QBDialog dialog){
       Bundle bundle = new Bundle();
        bundle.putSerializable(mNewChatFragmentView.EXTRA_DIALOG, dialog);
        bundle.putSerializable(mNewChatFragmentView.EXTRA_MODE, mNewChatFragmentView.Mode.GROUP);

                       ((MainActivity)getActivity()).changueChatView(mNewChatFragmentView.getInstance(((MainActivity) getActivity()), bundle));

    }

   /* private void loadNextPage() {
        ++currentPage;

        QBUsers.getUsers(getQBPagedRequestBuilder(currentPage), UsersFragment.this);
    }*/

    public static ArrayList<Integer> getUserIds(List<QBUser> users){
        ArrayList<Integer> ids = new ArrayList<Integer>();
        for(QBUser user : users){
            ids.add(user.getId());
        }
        return ids;
    }

    public void showAuthenticateDialog() {
        final Dialog dialogo = new Dialog(((MainActivity)getActivity()));
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialogo.setContentView(R.layout.new_group_alert_box);
        Button ok = (Button) dialogo.findViewById(R.id.OK_group);
        Button cancel = (Button) dialogo.findViewById(R.id.Cancel_group);
        final EditText titulo_group = (EditText)dialogo.findViewById(R.id.name_group);

        dialogo.getWindow().getAttributes().width = RelativeLayout.LayoutParams.MATCH_PARENT;
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (titulo_group.getText().toString().isEmpty()){
                    Toast.makeText(((MainActivity)getActivity()),
                            getString(R.string.name_dialog), Toast.LENGTH_SHORT)
                            .show();
                }
                else {
                   createGroupChat(titulo_group.getText().toString());
                    dialogo.dismiss();
                }


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogo.dismiss();

            }
        });


        dialogo.show();

    }

    public void createGroupChat(String groupName){

        ((myApp)getActivity().getApplication()).addDialogsUsers(usersAdapter.getSelected());

        // Create new group dialog
        //
        QBDialog dialogToCreate = new QBDialog();
        dialogToCreate.setName(groupName);
        if(usersAdapter.getSelected().size() == 1){
            dialogToCreate.setType(QBDialogType.PRIVATE);
        }else {
            dialogToCreate.setType(QBDialogType.GROUP);
        }
        dialogToCreate.setOccupantsIds(getUserIds(usersAdapter.getSelected()));


        QBChatService.getInstance().getGroupChatManager().createDialog(dialogToCreate, new QBEntityCallbackImpl<QBDialog>() {
            @Override
            public void onSuccess(QBDialog dialog, Bundle args) {
                if(usersAdapter.getSelected().size() == 1){
                    startSingleChat(dialog);
                } else {
                    Log.i("id_gupo","el id es "+dialog.getDialogId());
                    ((myApp)getActivity().getApplication()).setGroupUsersNames(dialog.getDialogId(),usersListToChatName());
                    startGroupChat(dialog);
                }
            }

            @Override
            public void onError(List<String> errors) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setMessage("dialog creation errors: " + errors).create().show();
            }
        });

    }


    private void  loadAllUsers(){

        usersAdapter = new UsersAdapter(users, getActivity());
        usersList.setAdapter(usersAdapter);
        usersList.onRefreshComplete();
        usersList.getRefreshableView().setSelectionFromTop(listViewIndex, listViewTop);
        progressBar.setVisibility(View.GONE);
        usersAdapter.notifyDataSetChanged();

    }

    private String usersListToChatName(){
        String chatName = "";
        for(QBUser user : usersAdapter.getSelected()){
            String prefix = chatName.equals("") ? " " : " , ";
            chatName = chatName + prefix + user.getFullName();
        }
        return chatName;
    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.search2){

            InputMethodManager inputManager = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            usersAdapter.notifyDataSetChanged();
            //this.activity.onBackPressed();
        }

    }
}
