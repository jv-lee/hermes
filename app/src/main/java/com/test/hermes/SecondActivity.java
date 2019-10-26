package com.test.hermes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.lee.hermes.ProcessManager;
import com.test.hermes.process.IUserManager;
import com.test.hermes.process.Person;


/**
 * @author jv.lee
 */
public class SecondActivity extends AppCompatActivity {
    IUserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        ProcessManager.getInstance().connect(this);

        findViewById(R.id.btn_getUserManager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userManager = ProcessManager.getInstance().getInstance(IUserManager.class);
            }
        });

        findViewById(R.id.btn_clickGetUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SecondActivity.this, "-------->" + userManager.getPerson(), Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_setPerson).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userManager.setPerson(new Person("我是子进程数据","88888888"));
            }
        });

    }


}
