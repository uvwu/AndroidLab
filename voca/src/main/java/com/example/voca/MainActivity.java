package com.example.voca;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private ActionBarDrawerToggle toggle;//메뉴 화면을 여는 버튼
    private SwitchCompat darkModeSwitch;//다크모드 스위치 버튼
    private SwitchCompat pushAlertSwitch;//푸쉬알림 스위치 버튼
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // to display a drawer icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawerLayout = findViewById(R.id.main_drawer);// drawerLayout
        NavigationView navigationView = findViewById(R.id.main_drawer_view);//navigationView

        /* to tie together the functionality of DrawerLayout and the framework ActionBar
        *
        * */
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) /*{
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
         }*/;
        // Set the drawer toggle as the DrawerListener.
        drawerLayout.addDrawerListener(toggle); // If omitted, the toggle icon is not changed.

        darkModeSwitch=(SwitchCompat) navigationView.getMenu().findItem(R.id.menu_drawer_darkMode).getActionView().findViewById(R.id.switch_dark_mode);
        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked)
                    showToast("unchecked");
                else showToast("checked");
            }
        });
        pushAlertSwitch=(SwitchCompat) navigationView.getMenu().findItem(R.id.menu_drawer_pushAlert).getActionView().findViewById(R.id.switch_push_alert);
        pushAlertSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked)
                    showToast("unchecked");
                else showToast("checked");
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            /*네비게이션 뷰를 클릭 했을 때*/
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id;
                id = item.getItemId();
                switch (id) {
                    case R.id.menu_drawer_editAccountInformation:
                        showToast("1");
                        break;
                    case R.id.menu_drawer_notice:
                        //Intent intent=new Intent(this,noticeActivity.class);
                        //Switch darkModeSwitch=
                        break;
                    case R.id.menu_drawer_goalSetting:
                        showToast("3");
                        break;
                    case R.id.menu_drawer_darkMode:
                        showToast("4");
                        break;
                    case R.id.menu_drawer_pushAlert:
                        showToast("5");
                        break;
                    case R.id.menu_drawer_logout:
                        showToast("6");
                        break;
                }
                return true;
            }
        });
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }
    // Whenever the Up button is clicked, this method is called
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Pass the event to ActionBarDrawerToggle,
// if it returns true, then it has handled the Up button touch event.

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
// Handle other action bar items.
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }
    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}