package me.dkzwm.widget.srl.sample.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import me.dkzwm.widget.srl.RefreshingListenerAdapter;
import me.dkzwm.widget.srl.SmoothRefreshLayout;
import me.dkzwm.widget.srl.extra.IRefreshView;
import me.dkzwm.widget.srl.extra.footer.ClassicFooter;
import me.dkzwm.widget.srl.extra.header.ClassicHeader;
import me.dkzwm.widget.srl.sample.R;
import me.dkzwm.widget.srl.sample.adapter.ListViewAdapter;
import me.dkzwm.widget.srl.sample.utils.DataUtil;

/**
 * Created by dkzwm on 2017/6/1.
 *
 * @author dkzwm
 */
public class WithListViewActivity extends AppCompatActivity implements View.OnClickListener {
    private SmoothRefreshLayout mRefreshLayout;
    private ListView mListView;
    private ListViewAdapter mAdapter;
    private Handler mHandler = new Handler();
    private int mCount = 0;
    private ClassicFooter mClassicFooter;
    private ClassicHeader mClassicHeader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_listview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.with_listView);
        mListView = (ListView) findViewById(R.id.listView_with_listView_activity);
        mAdapter = new ListViewAdapter(getLayoutInflater());
        mListView.setAdapter(mAdapter);
        mRefreshLayout = (SmoothRefreshLayout) findViewById(R.id.smoothRefreshLayout_with_listView_activity);
        mClassicHeader = (ClassicHeader) findViewById(R.id.classicHeader_with_listView_activity);
        mClassicHeader.setLastUpdateTimeKey("header_last_update_time");
        mClassicFooter = (ClassicFooter) findViewById(R.id.classicFooter_with_listView_activity);
        mClassicFooter.setLastUpdateTimeKey("footer_last_update_time");
        mClassicHeader.setTitleTextColor(Color.WHITE);
        mClassicHeader.setLastUpdateTextColor(Color.GRAY);
        mRefreshLayout.setEnableKeepRefreshView(true);
        mRefreshLayout.setDisableLoadMore(false);
        mRefreshLayout.setEnableScrollToBottomAutoLoadMore(true);
        mRefreshLayout.setOnRefreshListener(new RefreshingListenerAdapter() {
            @Override
            public void onRefreshBegin(final boolean isRefresh) {
                if (!isRefresh) {
                    Toast.makeText(WithListViewActivity.this, R.string.has_been_triggered_to_load_more,
                            Toast.LENGTH_SHORT).show();
                }
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isRefresh) {
                            mCount = 0;
                            List<String> list = DataUtil.createList(mCount, 20);
                            mCount += 20;
                            mAdapter.updateData(list);
                            mRefreshLayout.setEnableLoadMoreNoMoreData(false);
                        } else {
                            List<String> list = DataUtil.createList(mCount, 20);
                            mCount += 20;
                            mAdapter.appendData(list);
                            if (mCount > 50)
                                mRefreshLayout.setEnableLoadMoreNoMoreData(true);
                        }
                        mRefreshLayout.refreshComplete(1200);
                    }
                }, 5000);
            }

            @Override
            public void onRefreshComplete(boolean isSuccessful) {
                Toast.makeText(WithListViewActivity.this, R.string.sr_refresh_complete,
                        Toast.LENGTH_SHORT).show();
                if (mRefreshLayout.getState() != SmoothRefreshLayout.STATE_CONTENT)
                    mRefreshLayout.setState(SmoothRefreshLayout.STATE_CONTENT, false);
            }
        });

        mRefreshLayout.setOffsetRatioToKeepRefreshViewWhileLoading(1);
        mRefreshLayout.setRatioOfRefreshViewHeightToRefresh(1);
        mRefreshLayout.autoRefresh(false);

        findViewById(R.id.button_with_listView_activity_change_empty_state)
                .setOnClickListener(this);
        findViewById(R.id.button_with_listView_activity_change_content_state)
                .setOnClickListener(this);
        findViewById(R.id.button_with_listView_activity_change_error_state)
                .setOnClickListener(this);
        findViewById(R.id.button_with_listView_activity_change_custom_state)
                .setOnClickListener(this);
        findViewById(R.id.button_with_listView_activity_disable_refresh)
                .setOnClickListener(this);
        findViewById(R.id.button_with_listView_activity_enable_refresh)
                .setOnClickListener(this);
        findViewById(R.id.button_with_listView_activity_disable_loadMore)
                .setOnClickListener(this);
        findViewById(R.id.button_with_listView_activity_enable_loadMore)
                .setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_with_listView_activity_change_empty_state:
                mRefreshLayout.setState(SmoothRefreshLayout.STATE_EMPTY, true);
                break;
            case R.id.button_with_listView_activity_change_content_state:
                mRefreshLayout.setState(SmoothRefreshLayout.STATE_CONTENT, true);
                break;
            case R.id.button_with_listView_activity_change_error_state:
                mRefreshLayout.setState(SmoothRefreshLayout.STATE_ERROR, true);
                break;
            case R.id.button_with_listView_activity_change_custom_state:
                mRefreshLayout.setState(SmoothRefreshLayout.STATE_CUSTOM, true);
                break;
            case R.id.button_with_listView_activity_disable_refresh:
                mRefreshLayout.setDisableRefresh(true);
                break;
            case R.id.button_with_listView_activity_enable_refresh:
                mRefreshLayout.setDisableRefresh(false);
                break;
            case R.id.button_with_listView_activity_disable_loadMore:
                mRefreshLayout.setDisableLoadMore(true);
                break;
            case R.id.button_with_listView_activity_enable_loadMore:
                mRefreshLayout.setDisableLoadMore(false);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case Menu.FIRST:
                if (mClassicHeader.getStyle() == IRefreshView.STYLE_SCALE)
                    mClassicHeader.setStyle(IRefreshView.STYLE_DEFAULT);
                else
                    mClassicHeader.setStyle(IRefreshView.STYLE_SCALE);
                if (mClassicFooter.getStyle() == IRefreshView.STYLE_SCALE)
                    mClassicFooter.setStyle(IRefreshView.STYLE_DEFAULT);
                else
                    mClassicFooter.setStyle(IRefreshView.STYLE_SCALE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, R.string.change_style);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(WithListViewActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
