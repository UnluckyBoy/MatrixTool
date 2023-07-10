package com.matrix.matrixtool.Ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.matrix.matrixtool.R;
import com.matrix.matrixtool.Ui.Fragment.MainFragment;
import com.matrix.matrixtool.Ui.Fragment.MoreFragment;
import com.matrix.matrixtool.Ui.Fragment.UserFragment;
import com.matrix.matrixtool.UiTools.MatrixToast;
import com.matrix.matrixtool.databinding.ActivityMainBinding;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;
    private FrameLayout mainFrame;
    // 持有对应Fragment的对象
    private MainFragment mMainFragment;
    private MoreFragment mMoreFragment;
    private UserFragment mUserFragment;
    // 用于存放fragment的数组
    private Fragment[] mFragmentContainer;
    public int mLastFragmentTag;//用于标记最后一个fragment
    private static boolean mBackKeyPressed = false;//记录是否有首次按键

    //private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConfirmFirstStart();
        initView();
        setActionBar();
    }

    /**设置状态栏**/
    private void setActionBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.WHITE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private void initView(){
        mMainFragment = MainFragment.newInstance("");
        mMoreFragment= MoreFragment.newInstance("");
        mUserFragment=UserFragment.newInstance("");

        mFragmentContainer = new Fragment[]{mMainFragment, mMoreFragment, mUserFragment};
        mainFrame = (FrameLayout) findViewById(R.id.fragment_container);
        //设置fragment到布局
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mMainFragment)
                .show(mMainFragment)
                .commit();

        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        //bottomNavigation的点击事件
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_mainIcon:
                    /**
                     * 这里因为需要对3个fragment进行切换
                     * 如果只是想测试按钮点击，不管fragment的切换，可以把start到end里面的内容去掉
                     */
                    //start
                    if (mLastFragmentTag != 0) {
                        switchFragment(mLastFragmentTag, 0);
                        mLastFragmentTag = 0;
                    }
                    //end
                    return true;
                case R.id.action_moreIcon:
                    if (mLastFragmentTag != 1) {
                        switchFragment(mLastFragmentTag, 1);
                        mLastFragmentTag = 1;
                    }
                    return true;
                case R.id.action_userIcon:
                    if (mLastFragmentTag != 2) {
                        switchFragment(mLastFragmentTag, 2);
                        mLastFragmentTag = 2;
                    }
                    return true;
                default:
                    break;
            }
            return false;
        }
    };

    /**
     *切换fragment
     */
    private void switchFragment(int lastfragment, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //隐藏上个Fragment
        transaction.hide(mFragmentContainer[lastfragment]);
        //transaction.replace();
        if (mFragmentContainer[index].isAdded() == false) {
            transaction.add(R.id.fragment_container, mFragmentContainer[index]);
        }
        transaction.show(mFragmentContainer[index]).commitAllowingStateLoss();
    }

    private void ConfirmFirstStart(){
        SharedPreferences sharedPreferences = getSharedPreferences("share", MODE_PRIVATE);
        //默认false
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", false);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //如果是true就表示已经不是第一次运行app，跳转相应界面就可以

        if (isFirstRun) {
            //不是第一次运行

        } else {
            //第一次运行
            getPermission();

            editor.putBoolean("isFirstRun", true);
            editor.commit();
        }
    }

    /***
     * 动态获取申请读写权限
     */
    private void getPermission(){
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        //申请写权限
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        //申请创建文件
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 1);
        }

        //网络权限
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
        }
    }


    /**
     * 退出检测
     */
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (!mBackKeyPressed) {
            //Toast.makeText(this, "再滑一次退出", Toast.LENGTH_SHORT).show();
            MatrixToast.showToast(this,"再滑一次退出",0);
            mBackKeyPressed = true;
            new Timer().schedule(new TimerTask() {//延时两秒，如果超出则擦错第一次按键记录
                @Override
                public void run() {
                    mBackKeyPressed = false;
                }
            }, 2000);
        } else {
            this.finish();
            System.exit(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}