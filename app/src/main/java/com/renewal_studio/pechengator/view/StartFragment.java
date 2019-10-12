package com.renewal_studio.pechengator.view;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.AppLaunchChecker;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.renewal_studio.pechengator.R;

public class StartFragment extends Fragment {

    private View root;

    public StartFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(!checkFirstStart(getActivity())) {
            NavController navController = Navigation.
                    findNavController(getActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.languageChoiseFragment);
        }
        root = inflater.inflate(R.layout.fragment_start, container, false);
        root.findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.
                        findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.listLocationFragment);
            }
        });
        return root;
    }

    public static boolean checkFirstStart(Activity activity) {
        if(AppLaunchChecker.hasStartedFromLauncher(activity))
            return  true;
        AppLaunchChecker.onActivityCreate(activity);
        return  false;
    }

}
