package com.matrix.matrixtool.Ui.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.matrix.matrixtool.R;
import com.matrix.matrixtool.Ui.Activity.CompressImageActivity;
import com.matrix.matrixtool.UiTools.MatrixToast;
import com.matrix.matrixtool.UtilTools.ImageTool;
import com.matrix.matrixtool.UtilTools.StringUtil;
import com.matrix.matrixtool.UtilTools.TimeTool;

import java.util.ArrayList;

public class MoreFragment extends Fragment {
    private View view;
    private static Intent intent_More;
    private static String file_path="/sdcard/Download";

    private Button edit_video_btn,compress_image_btn;
    private PopupWindow popupWindow;//弹窗
    private RelativeLayout lay_compress_image_root,lay_compress_trans,compress;//压缩根视图,压缩逻辑布局,压缩完成视图
    private LinearLayout lay_compress;//压缩图片结果视图


    public static MoreFragment newInstance(String param1) {
        MoreFragment fragment = new MoreFragment();
        Bundle args = new Bundle();
        args.putString("args", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState) {

        if(view!=null){
            ViewGroup parent=(ViewGroup) view.getParent();
            if(parent!=null){
                parent.removeView(view);
            }
        }else{
            view = inflater.inflate(R.layout.fragment_more, container, false);

            Bundle bundle = getArguments();
        }

        InitView(view);
        return view;
    }

    private void InitView(View view){
        intent_More=getActivity().getIntent();
        edit_video_btn=view.findViewById(R.id.edit_video_btn);
        compress_image_btn=view.findViewById(R.id.compress_image_btn);

        edit_video_btn.setOnClickListener(new MatrixOnClicker());
        compress_image_btn.setOnClickListener(new MatrixOnClicker());
    }

    private class MatrixOnClicker implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.edit_video_btn:
                    //Toast.makeText(view.getContext(),view.getContext().getString(R.string.no_function), Toast.LENGTH_SHORT).show();
                    MatrixToast.showToast(view.getContext(),view.getContext().getString(R.string.no_function),0);
                    break;
                case R.id.compress_image_btn:
                    //Intent intent=new Intent(getActivity(), CompressImageActivity.class);
                    //startActivity(intent);
                    //getActivity().finish();
                    setUpWindow(view,R.layout.view_selectimage_window);
                    break;
            }
        }
    }

    /**弹窗选择相片**/
    public void setUpWindow(View view,int pop_lay_id){
        View popUpView=LayoutInflater.from(view.getContext()).inflate(pop_lay_id, null);//R.layout.view_selectimage_window
        popupWindow=new PopupWindow(view);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(popUpView);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.popup_selector_image_anim_style);//设置动画效果
        //popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(view.getResources().getColor(R.color.transparent,null)));
        /**设置背景为暗**/
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.5f;
        getActivity().getWindow().setAttributes(lp);
        popupWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER,0,0);
        /**点击外部时，关闭遮罩**/
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
        switch (pop_lay_id){
            case R.layout.view_selectimage_window:
                Button btn_photo,btn_camera,btn_cancel;
                btn_photo=(Button)popUpView.findViewById(R.id.btn_photo);
                btn_camera=(Button)popUpView.findViewById(R.id.btn_camera);
                btn_cancel=(Button)popUpView.findViewById(R.id.btn_cancel);
                btn_photo.setOnClickListener(new popClickListener());
                btn_camera.setOnClickListener(new popClickListener());
                btn_cancel.setOnClickListener(new popClickListener());
                break;
        }
    }
    private class popClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_photo:
                    //相册
                    PictureSelector.create(getActivity())
                            .openSystemGallery(SelectMimeType.ofImage())
                            .forSystemResult(new OnResultCallbackListener<LocalMedia>() {
                                @Override
                                public void onResult(ArrayList<LocalMedia> result) {
                                    //Toast.makeText(getContext(), "打开相册"+result.get(0).getRealPath(), Toast.LENGTH_SHORT).show();
                                    final String name=result.get(0).getFileName();
                                    final String localPicturePath = result.get(0).getRealPath();
                                    final long fileSize = result.get(0).getSize();//文件大小

                                    Show_lay_compress_image_root(name,localPicturePath,fileSize);
                                }
                                @Override
                                public void onCancel() {
                                }
                            });
                    closePopupWindow();
                    break;
                case R.id.btn_camera:
                    //拍照
                    PictureSelector.create(getActivity())
                            .openCamera(SelectMimeType.ofImage())
                            .forResultActivity(new OnResultCallbackListener<LocalMedia>() {
                                @Override
                                public void onResult(ArrayList<LocalMedia> result) {
                                    //Toast.makeText(getContext(), "拍照:"+result.get(0).getRealPath(), Toast.LENGTH_SHORT).show();
                                    final String name=result.get(0).getFileName();
                                    final String localPicturePath = result.get(0).getRealPath();
                                    final long fileSize = result.get(0).getSize();//文件大小
                                }
                                @Override
                                public void onCancel() {
                                }
                            });
                    closePopupWindow();
                    break;
                case R.id.btn_cancel:
                    //取消
                    closePopupWindow();
                    break;
            }
        }
    }
    /**关闭弹窗**/
    public void closePopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            lp.alpha = 1f;
            getActivity().getWindow().setAttributes(lp);
        }
    }

    private void Show_lay_compress_image_root(String name,String path,long size){
        lay_compress_image_root=view.findViewById(R.id.lay_compress_image_root);
        lay_compress_trans=view.findViewById(R.id.lay_compress_trans);
        compress=view.findViewById(R.id.compress);
        if(lay_compress_image_root!=null){
            lay_compress_image_root.setVisibility(View.VISIBLE);
            lay_compress_trans.setVisibility(View.VISIBLE);
            ImageView original_image;
            TextView original_image_name,original_image_path,original_image_size;
            EditText edit_size;//压缩值
            Button compress_btn;

            original_image_name=view.findViewById(R.id.original_image_name);
            original_image=view.findViewById(R.id.original_image);
            original_image_path=view.findViewById(R.id.original_image_path);
            original_image_size=view.findViewById(R.id.original_image_size);
            edit_size=view.findViewById(R.id.edit_size);
            compress_btn=view.findViewById(R.id.compress_btn);

            original_image.setImageURI(Uri.parse(path));

            original_image_name.setText(name);
            String targetChar = "0";
            int startIndex = path.indexOf(targetChar);
            int lastIndex = path.lastIndexOf("/");
            original_image_path.setText(path.substring(startIndex+2,lastIndex));
            double d_size =(size/1000d);
            original_image_size.setText(String.format("%.2f",d_size)+"kb");

            compress_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(StringUtil.isEmptyOrBlank(edit_size.getText().toString())){
                        MatrixToast.showToast(view.getContext(),view.getContext().getString(R.string.editNull),0);
                    }else{
                        Bitmap temp=ImageTool.imagePath2Bitmap(path);
                        Bitmap imageBit=ImageTool.customCompressImage(temp,Integer.parseInt(edit_size.getText().toString()));
                        MatrixToast.showToast(view.getContext(),"文件大小:",0);
                        if(imageBit!=null){
                            compress.setVisibility(View.VISIBLE);
                            ImageView compress_image;
                            TextView compress_image_name,compress_image_path,compress_image_size;
                            compress_image=view.findViewById(R.id.compress_image);
                            String name=TimeTool.GetSystemTime();
                            compress_image_name=view.findViewById(R.id.compress_image_name);
                            compress_image_path=view.findViewById(R.id.compress_image_path);
                            compress_image_size=view.findViewById(R.id.compress_image_size);

                            compress_image.setImageBitmap(imageBit);
                            ImageTool.compress_SaveBitmap(imageBit,file_path,view.getContext(), TimeTool.GetSystemTime());
                        }
                    }
                }
            });
        }
    }
}
