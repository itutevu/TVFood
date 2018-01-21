package com.example.user.tvfood.Local_Address;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.user.tvfood.Common.Common;
import com.example.user.tvfood.Common.SessionDiaDiem;
import com.example.user.tvfood.Common.SessionLanguage;
import com.example.user.tvfood.Common.SessionUser;
import com.example.user.tvfood.GlobalBus;
import com.example.user.tvfood.Model.EventFillter;
import com.example.user.tvfood.R;
import com.example.user.tvfood.UI.Fragment_TrangChu_Nav;

import java.util.HashMap;
import java.util.List;

/**
 * Created by THANH on 3/27/2017.
 */

public class CustomAdapterExpandableList extends BaseExpandableListAdapter {
    private static final String TAG = "CustomAdapterExpandableList";
    private Context mContext;
    private List<Parent_ExpandableListDTO> mHeaderGroup;
    private HashMap<Parent_ExpandableListDTO, List<DuongDTO>> mDataChild;
    SessionUser sessionUser;
    SessionLanguage sessionLanguage;


    public CustomAdapterExpandableList(Context context, List<Parent_ExpandableListDTO> headerGroup, HashMap<Parent_ExpandableListDTO, List<DuongDTO>> datas) {
        mContext = context;
        mHeaderGroup = headerGroup;
        mDataChild = datas;
        sessionUser = new SessionUser(mContext);
        sessionLanguage = new SessionLanguage(mContext);
    }

    @Override
    public int getGroupCount() {
        return mHeaderGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mDataChild.get(mHeaderGroup.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mHeaderGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mDataChild.get(mHeaderGroup.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, final ViewGroup parent) {
        if (convertView == null) {

            /*LayoutInflater li = LayoutInflater.from(mContext);
            convertView = li.inflate(R.layout.activity_row_layout1, parent, false);*/
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_expandable_listview, null);
        }
        TextView tvHeader = (TextView) convertView.findViewById(R.id.tvrow1);
        tvHeader.setText(mHeaderGroup.get(groupPosition).getTenQuan());
        TextView textView = (TextView) convertView.findViewById(R.id.tvrow2);


        if (sessionLanguage.getKeyLanguage() == Common.KEY_LANGUAGE.KEY_EN) {
            if (Integer.parseInt(mHeaderGroup.get(groupPosition).getSoDuong().trim()) != 0
                    || Integer.parseInt(mHeaderGroup.get(groupPosition).getSoDuong().trim()) != 1) {
                textView.setText(mHeaderGroup.get(groupPosition).getSoDuong() + " " + mContext.getResources().getString(R.string.duongdi));
                textView.append("s");
            } else
                textView.setText(mHeaderGroup.get(groupPosition).getSoDuong() + " " + mContext.getResources().getString(R.string.duongdi));
        } else {
            textView.setText(mHeaderGroup.get(groupPosition).getSoDuong() + " " + mContext.getResources().getString(R.string.duongdi));
        }
        textView.setFocusable(false);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    ((ExpandableListView) parent).collapseGroup(groupPosition);
                } else {

                    ((ExpandableListView) parent).expandGroup(groupPosition, true);
                }
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater li = LayoutInflater.from(mContext);
            convertView = li.inflate(R.layout.item_list_duong, parent, false);
        }
        TextView txtTenDuong = (TextView) convertView.findViewById(R.id.tvduong);
        txtTenDuong.setText(((DuongDTO) getChild(groupPosition, childPosition)).getTenDuong());
        txtTenDuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionDiaDiem.getInstance().setKEY_QuanHuyen(Common.KEY_SORT.KEY_QUANHUYEN_FALSE);
                SessionDiaDiem.getInstance().setKEY_Duong(Common.KEY_SORT.KEY_DUONG_TRUE);
                SessionDiaDiem.getInstance().setKEY_ThanhPho(Common.KEY_SORT.KEY_THANHPHO_FALSE);
                SessionDiaDiem.getInstance().setVALUE_Duong(mDataChild.get(mHeaderGroup.get(groupPosition)).get(childPosition).getId());

                GlobalBus.getBus().postSticky(new EventFillter("Fillter"));

                Fragment_TrangChu_Nav.mPager.setCurrentItem(1);
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


}
