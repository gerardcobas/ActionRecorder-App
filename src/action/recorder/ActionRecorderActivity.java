package action.recorder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class ActionRecorderActivity extends Activity {
	
	
	final Context contexto = this;
	
	List<String> lall = new ArrayList<String>();
	
	List<String> lread = new ArrayList<String>();
	List<String> lwatch = new ArrayList<String>();
	List<String> ldo = new ArrayList<String>();
	List<String> lbuy = new ArrayList<String>();
	List<String> lmusic = new ArrayList<String>();
	List<String> lduties = new ArrayList<String>();
	List<String> lbrowse = new ArrayList<String>();
	List<String> lrandom = new ArrayList<String>();
	
	
	String val;
	String tipo; 
	Spinner sp;
	ImageButton butadd;
	ImageButton butdel;
	ImageView img;
	TextView st;
	ListView lv;
	int pos_X, pos_Y;
	
	
    private SensorManager mSensorManager;
    boolean mogut=false;
private float mAccel; // acceleration apart from gravity
private float mAccelCurrent; // current acceleration including gravity
private float mAccelLast; // last acceleration including gravity
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        sp = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this, R.array.menu, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        
        tipo = "ALL";
        
        String d = ReadSettings(ActionRecorderActivity.this);
        if(d!=null){
        	String data[] = d.split("²¼²");
        	int j = 0;
		    for(int i=0; i<data.length-1; ++i){
		    	if(!data[i].equals("ˆÎ˜ÎÃâÃÎ˜Îˆ")){
		    		if(j==0) lall.add(data[i]);
		    		if(j==1) lread.add(data[i]);
		    		if(j==2) lwatch.add(data[i]);
		    		if(j==3) ldo.add(data[i]);
		    		if(j==4) lbuy.add(data[i]);
		    		if(j==5) lmusic.add(data[i]);
		    		if(j==6) lduties.add(data[i]);
		    		if(j==7) lbrowse.add(data[i]);
		    		if(j==8) lrandom.add(data[i]);
		    	}
		    	else ++j;
		    }
        }
        img = (ImageView) findViewById(R.id.TitleImage);
        butadd = (ImageButton) findViewById(R.id.add);
        butdel = (ImageButton) findViewById(R.id.del);
        lv = (ListView) findViewById(R.id.lista);
        registerForContextMenu(lv);
        st = (TextView) findViewById(R.id.Title);
    }
    
    
    @Override
    public void onStart(){
    	super.onStart();
    	
        butadd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent int_add=new Intent(ActionRecorderActivity.this, Add.class);
            	int_add.putExtra("tipo_add", tipo);
    			startActivityForResult(int_add,1);
            }
        });
        butdel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent int_del=new Intent(ActionRecorderActivity.this, Del.class);
            	if(tipo.equals("ALL")) Toast.makeText(ActionRecorderActivity.this, "Delete from inside a topic", Toast.LENGTH_SHORT).show();
				if(tipo.equals("Read")) int_del.putStringArrayListExtra("listdel", (ArrayList<String>) lread);
				if(tipo.equals("Watch")) int_del.putStringArrayListExtra("listdel", (ArrayList<String>) lwatch);
				if(tipo.equals("Do")) int_del.putStringArrayListExtra("listdel", (ArrayList<String>) ldo);
				if(tipo.equals("Buy")) int_del.putStringArrayListExtra("listdel", (ArrayList<String>) lbuy);
				if(tipo.equals("Music")) int_del.putStringArrayListExtra("listdel", (ArrayList<String>) lmusic);
				if(tipo.equals("Duties")) int_del.putStringArrayListExtra("listdel", (ArrayList<String>) lduties);
				if(tipo.equals("Browse")) int_del.putStringArrayListExtra("listdel", (ArrayList<String>) lbrowse);
				if(tipo.equals("Random")) int_del.putStringArrayListExtra("listdel", (ArrayList<String>) lrandom);

				if(!tipo.equals("ALL"))startActivityForResult(int_del,2);
            }
        });
        
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            	Object item = parent.getItemAtPosition(pos);
            	actualiza_lista((String) item);
            }
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
        });
        
        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int pos, long id){
            	if(tipo.equals("ALL")){
            		actualiza_lista(lall.get((int) id));
            		int index = 0;
            		if(lall.get((int) id).equals("Read")) index = 1;
            		if(lall.get((int) id).equals("Watch")) index = 2;
            		if(lall.get((int) id).equals("Do")) index = 3;
            		if(lall.get((int) id).equals("Buy")) index = 4;
            		if(lall.get((int) id).equals("Music")) index = 5;
            		if(lall.get((int) id).equals("Duties")) index = 6;
            		if(lall.get((int) id).equals("Browse")) index = 7;
            		if(lall.get((int) id).equals("Random")) index = 8;
            		sp.setSelection(index);
            	}
            }
        });
        
    }
    
    
    @Override
    public void onStop(){
    	 mSensorManager.unregisterListener(mSensorListener);
    	super.onStop();
    	FileOutputStream fos = null;
		try {
			fos = openFileOutput("actionrecorder.dat", Context.MODE_PRIVATE);
		} catch (FileNotFoundException e) {
			Toast.makeText(this, "Error",Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
    	
    	String a;
    	List<String> aux = lall;
	    for(int i=0; i<9; ++i){
    		if(i==1) aux = lread;
    		if(i==2) aux = lwatch;
    		if(i==3) aux = ldo;
    		if(i==4) aux = lbuy;
    		if(i==5) aux = lmusic;
    		if(i==6) aux = lduties;
    		if(i==7) aux = lbrowse;
    		if(i==8) aux = lrandom;
    		for(int j=0; j<aux.size(); ++j){
    			try {
					a = aux.get(j);
					fos.write(a.getBytes());
					a = "²¼²";
					fos.write(a.getBytes());
				} catch (IOException e) {
					Toast.makeText(this, "Error",Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
    		}
    		try {
    			a = "ˆÎ˜ÎÃâÃÎ˜Îˆ";
				fos.write(a.getBytes());
				a = "²¼²";
				fos.write(a.getBytes());
			} catch (IOException e) {
				Toast.makeText(this, "Error",Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
	    }
	    try {
		    fos.close();
	    } catch (IOException e) {
	    	Toast.makeText(this, "Error",Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
    }
    
    
    public String ReadSettings(Context context){
    	String data = null;
    	FileInputStream fIn = null;
        InputStreamReader isr = null;
        char[] inputBuffer = new char[1023];
        try{
		     fIn = openFileInput("actionrecorder.dat");      
		     isr = new InputStreamReader(fIn);
		     try{
			     isr.read(inputBuffer);
			     data = new String(inputBuffer);
		     }
		     catch (Exception e) {
		         Toast.makeText(context, "No data yet",Toast.LENGTH_SHORT).show();
	         }
		     finally {
	        	 try {
	               isr.close();
	               fIn.close();
	            } 
	            catch (IOException ioe) {
	            	Toast.makeText(context, "Error",Toast.LENGTH_SHORT).show();
	            }
	         }
         }
         catch (Exception e) {
	         Toast.makeText(context, "No data yet",Toast.LENGTH_SHORT).show();
         }
         return data;
    }
    
    
    public void actualiza_lista(String t){
        ArrayAdapter ad3 = null;
        if(t.equals("ALL")){
        	img.setImageResource(R.drawable.icon2);
        	tipo = "ALL";
        	st.setText("ALL");
        	ad3 = new ArrayAdapter(contexto, R.layout.list, lall);
        }
        if(t.equals("Read")){
        	img.setImageResource(R.drawable.read);
        	tipo = "Read";
        	st.setText("READ");
        	ad3 = new ArrayAdapter(contexto, R.layout.list, lread);
        }
        if(t.equals("Watch")){
        	img.setImageResource(R.drawable.watch);
        	tipo = "Watch";
        	st.setText("WATCH");
        	ad3 = new ArrayAdapter(contexto, R.layout.list, lwatch);
        }
        if(t.equals("Do")){
        	img.setImageResource(R.drawable.do_icon);
        	tipo = "Do";
        	st.setText("DO");
        	ad3 = new ArrayAdapter(contexto, R.layout.list, ldo);
        }
        if(t.equals("Buy")){
        	img.setImageResource(R.drawable.buy);
        	tipo = "Buy";
        	st.setText("BUY");
        	ad3 = new ArrayAdapter(contexto, R.layout.list, lbuy);
        }        
        if(t.equals("Music")){
        	img.setImageResource(R.drawable.music);
        	tipo = "Music";
        	st.setText("MUSIC");
        	ad3 = new ArrayAdapter(contexto, R.layout.list, lmusic);
        }
        if(t.equals("Duties")){
        	img.setImageResource(R.drawable.duties);
        	tipo = "Duties";
        	st.setText("DUTIES");
        	ad3 = new ArrayAdapter(contexto, R.layout.list, lduties);
        }     
        if(t.equals("Browse")){
        	img.setImageResource(R.drawable.browse);
        	tipo = "Browse";
        	st.setText("BROWSE");
        	ad3 = new ArrayAdapter(contexto, R.layout.list, lbrowse);
        }    
        if(t.equals("Random")){
        	img.setImageResource(R.drawable.random);
        	tipo = "Random";
        	st.setText("RANDOM");
        	ad3 = new ArrayAdapter(contexto, R.layout.list, lrandom);
        }       
        ad3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lv.setAdapter(ad3);
    }
       
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
        return true;
    }
    
    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_add:
        	Intent int_add=new Intent(ActionRecorderActivity.this, Add.class);
        	int_add.putExtra("tipo_add", tipo);
			startActivityForResult(int_add,1);
            return true;
        case R.id.menu_del:
        	Intent int_del=new Intent(ActionRecorderActivity.this, Del.class);
        	if(tipo.equals("ALL")) Toast.makeText(ActionRecorderActivity.this, "You have to delete from the especific list", Toast.LENGTH_SHORT).show();
			if(tipo.equals("Read")) int_del.putStringArrayListExtra("listdel", (ArrayList<String>) lread);
			if(tipo.equals("Watch")) int_del.putStringArrayListExtra("listdel", (ArrayList<String>) lwatch);
			if(tipo.equals("Do")) int_del.putStringArrayListExtra("listdel", (ArrayList<String>) ldo);
			if(tipo.equals("Buy")) int_del.putStringArrayListExtra("listdel", (ArrayList<String>) lbuy);
			if(tipo.equals("Music")) int_del.putStringArrayListExtra("listdel", (ArrayList<String>) lmusic);
			if(tipo.equals("Duties")) int_del.putStringArrayListExtra("listdel", (ArrayList<String>) lduties);
			if(tipo.equals("Browse")) int_del.putStringArrayListExtra("listdel", (ArrayList<String>) lbrowse);
			if(tipo.equals("Random")) int_del.putStringArrayListExtra("listdel", (ArrayList<String>) lrandom);
			if(!tipo.equals("ALL"))startActivityForResult(int_del,2);
            return true;
        case R.id.menu_end:
            finish();
            return true;
        case R.id.menu_about:
        	Toast.makeText(ActionRecorderActivity.this, "Action Recorder made by Gerard Cobas", Toast.LENGTH_LONG).show();
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
      super.onCreateContextMenu(menu, v, menuInfo);
      if(!tipo.equals("ALL")){
	      MenuInflater inflater = getMenuInflater();
	      inflater.inflate(R.menu.contxt_menu, menu);
      }
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
      AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
      switch (item.getItemId()) {
      case R.id.cntxt_change:
    	  Intent int_change=new Intent(ActionRecorderActivity.this, Change.class);
      	  if(tipo.equals("Read")) {
      		  int_change.putExtra("TXT", lread.get((int)info.id));
      		  lread.remove((int)info.id);
      	  }
		  if(tipo.equals("Watch")) {
			  int_change.putExtra("TXT", lwatch.get((int)info.id));
			  lwatch.remove((int)info.id);
		  }
		  if(tipo.equals("Do")) {
			  int_change.putExtra("TXT", ldo.get((int)info.id));
			  ldo.remove((int)info.id);
		  }
		  if(tipo.equals("Buy")) {
			  int_change.putExtra("TXT", lbuy.get((int)info.id));
			  lbuy.remove((int)info.id);
		  }
		  if(tipo.equals("Music")) {
			  int_change.putExtra("TXT", lmusic.get((int)info.id));
			  lmusic.remove((int)info.id);
		  }
		  if(tipo.equals("Duties")) {
			  int_change.putExtra("TXT", lduties.get((int)info.id));
			  lduties.remove((int)info.id);
		  }
		  if(tipo.equals("Browse")) {
			  int_change.putExtra("TXT", lbrowse.get((int)info.id));
			  lbrowse.remove((int)info.id);
		  }
		  if(tipo.equals("Random")) {
			  int_change.putExtra("TXT", lrandom.get((int)info.id));
			  lrandom.remove((int)info.id);
		  }
		  int_change.putExtra("change_tipo", tipo);
      	  startActivityForResult(int_change,4);
        return true;
      case R.id.cntxt_edit:
    	  Intent int_mod=new Intent(ActionRecorderActivity.this, Edit.class);
      	  if(tipo.equals("Read")) int_mod.putExtra("TXT", lread.get((int)info.id));
		  if(tipo.equals("Watch")) int_mod.putExtra("TXT", lwatch.get((int)info.id));
		  if(tipo.equals("Do")) int_mod.putExtra("TXT", ldo.get((int)info.id));
		  if(tipo.equals("Buy")) int_mod.putExtra("TXT", lbuy.get((int)info.id));
		  if(tipo.equals("Music")) int_mod.putExtra("TXT", lmusic.get((int)info.id));
		  if(tipo.equals("Duties")) int_mod.putExtra("TXT", lduties.get((int)info.id));
		  if(tipo.equals("Browse")) int_mod.putExtra("TXT", lbrowse.get((int)info.id));
		  if(tipo.equals("Random")) int_mod.putExtra("TXT", lrandom.get((int)info.id));
		  int_mod.putExtra("fedit", info.id);
		  int_mod.putExtra("edit_tipo", tipo);
      	  startActivityForResult(int_mod,3);
        return true;
      case R.id.cntxt_delete:
    	  if(tipo.equals("ALL")) Toast.makeText(ActionRecorderActivity.this, "You have to delete from the especific list", Toast.LENGTH_SHORT).show();
    	  if(tipo.equals("Read")) lread.remove((int)info.id);
    	  if(tipo.equals("Watch")) lwatch.remove((int)info.id);
    	  if(tipo.equals("Do")) ldo.remove((int)info.id);
    	  if(tipo.equals("Buy")) lbuy.remove((int)info.id);
    	  if(tipo.equals("Music")) lmusic.remove((int)info.id);
    	  if(tipo.equals("Duties")) lduties.remove((int)info.id);
    	  if(tipo.equals("Browse")) lbrowse.remove((int)info.id);
    	  if(tipo.equals("Random")) lrandom.remove((int)info.id);
    	  actualiza_lista(tipo);
    	  actualiza_all();
        return true;
      default:
        return super.onContextItemSelected(item);
      }
    }
    
    public void actualiza_all(){
    	lall = new ArrayList<String>();
    	if(!lread.isEmpty()) lall.add("Read");
    	if(!lwatch.isEmpty()) lall.add("Watch");
    	if(!ldo.isEmpty()) lall.add("Do");
    	if(!lbuy.isEmpty()) lall.add("Buy");
    	if(!lmusic.isEmpty()) lall.add("Music");
    	if(!lduties.isEmpty()) lall.add("Duties");
    	if(!lbrowse.isEmpty()) lall.add("Browse");
    	if(!lrandom.isEmpty()) lall.add("Random");
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
         if(keyCode == KeyEvent.KEYCODE_BACK && isTaskRoot() && !tipo.equals("ALL")) {
            actualiza_lista("ALL");
            return true;
        }
        else {
            return super.onKeyDown(keyCode, event);
        }
    }
    
    
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            	pos_X = x;
            	pos_Y = y;
            	break;
            case MotionEvent.ACTION_UP:
            	if((pos_X>x) && (pos_X-x)>100) {
            		int ind = -1;
            		if(tipo.equals("ALL")) {
            			ind = 1;
            			actualiza_lista("Read");
            		}
            		else if(tipo.equals("Read")) {
            			ind = 2;
            			actualiza_lista("Watch");
            		}
            		else if(tipo.equals("Watch")) {
            			ind = 3;
            			actualiza_lista("Do");
            		}
            		else if(tipo.equals("Do")) {
            			ind = 4;
            			actualiza_lista("Buy");
            		}
            		else if(tipo.equals("Buy")) {
            			ind = 5;
            			actualiza_lista("Music");
            		}
            		else if(tipo.equals("Music")) {
            			ind = 6;
            			actualiza_lista("Duties");
            		}
            		else if(tipo.equals("Duties")) {
            			ind = 7;
            			actualiza_lista("Browse");
            		}
            		else if(tipo.equals("Browse")) {
            			ind = 8;
            			actualiza_lista("Random");
            		}
            		else if(tipo.equals("Random")) {
            			ind = 0;
            			actualiza_lista("ALL");
            		}
            		if(ind != -1) sp.setSelection(ind);     
            	}
            	else if((pos_X<x) && (x-pos_X)>100) {
            		int ind = -1;
            		if(tipo.equals("ALL")) {
            			ind = 8;
            			actualiza_lista("Random");
            		}
            		else if(tipo.equals("Read")) {
            			ind = 0;
            			actualiza_lista("ALL");
            		}
            		else if(tipo.equals("Watch")) {
            			ind = 1;
            			actualiza_lista("Read");
            		}
            		else if(tipo.equals("Do")) {
            			ind = 2;
            			actualiza_lista("Watch");
            		}
            		else if(tipo.equals("Buy")) {
            			ind = 3;
            			actualiza_lista("Do");
            		}
            		else if(tipo.equals("Music")) {
            			ind = 4;
            			actualiza_lista("Buy");
            		}
            		else if(tipo.equals("Duties")) {
            			ind = 5;
            			actualiza_lista("Music");
            		}
            		else if(tipo.equals("Browse")) {
            			ind = 6;
            			actualiza_lista("Duties");
            		}
            		else if(tipo.equals("Random")) {
            			ind = 7;
            			actualiza_lista("Browse");
            		}
            		if(ind != -1) sp.setSelection(ind);
            	}   
            	break;
        }
		return true;
    }
    
    @Override 
    public void onActivityResult(int requestCode, int resultCode, Intent data){ 
    	super.onActivityResult(requestCode, resultCode, data); 
    	switch(requestCode) { 
    		case (1) : { 
    			if (resultCode == Activity.RESULT_OK) { 
    				if(data.getCharExtra("Cancel", '0')=='a'){
    					val = data.getStringExtra("txt_edited");
    					if(data.getStringExtra("add_tipo").equals("Read")) lread.add(val);
    					if(data.getStringExtra("add_tipo").equals("Watch")) lwatch.add(val);
    					if(data.getStringExtra("add_tipo").equals("Do")) ldo.add(val);
    					if(data.getStringExtra("add_tipo").equals("Buy")) lbuy.add(val);
    					if(data.getStringExtra("add_tipo").equals("Music")) lmusic.add(val);
    					if(data.getStringExtra("add_tipo").equals("Duties")) lduties.add(val);
    					if(data.getStringExtra("add_tipo").equals("Browse")) lbrowse.add(val);
    					if(data.getStringExtra("add_tipo").equals("Random")) lrandom.add(val);
    					if(data.getStringExtra("add_tipo").equals(tipo)) actualiza_lista(tipo);
    					actualiza_all();
    					if(tipo == "ALL") actualiza_lista(tipo);
    				}
                }
                break; 
    	    } 
    		case (2) : {
    			if (resultCode == Activity.RESULT_OK) { 
    				if(data.getCharExtra("Cancel", '0')=='d'){
    					ArrayList al = data.getStringArrayListExtra("listdel");
    					for(int i=0; i<al.size(); ++i){
        					if(tipo.equals("Read")) lread.remove(al.get(i));
        					if(tipo.equals("Watch")) lwatch.remove(al.get(i));
        					if(tipo.equals("Do")) ldo.remove(al.get(i));
        					if(tipo.equals("Buy")) lbuy.remove(al.get(i));
        					if(tipo.equals("Music")) lmusic.remove(al.get(i));
        					if(tipo.equals("Duties")) lduties.remove(al.get(i));
        					if(tipo.equals("Browse")) lbrowse.remove(al.get(i));
        					if(tipo.equals("Random")) lrandom.remove(al.get(i));
        					actualiza_all();
        					actualiza_lista(tipo);
    					}
    				}
                }
    			break;
    		}
    		case (3) : {
    			if (resultCode == Activity.RESULT_OK) { 
    				if(data.getCharExtra("Cancel", '0')=='e'){
    					val = data.getStringExtra("txt_edited");
    					long fila = data.getLongExtra("fila", 0);
    					if(tipo.equals("Read")) {
    						lread.remove((int)fila);
							lread.add((int)fila, val); 
    					}
    					if(tipo.equals("Watch")) {
    						lwatch.remove((int)fila);
    						lwatch.add((int)fila, val); 
    					}
    					if(tipo.equals("Do")) {
    						ldo.remove((int)fila);
    						ldo.add((int)fila, val); 
    					}
    					if(tipo.equals("Buy")) {
    						lbuy.remove((int)fila);
    						lbuy.add((int)fila, val); 
    					}
    					if(tipo.equals("Music")) {
    						lmusic.remove((int)fila);
    						lmusic.add((int)fila, val); 
    					}
    					if(tipo.equals("Duties")) {
    						lduties.remove((int)fila);
    						lduties.add((int)fila, val); 
    					}
    					if(tipo.equals("Browse")) {
    						lbrowse.remove((int)fila);
    						lbrowse.add((int)fila, val); 
    					}
    					if(tipo.equals("Random")) {
    						lrandom.remove((int)fila);
    						lrandom.add((int)fila, val); 
    					}
    					actualiza_lista(tipo);
    				}
    			}
    			break;
    		}
    		case (4) : {
    			if (resultCode == Activity.RESULT_OK) { 
    				if(data.getCharExtra("Cancel", '0')=='h'){
    					val = data.getStringExtra("txt");
    					String t = data.getStringExtra("new_tipo");
    					if(t.equals("Read")) lread.add(val); 
    					if(t.equals("Watch")) lwatch.add(val);
    					if(t.equals("Do")) ldo.add(val); 
    					if(t.equals("Buy")) lbuy.add(val); 
    					if(t.equals("Music")) lmusic.add(val); 
    					if(t.equals("Duties")) lduties.add(val); 
    					if(t.equals("Browse")) lbrowse.add(val); 
    					if(t.equals("Random")) lrandom.add(val); 
    					actualiza_lista(tipo);
    					actualiza_all();
    				}
    			}
    			break;
    		}
    	} 
    }
    
    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
          float x = se.values[0];
          float y = se.values[1];
          float z = se.values[2];
          mAccelLast = mAccelCurrent;
          mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
          float delta = mAccelCurrent - mAccelLast;
          mAccel = mAccel * 0.9f + delta; // perform low-cut filter
          if(mAccel > 5) 
          {
        	  if(!lall.isEmpty() && !mogut){
        		  mogut=true;
        		  esborrar();
        	  }
          }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
      };

      @Override
      protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
      }

      
      private void esborrar(){
    	  DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
    		    public void onClick(DialogInterface dialog, int which) {
    		        switch (which){
    		        case DialogInterface.BUTTON_POSITIVE:
    		        	  lall.clear();
    		        	  lread.clear();
    		        	  lwatch.clear();
    		        	  ldo.clear();
    		        	  lbuy.clear();
    		        	  lmusic.clear();
    		        	  lduties.clear();
    		        	  lbrowse.clear();
    		        	  lrandom.clear();
    		        	  actualiza_all();
    		        	  actualiza_lista("ALL");
    		        	  mogut=false;
    		            break;

    		        case DialogInterface.BUTTON_NEGATIVE:
    		            mogut=false;
    		            break;
    		        }
    		    }
    		};
    	  AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	  builder.setMessage("Are you sure you want to clear all?").setPositiveButton("Yes", dialogClickListener)
    	      .setNegativeButton("No", dialogClickListener).show();
      }
      
      
}