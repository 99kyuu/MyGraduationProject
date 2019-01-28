package com.ldx.mygraduationproject.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ldx.mygraduationproject.fragment.FragmentLogin;
import com.ldx.mygraduationproject.fragment.FragmentRegister;

/**
 * Created by freeFreAme on 2018/12/23.
 */
public class AdapterTab extends FragmentPagerAdapter {

    private String login = "登录";
    private String register = "注册";

    private FragmentLogin fragmentLogin;
    private FragmentRegister fragmentRegister ;

    public AdapterTab(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                if (fragmentLogin!=null) {
                    return fragmentLogin;
                }else {
                    fragmentLogin = new FragmentLogin();
                    return fragmentLogin;
                }
            case 1:
                if (fragmentRegister!=null) {
                    return fragmentRegister;
                }else {
                    fragmentRegister = new FragmentRegister();
                    return fragmentRegister;
                }
        }
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0){
            return login;
        }else{
            return register;
        }
    }
}
