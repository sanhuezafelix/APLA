package Fragments.chat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBEntityCallbackImpl;

import com.quickblox.core.request.QBCustomObjectRequestBuilder;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.module.chat.QBChatService;
import com.quickblox.module.chat.model.QBDialog;
import com.quickblox.module.chat.model.QBDialogType;
import com.quickblox.module.users.QBUsers;
import com.quickblox.module.users.model.QBUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Adaptadores.TextWatcherAdapter;
import Fragments.chat.adaptadores.UsersAdapter;
import Fragments.chat.adaptadores.UsersAdapterChat;
import Utils.ClearableEditText;
import mc.mCongressDemo.MainActivity;
import mc.mCongressDemo.R;
import mc.mCongressDemo.myApp;

public class mListUsersChat extends Fragment implements View.OnClickListener {

    private static final int PAGE_SIZE = 100;
    private static final int MAX_PAGE = 3;
    private ListView usersList;

    private ProgressBar progressBar;
    private UsersAdapterChat usersAdapter;
    private EditText _search_user_chat;
    private Button Close_Search;
    private int _counter = 0;
    private int currentPage = 0;


    private List<QBUser> users = new ArrayList<QBUser>();
    private List<QBUser> usuarios_finales = new ArrayList<QBUser>();
    private ArrayList<QBDialog> filterDialog = new ArrayList<QBDialog>();
    private ArrayList<QBDialog> myDialog = new ArrayList<QBDialog>();
    private QBUser currentUser;

    public static mListUsersChat getInstance(MainActivity activity) {
        activity.setHideNavigationBar();

        return new mListUsersChat();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.user_list_chat, container, false);
        ((MainActivity)getActivity()).setHideNavigationBar();
        ((MainActivity)getActivity()).setTituloVista(getString(R.string.UserList));
        ((MainActivity)getActivity()).UnSetBackButton();
         currentUser = ((myApp)getActivity().getApplication()).getCurrentUser();
        usersList = (ListView) v.findViewById(R.id.usersList_chat);
        _search_user_chat = (EditText) v.findViewById(R.id.search_list_user);
        this.Close_Search = (Button)v.findViewById(R.id.search);
        this.Close_Search.setOnClickListener(this);


        _search_user_chat.addTextChangedListener(new TextWatcher() {
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

       /* usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((MainActivity)getActivity()).createAlertDialog("apretaste ");
            }
        });*/

        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        //loadNextPage();
        this.users=((myApp)getActivity().getApplication()).getUsuarios_registrados();
        Collections.sort(users,new Comparator<QBUser>() {
            @Override
            public int compare(QBUser qbUser, QBUser qbUser2) {
                return cp(qbUser,qbUser2);
            }
        });





        Log.v("_registrados","usuarios "+users.size());
        loasAllChatAlone();

        return v;
    }

    static <QBuser extends Comparable<QBUser>> int cp(QBUser a, QBUser b) {
        return
                b.getExternalId()==null ?
                        (a.getExternalId()==null ? 0 : Integer.MIN_VALUE) :
                        (a.getExternalId()==null ? Integer.MAX_VALUE : a.getFullName().compareTo(b.getFullName()));
    }




   /* public static QBPagedRequestBuilder getQBPagedRequestBuilder(int page) {
        QBPagedRequestBuilder requestBuilder = new QBPagedRequestBuilder();
       // pagedRequestBuilder.setPage(page);
       // pagedRequestBuilder.setPerPage(PAGE_SIZE);
        requestBuilder.setPage(page);
        requestBuilder.setPerPage(PAGE_SIZE);

        return requestBuilder;
    }


    @Override
    public void onSuccess(ArrayList<QBUser> newUsers, Bundle bundle){

        users.addAll(newUsers);

            usuarios_finales = users;
            Log.i("final","usuarios finales"+usuarios_finales.size() + " = " +users.size()+" +"+currentPage);

            usersAdapter = new UsersAdapterChat(usuarios_finales, getActivity());
            usersList.setAdapter(usersAdapter);



            usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    usersAdapter.setSelecUser(i);

                    ((myApp)getActivity().getApplication()).addDialogsUsers(usersAdapter.getSelected());


                    // Create new group dialog
                    //
                    QBDialog dialogToCreate = new QBDialog();
                    dialogToCreate.setName(usersListToChatName());

                    dialogToCreate.setType(QBDialogType.PRIVATE);

                    dialogToCreate.setOccupantsIds(getUserIds(usersAdapter.getSelected()));

                    QBChatService.getInstance().getGroupChatManager().createDialog(dialogToCreate, new QBEntityCallbackImpl<QBDialog>() {
                        @Override
                        public void onSuccess(QBDialog dialog, Bundle args) {
                            startSingleChat(dialog);
                        }

                        @Override
                        public void onError(List<String> errors) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                            dialog.setMessage("dialog creation errors: " + errors).create().show();
                        }
                    });

                }
            });
            progressBar.setVisibility(View.GONE);








    }

    @Override
    public void onSuccess(){

    }

    @Override
    public void onError(List<String> errors){

    }*/






   /* private void loadNextPage() {
        ++currentPage;


        QBUsers.getUsers(getQBPagedRequestBuilder(currentPage), mListUsersChat.this);
    }*/
    private void  loadAllUsers(){


           // ++currentPage;
            //QBUsers.getUsers(getQBPagedRequestBuilder(currentPage), mListUsersChat.this);
        usersAdapter = new UsersAdapterChat(this.users, getActivity());
        usersList.setAdapter(usersAdapter);



        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                usersAdapter.setSelecUser(i);


                ((myApp)getActivity().getApplication()).addDialogsUsers(usersAdapter.getSelected());
                if (((myApp)getActivity().getApplication()).isChatUserCurrentDialogs(usersAdapter.getSelected().get(0).getId().toString())){
                    Log.i("create_user","este usurio  existe y su id es " +usersAdapter.getSelected().get(0).getId());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(mNewChatFragmentView.EXTRA_DIALOG, ((myApp)getActivity().getApplication()).getDialogTouserId(usersAdapter.getSelected().get(0).getId()));
                    bundle.putSerializable(mNewChatFragmentView.EXTRA_MODE, mNewChatFragmentView.Mode.GROUP);
                    ((MainActivity)getActivity()).PushView(mNewChatFragmentView.getInstance(((MainActivity) getActivity()), bundle));

                }
                else {
                    Log.i("create_user","este usurio nooo existe  y su id es " +usersAdapter.getSelected().get(0).getId());

                    QBDialog dialogToCreate = new QBDialog();
                    String dialog_name = ((myApp)getActivity().getApplication()).getCurrentUser().getId().toString()+"-"+getUserIds(usersAdapter.getSelected());

                    dialogToCreate.setName(dialog_name);
                    dialogToCreate.setType(QBDialogType.GROUP);
                    dialogToCreate.setOccupantsIds(getUserIds(usersAdapter.getSelected()));

                    QBChatService.getInstance().getGroupChatManager().createDialog(dialogToCreate, new QBEntityCallbackImpl<QBDialog>() {
                        @Override
                        public void onSuccess(QBDialog dialog, Bundle args) {
                            startGroupChat(dialog);
                        }
                        @Override
                        public void onError(List<String> errors) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                            dialog.setMessage("dialog creation errors: " + errors).create().show();
                        }
                    });
                }

                // Create new group dialog
                //


            }
        });
        progressBar.setVisibility(View.GONE);










    }

    private void startGroupChat(QBDialog dialog){
        Bundle bundle = new Bundle();
        bundle.putSerializable(mNewChatFragmentView.EXTRA_DIALOG, dialog);
        bundle.putSerializable(mNewChatFragmentView.EXTRA_MODE, mNewChatFragmentView.Mode.GROUP);

        ((MainActivity)getActivity()).PushView(mNewChatFragmentView.getInstance(((MainActivity) getActivity()), bundle));


    }


    public static ArrayList<Integer> getUserIds(List<QBUser> users){
        ArrayList<Integer> ids = new ArrayList<Integer>();
        for(QBUser user : users){
            ids.add(user.getId());
        }
        return ids;

    }


    public void onClick(View view) {
        if (view.getId() == R.id.search){

            InputMethodManager inputManager = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            usersAdapter.notifyDataSetChanged();
            //this.activity.onBackPressed();
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity)getActivity()).setTituloVista(getString(R.string.UserList));
        ((MainActivity)getActivity()).setLastActionChat(MainActivity.ButtonAction.USURIAOS);
        ((MainActivity)getActivity()).setVisibilityContentBotonNavbar(View.GONE);
        ((MainActivity)getActivity()).setVisibilityChangeViewChat(View.VISIBLE);
        ((MainActivity)getActivity()).UnSetBackButton();
    }

    public void loasAllChatAlone(){
        QBCustomObjectRequestBuilder customObjectRequestBuilder = new QBCustomObjectRequestBuilder();
        customObjectRequestBuilder.setPagesLimit(100);
        QBChatService.getChatDialogs(QBDialogType.GROUP, customObjectRequestBuilder, new QBEntityCallbackImpl<ArrayList<QBDialog>>(){

            @Override
            public void onSuccess(ArrayList<QBDialog> dialogs, Bundle params) {
                List<Integer> usersIDs = new ArrayList<Integer>();
                for (QBDialog dialog : dialogs) {
                    if (dialog.getOccupants().size()==2) {
                        filterDialog.add(dialog);
                    }
                }
               ((myApp)getActivity().getApplication()).setDialogToChat(filterDialog);
                for (QBDialog dialog : filterDialog) {

                    usersIDs.addAll(dialog.getOccupants());

                }

                ((myApp)getActivity().getApplication()).setChatUsersIds(usersIDs);
                loadAllUsers();

            }
        });



    }
}
