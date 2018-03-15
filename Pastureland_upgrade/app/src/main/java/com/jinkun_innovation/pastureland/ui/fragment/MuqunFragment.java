package com.jinkun_innovation.pastureland.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jinkun_innovation.pastureland.R;

/**
 * Created by Guan on 2018/3/15.
 */

public class MuqunFragment extends Fragment{


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View view= View.inflate(getActivity(), R.layout.fragment_muqun,null);

        return view;

    }


}
