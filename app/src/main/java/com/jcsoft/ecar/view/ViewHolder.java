package com.jcsoft.ecar.view;

import android.util.SparseArray;
import android.view.View;

/**
 * 适配器的 ViewHolder
 * @author Dive
 * version 1.0
 */
@SuppressWarnings("unchecked")
public class ViewHolder {
    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
}