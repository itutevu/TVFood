package com.example.user.tvfood.MaterialSearchView;

import com.example.user.tvfood.Model.DataSearch_Model;

/**
 * Created by USER on 01/12/2017.
 */

public interface onSimpleSearchActionsListener {
    void onItemClicked(DataSearch_Model item);
    void onScroll();
    void error(String localizedMessage);
}
