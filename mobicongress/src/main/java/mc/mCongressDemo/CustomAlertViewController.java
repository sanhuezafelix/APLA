package mc.mCongressDemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by luisgonzalez on 25-03-14.
 */
public class CustomAlertViewController extends Activity  implements View.OnClickListener{

    Button ok;
    Button cancel;

    boolean click = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.app_name));
        setContentView(R.layout.alerta_layout);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        TextView params = (TextView) findViewById(R.id.durationTitle);
        ok = (Button)findViewById(R.id.popOkB);
        ok.setOnClickListener(this);
        cancel = (Button)findViewById(R.id.popCancelB);
        cancel.setOnClickListener(this);
        Bundle reicieveParams = getIntent().getExtras();
        params.setText(reicieveParams.getString("push"));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.popCancelB){
            Intent mainIntent = new Intent().setClass(this, MainActivity.class);
            mainIntent.putExtra("noticias", true );
            startActivity(mainIntent);

        }

        finish();

    }
}
