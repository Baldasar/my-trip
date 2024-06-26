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
import com.zagcorp.my_trip.database.dao.HospedagemDAO;
import com.zagcorp.my_trip.database.model.HospedagemModel;

import java.util.List;

public class HospedagemActivity extends AppCompatActivity {
    private FloatingActionButton btnVoltar;
    private Button btnContinuar, btnPularEtapa;
    private EditText edtCusto, edtQtdNoite, edtNumQuarto;
    private Long hospedagemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospedagem);

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
        edtQtdNoite = findViewById(R.id.edtQtdNoite);
        edtNumQuarto = findViewById(R.id.edtNumQuarto);

        if (editar) {
            try {
                HospedagemDAO dao = new HospedagemDAO(getApplicationContext());
                List<HospedagemModel> Hospdedagens = dao.buscaHospedagem(""  + idViagem);

                for (HospedagemModel hospedagem : Hospdedagens) {
                    edtCusto.setText(String.valueOf(hospedagem.getCusto_noite()));
                    edtQtdNoite.setText(String.valueOf(hospedagem.getQtd_noite()));
                    edtNumQuarto.setText(String.valueOf(hospedagem.getQtd_quarto()));
                    hospedagemId = hospedagem.getId();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
                    if(editar) {
                        HospedagemModel hospedagemEdit = new HospedagemModel();
                        hospedagemEdit.setViagem(idViagem);
                        hospedagemEdit.setId(hospedagemId);
                        hospedagemEdit.setCusto_noite(Double.parseDouble(custo));
                        hospedagemEdit.setQtd_noite(Integer.parseInt(qtdNoite));
                        hospedagemEdit.setQtd_quarto(Integer.parseInt(numQuarto));
                        dao.update(hospedagemEdit);

                        Toast.makeText(HospedagemActivity.this, "Hospedagem atualizada com sucesso", Toast.LENGTH_SHORT).show();
                        Intent it = new Intent(HospedagemActivity.this, RefeicaoActivity.class);
                        it.putExtra("viagemId", idViagem);
                        it.putExtra("editar", true);
                        startActivity(it);
                    } else {
                        dao.Insert(hospedagem);
                        Toast.makeText(HospedagemActivity.this, "Hospedagem cadastrada com sucesso", Toast.LENGTH_SHORT).show();
                        Intent it = new Intent(HospedagemActivity.this, RefeicaoActivity.class);
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
                    Intent it = new Intent(HospedagemActivity.this, RefeicaoActivity.class);
                    it.putExtra("viagemId", idViagem);
                    if(editar) {
                        HospedagemDAO dao = new HospedagemDAO(getApplicationContext());
                        dao.deleteByViagemId(idViagem);
                        it.putExtra("editar", true);
                    }
                    startActivity(it);
                    Toast.makeText(HospedagemActivity.this, "Etapa pulada com sucesso", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
