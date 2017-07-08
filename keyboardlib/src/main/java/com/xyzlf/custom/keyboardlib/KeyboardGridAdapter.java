package com.xyzlf.custom.keyboardlib;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class KeyboardGridAdapter extends BaseAdapter {

    private static final int TYPE_COUNT = 3;

    private static final int TYPE_TEXT = 0;
    private static final int TYPE_IMAGE = 1;
    private static final int TYPE_BANK = 2;

    public static final int TYPE_VALUE_BANK = -1;
    public static final int TYPE_VALUE_DELETE = -2;

    private List<Integer> mDatas;

    public KeyboardGridAdapter(List<Integer> datas) {
        mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Integer getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        int value = mDatas.get(position);
        if (value == TYPE_VALUE_BANK) {
            return TYPE_BANK;
        } else if (value == TYPE_VALUE_DELETE) {
            return TYPE_IMAGE;
        }
        return TYPE_TEXT;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        int type = getItemViewType(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            switch (type) {
                case TYPE_TEXT:
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.keyboard_grid_item_text, parent, false);
                    viewHolder.text = (TextView) convertView.findViewById(R.id.text);
                    break;

                case TYPE_BANK:
                case TYPE_IMAGE:
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.keyboard_grid_item_image, parent, false);
                    viewHolder.deleteIcon = (ImageView) convertView.findViewById(R.id.delete);
                    break;

                default:
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.keyboard_grid_item_text, parent, false);
                    viewHolder.text = (TextView) convertView.findViewById(R.id.text);
                    break;
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        switch (type) {
            case TYPE_TEXT:
                int value = getItem(position);
                String text = String.valueOf(value);
                viewHolder.text.setText(text);
                break;

            case TYPE_IMAGE:
                viewHolder.deleteIcon.setEnabled(true);
                viewHolder.deleteIcon.setImageResource(R.drawable.keboard_icon_delete);
                break;

            case TYPE_BANK:
                viewHolder.deleteIcon.setEnabled(false);
                break;
        }
        return convertView;
    }


    private class ViewHolder {
        TextView text;
        ImageView deleteIcon;
    }
}
