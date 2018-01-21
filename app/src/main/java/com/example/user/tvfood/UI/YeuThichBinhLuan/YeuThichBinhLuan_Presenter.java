package com.example.user.tvfood.UI.YeuThichBinhLuan;

import com.example.user.tvfood.Common.Common;
import com.example.user.tvfood.Common.SessionUser;
import com.example.user.tvfood.UI.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

/**
 * Created by USER on 15/10/2017.
 */

public class YeuThichBinhLuan_Presenter {
    private YeuThichBinhLuan_ViewListener yeuThichBinhLuan_viewListener;
    private String idBinhLuan = "";

    public void receiveHandle(YeuThichBinhLuan_ViewListener yeuThichBinhLuan_viewListener, String idBinhLuan, SessionUser sessionUser,String idbinhluan, String idQuanAn) {
        this.yeuThichBinhLuan_viewListener = yeuThichBinhLuan_viewListener;
        this.idBinhLuan = idbinhluan;
        yeuThichBinhLuan(idBinhLuan, sessionUser, idQuanAn);

    }

    private boolean isYeuThich = false;

    private void yeuThichBinhLuan(final String idBinhLuan, SessionUser sessionUser, final String idQuanAn) {
        MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_YEUTHICHBINHLUANS).child(idQuanAn).child(idBinhLuan).child(sessionUser.getUserDTO().getId() + "").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {

                String result = mutableData.getValue(String.class);
                if (result == null) {
                    mutableData.setValue("1");
                    isYeuThich = true;
                    return Transaction.success(mutableData);
                }
                isYeuThich = false;
                mutableData.setValue(null);


                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                yeuThichBinhLuan_viewListener.onSuccessYeuThichBinhLuan(isYeuThich, idBinhLuan);
            }
        });
    }
}
