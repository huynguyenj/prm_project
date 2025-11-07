package com.nguyenlegiahuy.prm_project_v2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nguyenlegiahuy.prm_project_v2.activities.admin.AdminMainActivity;
import com.nguyenlegiahuy.prm_project_v2.activities.dealer_staff.DealerStaffMainActivity;
import com.nguyenlegiahuy.prm_project_v2.activities.evm_staff.EvmStaffMainActivity;
import com.nguyenlegiahuy.prm_project_v2.api.ApiClient;
import com.nguyenlegiahuy.prm_project_v2.api.ApiService;
import com.nguyenlegiahuy.prm_project_v2.models.authentication.LoginRequest;
import com.nguyenlegiahuy.prm_project_v2.models.authentication.LoginResponse;
import com.nguyenlegiahuy.prm_project_v2.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        apiService = ApiClient.getClient().create(ApiService.class);
        sessionManager = new SessionManager(this);

        // Nếu đã có token, bỏ qua màn login
        if (sessionManager.getToken() != null) {
            goToHome();
            return;
        }

        btnLogin.setOnClickListener(v -> login());
    }

    private void login() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        btnLogin.setEnabled(false);
        btnLogin.setText("Signing in...");

        LoginRequest request = new LoginRequest(email, password);

        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                btnLogin.setEnabled(true);
                btnLogin.setText("Sign In");

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse res = response.body();

                    if (res.getStatusCode() == 200 && res.getData() != null) {
                        sessionManager.saveUserSession(
                                res.getData().getAccessToken(),
                                res.getData().getUserId(),
                                res.getData().getAgencyId(),
                                res.getData().getRole()
                        );
                        new SessionManager(LoginActivity.this);
                        Toast.makeText(LoginActivity.this, "Login success!", Toast.LENGTH_SHORT).show();
                        goToHome();
                    } else {
                        Toast.makeText(LoginActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                btnLogin.setEnabled(true);
                btnLogin.setText("Sign In");
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToHome() {
        String[] roles = sessionManager.getRoles();

        Intent intent;
        if (roles.length > 0) {
            String role = roles[0].toLowerCase(); // Use first role (or enhance logic later)

            switch (role) {
                case "admin":
                    intent = new Intent(this, AdminMainActivity.class);
                    break;

                case "evm staff":
                    intent = new Intent(this, EvmStaffMainActivity.class);
                    break;

                case "dealer manager":
                    intent = new Intent(this, DealerStaffMainActivity.class);
                    break;

                case "dealer staff":
                    intent = new Intent(this, DealerStaffMainActivity.class);
                    break;

                default:
                    Toast.makeText(this, "Role not supported: " + role, Toast.LENGTH_LONG).show();
                    return;
            }
        } else {
            Toast.makeText(this, "No role assigned", Toast.LENGTH_LONG).show();
            return;
        }

        startActivity(intent);
        finish();
    }
}
