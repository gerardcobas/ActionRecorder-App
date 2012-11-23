package action.recorder;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class Change extends Activity{
	
	String tipo;
	String text;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change);
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
        	text = extras.getString("TXT");
        	tipo = extras.getString("change_tipo");
        }
        else {
        	text = null;
        	tipo = null;
        }
        
        final RadioButton radio_Read = (RadioButton) findViewById(R.id.T_Read);
        final RadioButton radio_Watch = (RadioButton) findViewById(R.id.T_Watch);
        final RadioButton radio_Do = (RadioButton) findViewById(R.id.T_Do);
        final RadioButton radio_Buy = (RadioButton) findViewById(R.id.T_Buy);
        final RadioButton radio_Music = (RadioButton) findViewById(R.id.T_Music);
        final RadioButton radio_Duties = (RadioButton) findViewById(R.id.T_Duties);
        final RadioButton radio_Browse = (RadioButton) findViewById(R.id.T_Browse);
        final RadioButton radio_Random = (RadioButton) findViewById(R.id.T_Random);

        radio_Read.setOnClickListener(radio_listener);
        radio_Watch.setOnClickListener(radio_listener);
        radio_Do.setOnClickListener(radio_listener);
        radio_Buy.setOnClickListener(radio_listener);
        radio_Music.setOnClickListener(radio_listener);
        radio_Duties.setOnClickListener(radio_listener);
        radio_Browse.setOnClickListener(radio_listener);
        radio_Random.setOnClickListener(radio_listener);
        
        if(tipo.equals("Read")) radio_Read.toggle();
        if(tipo.equals("Watch")) radio_Watch.toggle();
        if(tipo.equals("Do")) radio_Do.toggle();
        if(tipo.equals("Buy")) radio_Buy.toggle();
        if(tipo.equals("Music")) radio_Music.toggle();
        if(tipo.equals("Duties")) radio_Duties.toggle();
        if(tipo.equals("Browse")) radio_Browse.toggle();
        if(tipo.equals("Random")) radio_Random.toggle();

    }
    
    private OnClickListener radio_listener = new OnClickListener() {
        public void onClick(View v) {
            RadioButton rb = (RadioButton) v;
            String valor = (String) rb.getText();
     	   	Intent resultIntent;
     	   	resultIntent = new Intent();
     	   	resultIntent.putExtra("Cancel", 'h');
     	   	resultIntent.putExtra("new_tipo", valor);
     	   	resultIntent.putExtra("txt", text);
     	   	setResult(Activity.RESULT_OK, resultIntent);
     	   	finish();
        }
    };
}
