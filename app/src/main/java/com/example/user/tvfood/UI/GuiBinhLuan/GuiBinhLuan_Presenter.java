package com.example.user.tvfood.UI.GuiBinhLuan;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.user.tvfood.Common.Common;
import com.example.user.tvfood.Common.SessionUser;
import com.example.user.tvfood.UI.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by USER on 15/10/2017.
 */

public class GuiBinhLuan_Presenter {
    private GuiBinhLuan_ViewListener guiBinhLuan_viewListener;

    public void receiveHandle(GuiBinhLuan_ViewListener guiBinhLuan_viewListener, String urlimage, String key, SessionUser sessionUser, String idQuanAn, String noiDung, byte[] data) {
        this.guiBinhLuan_viewListener = guiBinhLuan_viewListener;
        if (urlimage.equals("null")) {
            guiBinhLuan(urlimage, key, sessionUser, idQuanAn, noiDung);
        } else {
            //guiBinhLuan_HinhAnh(path, urlimage, key, sessionUser, idQuanAn, noiDung);
            guiBinhLuan_HinhAnh(data, urlimage, key, sessionUser, idQuanAn, noiDung);
        }
    }

    private synchronized void guiBinhLuan(final String urlimage, String key, final SessionUser sessionUser, String idQuanAn, final String noiDung) {
        synchronized (this) {

            MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_BINHLUANS).child(idQuanAn).child(key).runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {

                    if (mutableData.getValue() != null) {
                        String key = new SimpleDateFormat("yyyyMMddHHmmss", Locale.SIMPLIFIED_CHINESE).format(new Date());
                        double keyInt = Double.parseDouble(key) + 1;
                        //guiBinhLuan(urlimage, keyInt + sessionUser.getUserDTO().getId() + "");
                        return Transaction.success(mutableData);
                    }

                    HashMap<String, Object> valueUpdate = new HashMap<String, Object>();
                    valueUpdate.put("noidung", noiDung);
                    valueUpdate.put("urlimage", urlimage);
                    valueUpdate.put("userid", sessionUser.getUserDTO().getId());


                    mutableData.setValue(valueUpdate);

                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                    guiBinhLuan_viewListener.onSuccessGuiBinhLuan();

                }

            });
        }
    }

    private void guiBinhLuan_HinhAnh(byte[] data, final String urlimage, final String key, final SessionUser sessionUser, final String idQuanAn, final String noiDung) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(Common.KEY_CODE.HINHQUANAN).child(urlimage);

        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                guiBinhLuan(urlimage, key, sessionUser, idQuanAn, noiDung);

            }
        });
    }

    private void guiBinhLuan_HinhAnh(String path, final String urlimage, final String key, final SessionUser sessionUser, final String idQuanAn, final String noiDung) {
        Uri file = Uri.fromFile(new File(path));


        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(Common.KEY_CODE.HINHQUANAN).child(urlimage);

        /*StorageReference riversRef = storageRef.child("images/" + file.getLastPathSegment());*/
        UploadTask uploadTask = storageReference.putFile(file);

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                //Uri downloadUrl = taskSnapshot.getDownloadUrl();

                guiBinhLuan(urlimage, key, sessionUser, idQuanAn, noiDung);
            }
        });
    }
}
