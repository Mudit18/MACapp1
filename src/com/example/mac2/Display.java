package com.example.mac2;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Display extends Activity implements OnItemClickListener{

    List<String> nameori = new ArrayList<String>();
    List<String> namefil = new ArrayList<String>();
    List<String> phnoori = new ArrayList<String>();
    List<String> phnofil = new ArrayList<String>();
    MyAdapter ma;
    Button select,plusminus,sort;
    EditText amnt,srcbox;
    int srctextlen=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);
        getAllContacts(this.getContentResolver());
         ListView lv= (ListView) findViewById(R.id.lv);
        amnt = (EditText) findViewById(R.id.amount);
        srcbox = (EditText) findViewById(R.id.searchbox);
        sort = (Button) findViewById(R.id.search);
            ma = new MyAdapter();
            lv.setAdapter(ma);
              lv.setOnItemClickListener(this); 
            lv.setItemsCanFocus(false);
            plusminus = (Button) findViewById(R.id.addsub);
           select = (Button) findViewById(R.id.go);
        // adding
        select.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v) {
                    StringBuilder checkedcontacts= new StringBuilder();

                for(int i = 0; i < nameori.size(); i++)

                    {
                    if(ma.mCheckStates.get(i)==true)
                    	        {
                         checkedcontacts.append(nameori.get(i).toString());
                         checkedcontacts.append("\n");

                    }
                    else
                    {

                    }


                }

                Toast.makeText(Display.this, checkedcontacts,Toast.LENGTH_SHORT).show();
                Toast.makeText(Display.this, plusminus.getText().toString() + amnt.getText(), Toast.LENGTH_SHORT).show();
            }       
        });
        
        plusminus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (plusminus.getText()=="+") {
					plusminus.setText("-");
				} else {
					plusminus.setText("+");

				}
				
			}
		});
        
        srcbox.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				srctextlen = srcbox.getText().length();
				String names_ori[]=new String[nameori.size()];
				String phnos_ori[]=new String[phnoori.size()];
				names_ori=nameori.toArray(names_ori);
				phnos_ori=phnoori.toArray(phnos_ori);
				namefil.clear();
				phnofil.clear();
				if (s==null || s.length()==0)
				{

					
					for (int i = 0; i < nameori.size(); i++)
					{
						namefil.add(names_ori[i]);
						phnofil.add(phnos_ori[i]);
			}} 
				else
				{
					for (int i = 0; i < names_ori.length; i++)
					{
						if (srctextlen <= names_ori[i].length()){
							if(srcbox.getText().toString().equalsIgnoreCase(
									(String)
									names_ori[i].subSequence(0,
											srctextlen))){
							namefil.add(names_ori[i]);
							phnofil.add(phnos_ori[i]);
						}
						
					} 
					}
				
				
		};
		ma.notifyDataSetChanged();			
			}});
		}
        
    
        
    
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
         ma.toggle(arg2);
    }

    public  void getAllContacts(ContentResolver cr) {

        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
          String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
          String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
          nameori.add(name);
          phnoori.add(phoneNumber);
          namefil.add(name);
          phnofil.add(phoneNumber);
          
        }

        phones.close();
        
     }
    
    class MyAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener
    {  private SparseBooleanArray mCheckStates;
       LayoutInflater mInflater;
        TextView tv1,tv;
        CheckBox cb;
        MyAdapter()
        {
            mCheckStates = new SparseBooleanArray(nameori.size());
            mInflater = (LayoutInflater)Display.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return namefil.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub

            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View vi=convertView;
            if(convertView==null)
             vi = mInflater.inflate(R.layout.row, null); 
             tv= (TextView) vi.findViewById(R.id.textView1);
             tv1= (TextView) vi.findViewById(R.id.textView2);
             cb = (CheckBox) vi.findViewById(R.id.checkBox1);
            
             tv.setText("Name :"+ namefil.get(position));
             tv1.setText("Phone No :"+ phnofil.get(position));
             String namesing = namefil.get(position);
             int pos = nameori.indexOf(namesing);
                         
             
             cb.setTag(pos);
             cb.setChecked(mCheckStates.get(pos, false));
             cb.setOnCheckedChangeListener(this);

            return vi;
        }
         public boolean isChecked(int position) {
                return mCheckStates.get(position, false);
            }

            public void setChecked(int position, boolean isChecked) {
                mCheckStates.put(position, isChecked);
            }

            public void toggle(int position) {
                setChecked(position, !isChecked(position));
            }
        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                boolean isChecked) {
            // TODO Auto-generated method stub

             mCheckStates.put((Integer) buttonView.getTag(), isChecked);         
        }
		
			 
    }   
}
	