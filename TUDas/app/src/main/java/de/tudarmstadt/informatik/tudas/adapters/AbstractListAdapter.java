package de.tudarmstadt.informatik.tudas.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractListAdapter<T> extends BaseAdapter {

    LayoutInflater layoutInflater;

    List<T> list;

    AbstractListAdapter(Context context) {
        list = new LinkedList<>();
        layoutInflater = LayoutInflater.from(context);
    }

    public void setList(List<T> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);
}
