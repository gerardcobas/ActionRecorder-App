package action.recorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Edit extends Activity {

	String texto;
	long f;
	EditText campo;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
        	texto = extras.getString("TXT");
        	f = extras.getLong("fedit");
        }
        else {
        	f = 0;
        	texto = null;
        }
        
        campo = (EditText) findViewById(R.id.texto_mod);
        campo.setText(texto);
        
        
        Button butedit = (Button) findViewById(R.id.MOD_mod);
        butedit.setOnClickListener(new View.OnClickListener() {
           public void onClick(View arg0) {
        	   String valor = campo.getText().toString();
        	   if(valor.length() > 0) {
	        	   Intent resultIntent;
	        	   resultIntent = new Intent();
	        	   resultIntent.putExtra("Cancel", 'e');
	        	   resultIntent.putExtra("txt_edited", valor);
	        	   resultIntent.putExtra("fila", f);
	        	   setResult(Activity.RESULT_OK, resultIntent);
	        	   finish();
        	   }
        	   else Toast.makeText(Edit.this, "Must have content", Toast.LENGTH_SHORT).show();
           } 
        });
        
        Button butcan = (Button) findViewById(R.id.MOD_cancell);
        butcan.setOnClickListener(new View.OnClickListener() {
           public void onClick(View arg0) {
        	   Intent resultIntent;
        	   resultIntent = new Intent();
        	   resultIntent.putExtra("Cancel", 'c');
        	   setResult(Activity.RESULT_OK, resultIntent);
        	   finish();
           } 
        });
    }
}
