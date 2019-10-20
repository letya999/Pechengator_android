package com.renewal_studio.pechengator.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.renewal_studio.pechengator.R;
import com.renewal_studio.pechengator.contract.ListLocationContract;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListLocationFragment extends Fragment implements ListLocationContract.View {

    public void ListLocationFragment() {}


    @OnClick(R.id.searchTextView)
    public void click(View view) {
        NavController navController = Navigation.
                findNavController(getActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.locationFragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_location, container, false);
        ButterKnife.bind(this, root);
        ((MainActivity)getActivity()).setVisibilityBack(View.INVISIBLE);
        ((MainActivity)getActivity()).setName(getString(R.string.list_locations));
        return root;
    }
}
