package com.yty.libframe.crash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yty.libframe.R;

public class DefaultErrorActivity extends AppCompatActivity {
    private Button restartButton;
    private Button moreInfoButton;
    private ImageView errorImageView;
    private CrashConfig config;
    String errorInfo = "";

    @SuppressLint("PrivateResource")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //样式绑定
        TypedArray a = obtainStyledAttributes(R.styleable.AppCompatTheme);
        if (!a.hasValue(R.styleable.AppCompatTheme_windowActionBar)) {
            setTheme(R.style.Theme_AppCompat_Light_DarkActionBar);
        }
        a.recycle();
        //布局绑定
        setContentView(R.layout.activity_default_crash);
        restartButton = (Button) findViewById(R.id.bt_restart);
        moreInfoButton = (Button) findViewById(R.id.bt_more_info);
        errorImageView = ((ImageView) findViewById(R.id.iv_error_image));
        config = CrashHandle.getInstance().crashConfig;

        //获取传过来的错误信息
        errorInfo = getIntent().getBundleExtra("BundleInfo").getString("ErrorInfo");

        //布局控件事件绑定
        restartButton.setText(R.string.restart_app);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取到CrashConfig里面的数据 启动设置的activity
                Intent intent = new Intent(DefaultErrorActivity.this, config.restartActivityClass);
                DefaultErrorActivity.this.startActivity(intent);
                DefaultErrorActivity.this.finish();
            }
        });

        moreInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //We retrieve all the error data and show it
                AlertDialog dialog = new AlertDialog.Builder(DefaultErrorActivity.this)
                        .setTitle(R.string.error_details)
                        .setMessage(errorInfo)
                        .setPositiveButton(R.string.close, null)
                        .setNeutralButton(R.string.copy_details,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        copyErrorToClipboard();
                                        Toast.makeText(DefaultErrorActivity.this, R.string.copy_details, Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .show();
                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 13);
            }
        });


        Integer drawableId = config.errorDrawable;

        if (drawableId != null) {
            errorImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), drawableId, getTheme()));
        }

    }

    private void copyErrorToClipboard() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(getString(R.string.error_info), errorInfo);
        clipboard.setPrimaryClip(clip);
    }
}