package com.example.choosepathdialog;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.tochange.yang.lib.log;

public class MainActivity extends Activity
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void onClick(View v)
    {
        log.e("");
        new ChoosePathDialog(this, false, ".mp3", new ChoosePathInterface() {

            @Override
            public void setPath(ArrayList<String> pathList)
            {
                log.e("pathList size:" + pathList.size());
                log.e("pathList:" + pathList);
            }
        }).show();
    }
}
