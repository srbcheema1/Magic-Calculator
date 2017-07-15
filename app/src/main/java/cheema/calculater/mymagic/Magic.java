package cheema.calculater.mymagic;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import cheema.calculater.CalculatorLib.Token;
import cheema.calculater.MainActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by srb on 7/11/17.
 */

public class Magic {
    int stage;
    String key;
    String val;

    public Magic(){
        stage=0;
    }

    public int verifytoken(String orgstr)  {
        try {
            String str = Token.lastToken(orgstr).getVal().toString();
            SQLiteDatabase db = MainActivity.activity.openOrCreateDatabase("data", MODE_PRIVATE, null);
            db.execSQL("create table if not exists key (key varchar,ans varchar)");

            Cursor resultSet = db.rawQuery("select * from key where key = '" + str + "'", null);

            if (resultSet.moveToNext() && stage==0) {
                key = str;
                val = resultSet.getString(1);
                stage = 1;
            }
            else if(stage == 1){
                stage=2;
            }
            else if(stage == 2){
                stage=3;
            }
            else if(stage == 3){
                stage=4;
            }
            else if(stage == 4){
                stage=0;
                return Integer.parseInt(val);
            }
        } catch (Exception e){
            Toast.makeText(MainActivity.activity,"exc in magic : "+ e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return 0;
    }
}
