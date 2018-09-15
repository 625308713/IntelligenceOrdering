package com.shenzhou.intelligenceordering.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.shenzhou.intelligenceordering.R;

public class PersonInfoDialog  extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //设置透明
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.frament_person_info, null);
        builder.setView(view);
        Button btn_1 = view.findViewById(R.id.btn_1);
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModifyPsdDialog mpd = new ModifyPsdDialog();
                mpd.show(getFragmentManager(),"psdDialog");
                dismiss();
            }
        });
        TextView tv_name = view.findViewById(R.id.tv_name);
        tv_name.setText(SPUtils.getInstance().getString("userName"));
        ImageView img_close = view.findViewById(R.id.img_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return builder.create();
    }

    //设置宽占满屏
    @Override
    public void onResume() {
        super.onResume();
        Window mWindow = getDialog().getWindow();
        WindowManager.LayoutParams mLayoutParams = mWindow.getAttributes();
        mLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mWindow.setAttributes(mLayoutParams);
    }
}
