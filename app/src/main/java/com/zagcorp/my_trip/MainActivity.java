package com.zagcorp.my_trip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.zagcorp.my_trip.database.dao.UsuarioDAO;
import com.zagcorp.my_trip.database.model.UsuarioModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button btnRegister, btnLogin;
    private CheckBox remember_me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        remember_me = findViewById(R.id.remember_me);

        Intent it = new Intent(MainActivity.this, RegisterActivity.class);
        Intent itHome = new Intent(MainActivity.this, HomeActivity.class);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        if (sharedPreferences.contains("userId")) {
            startActivity(itHome);
            finish();
        }

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(it);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ((TextInputEditText) findViewById(R.id.login_email_editText)).getText().toString();

                if (email.isEmpty()) {
                    ((TextInputEditText) findViewById(R.id.login_email_editText)).setError("Campo obrigatório");
                    return;
                }

                String password = ((TextInputEditText) findViewById(R.id.login_password_editText)).getText().toString();

                if (password.isEmpty()) {
                    ((TextInputEditText) findViewById(R.id.login_password_editText)).setError("Campo obrigatório");
                    return;
                }

                UsuarioDAO dao = new UsuarioDAO(MainActivity.this);
                try {
                    List<UsuarioModel> usuario = dao.Login(email, password);

                    if (!usuario.isEmpty()) {
                        Long userId = usuario.get(0).getId();
                        int userIdInt = Long.valueOf(userId).intValue();

                        if (remember_me.isChecked()) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("userId", userIdInt);
                            editor.apply();
                        }

                        startActivity(itHome);
                        Toast.makeText(MainActivity.this, "Login efetuado com sucesso", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Usuário ou senha inválidos", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
