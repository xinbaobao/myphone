package com.zzptc.zhongxin.myphone.download;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.x;

import java.io.File;

/**
 * Created by wyouflf on 15/11/11.
 */
public class DefaultDownloadViewHolder extends DownloadViewHolder {

    public DefaultDownloadViewHolder(View view, DownloadInfo downloadInfo) {
        super(view, downloadInfo);
    }

    @Override
    public void onWaiting() {

    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onLoading(long total, long current) {

    }

    @Override
    public void onSuccess(File result) {
        Toast.makeText(x.app(), "下载完成", Toast.LENGTH_LONG).show();

        //安装
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(result),"application/vnd.android.package-archive");
        x.app().startActivity(intent);
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        Toast.makeText(x.app(), "下载失败", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCancelled(Callback.CancelledException cex) {
    }
}
