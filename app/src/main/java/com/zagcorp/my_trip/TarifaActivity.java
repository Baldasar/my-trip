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

import java.sql.SQLException;

public class TarifaActivity extends AppCompatActivity {
    private FloatingActionButton btnVoltar;
    private Button btnContinuar, btnPularEtapa;
    private EditText edtCusto, edtQtdPessoa, edtValorVeiculo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarifa);

        Intent it = getIntent();
        Long idViagem = it.getLongExtra("viagemId", 0);

        btnVoltar = findViewById(R.id.btnVoltar);
        btnContinuar = findViewById(R.id.btnContinuar);
        btnPularEtapa = findViewById(R.id.btnPularEtapa);
        edtCusto = findViewById(R.id.edtCusto);
        edtQtdPessoa = findViewById(R.id.edtQtdPessoa);
        edtValorVeiculo = findViewById(R.id.edtValorVeiculo);

        preencherCampos(idViagem);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TarifaActivity.this);
                builder.setTitle("Confirmar Retorno");
                builder.setMessage("Tem certeza que deseja retornar a tela de gasolina?");

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
                int totalTarifa = 0;

                if (custo.isEmpty()) {
                    Toast.makeText(TarifaActivity.this, "Preencha o campo custo estimado por pessoa", Toast.LENGTH_SHORT).show();
                    return;
                }

                String qtdPessoa = edtQtdPessoa.getText().toString();

                if (qtdPessoa.isEmpty()) {
                    Toast.makeText(TarifaActivity.this, "Preencha o campo quantidade pessoas", Toast.LENGTH_SHORT).show();
                    return;
                }

                String valorVeiculo = edtValorVeiculo.getText().toString();

                if (valorVeiculo.isEmpty()) {
                    Toast.makeText(TarifaActivity.this, "Preencha o campo valor do veículo", Toast.LENGTH_SHORT).show();
                    return;
                }

                TarifaDAO dao = new TarifaDAO(getApplicationContext());

                try {
                    totalTarifa = dao.verificaTarifa(idViagem);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                TarifaModel tarifa = new TarifaModel();

                tarifa.setViagem(1);
                tarifa.setCusto_pessoa(Double.parseDouble(custo));
                tarifa.setQtd_pessoa(Integer.parseInt(qtdPessoa));
                tarifa.setCusto_veiculo(Double.parseDouble(valorVeiculo));

                try {
                    if (totalTarifa > 0) {
                        dao.Edit(tarifa, idViagem);
                        Toast.makeText(TarifaActivity.this, "Tarifa editada com sucesso", Toast.LENGTH_SHORT).show();
                    } else {
                        dao.Insert(tarifa);
                        Toast.makeText(TarifaActivity.this, "Tarifa cadastrada com sucesso", Toast.LENGTH_SHORT).show();
                    }
                    Intent it = new Intent(TarifaActivity.this, HospedagemActivity.class);
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
                excluirDados(idViagem);
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
    private void excluirDados(long idViagem) {
        TarifaDAO dao = new TarifaDAO(getApplicationContext());
        try {
            dao.deleteByViagemId(idViagem);  // Excluir atividades existentes
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void preencherCampos(long idViagem) {
        TarifaDAO dao = new TarifaDAO(getApplicationContext());
        try {
            TarifaModel tarifa = dao.buscaTarifaPorIdViagem(idViagem);
            if (tarifa != null) {
                edtCusto.setText(String.valueOf(tarifa.getCusto_pessoa()));
                edtQtdPessoa.setText(String.valueOf(tarifa.getQtd_pessoa()));
                edtValorVeiculo.setText(String.valueOf(tarifa.getCusto_veiculo()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
