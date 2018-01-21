package com.example.user.tvfood.MaterialSearchView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.user.tvfood.Common.DataSearch;
import com.example.user.tvfood.Model.DataSearch_Model;
import com.example.user.tvfood.R;

import java.util.ArrayList;

/**
 * Created by USER on 01/12/2017.
 */

public class SearchViewResults implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener, onMyClickItemRecyclerView {
    private static final int TRIGGER_SEARCH = 1;
    private static final long SEARCH_TRIGGER_DELAY_IN_MS = 400;
    private RecyclerView mRecyclerView;
    private String sequence;
    private int mPage;
    private SearchTask mSearch;
    private Handler mHandler;
    private boolean isLoadMore;
    private Adapter_RecyclerView_Search mAdapter;
    private onSearchActionsListener mListener;
    private ArrayList<DataSearch_Model> searchList;
    private Context context;

    /*
    * Used Handler in case implement Search remotely
    * */

    public SearchViewResults(Context context, String searchQuery) {
        this.context = context;
        sequence = searchQuery;
        searchList = new ArrayList<>();
        //searchList.addAll(DataSearch.getInstance().getDataSearch_models());
        mAdapter = new Adapter_RecyclerView_Search(context, searchList, this);

        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_SEARCH) {
                    clearAdapter();
                    String sequence = (String) msg.obj;
                    mSearch = new SearchTask();
                    mSearch.execute(sequence);
                }
                return false;
            }
        });
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(linearLayoutManager);


        //mRecyclerView.setOnItemClickListener(this);
        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        mRecyclerView.setAdapter(mAdapter);
        updateSequence();
    }

    public void updateSequence(String s) {
        sequence = s;
        updateSequence();
    }

    private void updateSequence() {
        mPage = 0;
        isLoadMore = true;

        if (mSearch != null) {
            mSearch.cancel(false);
        }
        if (mHandler != null) {
            mHandler.removeMessages(TRIGGER_SEARCH);
        }
        if (!sequence.isEmpty()) {
            Message searchMessage = new Message();
            searchMessage.what = TRIGGER_SEARCH;
            searchMessage.obj = sequence;
            mHandler.sendMessageDelayed(searchMessage, SEARCH_TRIGGER_DELAY_IN_MS);
        } else {
            isLoadMore = false;
            clearAdapter();
        }
    }

    private void clearAdapter() {
        mAdapter.refreshData();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mListener.onItemClicked((DataSearch_Model) mAdapter.getItem(position));

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_TOUCH_SCROLL || scrollState == SCROLL_STATE_FLING) {
            mListener.onScroll();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        boolean loadMore = totalItemCount > 0 && firstVisibleItem + visibleItemCount >= totalItemCount;
        if (loadMore && isLoadMore) {
            mPage++;
            isLoadMore = false;
            mSearch = new SearchTask();
            mSearch.execute(sequence);
        }
    }

    /*
    * Implement the Core search functionality here
    * Could be any local or remote
    */
    private ArrayList<DataSearch_Model> findItem(String query, int page) {
        ArrayList<DataSearch_Model> result = new ArrayList<>();
        for (DataSearch_Model item : DataSearch.getInstance().getDataSearch_models()) {
            if (item.getTenquanan().toLowerCase().trim().contains(query.toLowerCase().trim())) {
                result.add(item);
            }
        }
        return result;
    }

    public void setSearchProvidersListener(onSearchActionsListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onItemClick(int position) {
        mListener.onItemClicked((DataSearch_Model) mAdapter.getItem(position));
    }

    /*
    * Search for the item asynchronously
    */
    private class SearchTask extends AsyncTask<String, Void, ArrayList<DataSearch_Model>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mListener.showProgress(true);
        }

        @Override
        protected ArrayList<DataSearch_Model> doInBackground(String... params) {
            String query = params[0];
            ArrayList<DataSearch_Model> result = findItem(query, mPage);
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<DataSearch_Model> result) {
            super.onPostExecute(result);
            if (!isCancelled()) {
                mListener.showProgress(false);
                if (mPage == 0 && result.isEmpty()) {
                    mListener.listEmpty();
                } else {
                    mAdapter.refreshData();
                    mAdapter.appendData(result);
                }
            }
        }
    }
}
