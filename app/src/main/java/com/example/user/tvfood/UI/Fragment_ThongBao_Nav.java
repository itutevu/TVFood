package com.example.user.tvfood.UI;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.user.tvfood.Adapter.Adapter_RecyclerView_ListThongBao;
import com.example.user.tvfood.Common.Common;
import com.example.user.tvfood.Model.Notification_Model;
import com.example.user.tvfood.R;
import com.example.user.tvfood.RecyclerViewItemDecoratorList;
import com.example.user.tvfood.UI.LayDanhSachThongBao.LayDanhSachThongBao_Presenter;
import com.example.user.tvfood.UI.LayDanhSachThongBao.LayDanhSachThongBao_ViewListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_ThongBao_Nav extends Fragment implements LayDanhSachThongBao_ViewListener, SwipeRefreshLayout.OnRefreshListener {
    private static final int NUM_OF_LOAD = 15;
    private LayDanhSachThongBao_Presenter layDanhSachThongBao_presenter;
    private RecyclerView recyclerView;
    private ArrayList<Notification_Model> itemNotificationList;
    private Adapter_RecyclerView_ListThongBao adapter_recyclerView_listThongBao;
    private boolean isLoadMore = false;
    private boolean isEmpty = false;
    private boolean isRefresh = false;
    private boolean isFirst = true;
    private FrameLayout frameLayout;
    private ProgressBar progressBar_LoadMore;
    private SwipeRefreshLayout swipeRefresh;

    public Fragment_ThongBao_Nav() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragment_nav_notification, container, false);
        initView(v);
        initRecycleView();
        isFirst = true;
        layDanhSachThongBao_presenter = new LayDanhSachThongBao_Presenter();
        layDanhSachThongBao_presenter.receiveHandle(this, NUM_OF_LOAD, "");

        realTimeNotification();
        return v;
    }


    private int previousTotal = 0; // ting so item tinh tu lan load truoc
    private int previousTotalOld = 0;
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 0; // The minimum amount of items to have below your current scroll position before loading more.
    private int firstVisibleItem, visibleItemCount, totalItemCount;

    private void initRecycleView() {

        recyclerView.setHasFixedSize(false);
        recyclerView.setNestedScrollingEnabled(false);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        itemNotificationList = new ArrayList<>();
        adapter_recyclerView_listThongBao = new Adapter_RecyclerView_ListThongBao(getContext(), itemNotificationList);
        recyclerView.setAdapter(adapter_recyclerView_listThongBao);

        int spaceInPixels = 10;
        recyclerView.addItemDecoration(new RecyclerViewItemDecoratorList(spaceInPixels));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                if (isLoadMore || isEmpty || isRefresh)
                    return;
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    // van dang load
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotalOld = previousTotal;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    // gan het danh sach, load tiep item moi

                    loading = true;
                    loadMore();
                }
            }
        });
    }

    private void loadMore() {
        if (isLoadMore)
            return;
        isLoadMore = true;
        progressBar_LoadMore.setVisibility(View.VISIBLE);
        layDanhSachThongBao_presenter.receiveHandle(this, NUM_OF_LOAD + itemNotificationList.size(), itemNotificationList.get(itemNotificationList.size() - 1).getNotiDate());


    }

    private void initView(View v) {
        swipeRefresh = v.findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(this);

        progressBar_LoadMore = v.findViewById(R.id.progressBar_LoadMore);

        recyclerView = v.findViewById(R.id.recyclerView);

        frameLayout = v.findViewById(R.id.frameLayout);
        frameLayout.setVisibility(View.VISIBLE);
        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }

    @Override
    public void onSuccess_GetNotification(ArrayList<Notification_Model> notification_models) {
        ArrayList<Notification_Model> notification_models2 = new ArrayList<>();
        notification_models2.addAll(notification_models);
        adapter_recyclerView_listThongBao.appendData(notification_models2);
        if (frameLayout.getVisibility() == View.VISIBLE)
            frameLayout.setVisibility(View.GONE);
        if (progressBar_LoadMore.getVisibility() == View.VISIBLE)
            progressBar_LoadMore.setVisibility(View.GONE);
        isLoadMore = false;
        if (isRefresh) {
            isRefresh = false;
            swipeRefresh.setRefreshing(false);
        }

    }

    @Override
    public void onFailed_GetNotification(String message) {

        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        if (frameLayout.getVisibility() == View.VISIBLE)
            frameLayout.setVisibility(View.GONE);
        if (progressBar_LoadMore.getVisibility() == View.VISIBLE)
            progressBar_LoadMore.setVisibility(View.GONE);
        isLoadMore = false;
        if (isRefresh) {
            isRefresh = false;
            swipeRefresh.setRefreshing(false);
        }
        // Gán lại biến đếm trước khi loadMore
        this.previousTotal = previousTotalOld;


    }

    @Override
    public void onEmpty_GetNotification(String message) {

        isEmpty = true;
        isLoadMore = false;
        if (isRefresh) {
            isRefresh = false;
            swipeRefresh.setRefreshing(false);
        }
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        if (frameLayout.getVisibility() == View.VISIBLE)
            frameLayout.setVisibility(View.GONE);
        if (progressBar_LoadMore.getVisibility() == View.VISIBLE)
            progressBar_LoadMore.setVisibility(View.GONE);
        // Gán lại biến đếm trước khi loadMore
        this.previousTotal = previousTotalOld;


    }

    @Override
    public void onRefresh() {
        if (isLoadMore || isRefresh) {
            swipeRefresh.setRefreshing(false);
            return;
        }
        isRefresh = true;
        isEmpty = false;
        refresh();
    }

    private void refresh() {

        adapter_recyclerView_listThongBao.refreshData();
        layDanhSachThongBao_presenter.receiveHandle(this, NUM_OF_LOAD, "");
        //refresh lại biến đếm loadmore
        this.previousTotal = 0;

    }

    private void realTimeNotification() {

        MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_THONGBAOS).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (isFirst)
                    return;
                Notification_Model notification_model = dataSnapshot.getValue(Notification_Model.class);
                assert notification_model != null;
                notification_model.setNotiDate(dataSnapshot.getKey());

                adapter_recyclerView_listThongBao.addData(notification_model);
                MainActivity.txt_New.setVisibility(View.VISIBLE);
                int numOfNotification = Integer.parseInt(MainActivity.txt_New.getText().toString()) + 1;
                if (numOfNotification > 99)
                    MainActivity.txt_New.setText("99+");
                else
                    MainActivity.txt_New.setText(String.valueOf(numOfNotification));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
