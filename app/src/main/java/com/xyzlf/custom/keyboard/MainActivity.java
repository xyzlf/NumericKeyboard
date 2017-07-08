package com.xyzlf.custom.keyboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.xyzlf.custom.keyboardlib.IKeyboardListener;
import com.xyzlf.custom.keyboardlib.KeyboardDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final KeyboardDialog dialog = new KeyboardDialog(MainActivity.this);
                dialog.setKeyboardLintener(new IKeyboardListener() {
                    @Override
                    public void onPasswordChange(String pwd) {
//                        Toast.makeText(MainActivity.this, "change:" + pwd, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPasswordComplete(String pwd) {
                        Toast.makeText(MainActivity.this, "complete:" + pwd, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                    @Override
                    public void onForgetPwd() {
                        Toast.makeText(MainActivity.this, "forget password", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
            }
        });
    }
}
