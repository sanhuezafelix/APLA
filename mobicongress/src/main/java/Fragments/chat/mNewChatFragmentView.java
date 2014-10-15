package Fragments.chat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBEntityCallbackImpl;

import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBCustomObjectRequestBuilder;

import com.quickblox.module.chat.QBChatService;
import com.quickblox.module.chat.model.QBChatHistoryMessage;
import com.quickblox.module.chat.model.QBChatMessage;
import com.quickblox.module.chat.model.QBMessage;
import com.quickblox.module.chat.QBPrivateChat;
import com.quickblox.module.chat.listeners.QBPrivateChatManagerListener;
import com.quickblox.module.chat.model.QBDialog;
import com.quickblox.module.chat.model.QBMessage;
import com.quickblox.module.users.QBUsers;
import com.quickblox.module.users.model.QBUser;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Fragments.chat.adaptadores.ChatAdapter;
import Fragments.chat.core.ChatManager;
import Fragments.chat.core.GroupChatManagerImpl;
import Fragments.chat.core.PrivateChatManagerImpl;
import mc.mCongressDemo.MainActivity;
import mc.mCongressDemo.R;
import mc.mCongressDemo.myApp;

/**
 * Created by luisgonzalez on 23-09-14.
 */
public class mNewChatFragmentView extends Fragment {
    private static final String TAG = mNewChatFragmentView.class.getSimpleName();

    public static final String EXTRA_MODE = "mode";
    public static final String EXTRA_DIALOG = "dialog";
    private final String PROPERTY_SAVE_TO_HISTORY = "save_to_history";

    private EditText messageEditText;
    private ListView messagesContainer;
    private Button sendButton;
    private ProgressBar progressBar;

    private Mode mode = Mode.PRIVATE;
    private ChatManager chat;
    private ChatAdapter adapter;
    private QBDialog dialog;
    private List<QBUser> users = new ArrayList<QBUser>();
    private ArrayList<Integer> idsuers = new ArrayList<Integer>();
    private String GroupName = "";
    private String userChatName = "";

    private ArrayList<QBChatHistoryMessage> history;

    public static mNewChatFragmentView getInstance(MainActivity activity,Bundle bundle){
        mNewChatFragmentView fragment = new mNewChatFragmentView();
        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);
        return  fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_chat_layout,container,false);
        messagesContainer = (ListView) rootView.findViewById(R.id.messagesContainer);
        messageEditText = (EditText) rootView.findViewById(R.id.messageEdit);
        sendButton = (Button) rootView.findViewById(R.id.chatSendButton);
        ((MainActivity)getActivity()).setBackbutton();

        TextView meLabel = (TextView) rootView.findViewById(R.id.meLabel);
        TextView companionLabel = (TextView) rootView.findViewById(R.id.companionLabel);
        TextView list_user_gropup = (TextView) rootView.findViewById(R.id.list_user_group);
        RelativeLayout container2 = (RelativeLayout) rootView.findViewById(R.id.container);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        users.addAll(((myApp)getActivity().getApplication()).getUsuarios_registrados());




        // Get chat dialog
        //
        dialog = (QBDialog)getArguments().getSerializable(EXTRA_DIALOG);

        mode = (Mode) getArguments().getSerializable(EXTRA_MODE);
        switch (mode) {
            case GROUP:
                chat = new GroupChatManagerImpl(this);
                container2.removeView(meLabel);
                container2.removeView(companionLabel);
                list_user_gropup.setVisibility(View.VISIBLE);

                idsuers.addAll(dialog.getOccupants());

                if (dialog.getOccupants().size()<3){
                    ((MainActivity)getActivity()).setTituloVista(getString(R.string.Conversacion_privada));
                    for (QBUser user :users){

                        for (int i= 0 ; i< idsuers.size();i++){

                            if (user.getId().equals(idsuers.get(i))&&!user.getId().equals(((myApp)getActivity().getApplication()).getCurrentUser().getId())){


                                GroupName= getString(R.string.conversation) +" "+ user.getFullName();

                            }
                        }
                    }
                }
                else {
                    ((MainActivity)getActivity()).setTituloVista(getString(R.string.Conversacion_grupal));
                    for (QBUser user :users){

                        for (int i= 0 ; i< idsuers.size();i++){


                            if (user.getId().equals(idsuers.get(i))){
                                String prefix = GroupName.equals("") ? "Integrantes : " : " , ";
                                GroupName= GroupName + prefix + user.getFullName();
                            }
                        }
                    }
                }
                list_user_gropup.setText(GroupName);

                progressBar.setVisibility(View.VISIBLE);

                ((GroupChatManagerImpl) chat).joinGroupChat(dialog, new QBEntityCallbackImpl() {
                    @Override
                    public void onSuccess() {
                        loadChatHistory();
                    }

                    @Override
                    public void onError(List list) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(((MainActivity)getActivity()));
                        dialog.setMessage("error when join group chat: " + list.toString()).create().show();
                    }
                });

                break;
            case PRIVATE:
                Integer opponentID = ((myApp)getActivity().getApplication()).getOpponentIDForPrivateDialog(dialog);

                chat = new PrivateChatManagerImpl(this, opponentID);

                companionLabel.setText(((myApp) getActivity().getApplication()).getDialogsUsers().get(opponentID).getFullName());

                // Load CHat history
                //
                loadChatHistory();
                break;
        }

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageEditText.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }

                // Send chat message
                //
                QBChatMessage chatMessage = new QBChatMessage();
                chatMessage.setBody(messageText);
                chatMessage.setProperty(PROPERTY_SAVE_TO_HISTORY, "1");





                try {
                    chat.sendMessage(chatMessage);
                } catch (XMPPException e) {
                    Log.e(TAG, "failed to send a message", e);
                } catch (SmackException sme){
                    Log.e(TAG, "failed to send a message", sme);
                }

                messageEditText.setText("");

                if(mode == Mode.PRIVATE) {
                    showMessage(chatMessage);
                }
            }
        });
        return  rootView;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        try {
            chat.release();
        } catch (XMPPException e) {
            Log.e(TAG, "failed to release chat", e);
        }
    }





    private void loadChatHistory(){
        QBCustomObjectRequestBuilder customObjectRequestBuilder = new QBCustomObjectRequestBuilder();
        customObjectRequestBuilder.setPagesLimit(100);

        QBChatService.getDialogMessages(dialog, customObjectRequestBuilder, new QBEntityCallbackImpl<ArrayList<QBChatHistoryMessage>>() {
            @Override
            public void onSuccess(ArrayList<QBChatHistoryMessage> messages, Bundle args) {
                history = messages;

                adapter = new ChatAdapter(((MainActivity)getActivity()), new ArrayList<QBMessage>());
                messagesContainer.setAdapter(adapter);

                for(QBMessage msg : messages) {
                    showMessage(msg);
                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(List<String> errors) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(((MainActivity)getActivity()));
                dialog.setMessage("load chat history errors: " + errors).create().show();
            }
        });
    }

    public void showMessage(QBMessage message) {
        adapter.add(message);

        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                scrollDown();
            }
        });
    }

    private void scrollDown() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    public static enum Mode {PRIVATE, GROUP}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity)getActivity()).setHideNavigationBar();

    }


}