package com.ritu;

import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


/**
 * @author Vuetne on 2-Aug-18
 * */
public class MainAdapter extends BaseAdapter {

    private Context context;
    private SparseArray<View> mView = new SparseArray<>();
    private static SparseArray<String> mString = new SparseArray<>();
    private static SparseArray<Integer> mClass = new SparseArray<>();

    static {
        mString.put(0,"点");
        mClass.put(0, 0);

        mString.put(1,"基础");
        mClass.put(1, 1);


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
                Intent intent = new Intent();
                intent.putExtra("key",mClass.get(position));
                intent.setClass(context,SecondActivity.class);
                context.startActivity(intent);
            });
            mView.put(position,convertView);
        }
        return convertView;
    }

}
