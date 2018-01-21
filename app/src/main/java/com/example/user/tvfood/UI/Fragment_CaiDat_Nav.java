package com.example.user.tvfood.UI;


import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.tvfood.Adapter.Adapter_Spinner_Language;
import com.example.user.tvfood.Common.Common;
import com.example.user.tvfood.Common.IsConnect;
import com.example.user.tvfood.Common.LocaleHelper;
import com.example.user.tvfood.Common.SessionLanguage;
import com.example.user.tvfood.Common.SessionNotification;
import com.example.user.tvfood.Common.SessionUser;
import com.example.user.tvfood.Model.SpinnerLanguage;
import com.example.user.tvfood.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_CaiDat_Nav extends Fragment implements View.OnClickListener {


    public Fragment_CaiDat_Nav() {
        // Required empty public constructor
    }

    private Spinner spinner;
    private ArrayAdapter<SpinnerLanguage> adapter_spinner_language;
    private List<SpinnerLanguage> spinnerLanguageList;
    private RelativeLayout rela_NhanThongBao;
    public static TextView txt_NhanThongBao;
    private boolean isNew = true;
    SessionLanguage sessionLanguage;
    SessionUser sessionUser;
    SessionNotification sessionNotification;
    private LinearLayout linear_Main;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragment__cai_dat__nav, container, false);
        initView(v);


        sessionUser = new SessionUser(getContext());
        sessionLanguage = new SessionLanguage(getContext());
        sessionNotification = new SessionNotification(getContext());

        initSpinner();

        if (sessionNotification.getKeyNotification() == Common.KEY_NOTIFICATION.KEY_ON) {
            txt_NhanThongBao.setText("ON");
        } else {
            txt_NhanThongBao.setText("OFF");
        }

        return v;

    }

    private void initView(View v) {
        linear_Main = v.findViewById(R.id.linear_Main);
        spinner = v.findViewById(R.id.spinner);
        txt_NhanThongBao = v.findViewById(R.id.txt_NhanThongBao);
        rela_NhanThongBao = v.findViewById(R.id.rela_NhanThongBao);
        rela_NhanThongBao.setOnClickListener(this);
    }

    private void initSpinner() {
        spinnerLanguageList = new ArrayList<>();
        spinnerLanguageList.add(new SpinnerLanguage("English", R.drawable.usa64));
        spinnerLanguageList.add(new SpinnerLanguage("Vietnamese", R.drawable.vietnam64));
        adapter_spinner_language = new Adapter_Spinner_Language(getContext(), R.layout.item_spinner_language, spinnerLanguageList);
        spinner.setAdapter(adapter_spinner_language);


        int key_language = sessionLanguage.getKeyLanguage();
        switch (key_language) {
            case Common.KEY_LANGUAGE.KEY_VN: {
                isNew = true;
                spinner.setSelection(1);
                break;
            }
            case Common.KEY_LANGUAGE.KEY_EN: {
                isNew = true;
                spinner.setSelection(0);
                break;
            }
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0: {
                        if (isNew) {
                            isNew = false;
                            break;
                        }
                        sessionLanguage.createSessionLanguage(Common.KEY_LANGUAGE.KEY_EN);
                        setLocale("en");
                        //Toast.makeText(getContext(), "Vietnamese", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 1: {
                        if (isNew) {
                            isNew = false;
                            break;
                        }
                        sessionLanguage.createSessionLanguage(Common.KEY_LANGUAGE.KEY_VN);
                        setLocale("vi");
                        //Toast.makeText(getContext(), "English", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }


    @SuppressWarnings("deprecation")
    public void setLocale(String localeCode) {
        String localeCodeLowerCase = localeCode.toLowerCase();

        Resources resources = getContext().getApplicationContext().getResources();
        Configuration overrideConfiguration = resources.getConfiguration();
        Locale overrideLocale = new Locale(localeCodeLowerCase);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            overrideConfiguration.setLocale(overrideLocale);
        } else {
            overrideConfiguration.locale = overrideLocale;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getContext().getApplicationContext().createConfigurationContext(overrideConfiguration);
        } else {
            resources.updateConfiguration(overrideConfiguration, null);
        }

        MainActivity.isFirst = true;
        Intent refresh = new Intent(getContext(), SplashScreen.class);
        refresh.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(refresh);
    }

    /*public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        LocaleUtils.setLocale(myLocale);

        Resources res = getContext().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;

        res.updateConfiguration(conf, dm);
        MainActivity.isFirst = true;
        Intent refresh = new Intent(getContext(), SplashScreen.class);
        refresh.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(refresh);
    }*/

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.rela_NhanThongBao: {

                if (!IsConnect.getInstance().isConnect()) {
                    Snackbar.make(linear_Main, getContext().getResources().getString(R.string.khongcoketnoiinternet), Snackbar.LENGTH_LONG).show();
                    return;
                }

                if (sessionUser.getUserDTO().getId() == null || sessionUser.getUserDTO().getId().isEmpty()) {
                    Toast.makeText(getContext(), getResources().getString(R.string.banchuadangnhap), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (sessionNotification.getKeyNotification() == Common.KEY_NOTIFICATION.KEY_ON) {
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_THANHVIENS).child(sessionUser.getUserDTO().getId()).child(Common.KEY_CODE.NODE_FCMTOKEN).setValue("")
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    txt_NhanThongBao.setText("OFF");
                                    sessionNotification.createSessionNotificatione(Common.KEY_NOTIFICATION.KEY_OFF);
                                }
                            });
                } else {
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_THANHVIENS).child(sessionUser.getUserDTO().getId()).child(Common.KEY_CODE.NODE_FCMTOKEN).setValue(sessionUser.getUserDTO().getFcmToken())
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    txt_NhanThongBao.setText("ON");
                                    sessionNotification.createSessionNotificatione(Common.KEY_NOTIFICATION.KEY_ON);
                                }
                            });
                }
                break;
            }
        }
    }
}
