package com.test.hermes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lee.hermes.ProcessManager;
import com.test.hermes.process.Person;
import com.test.hermes.process.UserManager;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProcessManager.getInstance().register(UserManager.class);
        UserManager.getInstance().setPerson(new Person("jv.lee","1234567"));
    }

    public void startSecond(View view) {
        startActivity(new Intent(this,SecondActivity.class));
    }

    public void getPerson(View view) {
        Toast.makeText(this, "----->>>>"+UserManager.getInstance().getPerson(), Toast.LENGTH_SHORT).show();
    }
}
