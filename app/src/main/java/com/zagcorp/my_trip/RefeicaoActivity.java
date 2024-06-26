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
import java.util.List;

public class RefeicaoActivity extends AppCompatActivity {
    private FloatingActionButton btnVoltar;
    private Button btnContinuar, btnPularEtapa;
    private EditText edtCusto, edtDiarias,edtViajantes;
    private Long refeicaoId;

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
        Boolean editar = it.getBooleanExtra("editar", false);

        btnVoltar = findViewById(R.id.btnVoltar);
        btnContinuar = findViewById(R.id.btnContinuar);
        btnPularEtapa = findViewById(R.id.btnPularEtapa);
        edtCusto = findViewById(R.id.edtCusto);
        edtDiarias = findViewById(R.id.edtDiarias);
        edtViajantes = findViewById(R.id.edtViajantes);

        if(editar) {
            try {
                RefeicaoDAO dao = new RefeicaoDAO(getApplicationContext());
                List<RefeicaoModel> refeicoes = dao.buscaRefeicao("" + idViagem);

                for (RefeicaoModel refeicao : refeicoes) {
                    edtCusto.setText(String.valueOf(refeicao.getCusto_estimado()));
                    edtDiarias.setText(String.valueOf(refeicao.getQtd_refeicao()));
                    edtViajantes.setText(String.valueOf(refeicao.getQtd_viajantes()));
                    refeicaoId = refeicao.getId();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RefeicaoActivity.this);
                builder.setTitle("Confirmar Retorno");
                builder.setMessage("Tem certeza que deseja retornar?");

                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent it = new Intent(RefeicaoActivity.this, HomeActivity.class);
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

                int myColor = getResources().getColor(R.color.primary);

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(myColor);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(myColor);
            }
        });

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String custo = edtCusto.getText().toString();

                if (custo.isEmpty()) {
                    Toast.makeText(RefeicaoActivity.this, "Preencha o campo custo estimado", Toast.LENGTH_SHORT).show();
                    return;
                }

                String qtdDiarias = edtDiarias.getText().toString();

                if (qtdDiarias.isEmpty()) {
                    Toast.makeText(RefeicaoActivity.this, "Preencha o campo refeições diárias", Toast.LENGTH_SHORT).show();
                    return;
                }

                String qtdViajantes = edtViajantes.getText().toString();

                if (qtdViajantes.isEmpty()) {
                    Toast.makeText(RefeicaoActivity.this, "Preencha o campo quantidade de viajantes", Toast.LENGTH_SHORT).show();
                    return;
                }

                RefeicaoDAO dao = new RefeicaoDAO(getApplicationContext());

                RefeicaoModel refeicao = new RefeicaoModel();

                refeicao.setViagem(idViagem);
                refeicao.setCusto_estimado(Double.parseDouble(custo));
                refeicao.setQtd_refeicao(Integer.parseInt(qtdDiarias));
                refeicao.setQtd_viajantes(Integer.parseInt(qtdViajantes));

                try {
                    if(editar) {
                        RefeicaoModel refeicaoEdit = new RefeicaoModel();
                        refeicaoEdit.setId(refeicaoId);
                        refeicaoEdit.setViagem(idViagem);
                        refeicaoEdit.setCusto_estimado(Double.parseDouble(custo));
                        refeicaoEdit.setQtd_refeicao(Integer.parseInt(qtdDiarias));
                        refeicaoEdit.setQtd_viajantes(Integer.parseInt(qtdViajantes));
                        dao.update(refeicaoEdit);

                        Toast.makeText(RefeicaoActivity.this, "Refeicao atualizada com sucesso", Toast.LENGTH_SHORT).show();
                        Intent it = new Intent(RefeicaoActivity.this, HomeActivity.class);
                        startActivity(it);
                    } else {
                        dao.Insert(refeicao);
                        Toast.makeText(RefeicaoActivity.this, "Refeicao cadastrada com sucesso", Toast.LENGTH_SHORT).show();
                        Intent it = new Intent(RefeicaoActivity.this, HomeActivity.class);
                        startActivity(it);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnPularEtapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent it = new Intent(RefeicaoActivity.this, HomeActivity.class);
                    it.putExtra("viagemId", idViagem);
                    if(editar) {
                        RefeicaoDAO dao = new RefeicaoDAO(getApplicationContext());
                        dao.deleteByViagemId(idViagem);
                        it.putExtra("editar", true);
                    }
                    startActivity(it);
                    Toast.makeText(RefeicaoActivity.this, "Etapa pulada com sucesso", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
