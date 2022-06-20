package com.cjs.android.testtab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class TabActivity3 extends AppCompatActivity {
    private ServiceHallFragment2 serviceHallFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab3);

        serviceHallFragment = new ServiceHallFragment2();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, serviceHallFragment, "service_hall");
        fragmentTransaction.commitAllowingStateLoss();
    }
}
