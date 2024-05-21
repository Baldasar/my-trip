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
import com.zagcorp.my_trip.database.dao.TarifaDAO;
import com.zagcorp.my_trip.database.model.TarifaModel;

public class TarifaActivity extends AppCompatActivity {
    private FloatingActionButton btnVoltar;
    private Button btnContinuar, btnPularEtapa;
    private EditText edtCusto, edtQtdPessoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarifa);

        Intent it = getIntent();
        Integer idViagem = it.getIntExtra("viagemId", 0);

        btnVoltar = findViewById(R.id.btnVoltar);
        btnContinuar = findViewById(R.id.btnContinuar);
        btnPularEtapa = findViewById(R.id.btnPularEtapa);
        edtCusto = findViewById(R.id.edtCusto);
        edtQtdPessoa = findViewById(R.id.edtQtdPessoa);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TarifaActivity.this);
                builder.setTitle("Confirmar Saída");
                builder.setMessage("Tem certeza que deseja sair?");

                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent it = new Intent(TarifaActivity.this, GasolinaActivity.class);
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
                    Toast.makeText(TarifaActivity.this, "Preencha o campo custo estimado por pessoa", Toast.LENGTH_SHORT).show();
                    return;
                }

                String qtdPessoa = edtQtdPessoa.getText().toString();

                if (qtdPessoa.isEmpty()) {
                    Toast.makeText(TarifaActivity.this, "Preencha o campo quantidade pessoas", Toast.LENGTH_SHORT).show();
                    return;
                }

                TarifaDAO dao = new TarifaDAO(getApplicationContext());
                TarifaModel tarifa = new TarifaModel();

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(TarifaActivity.this);

                tarifa.setViagem(idViagem);
                tarifa.setCusto_pessoa(Double.parseDouble(custo));
                tarifa.setQtd_pessoa(Integer.parseInt(qtdPessoa));

                try {
                    dao.Insert(tarifa);
                    Toast.makeText(TarifaActivity.this, "Tarifa cadastrada com sucesso", Toast.LENGTH_SHORT).show();
                    Intent it = new Intent(TarifaActivity.this, TarifaActivity.class);
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
                    Intent it = new Intent(TarifaActivity.this, HospedagemActivity.class);
                    it.putExtra("viagemId", idViagem);
                    startActivity(it);
                    Toast.makeText(TarifaActivity.this, "Etapa pulada com sucesso", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
