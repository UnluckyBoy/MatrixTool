package com.matrix.matrixtool.Ui.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.matrix.matrix_chat.UI.Activity.Tools.ActivityUtil;
import com.matrix.matrixtool.R;

/**
 * @ClassName BaseActivity
 * @Author Create By Administrator
 * @Date 2023/4/18 0018 13:23
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{
    //获取TAG的activity名称
    protected final String TAG = this.getClass().getSimpleName();
    protected static Intent TAG_Intent;
    //设置子activity布局
    FrameLayout frameLayout;
    //title 如果 return null 则隐藏
    RelativeLayout rlTitle;
    //返回按钮
    LinearLayout llBack;
    //title文字
    TextView tvTitleBase;
    //右侧按钮
    public Button btnRightBase;
    LinearLayout llRightBase;
    //获取上下文
    public Context mContext;
    //progressDialog 可以用自己自定义的Loading
    private ProgressDialog progressdialog;
    //是否有返回键
    public boolean isHaveBack = true;

    protected BaseActivity() {
    }

    /**
     * 设置布局
     * return 布局
     */
    @LayoutRes
    protected abstract int setLayoutResourceID();
    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 设置title
     * @return title
     */
    protected abstract String initTitle();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        TAG_Intent=getIntent();

        //Activity管理
        ActivityUtil.getInstance().addActivity(this);
        //设置布局前处理一些逻辑
        init(savedInstanceState);
        //设置布局
        if (setLayoutResourceID() != 0) {
            setContentView(R.layout.activity_base);
            //ButterKnife.bind(this); 如果有需要可以在这里设置
            initBaseView();
        }
        //初始化控件 设置了ButterKnife 初始化控件就不需要了
        initView();
        //设置数据
        initData();
        //设置监听
        initListener();
    }

    /**
     * 设置布局前处理一些逻辑和设置
     * @param savedInstanceState bundle
     */
    protected void init(Bundle savedInstanceState) {

    }

    /**
     * 基础View设置
     */
    private void initBaseView() {
        llBack = findViewById(R.id.ll_back_base);
        tvTitleBase = findViewById(R.id.tv_title_base);
        frameLayout = findViewById(R.id.fl_base_content);
        rlTitle = findViewById(R.id.rl_title_base);
        //rlTitle.setVisibility(View.GONE);

        btnRightBase = findViewById(R.id.btn_right_base);
        llRightBase = findViewById(R.id.ll_right_base);
        //if(StringUtil.isNotEmpty(initTitle())){
        if(initTitle()!=null){
            tvTitleBase.setText(initTitle());
        }else{
            rlTitle.setVisibility(View.GONE);
        }
        if (isHaveBack) {
            llBack.setVisibility(View.VISIBLE);
            llBack.setOnClickListener(view -> finish());
        } else {
            llBack.setVisibility(View.GONE);
        }
        btnRightBase.setOnClickListener(view -> rightBtnClickListener());

        View baseView = LayoutInflater.from(mContext).inflate(setLayoutResourceID(), frameLayout, false);
        frameLayout.addView(baseView);
    }

    /**
     * 设置数据
     */
    protected void initData() {
    }
    /**
     * 设置监听
     */
    protected void initListener() {
    }

    @Override
    public void onClick(View view) {

    }

    /**
     * 设置右边Button样式
     * @param resource
     */
    public void setRightBtnResource(int resource) {
        if (resource != 0) {
            llRightBase.setVisibility(View.VISIBLE);
            btnRightBase.setBackground(ContextCompat.getDrawable(mContext, resource));
        }
    }

    /**
     * 右边Button的点击事件
     */
    protected void rightBtnClickListener() {
    }

    /**
     * show shotToast
     * @param msg 提示内容
     */
    public void showToast(String msg) {
        if (msg==null||msg=="") {
            return;
        }
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * showProgress
     * @param msg 提示内容
     */
    public void showProgress(String msg) {
        if (progressdialog == null) {
            progressdialog = new ProgressDialog(mContext);
        }
        progressdialog.setTitle("请等待...");
        progressdialog.setMessage(msg);
        progressdialog.show();
    }

    /**
     * dismissProgress
     */
    protected void dismissProgress() {
        if (progressdialog != null) {
            progressdialog.dismiss();
        }
    }

    /**
     * 普通跳转
     * @param activity activity
     */
    protected void jumpToActivity(Class<?> activity) {
        startActivity(new Intent(mContext, activity));
    }

    /**获取组件**/
    protected <T extends View> T findComponent(int resId){
        return (T) findViewById(resId);
    }

    /**保证同一按钮在1秒内只会响应一次点击事件**/
    public abstract class OnSingleClickListener implements View.OnClickListener {
        //两次点击按钮之间的间隔，目前为1000ms
        private static final int MIN_CLICK_DELAY_TIME = 1000;
        private long lastClickTime;
        public abstract void onSingleClick(View view);

        @Override
        public void onClick(View view) {
            long curClickTime = System.currentTimeMillis();
            if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
                lastClickTime = curClickTime;
                onSingleClick(view);
            }
        }
    }
    /**同一按钮在短时间内可重复响应点击事件**/
    public abstract class OnMultiClickListener implements View.OnClickListener {
        public abstract void onMultiClick(View view);

        @Override
        public void onClick(View v) {
            onMultiClick(v);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.getInstance().removeActivity(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
