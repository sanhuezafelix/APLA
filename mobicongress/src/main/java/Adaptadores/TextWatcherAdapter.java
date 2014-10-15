package Adaptadores;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by luisgonzalez on 18-04-14.
 */
public class TextWatcherAdapter implements TextWatcher {

    public interface TextWatcherListener {

        void onTextChanged(EditText view, String text);

    }

    private final EditText view;
    private final TextWatcherListener listener;

    public TextWatcherAdapter(EditText view, TextWatcherListener listener) {
        this.view = view;
        this.listener = listener;
    }



    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        listener.onTextChanged(view, charSequence.toString());

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
