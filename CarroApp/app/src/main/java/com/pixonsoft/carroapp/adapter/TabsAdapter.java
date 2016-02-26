package com.pixonsoft.carroapp.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.pixonsoft.carroapp.R;
import com.pixonsoft.carroapp.fragment.CarrosFragment;

/**
 * Created by mobile6 on 11/19/15.
 */
public class TabsAdapter extends FragmentStatePagerAdapter{

    private final String TIPO = "tipo";

    public TabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        CarrosFragment fragment = new CarrosFragment();
        Bundle args = new Bundle();

        switch (position){
            case 0:
                args.putInt(TIPO, R.raw.carros_classicos);
                break;

            case 1:
                args.putInt(TIPO, R.raw.carros_esportivos);
                break;

            case 2:
                args.putInt(TIPO, R.raw.carros_luxuosos);
                break;
        }

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
