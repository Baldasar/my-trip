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
import com.zagcorp.my_trip.database.dao.GasolinaDAO;
import com.zagcorp.my_trip.database.model.GasolinaModel;

import java.util.List;

public class GasolinaActivity extends AppCompatActivity {
    private FloatingActionButton btnVoltar;
    private Button btnContinuar, btnPularEtapa;
    private EditText edtTotalKM, edtMediaKM, edtCustoMedio, edtTotalVeiculo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasolina);

        Intent it = getIntent();
        Long idViagem = it.getLongExtra("viagemId", 0);
        Boolean editar = it.getBooleanExtra("editar", false);

        btnVoltar = findViewById(R.id.btnVoltar);
        btnContinuar = findViewById(R.id.btnContinuar);
        btnPularEtapa = findViewById(R.id.btnPularEtapa);
        edtTotalKM = findViewById(R.id.edtTotalKM);
        edtMediaKM = findViewById(R.id.edtMediaKM);
        edtCustoMedio = findViewById(R.id.edtCustoMedio);
        edtTotalVeiculo = findViewById(R.id.edtTotalVeiculo);

        if(editar) {
            try {
                GasolinaDAO dao = new GasolinaDAO(getApplicationContext());
                List<GasolinaModel> gasolinas = dao.buscaGasolinaByIdViagem(idViagem);

                for ( GasolinaModel gasolina : gasolinas ) {
                    edtTotalKM.setText(String.valueOf(gasolina.getKm()));
                    edtMediaKM.setText(String.valueOf(gasolina.getKm_litro()));
                    edtCustoMedio.setText(String.valueOf(gasolina.getCusto_medio()));
                    edtTotalVeiculo.setText(String.valueOf(gasolina.getQntd_veiculo()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GasolinaActivity.this);
                builder.setTitle("Confirmar Saída");
                builder.setMessage("Tem certeza que deseja sair?");

                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent it = new Intent(GasolinaActivity.this, HomeActivity.class);
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
                String totalKM = edtTotalKM.getText().toString();

                if (totalKM.isEmpty()) {
                    Toast.makeText(GasolinaActivity.this, "Preencha o campo total estimado de quilômetros", Toast.LENGTH_SHORT).show();
                    return;
                }

                String mediaKM = edtMediaKM.getText().toString();

                if (mediaKM.isEmpty()) {
                    Toast.makeText(GasolinaActivity.this, "Preencha o campo média de quilômetros por litro", Toast.LENGTH_SHORT).show();
                    return;
                }

                String custoMedio = edtCustoMedio.getText().toString();

                if (custoMedio.isEmpty()) {
                    Toast.makeText(GasolinaActivity.this, "Selecione o custo médio por litro", Toast.LENGTH_SHORT).show();
                    return;
                }

                String totalVeiculo = edtTotalVeiculo.getText().toString();

                if (totalVeiculo.isEmpty()) {
                    Toast.makeText(GasolinaActivity.this, "Selecione o total de veículos", Toast.LENGTH_SHORT).show();
                    return;
                }

                GasolinaDAO dao = new GasolinaDAO(getApplicationContext());
                GasolinaModel gasolina = new GasolinaModel();

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(GasolinaActivity.this);

                gasolina.setViagem(idViagem);
                gasolina.setKm(Double.parseDouble(totalKM));
                gasolina.setKm_litro(Double.parseDouble(mediaKM));
                gasolina.setCusto_medio(Double.parseDouble(custoMedio));
                gasolina.setQntd_veiculo(Double.parseDouble(totalVeiculo));

                try {
                    if(editar) {
                        GasolinaModel gasolinaEdit = new GasolinaModel();
                        gasolinaEdit.setId(idViagem);
                        gasolinaEdit.setViagem(idViagem);
                        gasolinaEdit.setKm(Double.parseDouble(totalKM));
                        gasolinaEdit.setKm_litro(Double.parseDouble(mediaKM));
                        gasolinaEdit.setCusto_medio(Double.parseDouble(custoMedio));
                        gasolinaEdit.setQntd_veiculo(Double.parseDouble(totalVeiculo));

                        int rowsAffected = dao.update(gasolinaEdit);

                        Toast.makeText(GasolinaActivity.this, "Gasolina atualizada com sucesso", Toast.LENGTH_SHORT).show();
                        Intent it = new Intent(GasolinaActivity.this, TarifaActivity.class);
                        it.putExtra("viagemId", idViagem);
                        it.putExtra("editar", true);
                        startActivity(it);
                    } else {
                        dao.Insert(gasolina);
                        Toast.makeText(GasolinaActivity.this, "Gasolina cadastrada com sucesso", Toast.LENGTH_SHORT).show();
                        Intent it = new Intent(GasolinaActivity.this, TarifaActivity.class);
                        it.putExtra("viagemId", idViagem);
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
                    Intent it = new Intent(GasolinaActivity.this, TarifaActivity.class);
                    it.putExtra("viagemId", idViagem);
                    if(editar) {
                        GasolinaDAO dao = new GasolinaDAO(getApplicationContext());
                        dao.deleteByViagemId(idViagem);
                        it.putExtra("editar", true);
                    }
                    startActivity(it);
                    Toast.makeText(GasolinaActivity.this, "Etapa pulada com sucesso", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
