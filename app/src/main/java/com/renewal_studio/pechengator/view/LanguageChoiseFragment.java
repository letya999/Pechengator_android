package com.renewal_studio.pechengator.view;

import android.Manifest;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.renewal_studio.pechengator.R;
import com.renewal_studio.pechengator.contract.LanguageChoiseContract;
import java.util.List;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LanguageChoiseFragment extends Fragment implements LanguageChoiseContract.View  {

    public void LanguageChoiseFragment() {}

    @OnClick(R.id.lang_rus)
    public void startMain(View view) {
        NavController navController = Navigation.
                findNavController(getActivity(), R.id.splash_nav_host_fragment);
        navController.navigate(R.id.startFragment);
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
        View root = inflater.inflate(R.layout.fragment_language_choise, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

}
