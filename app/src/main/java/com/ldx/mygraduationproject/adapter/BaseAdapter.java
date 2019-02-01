package com.ldx.mygraduationproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
                   _ooOoo_
                  o8888888o
                  88" . "88
                  (| -_- |)
                  O\  =  /O
               ____/`---'\____
             .'  \\|     |//  `.
            /  \\|||  :  |||//  \
           /  _||||| -:- |||||-  \
           |   | \\\  -  /// |   |
           | \_|  ''\---/''  |   |
           \  .-\__  `-`  ___/-. /
         ___`. .'  /--.--\  `. . __
      ."" '<  `.___\_<|>_/___.'  >'"".
     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
     \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                   `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
         佛祖保佑       永无BUG
*/
public abstract class BaseAdapter<T, Holder extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder> {

    protected List<T> mData;

    protected final Context mContext;

    protected int mLayoutRes;

    private OnItemClickListener onItemClickListener;

    public BaseAdapter(Context context, int mLayoutRes) {
        this(context, null, mLayoutRes);
    }

    public BaseAdapter(Context context, List<T> mData, int mLayoutRes) {
        this.mData = mData == null ? new ArrayList<T>() : mData;
        this.mContext = context;
        this.mLayoutRes = mLayoutRes;

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
        void onItemLongClick(View v, int position);
    }

    @Override
    public int getItemCount() {

        if (mData == null || mData.size() <= 0)
            return 0;

        return mData.size();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutRes, parent, false);
        return new BaseViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {

        T t = getItemPosition(position);
        change((Holder) holder, t,position);
        if (onItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v,position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onItemLongClick(v,position);
                    return false;
                }
            });
        }
    }

    protected abstract void change(Holder viewHolder, T t,int position);

    public T getItemPosition(int p) {
        if (p >= mData.size())
            return null;
        return mData.get(p);
    }

    /*刷新数据*/
    public void refreshData(List<T> list) {

        if (list != null && list.size() > 0) {
            clear();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                mData.add(i, list.get(i));
                notifyItemInserted(i);
            }
        }
    }

    /*加载更多数据*/
    public void loadMoreData(List<T> list) {

        if (list != null && list.size() > 0) {
            int size = list.size();
            int begin = mData.size();
            for (int i = 0; i < size; i++) {
                mData.add(list.get(i));
                notifyItemInserted(i + begin);
            }
        }
    }

    public void clear() {
        for (Iterator it = mData.iterator(); it.hasNext(); ) {

            T t = (T) it.next();
            int position = mData.indexOf(t);
            it.remove();
            notifyItemRemoved(position);
        }
    }

    /*从列表中删除某项数据*/
    public void removeItem(T t) {

        int position = mData.indexOf(t);
        mData.remove(position);
        notifyItemRemoved(position);
    }

    public List<T> getDatas() {
        return mData;
    }

    public void addData(List<T> datas) {
        addData(0, datas);
    }

    public void addData(int position, List<T> list) {

        if (list != null && list.size() > 0) {
            for (T t : list) {
                mData.add(position, t);
                notifyItemInserted(position);
            }
        }
    }
}
