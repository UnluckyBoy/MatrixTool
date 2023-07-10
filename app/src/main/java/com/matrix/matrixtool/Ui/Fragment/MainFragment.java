package com.matrix.matrixtool.Ui.Fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.matrix.matrixtool.R;


@SuppressWarnings("EmptyMethod")
public class MainFragment extends Fragment {
    private final String mImagePath="/sdcard/Download";

    private Intent intent_MainFragment;
    private View view;
    private ActivityResultLauncher<Intent> mLauncher;//启动activity对象,必须要在onCreate中初始化

    public static MainFragment newInstance(String param1) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString("args_account",param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**ActivityResultLauncher<Intent>必须要在onCreate中启动**/
        //setLauncher();
    }

    /**使用registerForActivityResult打开相册,已使用新组件PictureSelector替换**/
//    private void setLauncher(){
//        mLauncher = registerForActivityResult(
//                new StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult result) {
//
//                    }
//                });
//    }

    @SuppressWarnings("RedundantCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //mContext = this.getContext();
        if(view!=null){
            ViewGroup parent=(ViewGroup) view.getParent();
            if(parent!=null){
                parent.removeView(view);
            }
        }else{
            view = inflater.inflate(R.layout.fragment_main, container, false);
            Bundle bundle = getArguments();
        }

        return view;
    }
}
