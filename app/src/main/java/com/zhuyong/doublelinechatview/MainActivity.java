package com.zhuyong.doublelinechatview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    /**
     * 第一组数据
     */
    private int[] mDataLeft = {60, 180, 130, 10, 299, 45, 199, 20, 250};
    private int[] mDataRight = {151, 65, 280, 66, 105, 88, 198, 299, 45};
    private String[] mDataTextX = {"项目1", "项目2", "项目3", "项目4", "项目5", "项目6", "项目7", "项目8", "项目9"};

    /**
     * 第二组数据
     */
    private int[] mDataLeftTwo = {60, 181, 130, 100};
    private int[] mDataRightTwo = {151, 65, 40, 20};
    private String[] mDataTextXTwo = {"测试1", "测试2", "测试3", "测试4"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DoubleLineChatView doubleLineChatViewOne = (DoubleLineChatView) findViewById(R.id.line_chat_one);
        doubleLineChatViewOne.setData(mDataLeft, mDataRight, mDataTextX);
        doubleLineChatViewOne.start();

        final DoubleLineChatView doubleLineChatViewTwo = (DoubleLineChatView) findViewById(R.id.line_chat_two);
        doubleLineChatViewTwo.setData(mDataLeftTwo, mDataRightTwo, mDataTextXTwo);

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doubleLineChatViewTwo.start();
            }
        });
    }
}
