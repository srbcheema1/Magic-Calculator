package cheema.calculater;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicBoolean;

import cheema.calculater.CalculatorLib.SrbCalculator;
import cheema.calculater.CalculatorLib.Token;
import cheema.calculater.mymagic.KeyTables;
import cheema.calculater.mymagic.Magic;

import static cheema.calculater.CalculatorLib.Token.lastToken;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myinit();
        setListeners();
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void myinit(){
        this.activity=this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("RAD");
        toolbar.setTitleTextColor(Color.rgb(119,119,119));
        setSupportActionBar(toolbar);

        handler = new Handler();

        ansTv = (TextView)findViewById(R.id.ansTv);
        qusTv = (TextView)findViewById(R.id.qusTv);

        eqlBtn = (Button)findViewById(R.id.eqlBtn);
        delBtn = (Button)findViewById(R.id.delBtn);
        decBtn = (Button)findViewById(R.id.decBtn);

        magic = new Magic();

        toggleDynamic = true;

        asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {Thread.sleep(3000);} catch (InterruptedException e) {e.printStackTrace();}
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                getSupportActionBar().show();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                getSupportActionBar().hide();
                asyncTask=null;
            }
        };
        asyncTask.execute();

        stopWatch = new Thread(new Runnable() {
            @Override
            public void run() {
                for(counter = 0; counter<80; counter++) {
                    if(lock.get()) {
                        synchronized(stopWatch) {
                            try {
                                stopWatch.wait();
                            } catch (InterruptedException e) {}
                        }
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                        //     ansTv.append(Integer.toString(i) + ", ");
                        }
                    });
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {}
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setListeners(){
        menuviewListner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getSupportActionBar().isShowing()){
                    getSupportActionBar().hide();
                }
                else if(asyncTask!=null){
                    synchronized(stopWatch)
                    {
                        counter=0;
                        getSupportActionBar().show();
                    }
                }
                else{
                    asyncTask = new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            if(stopWatch.isAlive())
                                stopWatch.stop();
                            stopWatch.start();
                            while (stopWatch.isAlive()){}
                            return null;
                        }

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            getSupportActionBar().show();
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            if(asyncTask==this)
                                getSupportActionBar().hide();
                            asyncTask=null;
                        }
                    };
                    asyncTask.execute();
                }
            }
        };
        ansTv.setOnClickListener(menuviewListner);
        qusTv.setOnClickListener(menuviewListner);
        ((RelativeLayout)findViewById(R.id.menuContainerRl)).setOnClickListener(menuviewListner);
        morefxnlBtnLl=(LinearLayout)findViewById(R.id.morefxnBtnLl);
        morefxnlBtnLl.setOnTouchListener(new SwipeListener(MainActivity.this));
        qusTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String qus = qusTv.getText().toString();
                if(qus.length()>15){
                    qus = qus.substring(0,15);
                    qusTv.setText(qus);
                }
                try {
                    String str;
                    Token temp = lastToken(qus);
                    if(temp.getType()==Token.num ) {
                        str = SrbCalculator.compute(qus);
                        //write code to get ans
                        ansTv.setText(str) ;
                    }
                    else if(temp.getType()==Token.nul){
                        ansTv.setText("");
                    }
                } catch (Exception e) {
                    if(e.getMessage()=="infinity"){
                        ansTv.setText("∞");
                    }
                    else
                        Toast.makeText(MainActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                //set size
                setTextSize();
            }
        });

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = qusTv.getText().toString();
                if(str.length()>0)
                    str = str.substring(0,str.length()-1);
                else
                    str = "";
                qusTv.setText(str);
            }
        });
        delBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                qusTv.setText("");
                return true;
            }
        });
        eqlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int reply =magic.verifytoken(qusTv.getText().toString());
                if(reply==0)
                    qusTv.setText(ansTv.getText().toString());
                else
                    qusTv.setText(Integer.toString(reply));
                ansTv.setText("");
            }
        });

        decBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int deccount=0;
                    Token lasttoken =Token.lastToken(qusTv.getText().toString());
                    String str = lasttoken.getVal();
                    for(int i =0;i<str.length();i++){
                        if(str.charAt(i)=='.'){
                            deccount++;
                        }
                    }
                    if(deccount==0 &&
                            !(lasttoken.getType()==Token.num && lasttoken.getVal().toString().compareTo("-")==0)
                            && lasttoken.getVal().toString().compareTo("∞")!=0)
                        qusTv.append(".");
                } catch (Exception e) {e.printStackTrace();}
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if(qusTv.getText().toString().compareTo("1501")!=0) {
            getMenuInflater().inflate(R.menu.menu1, menu);
        }
        else {
            getMenuInflater().inflate(R.menu.menu2, menu);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        super.onMenuOpened(featureId, menu);
        lock.set(true);
        return true;
    }

    @Override
    public void onPanelClosed(int featureId, Menu menu) {
        super.onPanelClosed(featureId, menu);
        lock.set(false);
        synchronized(stopWatch)
        {
            counter=0;
            stopWatch.notify();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu1_0) {

            return true;
        }
        if(id==R.id.menu1_1){
            try {
                d = new Dialog(MainActivity.this);
                d.setContentView(R.layout.dialog_rating);

                rateBar = (RatingBar) d.findViewById(R.id.rateBar);

                rateTv = (TextView) d.findViewById(R.id.rateTv);

                rateBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        rateVal = Float.toString(rateBar.getRating());
                        rateTv.setText("rating : " + rateVal);
                    }
                });

                Button rateBtn = (Button) d.findViewById(R.id.rateBtn);

                rateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });

                d.show();
            } catch (Exception e){
                Toast.makeText(MainActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        if(id==R.id.menu1_2){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(String.format("%1$s", getString(R.string.app_name)));
            builder.setMessage(getResources().getText(R.string.aboutus));
            builder.setPositiveButton("OK", null);
            builder.setIcon(R.drawable.calc);
            AlertDialog welcomeAlert = builder.create();
            welcomeAlert.show();
            return true;
        }
        if(id==R.id.menu1_3){
            try {
                startActivity(new Intent(MainActivity.this, KeyTables.class));
            }catch(Exception e){
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        if(id==R.id.menu1_4){
            if(toggleDynamic){
                ansTv.setTextColor(Color.rgb(255,255,255));
                toggleDynamic = false;
            }
            else{
                ansTv.setTextColor(Color.rgb(102,102,102));
                toggleDynamic = true;
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setTextSize(){
        int size = 51;
        int n = qusTv.getText().toString().length();
        if(n<14) size=51;
        else if(n==14) size=48;
        else if(n==15) size=45;
        else size = 42;
        qusTv.setTextSize(size);
        ansTv.setTextSize(size-15*(2*size)/100);
    }


    //variables
    Dialog d;
    RatingBar rateBar;
    TextView rateTv;
    String rateVal;
    TextView ansTv,qusTv;
    AsyncTask<Void,Void,Void> asyncTask;
    Handler handler;
    Thread stopWatch;
    AtomicBoolean lock=new AtomicBoolean(false);
    int counter;
    View.OnClickListener menuviewListner;
    LinearLayout morefxnlBtnLl,buttonLl;
    Button eqlBtn,delBtn,decBtn;
    Boolean toggleDynamic;
    public static Activity activity=null;
    Magic magic;
}
