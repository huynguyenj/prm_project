package com.nguyenlegiahuy.prm_project_v2.interceptor;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.nguyenlegiahuy.prm_project_v2.LoginActivity;
import com.nguyenlegiahuy.prm_project_v2.utils.SessionManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private final SessionManager sessionManager;
    private static boolean isRedirecting = false;

    public AuthInterceptor(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        String token = sessionManager.getToken();

        Request.Builder builder = original.newBuilder();
        if (token != null && !token.isEmpty()) {
            builder.header("Authorization", "Bearer " + token);
        }

        Response response = chain.proceed(builder.build());

        if (response.code() == 401 && !isRedirecting) {
            isRedirecting = true;

            sessionManager.clearSession();

            new Handler(Looper.getMainLooper()).post(() -> {
                Intent intent = new Intent(SessionManager.getAppContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                SessionManager.getAppContext().startActivity(intent);
            });
        }

        return response;
    }
}
