package com.ldx.mygraduationproject.adapter;

import android.content.Context;

import java.util.List;

public abstract class SimpleAdapter<T> extends BaseAdapter<T,BaseViewHolder> {

    public SimpleAdapter(Context context, int mLayoutRes) {
        super(context, mLayoutRes);
    }
    public SimpleAdapter(Context context, List<T> data, int mLayoutRes) {
        super(context, data, mLayoutRes);
    }
}
