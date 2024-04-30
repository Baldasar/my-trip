package com.zagcorp.my_trip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.zagcorp.my_trip.database.dao.UsuarioDAO;
import com.zagcorp.my_trip.database.model.UsuarioModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button btnRegister, btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);

        Intent it = new Intent(MainActivity.this, RegisterActivity.class);

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

                if(email.isEmpty()){
                    ((TextInputEditText) findViewById(R.id.login_email_editText)).setError("Campo obrigatório");
                    return;
                }

                String password = ((TextInputEditText) findViewById(R.id.login_password_editText)).getText().toString();

                if(password.isEmpty()){
                    ((TextInputEditText) findViewById(R.id.login_password_editText)).setError("Campo obrigatório");
                    return;
                }

                UsuarioDAO dao = new UsuarioDAO(MainActivity.this);
                try {
                    List<UsuarioModel> usuario = dao.Login(email, password);
                    if (!usuario.isEmpty()) {
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