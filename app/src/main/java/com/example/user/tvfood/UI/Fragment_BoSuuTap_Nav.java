package com.example.user.tvfood.UI;


import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.tvfood.Adapter.Adapter_RecyclerView_GridBoSuuTap;
import com.example.user.tvfood.Common.Common;
import com.example.user.tvfood.Common.IsConnect;
import com.example.user.tvfood.Common.SessionUser;
import com.example.user.tvfood.GlobalBus;
import com.example.user.tvfood.Model.EventBoSuuTap;
import com.example.user.tvfood.Model.QuanAnDTO;
import com.example.user.tvfood.R;
import com.example.user.tvfood.RecyclerViewItemDecorator;
import com.example.user.tvfood.UI.LayDanhSachQuanAnGHIM.LayDanhSachQuanAnGHIM_Presenter;
import com.example.user.tvfood.UI.LayDanhSachQuanAnGHIM.LayDanhSachQuanAnGHIM_ViewListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_BoSuuTap_Nav extends Fragment implements ChildEventListener, LayDanhSachQuanAnGHIM_ViewListener {

    public static final int NUM_OF_ITEM = 10;

    public Fragment_BoSuuTap_Nav() {
        // Required empty public constructor
    }

    private RecyclerView recycleView;
    public Adapter_RecyclerView_GridBoSuuTap adapter_recyclerView_gridBoSuuTap;
    public ArrayList<QuanAnDTO> itemList;
    public FrameLayout progressBar_Load, frameLayout;
    SessionUser sessionUser;
    private boolean isFisrt = true, isLoadMore = false, isEmpty = false;
    private ProgressBar progressBar_LoadMore;
    private TextView txt_ThongBao;
    private LayDanhSachQuanAnGHIM_Presenter layDanhSachQuanAnGHIM_presenter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragment__bo_suu_tap__nav, container, false);
        if (!GlobalBus.getBus().isRegistered(this))
            GlobalBus.getBus().register(this);
        sessionUser = new SessionUser(getContext());
        initView(v);
        initRecyclerView();
        layDanhSachQuanAnGHIM_presenter = new LayDanhSachQuanAnGHIM_Presenter();
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (GlobalBus.getBus().isRegistered(this))
            GlobalBus.getBus().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!GlobalBus.getBus().isRegistered(this))
            GlobalBus.getBus().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBoSuuTap(EventBoSuuTap event) {
        if (!IsConnect.getInstance().isConnect()) {
            Snackbar.make(frameLayout, getResources().getString(R.string.khongcoketnoiinternet), Snackbar.LENGTH_LONG).show();
            return;
        }
        switch (event.getIdEvent()) {
            // 1 : Refresh ; 2 : Load ; 3 : Clear
            case "1": {
                //refresh lại biến đếm loadmore
                isEmpty = false;
                this.previousTotal = 0;
                if (itemList.size() != 0)
                    adapter_recyclerView_gridBoSuuTap.refreshData();
                progressBar_Load.setVisibility(View.VISIBLE);
                layDanhSachQuanAnGHIM_presenter.receiveHandle(this, sessionUser, NUM_OF_ITEM, "0");
                break;

            }
            case "2": {
                //refresh lại biến đếm loadmore
                isEmpty = false;
                this.previousTotal = 0;
                if (itemList.size() != 0)
                    adapter_recyclerView_gridBoSuuTap.refreshData();
                progressBar_Load.setVisibility(View.VISIBLE);
                layDanhSachQuanAnGHIM_presenter.receiveHandle(this, sessionUser, NUM_OF_ITEM, "0");
                break;
            }
            case "3": {
                //refresh lại biến đếm loadmore
                isEmpty = false;
                this.previousTotal = 0;
                adapter_recyclerView_gridBoSuuTap.refreshData();
                break;
            }
        }
    }

    private int previousTotal = 0; // ting so item tinh tu lan load truoc
    private int previousTotalOld = 0;
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 0; // The minimum amount of items to have below your current scroll position before loading more.
    private int firstVisibleItem, visibleItemCount, totalItemCount;

    private void initRecyclerView() {
// set true if your RecyclerView is finite and has fixed size
        recycleView.setHasFixedSize(false);


        itemList = new ArrayList<>();


        //
        adapter_recyclerView_gridBoSuuTap = new Adapter_RecyclerView_GridBoSuuTap(getContext(), itemList);
        recycleView.setAdapter(adapter_recyclerView_gridBoSuuTap);
        recycleView.setNestedScrollingEnabled(false);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);

        if (recycleView.getLayoutManager() == null) {
            recycleView.setLayoutManager(gridLayoutManager);
        }
        int spaceInPixels = 5;
        recycleView.addItemDecoration(new RecyclerViewItemDecorator(spaceInPixels));


        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                if (isLoadMore || isEmpty)
                    return;
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = gridLayoutManager.getItemCount();
                firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();

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
        layDanhSachQuanAnGHIM_presenter.receiveHandle(this, sessionUser, NUM_OF_ITEM + itemList.size(), itemList.size() + "");

    }

    private void initView(View v) {
        frameLayout = v.findViewById(R.id.frameLayout);
        progressBar_LoadMore = v.findViewById(R.id.progressBar_LoadMore);
        txt_ThongBao = v.findViewById(R.id.txt_ThongBao);
        txt_ThongBao.setVisibility(View.GONE);
        recycleView = v.findViewById(R.id.recycleView);
        progressBar_Load = v.findViewById(R.id.progressBar_Load);
        MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_GHIMS).child(sessionUser.getUserDTO().getId()).addChildEventListener(this);

    }


    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        if (isFisrt)
            return;
        txt_ThongBao.setVisibility(View.GONE);
        MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final QuanAnDTO quanAnDTO = dataSnapshot.getValue(QuanAnDTO.class);
                quanAnDTO.setIdquanan(dataSnapshot.getKey());


                //lấy data hình ảnh quán ăn
                MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_HINHQUANANS).child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> listHinhAnh = new ArrayList<>();
                        for (DataSnapshot valueHinhAnh : dataSnapshot.getChildren()
                                ) {
                            listHinhAnh.add(valueHinhAnh.getValue(String.class));
                        }
                        quanAnDTO.setHinhquanan(listHinhAnh);
                        // Duyệt list Quán Ăn để đổi idhinhanh thành link url

                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("hinhquanan").child(quanAnDTO.getHinhquanan().get(0));
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                quanAnDTO.getHinhquanan().set(0, uri.toString());

                                adapter_recyclerView_gridBoSuuTap.addData(quanAnDTO);
                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        String idQuanAn = dataSnapshot.getKey();
        for (int i = 0; i < itemList.size(); i++
                ) {
            if (itemList.get(i).getIdquanan().equals(idQuanAn)) {
                if (itemList.size() == 1) {
                    txt_ThongBao.setText(getResources().getString(R.string.chuacodulieu));
                    txt_ThongBao.setVisibility(View.VISIBLE);

                }
                adapter_recyclerView_gridBoSuuTap.removeData(i);
                break;
            }
        }

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }


    @Override
    public void onSuccess_LayDanhSachQuanAnGHIM(List<QuanAnDTO> quanAnDTOList) {
        adapter_recyclerView_gridBoSuuTap.appendData(quanAnDTOList);
        progressBar_Load.setVisibility(View.GONE);
        txt_ThongBao.setVisibility(View.GONE);
        isLoadMore = false;
        progressBar_LoadMore.setVisibility(View.GONE);
        isFisrt = false;
    }

    @Override
    public void onFailed_LayDanhSachQuanAnGHIM(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        if (isFisrt) {
            txt_ThongBao.setVisibility(View.VISIBLE);
            txt_ThongBao.setText(getResources().getString(R.string.chuacodulieu));
        }
        progressBar_Load.setVisibility(View.GONE);
        isLoadMore = false;
        progressBar_LoadMore.setVisibility(View.GONE);
        isFisrt = false;
        // Gán lại biến đếm trước khi loadMore
        this.previousTotal = previousTotalOld;
    }

    @Override
    public void onEmpty_LayDanhSachQuanAnGHIM(String message) {
        //Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        if (isFisrt) {
            txt_ThongBao.setVisibility(View.VISIBLE);
            txt_ThongBao.setText(getResources().getString(R.string.chuacodulieu));
        }
        progressBar_Load.setVisibility(View.GONE);
        progressBar_LoadMore.setVisibility(View.GONE);
        isLoadMore = false;
        isFisrt = false;
        isEmpty = true;
    }
}
