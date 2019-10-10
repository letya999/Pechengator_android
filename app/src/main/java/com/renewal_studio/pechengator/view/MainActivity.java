package com.renewal_studio.pechengator.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.renewal_studio.pechengator.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
/*    @BindView(R.id.nav_host_fragment) - пример использования butterknife
    Fragment fragment;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*        ButterKnife.bind(this);*/
    }
}
