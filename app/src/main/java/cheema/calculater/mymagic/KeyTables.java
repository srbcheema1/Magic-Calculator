package cheema.calculater.mymagic;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import cheema.calculater.R;
import cheema.calculater.mymagic.adapters.Keytables_list_adapter;
import cheema.calculater.mymagic.pojo_classes.KeylogPojo;

public class KeyTables extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_tables);

        myinit();
        setListeners();
    }

    public void myinit(){
        listView = (ListView)findViewById(R.id.listView);
        Loader loder = new Loader();
        loder.execute();
        list_adapter = new Keytables_list_adapter(this,R.layout.item, arrayList);
        listView.setAdapter(list_adapter);
        newBtn = (Button)findViewById(R.id.newBtn);
    }

    public void setListeners(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d = new Dialog(KeyTables.this);
                d.setContentView(R.layout.dialog_newkey);
                ansEt = (EditText) d.findViewById(R.id.ansEt);
                keyEt = (EditText) d.findViewById(R.id.keyEt);
                okBtn = (Button) d.findViewById(R.id.okBtn);
                if(okBtn==null){
                    Toast.makeText(KeyTables.this, "null ok", Toast.LENGTH_SHORT).show();
                }
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ansStr = ansEt.getText().toString();
                        String keyStr = keyEt.getText().toString();
                        if(keyStr.length()!=4 || ansStr.length()!=keyStr.length()*2){
                            Toast.makeText(KeyTables.this,"key must be 4 characters long and Value must be 8 characters long",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        SQLiteDatabase db = openOrCreateDatabase("data", MODE_PRIVATE, null);
                        db.execSQL("create table if not exists key (key varchar,ans varchar)");

                        Cursor resultSet = db.rawQuery("select * from key where key = '" + keyStr + "'", null);
                        if (resultSet.moveToNext()) {
                            Toast.makeText(KeyTables.this, "key already exists", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            db.execSQL("Insert into key values ( '" + keyStr + "','" + ansStr + "')");
                            Toast.makeText(KeyTables.this, "Successfully inserted", Toast.LENGTH_SHORT).show();
                            list_adapter.notifyDataSetChanged();
                            arrayList.add(new KeylogPojo(keyStr,ansStr));
                            d.dismiss();
                        } catch (Exception e) {
                            Toast.makeText(KeyTables.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                d.show();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(KeyTables.this);
                builder.setTitle("Delete key");
                builder.setMessage("Are you sure to delete this key");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str = "";
                        str = arrayList.get(position).getKey();
                        arrayList.remove(position);
                        SQLiteDatabase db = openOrCreateDatabase("data", MODE_PRIVATE, null);
                        db.execSQL("delete from key where key = '" + str + "'");
                        list_adapter.notifyDataSetChanged();
                    }
                });
                builder.setIcon(R.drawable.key);
                AlertDialog welcomeAlert = builder.create();
                welcomeAlert.show();
                return false;
            }
        });
    }

    public class Loader extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            String keySt,ansSt;
            SQLiteDatabase db = openOrCreateDatabase("data", MODE_PRIVATE, null);
            db.execSQL("create table if not exists key (key varchar,ans varchar)");
            Cursor resultSet = db.rawQuery("select * from key",null);
            if(resultSet.getCount()==0) return null;
            while (resultSet.moveToNext()) {
                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                keySt = resultSet.getString(0);
                ansSt = resultSet.getString(1);
                arrayList.add(new KeylogPojo(keySt,ansSt));
            }
            System.out.println("here  " + resultSet.getCount());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            list_adapter.notifyDataSetChanged();
            dialog.dismiss();
        }

        //variables
        ProgressDialog dialog = ProgressDialog.show(KeyTables.this,"loading","please wait");
    }// end class Loader

    //variables
    ListView listView;
    ArrayList<KeylogPojo> arrayList = new ArrayList<>();
    Keytables_list_adapter list_adapter;
    Button newBtn,okBtn;
    Dialog d;
    EditText ansEt,keyEt;
}
