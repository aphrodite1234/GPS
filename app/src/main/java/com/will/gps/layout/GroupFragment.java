package com.will.gps.layout;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.will.gps.R;

/**
 * Created by MaiBenBen on 2019/4/20.
 */
@SuppressLint("ValidFragment")
public class GroupFragment extends Fragment {
    private View view;
    private Context context;

    @SuppressLint("ValidFragment")
    public GroupFragment(Context context){
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_fragment_group,container,false);
        return view;
    }
}
