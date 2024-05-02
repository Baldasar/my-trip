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

public class RegisterActivity extends AppCompatActivity {
    private Button btnVoltar, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnVoltar = findViewById(R.id.btnVoltar);
        btnRegister = findViewById(R.id.btnRegister);

        Intent it = new Intent(RegisterActivity.this, MainActivity.class);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(it);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = ((TextInputEditText) findViewById(R.id.registration_name_editText)).getText().toString();

                if(nome.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Campo nome é obrigatório", Toast.LENGTH_SHORT).show();
                    return;
                }

                String email = ((TextInputEditText) findViewById(R.id.registration_email_editText)).getText().toString();

                if(email.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Campo email é obrigatório", Toast.LENGTH_SHORT).show();
                    return;
                }

                String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

                if(!email.matches(regex)){
                    Toast.makeText(RegisterActivity.this, "Email inválido", Toast.LENGTH_SHORT).show();
                    return;
                }

                String senha = ((TextInputEditText) findViewById(R.id.registration_password_editText)).getText().toString();

                if(senha.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Campo senha é obrigatório", Toast.LENGTH_SHORT).show();
                    return;
                }

                String confirmarSenha = ((TextInputEditText) findViewById(R.id.registration_confirm_password_editText)).getText().toString();

                if(confirmarSenha.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Campo confirmar senha é obrigatório", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!senha.equals(confirmarSenha)){
                    Toast.makeText(RegisterActivity.this, "Senhas não conferem", Toast.LENGTH_SHORT).show();
                    return;
                }

                UsuarioDAO usuarioDAO = new UsuarioDAO(getApplicationContext());
                UsuarioModel usuarioModel = new UsuarioModel();

                usuarioModel.setNomeCompleto(nome);
                usuarioModel.setEmail(email);
                usuarioModel.setSenha(senha);

                try {
                    usuarioDAO.Insert(usuarioModel);
                    Toast.makeText(getApplicationContext(), "Usuário cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                    startActivity(it);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
