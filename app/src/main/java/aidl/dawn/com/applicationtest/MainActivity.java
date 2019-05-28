package aidl.dawn.com.applicationtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import aidl.dawn.com.applicationtest.hook.HookHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HookHelper.init(this);
        findViewById(R.id.tv_hook_amn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startJump();
            }
        });
    }

    public void startJump() {
        Intent intent = new Intent(this, HideActivity.class);
        startActivity(intent);
    }


}
