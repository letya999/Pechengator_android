package com.renewal_studio.pechengator.view;

import android.Manifest;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.renewal_studio.pechengator.R;
import com.renewal_studio.pechengator.contract.LanguageChoiseContract;

import java.util.List;

public class LanguageChoiseFragment extends Fragment implements LanguageChoiseContract.View  {

    private static final String TAG = "LanguageChoiseFragment";

    private View root;

    public void LanguageChoiseFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {}
        }).check();

       root = inflater.inflate(R.layout.fragment_language_choise, container, false);
       root.findViewById(R.id.lang_rus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.
                        findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.startFragment);
            }
        });
        return root;
    }

}
