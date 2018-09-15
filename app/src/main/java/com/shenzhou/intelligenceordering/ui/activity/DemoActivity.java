package com.shenzhou.intelligenceordering.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shenzhou.intelligenceordering.R;
import com.shenzhou.intelligenceordering.adapter.BookAdapter;
import com.shenzhou.intelligenceordering.app.API;
import com.shenzhou.intelligenceordering.bean.Book;
import com.shenzhou.intelligenceordering.custom.CustomLoadMoreView;
import com.shenzhou.intelligenceordering.presenter.BookPresenter;
import com.shenzhou.intelligenceordering.presenter.view.BookView;
import com.shenzhou.intelligenceordering.until.MyUntil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DemoActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    private BookPresenter mBookPresenter;
    private RecyclerView my_recycler;
    private List<Book.BooksBean> childBooks;
    private BookAdapter bookAdapter;
    //下拉刷新
    private SwipeRefreshLayout mRefreshLayout;
    //首次请求
    private boolean isFirst = true;
    //当前数据数
    private int mCurrentCounter = 0;
    //数据总数
    private int totalCount = 30;
    //每次请求数量
    private int requestCount = 10;
    //加载更多监听
    private BaseQuickAdapter.RequestLoadMoreListener requestMoreListener;
    private ScheduledExecutorService scheduler;
    private long period = API.REQUEST_ORDERING_TIME;
    private long initDelay = 1*1000;
    //定时请求每次请求个数
    private int timeReqCount = 0;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_demo);
    }

    @Override
    protected void initView() {
        my_recycler = findViewById(R.id.my_recycler);

    }

    @Override
    protected void initOther() {
        mBookPresenter = new BookPresenter();
        mBookPresenter.onCreate();
        childBooks = new ArrayList<Book.BooksBean>();
        bookAdapter = new BookAdapter(R.layout.adapter_book,childBooks);
        mRefreshLayout = findViewById(R.id.pull_refresh);
        scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    protected void toDo() {
        mBookPresenter.attachView(mBookView);
        mRefreshLayout.setOnRefreshListener(this);
        //关闭下拉刷新
        mRefreshLayout.setEnabled(false);
        my_recycler.setLayoutManager(new LinearLayoutManager(this));
        // 没有数据的时候默认显示该布局
        bookAdapter.setEmptyView(View.inflate(myContext,R.layout.empty_view, null));
        //基础布局
        my_recycler.setAdapter(bookAdapter);
        setAdapter();
        //开启定时请求服务
        scheduler.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    mBookPresenter.getSearchBooks("西游记", null, timeReqCount, 10);
                    timeReqCount += 10;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, initDelay, period, TimeUnit.MILLISECONDS);

    }

    private void setAdapter(){
        //设置是否开启上滑加载更多
        bookAdapter.setEnableLoadMore(false);
        //添加加载list动画
        bookAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        requestMoreListener = new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override public void onLoadMoreRequested() {
                my_recycler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //成功获取更多数据
                        mBookPresenter.getSearchBooks("西游记", null, requestCount, 10);
                        requestCount += 10;
                    }
                }, 200);
            }
        };
        // 当列表滑动到倒数第N个Item的时候(默认是1)回调onLoadMoreRequested方法
//        bookAdapter.setPreLoadNumber(5);
        //设置自定义加载更多布局
        bookAdapter.setLoadMoreView(new CustomLoadMoreView());
    }

    private BookView mBookView = new BookView() {
        @Override
        public void onSuccess(Book mBook) {
            //这里需要给数据添加值而不能在这里赋值
            if(isFirst){
                isFirst = false;
                childBooks.clear();
                //首次或刷新后设置加载更多监听，刷新后必须再次设置
                bookAdapter.setOnLoadMoreListener(requestMoreListener, my_recycler);
            }
            //请求完成，隐藏下拉刷新UI
            mRefreshLayout.setRefreshing(false);
//            childBooks.addAll(mBook.getBooks());
            childBooks.addAll(0,mBook.getBooks());
            if(MyUntil.isNotNullCollect(bookAdapter.getData())){
                mCurrentCounter = bookAdapter.getData().size();
            }
            bookAdapter.notifyDataSetChanged();
            if (mCurrentCounter >= totalCount) {
                //数据全部加载完毕
                bookAdapter.loadMoreEnd();
                ToastUtils.showShort(getString(R.string.no_more_data));
            }else{
                bookAdapter.loadMoreComplete();
            }
        }

        @Override
        public void onError(String result) {
            //获取更多数据失败
            bookAdapter.loadMoreFail();
        }
    };

    /***
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        isFirst = true;
        mBookPresenter.getSearchBooks("西游记", null, 0, 10);
    }
}
