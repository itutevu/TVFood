package com.example.user.tvfood.Controller;

import android.view.View;

import com.example.user.tvfood.Adapter.Adapter_List_TimQuanAn;
import com.example.user.tvfood.Common.CalculationByDistance;
import com.example.user.tvfood.Common.Common;
import com.example.user.tvfood.Controller.Interface.MapsActivity_TimQuanAn_Interface;
import com.example.user.tvfood.UI.MainActivity;
import com.example.user.tvfood.Model.QuanAnDTO;
import com.example.user.tvfood.R;
import com.example.user.tvfood.UI.MapsActivity_TimQuanAn;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by USER on 27/09/2017.
 */

public class MapsActivity_TimQuanAn_Controller {
    public void getDuLieuQuanAn(LatLng latLngCurrent, final GoogleMap mMap, final List<QuanAnDTO> quanAnDTOList, final List<Marker> markerList, final Adapter_List_TimQuanAn adapter_list_timQuanAn) {
        MapsActivity_TimQuanAn_Interface mapsActivity_timQuanAn_interface = new MapsActivity_TimQuanAn_Interface() {
            @Override
            public void getDuLieuQuanAn(List<QuanAnDTO> quanAnDTOs) {
                for (QuanAnDTO item : quanAnDTOs) {
                    LatLng latLng = new LatLng(item.getLatitude(), item.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions()
                            .title(item.getTenquanan())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.green_marker))
                            .position(latLng);

                    Marker marker = mMap.addMarker(markerOptions);
                    markerList.add(marker);
                }
                if (quanAnDTOs.size() != 0)
                    MapsActivity_TimQuanAn.txt_KhongCoDuLieu.setVisibility(View.GONE);
                else
                    MapsActivity_TimQuanAn.txt_KhongCoDuLieu.setVisibility(View.VISIBLE);
                adapter_list_timQuanAn.notifyDataSetChanged();
                MapsActivity_TimQuanAn.progressBar_Load.setVisibility(View.GONE);
            }
        };

        getData(latLngCurrent, mapsActivity_timQuanAn_interface, quanAnDTOList);
    }

    private void getData(final LatLng latLngCurrent, final MapsActivity_TimQuanAn_Interface mapsActivity_timQuanAn_interface, final List<QuanAnDTO> quanAnDTOList) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot valueQuanAn : dataSnapshot.getChildren()
                        ) {

                    QuanAnDTO item = valueQuanAn.getValue(QuanAnDTO.class);
                    item.setIdquanan(valueQuanAn.getKey());
                    LatLng latLngQuanAn = new LatLng(item.getLatitude(), item.getLongitude());
                    if (Double.parseDouble(CalculationByDistance.getKm(latLngCurrent, latLngQuanAn)) <= MapsActivity_TimQuanAn.DISTANCE)
                        quanAnDTOList.add(item);
                }
                mapsActivity_timQuanAn_interface.getDuLieuQuanAn(quanAnDTOList);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).addListenerForSingleValueEvent(valueEventListener);
    }
}
