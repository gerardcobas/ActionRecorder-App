package action.recorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Add extends Activity {

	String tipo;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        Bundle extras = getIntent().getExtras();
        if(extras !=null) tipo = extras.getString("tipo_add");
        else tipo = null;
        
        Spinner s = (Spinner) findViewById(R.id.lista_add);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this, R.array.menu_add, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        
        if(tipo.equals("Read")) s.setSelection(0);
        if(tipo.equals("Watch")) s.setSelection(1);
        if(tipo.equals("Do")) s.setSelection(2);
        if(tipo.equals("Buy")) s.setSelection(3);
        if(tipo.equals("Music")) s.setSelection(4);
        if(tipo.equals("Duties")) s.setSelection(5);
        if(tipo.equals("Browse")) s.setSelection(6);
        if(tipo.equals("Random")) s.setSelection(7);       
        
        final EditText edittext = (EditText) findViewById(R.id.editText_add);
        
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                tipo = item.toString();
            }
            public void onNothingSelected(AdapterView<?> arg0) {
			}
        });
        
        Button butadd = (Button) findViewById(R.id.button_add);
        butadd.setOnClickListener(new View.OnClickListener() {
           public void onClick(View arg0) {
        	   String valor = edittext.getText().toString();
        	   if(valor.length() > 0) {
	        	   Intent resultIntent;
	        	   resultIntent = new Intent();
	        	   resultIntent.putExtra("Cancel", 'a');
	        	   resultIntent.putExtra("txt_edited", valor);
	        	   resultIntent.putExtra("add_tipo", tipo);
	        	   setResult(Activity.RESULT_OK, resultIntent);
	        	   finish();
        	   }
        	   else Toast.makeText(Add.this, "Must have content", Toast.LENGTH_SHORT).show();
           } 
        });
        
        Button butcan = (Button) findViewById(R.id.button_cancel);
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
