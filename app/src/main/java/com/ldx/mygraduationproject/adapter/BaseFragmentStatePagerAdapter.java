package com.ldx.mygraduationproject.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.ldx.mygraduationproject.activity.MainActivity;
import com.ldx.mygraduationproject.fragment.FragmentBuy;
import com.ldx.mygraduationproject.fragment.FragmentHeath;
import com.ldx.mygraduationproject.fragment.FragmentMedicalRecords;
import com.ldx.mygraduationproject.fragment.FragmentReader;
import com.ldx.mygraduationproject.fragment.FragmentDetails;


/**
 * Created by freeFreAme on 2018/12/29.
 */

public class BaseFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
    private final int PAGER_COUNT = 3;
    private FragmentDetails fragmentDetails = null;
//    private FragmentReader fragmentReader =null;
    private FragmentHeath fragmentHeath=null;
//    private FragmentMedicalRecords fragmentMedicalRecords=null;
    private FragmentBuy fragmentBuy=null;




    public BaseFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentDetails = new FragmentDetails();
//        fragmentReader =new FragmentReader();
        fragmentHeath=new FragmentHeath();
//        fragmentMedicalRecords=new FragmentMedicalRecords();
        fragmentBuy=new FragmentBuy();
    }


    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case MainActivity.PAGE_ONE:
                fragment = fragmentHeath;
                break;
            case MainActivity.PAGE_TWO:
                fragment = fragmentDetails;
                break;
            case MainActivity.PAGE_THREE:
                fragment = fragmentBuy;
        }
        return fragment;
    }
}
