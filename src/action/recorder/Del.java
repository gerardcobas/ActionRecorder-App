package action.recorder;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.CompoundButton.OnCheckedChangeListener;
 
public class Del extends Activity {
	private ChkListAdapter adapter;
	private ListView list;
	private ArrayList<String> var_ent;
		 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.del);
		Bundle extras = getIntent().getExtras();
        if(extras !=null) var_ent = extras.getStringArrayList("listdel");
        else var_ent = null;
		list = (ListView)findViewById(R.id.listdel);
		
		Button butdel = (Button) findViewById(R.id.button_del);
        butdel.setOnClickListener(new View.OnClickListener() {
           public void onClick(View arg0) {
        	   
        	   ArrayList<String> var_sal = new ArrayList<String>();
        	   
        	   for(int i = 0; i<var_ent.size(); i++) if(adapter.itemSelection[i]) var_sal.add(var_ent.get(i));

        	   Intent resultIntent;
        	   resultIntent = new Intent();
        	   resultIntent.putExtra("Cancel", 'd');
        	   resultIntent.putStringArrayListExtra("listdel", (ArrayList<String>) var_sal);
        	   setResult(Activity.RESULT_OK, resultIntent);
        	   finish();
           } 
        });
        
        Button butcan = (Button) findViewById(R.id.button_cancel_del);
        butcan.setOnClickListener(new View.OnClickListener() {
           public void onClick(View arg0) {
        	   Intent resultIntent;
        	   resultIntent = new Intent();
        	   resultIntent.putExtra("Cancel", 'c');
        	   setResult(Activity.RESULT_OK, resultIntent);
        	   finish();
           } 
        });
		
		fillList();
	}
	 
	private void fillList() {
		int num = var_ent.size();
		adapter = new ChkListAdapter(num);
		for(int i = 0; i < num; i++){
			adapter.addItem(var_ent.get(i));
		}
		list.setAdapter(adapter);
	}
	 
	private class ChkListAdapter extends BaseAdapter {
	private ArrayList<String> items = new ArrayList<String>();
	private LayoutInflater inflater;
	private boolean[] itemSelection;
	public ChkListAdapter(int size) {
		inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.itemSelection = new boolean[size];
	}
	 
	public void addItem(final String object) {
		items.add(object);
		notifyDataSetChanged();
	}
	 
	public int getCount() {
		return items.size();
	}
	 
	public String getItem(int position) {
		return items.get(position).toString();
	}
	 
	public long getItemId(int position) {
		return position;
	}
	 
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.del2, null);
		final ViewHolder holder = new ViewHolder();
		holder.chkItem = (CheckBox)convertView.findViewById(R.id.checkBox);
		holder.chkItem.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				itemSelection[position] = holder.chkItem.isChecked();
			}
		});
		holder.chkItem.setChecked(itemSelection[position]);
		convertView.setTag(holder);
		holder.chkItem.setText(getItem(position));
		return convertView;
	}
	 
	public int getItemsLength() {
		if(itemSelection == null) return 0;
			return itemSelection.length;
		}
	 
		public void set(int i, boolean b) {
			itemSelection[i] = b;
		}
	 
	}
	 
	public static class ViewHolder {
		public CheckBox chkItem;
	}
	 
}