package com.matrix.matrixtool.UiTools;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.matrix.matrixtool.R;

/**
 * @ClassName MatrixDialog
 * @Author Create By Administrator
 * @Date 2023/4/9 0009 22:08
 */
public class MatrixDialog extends Dialog {
    private static Context mContext;
    /** 对话框的宽度 */
    private int dialogWidth;
    /** 是否设置对话框的最后一行为两个Item（如：确定和取消） */
    private boolean isTwoItemsOnLastLine = false;
    /** 是否为倒数第二个Item设置了点击监听事件（只在最后一行显示为两个Item时可用） */
    private boolean isSetOnClickListener2SecondLastItem = false;
    /** 是否为最后一个Item设置了点击监听事件 */
    private boolean isSetOnClickListener2LastItem = false;
    /** 存放所有Item中的文本信息，文本顺序为从上至下，从左至右 */
    private ArrayList<TextView> mList = new ArrayList<TextView>();
    /** 存放所有的分割线，分割线顺序为从上至下，从左至右 */
    private ArrayList<TextView> mDividerList = new ArrayList<TextView>();

    /**
     * 自定义对话框的构造方法，将根据names中存放的文本从上至下依次创建Item，每个Item中的文本信息为对应names索引位置的值
     *
     * @param context
     * @param names
     *      --每条展示的文本信息组成的数组
     */
    public MatrixDialog(Context context, String[] names) {
        this(context, names, false);
    }

    /**
     * 自定义对话框的构造方法，将根据names中存放的文本从上至下依次创建Item，每个Item中的文本信息为对应names索引位置的值
     *
     * @param context
     * @param names
     * --每条展示的文本信息组成的数组
     * @param isLastLine2Items
     * --为true时,最后一行将展示为两个Item横向并列的视图(如:一个"确定"和一个"取消"),同时按钮的顺序与names中的按钮文本顺序相反
     * --为false时，每行展示一个Item，顺序为从上至下
     */
    public MatrixDialog(Context context, String[] names, boolean isLastLine2Items) {
        super(context, R.style.alertdialog_theme);
        this.isTwoItemsOnLastLine = isLastLine2Items;
        if (null == context || null == names || names.length < 1) {
            return;
        }
        mContext = context;
        Window w = getWindow();
        WindowManager wmManager = w.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        wmManager.getDefaultDisplay().getMetrics(outMetrics);
        dialogWidth = outMetrics.widthPixels * 3 / 4;
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, outMetrics);
        int height1dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, outMetrics);
        outMetrics = null;
        TextView contentView, dividerView;
        // 定义根部局控件
        LinearLayout mView = new LinearLayout(context);
        mView.setBackgroundResource(R.drawable.rectangle_teal_a100);
        mView.setOrientation(LinearLayout.VERTICAL);
        // 向根部局文件中添加子控件
        for (int i = 0; i < names.length; i++) {
            contentView = new TextView(mContext);
            dividerView = new TextView(mContext);

            contentView.setPadding(0, padding, 0, padding);
            contentView.setGravity(Gravity.CENTER);
            contentView.setText(names[i]);
            dividerView.setHeight(height1dp);
            dividerView.setWidth(dialogWidth);
            if (names.length == 1) {// 只包含1个Item时
                contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
                contentView.setTextColor(Color.rgb(52, 158, 57));
                contentView.setOnClickListener(new defaultOnClickListener());
            } else if (names.length == 2) {// 包含两个Item时
                if (!isLastLine2Items) {// 如果不是显示到同一行中
                    if (i == 0) {
                        contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
                        contentView.setTextColor(Color.rgb(102, 102, 102));
                        dividerView.setBackgroundColor(Color.rgb(198, 198, 196));
                    } else {
                        contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
                        contentView.setTextColor(Color.rgb(43, 142, 240));
                        contentView.setOnClickListener(new defaultOnClickListener());
                    }
                } else {
                    makeTextButton(context, padding, height1dp, mView, names[names.length - 2], names[names.length - 1]);
                    break;
                }
            } else {
                if (i == 0) {
                    contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
                    contentView.setTextColor(Color.rgb(102, 102, 102));
                    dividerView.setBackgroundColor(Color.rgb(52, 158, 57));
                } else if (i >= names.length - 2) {
                    if (!isLastLine2Items) {
                        if (i == names.length - 1) {
                            contentView.setTextColor(Color.rgb(52, 158, 57));
                            contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
                            contentView.setOnClickListener(new defaultOnClickListener());
                        } else {
                            contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
                            contentView.setTextColor(Color.rgb(43, 142, 240));
                            dividerView.setBackgroundColor(Color.rgb(198, 198, 196));
                        }
                    } else {
                        makeTextButton(context, padding, height1dp, mView, names[names.length - 2], names[names.length - 1]);
                        break;
                    }
                } else {
                    contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
                    contentView.setTextColor(Color.rgb(43, 142, 240));
                    dividerView.setBackgroundColor(Color.rgb(198, 198, 196));
                }
            }

            mView.addView(contentView);
            mList.add(contentView);
            if (i != names.length - 1) {
                mView.addView(dividerView);
                mDividerList.add(dividerView);
            }
        }

        setContentView(mView);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.width = dialogWidth;
    }

    /** 设置最后一行为两个Item横向排列的布局视图 */
    private void makeTextButton(Context context, int padding, int height1dp, LinearLayout mView, String btn1Text, String btn2Text) {
        TextView btn_1, btn_spline, btn_2;
        LinearLayout btnContainer = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        mView.addView(btnContainer, params);
        btn_1 = new TextView(mContext);
        btn_1.setPadding(0, padding, 0, padding);
        btn_1.setText(btn1Text);
        btn_1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
        btn_1.setTextColor(Color.rgb(52, 158, 57));
        btn_1.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
        params2.weight = 1;
        btnContainer.addView(btn_1, params2);
        btn_spline = new TextView(mContext);
        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(height1dp, LayoutParams.MATCH_PARENT);
        btn_spline.setBackgroundColor(Color.rgb(198, 198, 196));
        btnContainer.addView(btn_spline, params3);
        btn_2 = new TextView(mContext);
        btn_2.setPadding(0, padding, 0, padding);
        btn_2.setText(btn2Text);
        btn_2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
        btn_2.setTextColor(Color.rgb(52, 158, 57));
        btn_2.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
        params4.weight = 1;
        params4.gravity = Gravity.CENTER;
        btnContainer.addView(btn_2, params4);
        mList.add(btn_1);
        mList.add(btn_2);
        mDividerList.add(btn_spline);

        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSetOnClickListener2SecondLastItem && isShowing()) {
                    dismiss();
                }
            }
        });
        btn_2.setOnClickListener(new defaultOnClickListener());

    }

    /**
     * 为每个Item中的文字设置填充,单位默认为dp
     *
     * @param left
     *      --左填充
     * @param top
     *      --上填充
     * @param right
     *      --右填充
     * @param bottom
     *      --下填充
     */
    public void setPadding2Items(int left, int top, int right, int bottom) {
        setPadding2Items(left, top, right, bottom, -1);
    }

    /**
     * 为每个Item中的文字设置填充
     *
     * @param left
     *      --左填充,单位为
     * @param top
     *      --上填充
     * @param right
     *      --右填充
     * @param bottom
     *      --下填充
     * @param flagUnit
     *      --单位：TypedValue.COMPLEX_UNIT_DIP（1）、TypedValue.COMPLEX_UNIT_SP（2）、TypedValue.COMPLEX_UNIT_PX（0）
     */
    public void setPadding2Items(int left, int top, int right, int bottom, int flagUnit) {
        int tmpUnit = TypedValue.COMPLEX_UNIT_DIP;// 单位默认是dp
        switch (flagUnit) {
            case TypedValue.COMPLEX_UNIT_SP:
                tmpUnit = TypedValue.COMPLEX_UNIT_SP;
                break;
            case TypedValue.COMPLEX_UNIT_PX:
                tmpUnit = TypedValue.COMPLEX_UNIT_PX;
                break;
        }
        int tmpLeft = (int) TypedValue.applyDimension(tmpUnit, left, mContext.getResources().getDisplayMetrics());
        int tmpTop = (int) TypedValue.applyDimension(tmpUnit, top, mContext.getResources().getDisplayMetrics());
        int tmpRight = (int) TypedValue.applyDimension(tmpUnit, right, mContext.getResources().getDisplayMetrics());
        int tmpBottom = (int) TypedValue.applyDimension(tmpUnit, bottom, mContext.getResources().getDisplayMetrics());
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).setPadding(tmpLeft, tmpTop, tmpRight, tmpBottom);
        }
    }

    /**
     * 设置对话框的宽度,单位默认为dp
     *
     * @param width
     *      --对话框的宽度
     */
    public void setDialogWidth(int width) {
        setDialogWidth(width, -1);
    }

    /**
     * 设置对话框的宽度，当宽度值为屏幕宽度的1/4到屏幕的宽度之间的值时有效
     *
     * 注意：对话框的宽度变化时，会相应调整Item中字体的大小为适中，所以要想设置自己想要的字体大小，必须在调用此方法后再次调用设置字体大小的方法
     *
     * @param width
     *      --对话框的宽度
     * @param unit
     *      ---宽度的单位：TypedValue.COMPLEX_UNIT_DIP（1）、TypedValue.COMPLEX_UNIT_SP（2）、TypedValue.COMPLEX_UNIT_PX（0）
     */
    public void setDialogWidth(int width, int unit) {
        int tmpUnit = TypedValue.COMPLEX_UNIT_DIP;// 单位默认是dp
        switch (unit) {
            case TypedValue.COMPLEX_UNIT_SP:
                tmpUnit = TypedValue.COMPLEX_UNIT_SP;
                break;
            case TypedValue.COMPLEX_UNIT_PX:
                tmpUnit = TypedValue.COMPLEX_UNIT_PX;
                break;
        }
        width = (int) TypedValue.applyDimension(tmpUnit, width, mContext.getResources().getDisplayMetrics());
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        if (width >= dialogWidth / 3 && width <= dialogWidth * 4 / 3) {// 限制对话框的宽度为屏幕宽度的1/4到屏幕宽度
            lp.width = width;
            if (width >= dialogWidth / 3 && width < dialogWidth / 2) {
                setTextSize(12);
            } else if (width >= dialogWidth / 2 && width < dialogWidth * 2 / 3) {
                setTextSize(14);
            } else if (width >= dialogWidth * 2 / 3 && width < dialogWidth * 5 / 6) {
                setTextSize(16);
            } else if (width >= dialogWidth * 7 / 6 && width <= dialogWidth * 4 / 3) {
                setTextSize(20);
            }
        }
    }

    private void setTextSize(int size) {
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        }
        if (mList.size() > 2) {
            mList.get(0).setTextSize(TypedValue.COMPLEX_UNIT_SP, size - 2);
        }
    }

    /**
     * 设置对话框中字体颜色，所有item中的字体颜色均设置为color所表示的颜色
     *
     * @param color
     */
    public void setTextColor2AllItems(int color) {
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).setTextColor(color);
        }
    }

    /**
     * 为指定Item中的文字设置颜色
     *
     * @param color
     * @param itemIndex
     *      --指定Item的索引，从1开始，即第一个Item的itemIndex=1
     */
    public void setTextColor2Item(int color, int itemIndex) {
        if (color < 0 || itemIndex < 1 || itemIndex > mList.size()) {
            return;
        }
        mList.get(itemIndex - 1).setTextColor(color);
    }

    /**
     * 设置对话框中字体颜色,color中的颜色顺序和item的顺序一致，
     *
     * 当给定数组color长度小于item数的时候，只设置前color.length个item的字体颜色；
     *
     * 当color长度大于item数的时候，只将color的前item数个颜色值依次设置给item的字体；
     *
     * 如果想跳过中间的某个Item不为其设置颜色的话，可将color中该Item对应的值设置为-1
     *
     * @param color
     */
    public void setTextColor2Items(int[] color) {
        if (null == color || color.length == 0) {
            return;
        }
        for (int i = 0; i < color.length && i < mList.size(); i++) {
            if (color[i] != -1) {
                mList.get(i).setTextColor(color[i]);
            }
        }
    }

    /**
     * 设置对话框中分割线的颜色，所有分割线的颜色均设置为color所表示的颜色
     *
     * @param color
     */
    public void setColor2DividerLine(int color) {
        for (int i = 0; i < mDividerList.size(); i++) {
            mDividerList.get(i).setBackgroundColor(color);
        }
    }

    /**
     * 为指定分割线设置颜色，
     *
     * @param color
     * @param dividerLineIndex
     *      --指定分割线的索引，从1开始，即第一个分割线的itemIndex=1，分割线的索引顺序为从上至下，从左至右；每个item下都有一个分割线（最后一个除外），如果最后一行设置为两个Item，则两个Item中间的分割线也算一个）
     */
    public void setColor2DividerLine(int color, int dividerLineIndex) {
        if (color < 0 || dividerLineIndex < 1 || dividerLineIndex > mDividerList.size()) {
            return;
        }
        mDividerList.get(dividerLineIndex - 1).setBackgroundColor(color);
    }

    /**
     * 设置对话框中分割线的颜色,color中的颜色顺序和分割线的顺序（从上至下，从左至右）一致（每个item下都有一个分割线，最后一个除外；如果最后一行设置为两个Item，则两个Item中间的分割线也算一个），
     *
     * 当给定数组color长度小于分割线数的时候，只设置前color.length个分割线的颜色；
     *
     * 当color长度大于分割线数的时候，只将color的前分割线数个颜色值依次设置给分割线；
     *
     * 如果想跳过中间的某个分割线不为其设置颜色的话，可将color中该分割线对应的值设置为-1
     *
     * @param color
     */
    public void setColor2DividerLine(int[] color) {
        if (null == color || color.length == 0) {
            return;
        }
        for (int i = 0; i < color.length && i < mDividerList.size(); i++) {
            if (color[i] != -1) {
                mDividerList.get(i).setBackgroundColor(color[i]);
            }
        }
    }

    /**
     * 为对话框中的Item设置点击的监听事件,listeners的顺序与item的顺序一致，
     *
     * 当给定数组listeners长度小于item数的时候，只设置前listeners.length个item的点击监听事件；
     *
     * 当listeners长度大于item数的时候，只将listeners的前item数个点击监听事件依次设置给item的点击监听；
     *
     * 如果想跳过中间的某个Item不为其设置点击监听事件的话，可将listeners中该Item对应的值设置为null;
     *
     * 另，如果不为最后一个Item设置点击监听的话，其会有一个默认的点击事件，该点击事件执行隐藏当前对话框的操作， 如果将最后一行设置成了两个Item，则如果没有为其设置点击监听的话，其默认也执行隐藏当前对话框的操作
     *
     *
     * @param listeners
     */
    public void setOnClickListener2Items(View.OnClickListener[] listeners) {
        if (listeners == null || listeners.length == 0) {
            return;
        }
        for (int i = 0; i < listeners.length && i < mList.size(); i++) {
            if (listeners[i] != null) {
                mList.get(i).setOnClickListener(listeners[i]);
            }
        }

        if (listeners.length < mList.size() || listeners[listeners.length - 1] == null) {
            isSetOnClickListener2LastItem = false;
            if (isTwoItemsOnLastLine && (listeners.length < mList.size() - 1 || (listeners.length > 1 && listeners[listeners.length - 2] == null))) {
                isSetOnClickListener2SecondLastItem = true;
            }
        } else {
            isSetOnClickListener2LastItem = true;
        }

    }

    /**
     * 为最后一个Item设置点击监听
     *
     * @param listener
     */
    public void setOnClickListener2LastItem(View.OnClickListener listener) {
        if (listener == null) {
            return;
        }
        if (null != mList && mList.size() > 0) {
            mList.get(mList.size() - 1).setOnClickListener(listener);
            isSetOnClickListener2LastItem = true;
        }
    }

    /**
     * 为倒数第二个Item设置点击监听
     *
     * @param listener
     */
    public void setOnClickListener2SecondLastItem(View.OnClickListener listener) {
        if (listener == null) {
            return;
        }
        if (null != mList && mList.size() > 1) {
            mList.get(mList.size() - 2).setOnClickListener(listener);
            isSetOnClickListener2SecondLastItem = true;
        }
    }

    /**
     * 为指定Item设置点击监听
     *
     * @param listener
     * @param itemIndex
     *      --指定Item的索引，从1开始，即第一个Item的itemIndex=1
     */
    public void setOnClickListener2Item(View.OnClickListener listener, int itemIndex) {
        if (listener == null || itemIndex < 1 || itemIndex > mList.size()) {
            return;
        }
        if (null != mList) {
            mList.get(itemIndex - 1).setOnClickListener(listener);
            if (itemIndex == mList.size()) {
                isSetOnClickListener2LastItem = true;
            } else if (itemIndex == mList.size() - 1) {
                isSetOnClickListener2SecondLastItem = true;
            }
        }
    }

    /**
     * 为最后两个Item设置点击监听
     *
     * @param listener
     * --指定Item的索引，从1开始，即第一个Item的itemIndex=1
     */
    public void setOnClickListener2LastTwoItems(final OnClickListener2LastTwoItem listener) {
        if (listener == null) {
            return;
        }
        if (null != mList && mList.size() >= 2) {
            isSetOnClickListener2LastItem = true;
            isSetOnClickListener2SecondLastItem = true;
            mList.get(mList.size() - 1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickListener2LastItem();
                }
            });
            mList.get(mList.size() - 2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickListener2SecondLastItem();
                }
            });
        }
    }

    /**
     * 最后两个Item的点击回调接口
     */
    public interface OnClickListener2LastTwoItem {
        /** 最后一个Item的点击监听回调方法 */
        void onClickListener2LastItem();

        /** 倒数第二个Item的点击监听回调方法 */
        void onClickListener2SecondLastItem();
    }

    /**
     * 默认的点击事件
     */
    private class defaultOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (!isSetOnClickListener2LastItem) {
                if (isShowing()) {
                    dismiss();
                }
            }
        }
    }
}
