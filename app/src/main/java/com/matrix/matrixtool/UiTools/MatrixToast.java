package com.matrix.matrixtool.UiTools;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.matrix.matrixtool.R;

/**
 * @ClassName MatrixToast
 * @Author Create By matrix
 * @Date 2023/7/10 0010 23:12
 */
public class MatrixToast extends Toast {
    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     * @param context The context to use.  Usually your {@link Application} or {@link Activity} object.
     */
    public MatrixToast(Context context) {
        super(context);
    }
    private static MatrixToast toast;
    private static void cancelToast() {
        //防止异常终止,程序第一次进来时,toast时为空的,第二次及以后,它才不为空,才能执行toast.cancel()
        if (toast != null) {
            toast.cancel();
        }
    }
    public void cancel() {
        super.cancel();
    }
    public void show() {
        super.show();
    }

    public static void showToast(Context context,CharSequence text,int showType) {
        //第二次点击及以后,它会先取消上一次的Toast,然后show本次的Toast
        cancelToast();
        toast = new MatrixToast(context);
        View mView = LayoutInflater.from(context).inflate(R.layout.view_matrix_toast, null);
        TextView mText = mView.findViewById(R.id.toast_text);
        mText.setText(text);
        toast.setView(mView);
        toast.setGravity(Gravity.CENTER, 0, 0);
        switch (showType){
            case 1:
                toast.setDuration(Toast.LENGTH_LONG);
                break;
            case 0:
                toast.setDuration(Toast.LENGTH_SHORT);
                break;
        }
        toast.show();
    }
}
