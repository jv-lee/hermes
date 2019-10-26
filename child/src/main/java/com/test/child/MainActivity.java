package com.test.child;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lee.hermes.ProcessManager;
import com.test.hermes.process.IUserManager;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //其他进程app 注意事项
        // 1. Person数据类包地址和主app保持一致
        // 2. connect连接地址必须是主app包名
        ProcessManager.getInstance().connect(this,"com.test.hermes");

        findViewById(R.id.btn_getPerson).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IUserManager userManager = ProcessManager.getInstance().getInstance(IUserManager.class);
                Toast.makeText(MainActivity.this, "----->"+userManager.getPerson(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
