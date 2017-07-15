package cheema.calculater;

import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by srb on 7/8/17.
 */

public class SwipeListener implements View.OnTouchListener {

    static final String logTag = "ActivitySwipeDetector";
    private Activity activity;
    static final int MIN_DISTANCE = 10;// TODO change this runtime based on screen resolution. for 1920x1080 is to small the 100 distance
    private float downX, downY, upX, upY;

    // private MainActivity mMainActivity;

    public SwipeListener(MainActivity mainActivity) {
        activity = mainActivity;
    }

    public void onRightToLeftSwipe() {
        Log.i(logTag, "RightToLeftSwipe!");
        Toast.makeText(activity, "RightToLeftSwipe", Toast.LENGTH_SHORT).show();
        // activity.doSomething();
    }

    public void onLeftToRightSwipe() {
        Log.i(logTag, "LeftToRightSwipe!");
        Toast.makeText(activity, "LeftToRightSwipe", Toast.LENGTH_SHORT).show();
        // activity.doSomething();
    }

    public void onTopToBottomSwipe() {
        Log.i(logTag, "onTopToBottomSwipe!");
        Toast.makeText(activity, "onTopToBottomSwipe", Toast.LENGTH_SHORT).show();
        // activity.doSomething();
    }

    public void onBottomToTopSwipe() {
        Log.i(logTag, "onBottomToTopSwipe!");
        Toast.makeText(activity, "onBottomToTopSwipe", Toast.LENGTH_SHORT).show();
        // activity.doSomething();
    }

    public boolean onTouch(View v, MotionEvent event) {
        int flag=0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                return true;
            }
            case MotionEvent.ACTION_UP: {
                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                if(Math.abs(deltaX)>Math.abs(deltaY)){
                    if (Math.abs(deltaX) > MIN_DISTANCE) {
                        if (deltaX < 0)
                            this.onLeftToRightSwipe();
                        if (deltaX > 0)
                            this.onRightToLeftSwipe();
                        flag=1;
                    }
                }else{
                    if (Math.abs(deltaY) > MIN_DISTANCE) {
                        if (deltaY < 0)
                            this.onTopToBottomSwipe();
                        if (deltaY > 0)
                            this.onBottomToTopSwipe();
                        flag=1;
                    }
                }

                if(flag==1) return true;
                else return false; // no swipe horizontally and no swipe vertically
            }// case MotionEvent.ACTION_UP:
        }
        return false;
    }

}