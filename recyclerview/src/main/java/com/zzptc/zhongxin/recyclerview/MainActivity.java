package com.zzptc.zhongxin.recyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rl_view;
    private List<String> listitem;
    private MyAdapter myAdapter;
    private Button btn_add;
    private Button btn_remove;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //找到相应的控件
        rl_view = (RecyclerView) findViewById(R.id.rl_view);
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_remove = (Button) findViewById(R.id.btn_remove);
        btn_add.setOnClickListener(this);
        btn_remove.setOnClickListener(this);
        //准备数据
        initData();
        //布局管理器
        rl_view.setLayoutManager(new LinearLayoutManager(this));
        //设置适配器
        myAdapter = new MyAdapter(listitem,this);
        rl_view.setAdapter(myAdapter);
        //设置分割线
        rl_view.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

        //添加监听
        //给适配器配置监听
        myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View itemView, int position) {
                Toast.makeText(MainActivity.this, "你点击了我，我的位置是" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClickListener(View itemView, int position) {
                Toast.makeText(MainActivity.this, "你长按了第" + position + "个条目", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 初始化数据
     */
    private void initData(){
        listitem = new ArrayList<>();
        for(int i = 0; i < 20; i ++){
            listitem.add("第" +i + "天");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add:
                myAdapter.addData(1,"添加条目");
                break;

            case R.id.btn_remove:
                myAdapter.removeData(1,"删除条目");
                break;
        }
    }
}
