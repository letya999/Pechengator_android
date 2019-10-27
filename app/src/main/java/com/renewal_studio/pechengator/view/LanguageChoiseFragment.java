package com.renewal_studio.pechengator.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.renewal_studio.pechengator.R;
import com.renewal_studio.pechengator.common.LoadService;
import com.renewal_studio.pechengator.contract.LanguageChoiseContract;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.renewal_studio.pechengator.utils.Constant.BROADCAST_ACTION;
import static com.renewal_studio.pechengator.utils.Constant.CHI;
import static com.renewal_studio.pechengator.utils.Constant.DEBUG_TAG;
import static com.renewal_studio.pechengator.utils.Constant.ENG;
import static com.renewal_studio.pechengator.utils.Constant.NOR;
import static com.renewal_studio.pechengator.utils.Constant.RU;
import static com.renewal_studio.pechengator.utils.Constant.SAVE_LANG;

public class LanguageChoiseFragment extends Fragment implements LanguageChoiseContract.View  {

    public void LanguageChoiseFragment() {}

    @BindView(R.id.progress)
    FrameLayout progress;
    @BindView(R.id.lang_rus)
    TextView lang_rus;
    @BindView(R.id.lang_eng)
    TextView lang_eng;
    @BindView(R.id.lang_nor)
    TextView lang_nor;
    @BindView(R.id.lang_chi)
    TextView lang_chi;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    SharedPreferences pref;
    String lang = "";
    BroadcastReceiver br;


    @OnClick({R.id.lang_rus, R.id.lang_nor, R.id.lang_chi, R.id.lang_eng})
    public void onClick(View view) {
        pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = pref.edit();
        switch (view.getId()) {
            case R.id.lang_rus: {
                lang = RU;
                break;
            }
            case R.id.lang_eng: {
                lang = ENG;
                break;
            }
            case R.id.lang_nor: {
                lang = NOR;
                break;
            }
            case R.id.lang_chi: {
                lang = CHI;
                break;
            }
        }
        ed.putString(SAVE_LANG, lang);
        ed.commit();
        progress.setVisibility(View.VISIBLE);
        Log.d(DEBUG_TAG, "onClick");
        Intent msgIntent = new Intent(getContext(), LoadService.class);
        msgIntent.putExtra(LoadService.PARAM_IN_MSG, lang);
        getActivity().startService(msgIntent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        br = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                Log.d(DEBUG_TAG, "onReceive");
                int val = intent.getIntExtra(LoadService.PARAM_OUT_MSG, 0);
                if (val == 386) {
                    progress.setVisibility(View.INVISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            NavController navController = Navigation.
                                    findNavController(getActivity(), R.id.splash_nav_host_fragment);
                            navController.navigate(R.id.startFragment);
                        }
                    }, 1000);
                }
            }
        };

        IntentFilter filter = new IntentFilter(BROADCAST_ACTION);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        getContext().registerReceiver(br, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().unregisterReceiver(br);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_language_choise, container, false);
        ButterKnife.bind(this, root);
        pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        lang = pref.getString(SAVE_LANG, "");
        switch (lang) {
            case RU: {
                lang_rus.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                break;
            }
            case ENG: {
                lang_eng.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                break;
            }
            case NOR: {
                lang_nor.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                break;
            }
            case CHI: {
                lang_chi.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                break;
            }
        }
        return root;
    }
}
