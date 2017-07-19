package com.example.hyunji.moivo;

import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Window;

/**
 * Created by Hyunji on 7/18/17.
 */

public class PopUp extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.popup);
        
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .6));

    }



}
