package com.example.user.tvfood.Common;

import android.os.AsyncTask;

import com.example.user.tvfood.Model.DataSearch_Model;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 11/10/2017.
 */

public class DataSearch {
    private List<DataSearch_Model> dataSearch_models = new ArrayList<>();
    private static DataSearch instance;

    public List<DataSearch_Model> getDataSearch_models() {
        return dataSearch_models;
    }

    public void setDataSearch_models(List<DataSearch_Model> dataSearch_models) {
        this.dataSearch_models = dataSearch_models;
    }

    public static DataSearch getInstance() {
        if (instance == null) {
            instance = new DataSearch();
        }
        return instance;
    }


}
