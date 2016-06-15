package com.zzptc.zhongxin.myphone.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zzptc.zhongxin.myphone.R;
import com.zzptc.zhongxin.myphone.activity.HelpActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends Fragment {

    private Button btn_help;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_help, container, false);
        btn_help = (Button) view.findViewById(R.id.btn_help);
        btn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpActivity helpActivity = (HelpActivity) getActivity();
                helpActivity.replace();
            }
        });

        return view;
    }

}
