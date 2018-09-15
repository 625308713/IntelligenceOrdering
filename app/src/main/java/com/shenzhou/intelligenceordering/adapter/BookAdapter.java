package com.shenzhou.intelligenceordering.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shenzhou.intelligenceordering.R;
import com.shenzhou.intelligenceordering.bean.Book;

import java.util.List;

public class BookAdapter extends BaseQuickAdapter<Book.BooksBean, BaseViewHolder> {
    public BookAdapter(int layoutResId, @Nullable List<Book.BooksBean> data) {
        super(layoutResId, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, Book.BooksBean item) {
        //支持链式调用
        helper.setText(R.id.tv,item.getTitle())
        .addOnClickListener(R.id.img)
        .addOnClickListener(R.id.tv);//设置子空间单击
        Glide.with(mContext).load(item.getImage()).crossFade().into((ImageView) helper.getView(R.id.img));
    }
}
