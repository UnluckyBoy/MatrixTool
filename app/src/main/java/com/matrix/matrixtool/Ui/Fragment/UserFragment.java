package com.matrix.matrixtool.Ui.Fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.matrix.matrixtool.R;

public class UserFragment extends Fragment {
    private Intent intent_UserFragment;
    private View view;

    public static UserFragment newInstance(String param1) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString("args", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**launcher必须在onCreate初始化并处理结果**/
        //initLauncher();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        if (view != null) {
//            ViewGroup parent = (ViewGroup) view.getParent();
//            if (parent != null) {
//                parent.removeView(view);
//            }
//        } else {
//            view = inflater.inflate(R.layout.user_fragment, container, false);
//            Bundle bundle = getArguments();
//            agrs1 = bundle.getString("agrs1");
//        }

        intent_UserFragment=getActivity().getIntent();
        view = inflater.inflate(R.layout.fragment_user, container, false);
        Bundle bundle = getArguments();

        InitData(view);
        return view;
    }

    private void InitData(View view) {

    }
}
