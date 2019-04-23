package com.ldx.mygraduationproject.activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.bean.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderActivity extends BaseActivity {

private ExpandableListView elMainOrdercenter;
private Map<String,List<Order>> dataMap;
private String[] titleArr;

private MyAdapter myAdapter;
    private int[] iconArr=new int[]{R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher};

    @Override
    protected int setLayoutId() {
        return R.layout.activity_order;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    /**
 * 初始化数据
 */
    protected void initData(){
        elMainOrdercenter=(ExpandableListView)findViewById(R.id.el_main_ordercenter);
        //初始化列表数据，正常由服务器返回的Json数据
        initJsonData();
        myAdapter=new MyAdapter();
        elMainOrdercenter.setAdapter(myAdapter);
        //设置列表展开
        for(int i=0;i<dataMap.size();i++){
            elMainOrdercenter.expandGroup(i);
        }

        }

    @Override
    protected void setListener() {
        super.setListener();
        //设置父标题点击不能收缩
        elMainOrdercenter.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });
        //订单子条目的点击事件
        elMainOrdercenter.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(OrderActivity.this,"跳转到订单详细页面:"+childPosition,Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }


/**
 * ExpandableListviewAdapter初始化
 */
private class MyAdapter extends BaseExpandableListAdapter {
    //  获得父项的数量
    @Override
    public int getGroupCount() {
        return dataMap.size();
    }
    //  获得某个父项的子项数目
    @Override
    public int getChildrenCount(int groupPosition) {
        return dataMap.get(titleArr[groupPosition]).size();
    }
    //  获得某个父项
    @Override
    public Object getGroup(int groupPosition) {
        return dataMap.get(titleArr[groupPosition]);
    }
    //  获得某个父项的某个子项
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return dataMap.get(titleArr[groupPosition]).get(childPosition);
    }
    //  获得某个父项的id
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    //  获得某个父项的某个子项的id
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    //  按函数的名字来理解应该是是否具有稳定的id，这个方法目前一直都是返回false，没有去改动过
    @Override
    public boolean hasStableIds() {
        return false;
    }
    //  获得父项显示的view
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=View.inflate(OrderActivity.this,R.layout.parent_view,null);
        }
        ImageView ivParentviewIcon=convertView.findViewById(R.id.iv_parentview_icon);
        TextView tvParentviewTitle= convertView.findViewById(R.id.tv_parentview_title);
        ivParentviewIcon.setImageResource(iconArr[groupPosition]);
        tvParentviewTitle.setText(titleArr[groupPosition]);
        return convertView;
    }
    //  获得子项显示的view
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=View.inflate(OrderActivity.this,R.layout.child_view,null);
        }
        //获取布局控件id
        TextView tvChildviewContent=(TextView) convertView.findViewById(R.id.tv_childview_content);
        tvChildviewContent.setText(dataMap.get(titleArr[groupPosition]).get(childPosition).getMedicineName());
        Button btnChildviewDelete= convertView.findViewById(R.id.btn_childview_delete);
        Button btnChildviewEvaluate= convertView.findViewById(R.id.btn_childview_evaluate);
        //根据服务器返回的数据来显示和隐藏按钮
        final Order order=dataMap.get(titleArr[groupPosition]).get(childPosition);
//        if(order.isEvaluateState()){
//            btnChildviewEvaluate.setVisibility(View.VISIBLE);
//        }else {
//            btnChildviewEvaluate.setVisibility(View.GONE);
//        }
//        if(order.isDeleteState()){
//            btnChildviewDelete.setVisibility(View.VISIBLE);
//        }else {
//            btnChildviewDelete.setVisibility(View.GONE);
//        }
        //设置评价按钮的点击事件
        btnChildviewEvaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OrderActivity.this,"跳转到"+order.getMedicineName()+"的评价页面",Toast.LENGTH_SHORT).show();
            }
        });
        //设置删除按钮的点击事件
        btnChildviewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataMap.get(titleArr[groupPosition]).remove(childPosition);
                myAdapter.notifyDataSetChanged();
            }
        });
        return convertView;
    }
    //  子项是否可选中，如果需要设置子项的点击事件，需要返回true
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
    /**
     * 初始化列表数据，正常由服务器返回的Json数据
     */
    private void initJsonData(){
        dataMap=new HashMap<String,List<Order>>();
        titleArr=new String[]{"商城店铺1","商城店铺2","商城店铺3"};
        List<Order> list1=new ArrayList<Order>();
        List<Order> list2=new ArrayList<>();
        List<Order> list3=new ArrayList<Order>();
        Order Order1=new Order();
        Order1.setMedicineName(titleArr[0]+"_one");
        Order1.setMedicineImg("1");
        Order1.setMedicinePrice("1");
        Order Order2=new Order();
        Order2.setMedicineName(titleArr[0]+"_one");
        Order2.setMedicineImg("1");
        Order2.setMedicinePrice("1");
        Order Order3=new Order();
        Order3.setMedicineName(titleArr[0]+"_one");
        Order3.setMedicineImg("1");
        Order3.setMedicinePrice("1");
//
//        Order Order4=new Order();
//        Order4.setName(titleArr[1]+"_one");
//        Order4.setEvaluateState(true);
//        Order4.setDeleteState(false);
//        Order Order5=new Order();
//        Order5.setName(titleArr[1]+"_two");
//        Order5.setEvaluateState(false);
//        Order5.setDeleteState(true);
//
//        Order Order6=new Order();
//        Order6.setName(titleArr[2]+"_one");
//        Order6.setEvaluateState(true);
//        Order6.setDeleteState(false);
//        Order Order7=new Order();
//        Order7.setName(titleArr[2]+"_two");
//        Order7.setEvaluateState(false);
//        Order7.setDeleteState(true);
//        Order Order8=new Order();
//        Order8.setName(titleArr[2]+"_three");
//        Order8.setEvaluateState(true);
//        Order8.setDeleteState(true);
//        Order Order9=new Order();
//        Order9.setName(titleArr[2]+"_four");
//        Order9.setEvaluateState(false);
//        Order9.setDeleteState(false);
        list1.add(Order1);
        list1.add(Order2);
        list1.add(Order3);
//        list2.add(Order4);
//        list2.add(Order5);
//        list3.add(Order6);
//        list3.add(Order7);
//        list3.add(Order8);
//        list3.add(Order9);
        dataMap.put(titleArr[0],list1);
        dataMap.put(titleArr[1],list2);
//        dataMap.put(titleArr[2],list3);
    }
}
