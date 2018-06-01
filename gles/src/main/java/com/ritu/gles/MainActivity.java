package com.ritu.gles;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ritu.gles.geometric.GeometricActivity;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        String[] data = {"绘制图形"};
        //2.适配器
        @SuppressWarnings("unchecked")
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
        //3.绑定
        setListAdapter(arrayAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent();
        switch (position){
            case 0:
                intent.setClass(MainActivity.this, GeometricActivity.class);
                break;
             default:break;
        }
        startActivity(intent);
    }
}
