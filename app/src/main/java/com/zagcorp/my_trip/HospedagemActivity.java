package com.zagcorp.my_trip;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zagcorp.my_trip.database.dao.HospedagemDAO;
import com.zagcorp.my_trip.database.model.HospedagemModel;

public class HospedagemActivity extends AppCompatActivity {
    private FloatingActionButton btnVoltar;
    private Button btnContinuar, btnPularEtapa;
    private EditText edtCusto, edtQtdNoite, edtNumQuarto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospedagem);

        Intent it = getIntent();
        Integer idViagem = it.getIntExtra("viagemId", 0);

        btnVoltar = findViewById(R.id.btnVoltar);
        btnContinuar = findViewById(R.id.btnContinuar);
        btnPularEtapa = findViewById(R.id.btnPularEtapa);
        edtCusto = findViewById(R.id.edtCusto);
        edtQtdNoite = findViewById(R.id.edtQtdNoite);
        edtNumQuarto = findViewById(R.id.edtNumQuarto);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HospedagemActivity.this);
                builder.setTitle("Confirmar Saída");
                builder.setMessage("Tem certeza que deseja sair?");

                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent it = new Intent(HospedagemActivity.this, HomeActivity.class);
                        startActivity(it);
                    }
                });

                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String custo = edtCusto.getText().toString();

                if (custo.isEmpty()) {
                    Toast.makeText(HospedagemActivity.this, "Preencha o campo custo por noite", Toast.LENGTH_SHORT).show();
                    return;
                }

                String qtdNoite = edtQtdNoite.getText().toString();

                if (qtdNoite.isEmpty()) {
                    Toast.makeText(HospedagemActivity.this, "Preencha o campo quantidade de noites", Toast.LENGTH_SHORT).show();
                    return;
                }

                String numQuarto = edtNumQuarto.getText().toString();

                if (numQuarto.isEmpty()) {
                    Toast.makeText(HospedagemActivity.this, "Preencha o campo número de quartos", Toast.LENGTH_SHORT).show();
                    return;
                }

                HospedagemDAO dao = new HospedagemDAO(getApplicationContext());
                HospedagemModel hospedagem = new HospedagemModel();

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(HospedagemActivity.this);

                hospedagem.setViagem(idViagem);
                hospedagem.setCusto_noite(Double.parseDouble(custo));
                hospedagem.setQtd_noite(Integer.parseInt(qtdNoite));
                hospedagem.setQtd_quarto(Integer.parseInt(numQuarto));

                try {
                    dao.Insert(hospedagem);
                    Toast.makeText(HospedagemActivity.this, "Hospedagem cadastrada com sucesso", Toast.LENGTH_SHORT).show();
                    Intent it = new Intent(HospedagemActivity.this, HospedagemActivity.class);
                    it.putExtra("viagemId", idViagem);
                    startActivity(it);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnPularEtapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent it = new Intent(HospedagemActivity.this, HomeActivity.class);
                    it.putExtra("viagemId", idViagem);
                    startActivity(it);
                    Toast.makeText(HospedagemActivity.this, "Etapa pulada com sucesso", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
