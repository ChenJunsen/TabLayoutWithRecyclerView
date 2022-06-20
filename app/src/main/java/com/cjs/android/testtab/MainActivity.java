package com.cjs.android.testtab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_tab1, btn_tab2, btn_from_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        btn_tab1 = findViewById(R.id.btn_tab_1);
        btn_tab2 = findViewById(R.id.btn_tab_2);
        btn_from_fragment = findViewById(R.id.btn_from_fragment);

        btn_tab1.setOnClickListener(this);
        btn_tab2.setOnClickListener(this);
        btn_from_fragment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Class cls = null;
        if (v == btn_tab1) {
            cls = TabActivity1.class;
        } else if (v == btn_tab2) {
            cls = TabActivity2.class;
        } else if (v == btn_from_fragment) {
            cls = TabActivity3.class;
        }
        Intent i = new Intent(this, cls);
        startActivity(i);
    }
}
