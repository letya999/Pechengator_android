package com.renewal_studio.pechengator.view;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.renewal_studio.pechengator.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.side_drawer)
    DrawerLayout side_menu;
    @BindView(R.id.btn_back)
    public ImageButton btn_back;
    @BindView(R.id.name)
    public TextView name;

    @OnClick(R.id.menu_tab)
    public void openSideMenu(View view) {
        side_menu.openDrawer(Gravity.RIGHT);
    }

    @OnClick({R.id.route, R.id.list_loc, R.id.add_loc, R.id.choise_lang, R.id.feedback})
    public void clickMenuItem(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        switch (view.getId()) {
            case R.id.route: {
                NavController navController = Navigation.
                        findNavController(this, R.id.nav_host_fragment);
                navController.navigate(R.id.routeFragment);
                break;
            }
            case R.id.list_loc: {
                NavController navController = Navigation.
                        findNavController(this, R.id.nav_host_fragment);
                navController.navigate(R.id.listLocationFragment);
                break;
            }
            case R.id.choise_lang: {
                startActivity(new Intent(this, SplashActivity.class));
                break;
            }
            case R.id.add_loc: {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:notdevelop@gmail.com"));
                try {
                    startActivity(emailIntent);
                } catch (ActivityNotFoundException e) {
                }
                break;
            }
            case R.id.feedback: {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:notdevelop@gmail.com"));
                try {
                    startActivity(emailIntent);
                } catch (ActivityNotFoundException e) {
                }
                break;
            }
        }
        side_menu.closeDrawers();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_back)
    public void back(View view) {
        setVisibilityBack(View.INVISIBLE);
        onBackPressed();
    }

    public void setVisibilityBack(int visibility) {
        switch (visibility) {
            case View.INVISIBLE: {
                btn_back.setVisibility(View.INVISIBLE);
                break;
            }
            case View.VISIBLE: {
                btn_back.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    public void setName(String text) {
        name.setText(text);
    }
}
