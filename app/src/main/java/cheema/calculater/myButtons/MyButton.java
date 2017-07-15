package cheema.calculater.myButtons;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by srb on 7/14/17.
 */

public class MyButton extends android.support.v7.widget.AppCompatButton {
    public MyButton(){
        super(null);
        myinit();
    }

    public MyButton(Context context){
        super(context);
        myinit();
    }

    public MyButton(Context context, AttributeSet attrs){
        super(context, attrs);
        myinit();
    }

    public MyButton(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        myinit();
    }

    public void myinit(){
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                       // v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.getBackground().setColorFilter(Color.rgb(200,200,200), PorterDuff.Mode.LIGHTEN);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }
}
