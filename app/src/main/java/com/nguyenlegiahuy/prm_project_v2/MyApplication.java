package com.nguyenlegiahuy.prm_project_v2;

import android.app.Application;

import com.nguyenlegiahuy.prm_project_v2.utils.SessionManager;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Khởi tạo SessionManager toàn cục ngay khi app start
        new SessionManager(this);
    }
}
