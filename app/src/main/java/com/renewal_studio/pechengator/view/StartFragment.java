package com.renewal_studio.pechengator.view;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.renewal_studio.pechengator.R;
import com.renewal_studio.pechengator.contract.StartContract;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class StartFragment extends Fragment implements StartContract.View {

    public StartFragment() {}

    @OnClick(R.id.btn_start)
    public void openListLocation(View view) {
        startActivity(new Intent(getContext(), MainActivity.class));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }

                }).check();
        View root = inflater.inflate(R.layout.fragment_start, container, false);
        ButterKnife.bind(this, root);
        return root;
    }
}
