package com.renewal_studio.pechengator.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.renewal_studio.pechengator.R;
import com.renewal_studio.pechengator.contract.StartContract;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class StartFragment extends Fragment implements StartContract.View {

    public StartFragment() {}

    @OnClick(R.id.btn_start)
    public void startMain(View view) {
        startActivity(new Intent(getContext(), MainActivity.class));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_start, container, false);
        ButterKnife.bind(this, root);
        return root;
    }
}
