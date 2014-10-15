package Fragments.chat;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

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

import mc.mCongressDemo.MainActivity;
import mc.mCongressDemo.R;
import mc.mCongressDemo.myApp;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.

 * create an instance of this fragment.
 *
 */
public class mLoginChat extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PUSH_CHAT = "push_Chat";
    private static final String DIALOG_CHAT = "dialogo";
    private static final String TYPE_DIALOG_CHAT = "tipo_dialogo";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private Button log_in_button;
    private EditText _user_log_in, _user_pass;
    private ProgressBar _progress;
    private MainActivity activity;
    private View RootView;
    private ProgressBar _progressbar;
    private int currentPage = 0;
    private int counter=0;
    private List<QBUser> users = new ArrayList<QBUser>();
    private ArrayList<QBDialog> filterDialog = new ArrayList<QBDialog>();



    public static mLoginChat getInstance(MainActivity activity, boolean isPush) {
        mLoginChat fragment = new mLoginChat();
        activity.setHideNavigationBar();
        Bundle bundle = new Bundle();
        bundle.putBoolean(PUSH_CHAT, isPush);

        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);




        return fragment;
    }
    public static mLoginChat getInstance(MainActivity activity, boolean isPush, String userID, String type) {
        mLoginChat fragment = new mLoginChat();
        activity.setHideNavigationBar();
        Bundle bundle = new Bundle();
        bundle.putBoolean(PUSH_CHAT, isPush);
        bundle.putString(DIALOG_CHAT,userID);
        bundle.putString(TYPE_DIALOG_CHAT,type);
        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);
        return fragment;
    }
    public mLoginChat() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RootView  = inflater.inflate(R.layout.fragment_m_login_chat, container, false);
        this.activity=(MainActivity)getActivity();
        this.log_in_button = (Button)RootView.findViewById(R.id.log_in_chat);
        this._user_log_in = (EditText)RootView.findViewById(R.id.user_log_in_chat);
        // _user_log_in.setText("agustin");
        this._user_pass = (EditText)RootView.findViewById(R.id.user_pass_log_in);
        //_user_pass.setText("prueba0199");
        this._progress = (ProgressBar)RootView.findViewById(R.id.progress_log_in);
        ((MainActivity)getActivity()).setTituloVista(getString(R.string.login));
        ((MainActivity)getActivity()).setVisibilityChangeViewChat(View.GONE);

        this.log_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user , pass;
                user = _user_log_in.getText().toString();
                pass = _user_pass.getText().toString();
                user = user.replaceAll("[@]","");
                if (!user.isEmpty()&&!pass.isEmpty()) {
                    ((MainActivity) getActivity()).initChat(user, pass);
                    _progress.setVisibility(View.VISIBLE);
                }
                else {
                    ((MainActivity)getActivity()).createAlertDialog("Porfavor complete todos los campos");
                    _progress.setVisibility(View.GONE);
                }
            }
        });
        if (((myApp)getActivity().getApplication()).getCurrentUser()!=null)
        {


         loasAllChat();
            Log.i("usuarios","cargando usuarios");

        }
        else {
            if (((myApp)getActivity().getApplication()).isRegistred())
            {
                Log.i("registro","el registro existe");
                String[] registed = ((myApp)getActivity().getApplication()).loadRegistred(((MainActivity)getActivity()));
                ((MainActivity) getActivity()).initChat(registed[0], registed[1]);
            }
            else
            {
                Log.i("registro","el registro no existe");
            }
        }

        if (((myApp)getActivity().getApplication()).isRegistred()){
          _user_log_in.setVisibility(View.GONE);
            _user_pass.setVisibility(View.GONE);
            _progress.setVisibility(View.VISIBLE);
            log_in_button.setVisibility(View.GONE);

        }


        return RootView;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity)getActivity()).setVisibilityChangeViewChat(View.GONE);
        ((MainActivity)getActivity()).setHideNavigationBar();
        ((MainActivity)getActivity()).setVisibilityContentBotonNavbar(View.GONE);
    }

    @Override
    public void onDetach() {
        super.onDetach();


    }

    public void loadAllUsers(){

        Log.v("_registrados", "cargando usuraios");
        int i;
        for (i = 0; i<8;i++){
            ++currentPage;

            Log.v("_registrados", "for " +i);
            Log.v("_registrados", "currentpage  " +currentPage);

            QBUsers.getUsers(getQBPagedRequestBuilder(currentPage), new QBEntityCallback<ArrayList<QBUser>>() {
                @Override
                public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {

                    users.addAll(qbUsers);
                    counter++;

                    if (counter == 8) {
                        Collections.sort(users, new Comparator<QBUser>() {
                            @Override
                            public int compare(QBUser qbUser, QBUser qbUser2) {

                                int orden = qbUser.getFullName().compareToIgnoreCase(qbUser2.getFullName());


                                return  orden ;



                            }
                        });

                        ((myApp) getActivity().getApplication()).setUsuarios_registrados(users);
                        if (getArguments().getBoolean(PUSH_CHAT)){
                            Log.i("isChatPush","si biene de un push");

                            Log.i("isChatPush","y el dialogo es==> "+getArguments().getString(DIALOG_CHAT)+" y su tipo "+getArguments().get(TYPE_DIALOG_CHAT));
                            if (getArguments().get(TYPE_DIALOG_CHAT).equals("grupal")){
                                ((MainActivity)getActivity()).setSelectedchatIndex(R.id.rooms);
                                //((MainActivity)getActivity()).PushView(new mMainchatViewController());

                            }
                            else {
                                ((MainActivity)getActivity()).setSelectedchatIndex(R.id.dialogs);
                                //((MainActivity)getActivity()).changueChatView(new mDialogsUsersChat());

                            }
                        }
                        else {
                            Log.i("isChatPush","no biene de un otra parte");
                            MainActivity.ButtonAction accion = ((MainActivity)getActivity()).getLastActionChat();
                            if (accion==MainActivity.ButtonAction.GRUPOS){
                                ((MainActivity)getActivity()).setSelectedchatIndex(R.id.rooms);
                                ((MainActivity)getActivity()).PushView(new mMainchatViewController());
                            }
                            else if (accion==MainActivity.ButtonAction.USURIAOS){
                                ((MainActivity)getActivity()).PushView(new mListUsersChat());
                                ((MainActivity)getActivity()).setSelectedchatIndex(R.id.users);

                            }
                            else {
                                ((MainActivity)getActivity()).setSelectedchatIndex(R.id.dialogs);
                                ((MainActivity)getActivity()).changueChatView(new mDialogsUsersChat());
                            }

                        }

                    }

                }
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(List<String> strings) {
                    Log.v("_registrados", "error " + strings);

                }
            });


        }

    }

    public static QBPagedRequestBuilder getQBPagedRequestBuilder(int page) {
        QBPagedRequestBuilder requestBuilder = new QBPagedRequestBuilder();
        requestBuilder.setPage(page);
        requestBuilder.setPerPage(100);
        return requestBuilder;
    }
    public void loasAllChat(){
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
