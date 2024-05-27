package com.zagcorp.my_trip;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zagcorp.my_trip.database.dao.RefeicaoDAO;
import com.zagcorp.my_trip.database.model.RefeicaoModel;

import java.sql.SQLException;

public class RefeicaoActivity extends AppCompatActivity {
    private FloatingActionButton btnVoltar;
    private Button btnContinuar, btnPularEtapa;
    private EditText edtCusto, edtDiarias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refeicao);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.primary));
        window.setNavigationBarColor(getResources().getColor(R.color.primary));

        Intent it = getIntent();
        Long idViagem = it.getLongExtra("viagemId", 0);

        btnVoltar = findViewById(R.id.btnVoltar);
        btnContinuar = findViewById(R.id.btnContinuar);
        btnPularEtapa = findViewById(R.id.btnPularEtapa);
        edtCusto = findViewById(R.id.edtCusto);
        edtDiarias = findViewById(R.id.edtDiarias);

        preencherCampos(idViagem);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RefeicaoActivity.this);
                builder.setTitle("Confirmar Retorno");
                builder.setMessage("Tem certeza que deseja retornar a tela de hospedagem?");

                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent it = new Intent(RefeicaoActivity.this, HospedagemActivity.class);
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
                int totalRefeicao = 0;

                if (custo.isEmpty()) {
                    Toast.makeText(RefeicaoActivity.this, "Preencha o campo custo estimado", Toast.LENGTH_SHORT).show();
                    return;
                }

                String qtdDiarias = edtDiarias.getText().toString();

                if (qtdDiarias.isEmpty()) {
                    Toast.makeText(RefeicaoActivity.this, "Preencha o campo refeições diárias", Toast.LENGTH_SHORT).show();
                    return;
                }

                RefeicaoDAO dao = new RefeicaoDAO(getApplicationContext());

                try {
                    totalRefeicao = dao.verificaRefeicao(idViagem);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                RefeicaoModel refeicao = new RefeicaoModel();

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RefeicaoActivity.this);

                refeicao.setViagem(idViagem);
                refeicao.setCusto_estimado(Double.parseDouble(custo));
                refeicao.setQtd_refeicao(Integer.parseInt(qtdDiarias));

                try {
                    if (totalRefeicao > 0) {
                        dao.Edit(refeicao, idViagem);
                        Toast.makeText(RefeicaoActivity.this, "Refeicao editada com sucesso", Toast.LENGTH_SHORT).show();
                    } else {
                        dao.Insert(refeicao);
                        Toast.makeText(RefeicaoActivity.this, "Refeicao cadastrada com sucesso", Toast.LENGTH_SHORT).show();
                    }
                    Intent it = new Intent(RefeicaoActivity.this, EntretenimentoActivity.class);
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
                    Intent it = new Intent(RefeicaoActivity.this, EntretenimentoActivity.class);
                    it.putExtra("viagemId", idViagem);
                    startActivity(it);
                    Toast.makeText(RefeicaoActivity.this, "Etapa pulada com sucesso", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void excluirDados(long idViagem) {
        RefeicaoDAO dao = new RefeicaoDAO(getApplicationContext());
        try {
            dao.deleteByViagemId(idViagem);  // Excluir atividades existentes
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void preencherCampos(long idViagem) {
        RefeicaoDAO dao = new RefeicaoDAO(getApplicationContext());
        try {
            RefeicaoModel refeicao = dao.buscaRefeicaoPorIdViagem(idViagem);
            if (refeicao != null) {
                edtCusto.setText(String.valueOf(refeicao.getCusto_estimado()));
                edtDiarias.setText(String.valueOf(refeicao.getQtd_refeicao()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
