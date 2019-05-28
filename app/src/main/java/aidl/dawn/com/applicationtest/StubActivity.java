package aidl.dawn.com.applicationtest;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * 作者：created by meixi
 * 邮箱：15913707499@163.com
 * 日期：2019/5/6 10
 */

public class StubActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setNavigationBarColor(Color.parseColor("#889900"));
        TextView textView = new TextView(StubActivity.this);
        textView.setText("jdsjdjskjdskjsjkkjdsjk");
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = 120;
        layoutParams.leftMargin = 20;
        window.addContentView(textView,layoutParams);
        setContentView(R.layout.activity_stub);
    }
}
