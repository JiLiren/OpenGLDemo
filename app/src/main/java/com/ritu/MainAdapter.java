package com.ritu;

import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ritu.d1_hello.D1Activity;
import com.ritu.d2_alert.D2Activity;
import com.ritu.d3_start.D3Activity;

/**
 * @author Vuetne on 2-Aug-18
 * */
public class MainAdapter extends BaseAdapter {

    private Context context;
    private SparseArray<View> mView = new SparseArray<>();
    private static SparseArray<String> mString = new SparseArray<>();
    private static SparseArray<Class> mClass = new SparseArray<>();

    static {
        mString.put(0,"HelloOpenGl");
        mClass.put(0, D1Activity.class);

        mString.put(1,"Alert");
        mClass.put(1, D2Activity.class);

        mString.put(2,"Star");
        mClass.put(2, D3Activity.class);
    }

    public MainAdapter(Context context) {
        this.context = context;

    }

    @Override
    public int getCount() {
        return mString.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mView.get(position);
        if (convertView == null){
            convertView = View.inflate(context, R.layout.item_adapter,null);
            TextView textView = convertView.findViewById(R.id.tv_name);
            textView.setText(mString.get(position));
            textView.setOnClickListener(v -> {
                context.startActivity(new Intent(context,mClass.get(position)));
            });
            mView.put(position,convertView);
        }
        return convertView;
    }

}
