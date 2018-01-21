package com.example.user.tvfood.UI;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.user.tvfood.Adapter.Adapter_RecyclerView_ListTrangChu;
import com.example.user.tvfood.Adapter.CustomRecyclerViewAdapter;
import com.example.user.tvfood.Common.Common;
import com.example.user.tvfood.Common.SessionUser;
import com.example.user.tvfood.Common.SesstionFocus;
import com.example.user.tvfood.GlobalBus;
import com.example.user.tvfood.LinearLayoutManagerWithSmoothScroller;
import com.example.user.tvfood.Model.EventFillter;
import com.example.user.tvfood.Model.QuanAnDTO;
import com.example.user.tvfood.R;
import com.example.user.tvfood.UI.LayDanhSachQuanAn.LayDanhSachQuanAn_Presenter;
import com.example.user.tvfood.UI.LayDanhSachQuanAn.LayDanhSachQuanAn_ViewListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressCustom;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab_TrangChu extends Fragment implements View.OnClickListener, CustomRecyclerViewAdapter.MyClickListener, LayDanhSachQuanAn_ViewListener
        , SwipeRefreshLayout.OnRefreshListener {
    public static final int NUM_OF_LOAD = 5;
    public static final int DISTANCE = 15;

    public static boolean isLoadMore = false;

    private boolean isLoadGanToi = false;
    private boolean isEmpty = false;
    private ProgressBar progressBar_LoadMore;
    private FrameLayout frameLayout;

    //private ObservableScrollView scrollView;
    public static ArrayList<QuanAnDTO> itemListTrangChus;


    public Tab_TrangChu() {
        // Required empty public constructor
    }


    //
    private RecyclerView recyclerViewList;
    private Adapter_RecyclerView_ListTrangChu adapter_recyclerView_listTrangChu;
    //
    ACProgressCustom dialog_Load;


    public static SwipeRefreshLayout swipeRefresh;
    public static boolean isRefresh = false;
    private Button btnUp;
    //public static FrameLayout progressBar_LoadListQA;


    private LayoutInflater inflater;

    private SessionUser sessionUser;
    private String tokenID = "";

    public static LayDanhSachQuanAn_Presenter layDanhSachQuanAn_presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab__trang_chu, container, false);
        if (!GlobalBus.getBus().isRegistered(this))
            GlobalBus.getBus().register(this);


        dialog_Load = new ACProgressCustom.Builder(getContext())
                .speed(25)
                .sizeRatio(0.6f)
                .useImages(R.drawable.load01, R.drawable.load02, R.drawable.load03, R.drawable.load04, R.drawable.load05, R.drawable.load06, R.drawable.load07,
                        R.drawable.load08, R.drawable.load09, R.drawable.load10, R.drawable.load11, R.drawable.load12)
                .build();
        dialog_Load.show();
        // Inflate the layout for this fragment
        initView(v);
        sessionUser = new SessionUser(getContext());
        if (sessionUser.getUserDTO() != null)
            tokenID = sessionUser.getUserDTO().getId();

        //Khởi tạo RecyclerView List Quán ăn
        initRecyclerView();


        // Lấy danh sách quán ăn
        layDanhSachQuanAn_presenter = new LayDanhSachQuanAn_Presenter();
        layDanhSachQuanAn_presenter.receiveHandle(this, SesstionFocus.getInstance().getKEY_SORT(), NUM_OF_LOAD, tokenID, itemListTrangChus.size());


        //Lấy danh sách quán ăn gần tôi
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //getDanhSachQuanAnGanToi(NUM_OF_LOAD_NEAR_ME);
            }
        }, 1000);


        return v;
    }


    private void initView(View v) {

        frameLayout = v.findViewById(R.id.frameLayout);
        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        recyclerViewList = v.findViewById(R.id.recyclerViewList);


        swipeRefresh = v.findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(this);
        btnUp = v.findViewById(R.id.btnUp);
        btnUp.setVisibility(View.INVISIBLE);
        btnUp.setOnClickListener(this);

        progressBar_LoadMore = v.findViewById(R.id.progressBar_LoadMore);
        progressBar_LoadMore.setVisibility(View.GONE);


    }


    private int previousTotal = 0; // ting so item tinh tu lan load truoc
    private int previousTotalOld = 0;
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 0; // The minimum amount of items to have below your current scroll position before loading more.
    private int firstVisibleItem, visibleItemCount, totalItemCount;

    // Hide Show toolbar
    private int mScrolledAmount = 0;
    private int mScrolledAmountOld = 0;
    public static boolean isExpand = true;

    private void initRecyclerView() {

        // set true if your RecyclerView is finite and has fixed size
        recyclerViewList.setHasFixedSize(false);


        itemListTrangChus = new ArrayList<>();


        //
        adapter_recyclerView_listTrangChu = new Adapter_RecyclerView_ListTrangChu(getContext(), itemListTrangChus, frameLayout);
        recyclerViewList.setAdapter(adapter_recyclerView_listTrangChu);
        //recyclerViewList.setNestedScrollingEnabled(false);

        final LinearLayoutManagerWithSmoothScroller linearLayoutManager = new LinearLayoutManagerWithSmoothScroller(getContext());
        if (recyclerViewList.getLayoutManager() == null) {
            recyclerViewList.setLayoutManager(linearLayoutManager);
        }


        recyclerViewList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy == 0) {
                    if (btnUp.getVisibility() == View.VISIBLE) {
                        btnUp.setVisibility(View.INVISIBLE);
                    }
                } else {
                    if (btnUp.getVisibility() == View.INVISIBLE)
                        btnUp.setVisibility(View.VISIBLE);
                }
                mScrolledAmountOld = mScrolledAmount;
                mScrolledAmount += dy;


                if (isEmpty || isRefresh || isLoadMore)
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
        if (isLoadMore || isRefresh)
            return;
        isLoadMore = true;
        progressBar_LoadMore.setVisibility(View.VISIBLE);
        layDanhSachQuanAn_presenter.receiveHandle(this, SesstionFocus.getInstance().getKEY_SORT(), NUM_OF_LOAD, tokenID, itemListTrangChus.size());

    }

    public boolean checkGPS() {
        LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        return gps_enabled;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

        // Setting Dialog Title
        alertDialog.setTitle(getResources().getString(R.string.caidatgps));
        alertDialog.setCancelable(false);

        // Setting Dialog Message
        alertDialog.setMessage(getResources().getString(R.string.gpskhonghoatdong));

        // On pressing Settings button
        alertDialog.setPositiveButton(getResources().getString(R.string.caidat), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, 101);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton(getResources().getString(R.string.thoat), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    private void makeText(String text) {
        Toast.makeText(getContext(), text + "", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onItemClick(int position, View v) {


    }

    @Override
    public void onRefresh() {

        if (isRefresh || isLoadMore) {
            swipeRefresh.setRefreshing(false);
            return;
        }
        //swipeRefresh.setRefreshing(true);
        isRefresh = true;
        isEmpty = false;
        refresh();
    }

    private void refresh() {
        //dialog_Load.show();
        adapter_recyclerView_listTrangChu.refreshData();
        layDanhSachQuanAn_presenter.receiveHandle(this, SesstionFocus.getInstance().getKEY_SORT(), NUM_OF_LOAD, tokenID, itemListTrangChus.size());
        //refresh lại biến đếm loadmore
        this.previousTotal = 0;
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {

            case R.id.btnUp: {
                recyclerViewList.smoothScrollToPosition(0);
                btnUp.setVisibility(View.INVISIBLE);
                MainActivity.myAppbar.setExpanded(true, true);

                break;
            }
        }
    }


    @Override
    public void onSuccess_LayDanhSachQuanAn(ArrayList<QuanAnDTO> quanAnDTOs) {
        adapter_recyclerView_listTrangChu.appendData(quanAnDTOs);

        //
        progressBar_LoadMore.setVisibility(View.GONE);
        dialog_Load.dismiss();
        Tab_TrangChu.swipeRefresh.setRefreshing(false);
        Tab_TrangChu.isRefresh = false;
        Tab_TrangChu.isLoadMore = false;

    }

    @Override
    public void onFailed_LayDanhSachQuanAn(String message) {
        makeText(message);
        //
        progressBar_LoadMore.setVisibility(View.GONE);
        dialog_Load.dismiss();
        Tab_TrangChu.swipeRefresh.setRefreshing(false);
        Tab_TrangChu.isRefresh = false;
        Tab_TrangChu.isLoadMore = false;

        // Gán lại biến đếm trước khi loadMore
        this.previousTotal = previousTotalOld;
    }

    @Override
    public void onEmpty_LayDanhSachQuanAn(String message) {
        makeText(message);
        //
        isEmpty = true;
        progressBar_LoadMore.setVisibility(View.GONE);
        dialog_Load.dismiss();
        Tab_TrangChu.swipeRefresh.setRefreshing(false);
        Tab_TrangChu.isRefresh = false;
        Tab_TrangChu.isLoadMore = false;

        // Gán lại biến đếm trước khi loadMore
        this.previousTotal = previousTotalOld;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClickFillterEvent(EventFillter eventFillter) {

        progressBar_LoadMore.setVisibility(View.GONE);
        dialog_Load.show();
        adapter_recyclerView_listTrangChu.refreshData();
        layDanhSachQuanAn_presenter.receiveHandle(this, SesstionFocus.getInstance().getKEY_SORT(), NUM_OF_LOAD, tokenID, itemListTrangChus.size());
        this.previousTotal = 0;
    }


}
