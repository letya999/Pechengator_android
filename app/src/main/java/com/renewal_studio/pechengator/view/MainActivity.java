package com.renewal_studio.pechengator.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
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
