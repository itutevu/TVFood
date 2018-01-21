package com.example.user.tvfood.MaterialSearchView;

/**
 * Created by USER on 01/12/2017.
 */

public interface onSearchListener {
    void onSearch(String query);
    void searchViewOpened();
    void searchViewClosed();
    void onCancelSearch();
}
