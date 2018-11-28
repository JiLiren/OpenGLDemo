package com.ritu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;



/**
 * @author vurtne on 19-May-18.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = findViewById(R.id.layout_content);
        MainAdapter adapter = new MainAdapter(this);
        listView.setAdapter(adapter);
    }
}
