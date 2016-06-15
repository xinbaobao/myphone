package com.zzptc.zhongxin.myphone.fragment;


import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.zzptc.zhongxin.myphone.R;
import com.zzptc.zhongxin.myphone.bean.UpdateInfo;
import com.zzptc.zhongxin.myphone.download.DownloadManager;

import org.xutils.ex.DbException;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateFragment extends DialogFragment {

    private DownloadManager downloadManager;

    public static UpdateFragment newInstance(String info){
        UpdateFragment updateFragment = new UpdateFragment();

        Bundle bundle = new Bundle();
        bundle.putString("info", info);
        updateFragment.setArguments(bundle);

        return updateFragment;
    }

    public static UpdateFragment newInstance(String info,UpdateInfo updateInfo){
        UpdateFragment updateFragment = new UpdateFragment();

        Bundle bundle = new Bundle();
        bundle.putString("info", info);
        bundle.putSerializable("updateinfo",updateInfo);
        updateFragment.setArguments(bundle);

        return updateFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCancelable(false);

        downloadManager = DownloadManager.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);//无标题

        View v = inflater.inflate(R.layout.fragment_update, container, false);

        TextView textView = (TextView) v.findViewById(R.id.tv_content);
        Button btn_ok = (Button) v.findViewById(R.id.btn_ok);

        String info = getArguments().getString("info");
        textView.setText(info);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //
                UpdateInfo updateInfo = (UpdateInfo) getArguments().getSerializable("updateinfo");
                if(updateInfo != null){
                    if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                        try {
                            downloadManager.startDownload(updateInfo.getDownloadUrl(),"weishi","/sdcard/download/safeguar.apk",true,true,null);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                }

                dismiss();
            }
        });

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                }
                return false;
            }
        });

        return v;
    }

}
