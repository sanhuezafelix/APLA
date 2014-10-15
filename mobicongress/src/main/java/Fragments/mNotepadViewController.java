package Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import mc.mCongressDemo.MainActivity;
import mc.mCongressDemo.R;

/**
 * Created by luisgonzalez on 05-08-14.
 */
public class mNotepadViewController extends Fragment implements View.OnClickListener {

    private static final String EVENTO_ID = "evento_id";
    private static final String EVENTO_TITLE = "evento_titule";
    private MainActivity activity;
    private EditText text;
    private TextView titulo;
    private ImageButton send_mail;
    private ImageButton delete_note;

    public static mNotepadViewController getInstance( MainActivity activity ,int _id,String titulo){

        mNotepadViewController fragment = new mNotepadViewController();
        Bundle bundle = new Bundle();
        bundle.putInt(EVENTO_ID, _id);
        bundle.putString(EVENTO_TITLE, titulo);

        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);
        activity.setBackbutton();

        return fragment;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.notepad_layout,container,false);
        this.text = (EditText)RootView.findViewById(R.id.text_note_pad);
        this.titulo = (TextView)RootView.findViewById(R.id.title_text_note_pad);
        this.titulo.setText(getArguments().getString(EVENTO_TITLE));

        activity = (MainActivity)getActivity();
        this.text.setText(activity.getDbHandler().getTextNotePad(getArguments().getInt(EVENTO_ID)));
        this.send_mail = (ImageButton)RootView.findViewById(R.id.mail_note);
        this.delete_note = (ImageButton)RootView.findViewById(R.id.delete_note);
        delete_note.setOnClickListener(this);

        send_mail.setOnClickListener(this);
        return RootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity)activity).setHideNavigationBar();
        ((MainActivity)activity).setTituloVista(getString(R.string.Note_pad));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (this.text.getText().toString().isEmpty()){
            this.activity.getDbHandler().updateNotePad(getArguments().getInt(EVENTO_ID),0,"");
        }
        else {
            this.activity.getDbHandler().updateNotePad(getArguments().getInt(EVENTO_ID),1,this.text.getText().toString());
            Toast toast1 =
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Save Note", Toast.LENGTH_SHORT);

            toast1.show();
        }


    }

    @Override
    public void onClick(View view) {
        if (view.getId()== R.id.mail_note){

            Intent itSend = new Intent(android.content.Intent.ACTION_SEND);
            itSend.setType("plain/text");

            itSend.putExtra(android.content.Intent.EXTRA_SUBJECT, this.titulo.getText().toString());
            itSend.putExtra(android.content.Intent.EXTRA_TEXT, text.getText().toString());

            startActivity(itSend);
        }
        if (view.getId()== R.id.delete_note){
            this.activity.getDbHandler().updateNotePad(getArguments().getInt(EVENTO_ID),0,"");
            this.text.setText("");
            Toast toast1 =
                    Toast.makeText(getActivity().getApplicationContext(),
                            "delete Note", Toast.LENGTH_SHORT);

            toast1.show();
        }
    }
}
