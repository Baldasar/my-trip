package com.zagcorp.my_trip;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.zagcorp.my_trip.database.dao.EntretenimentoDAO;
import com.zagcorp.my_trip.database.dao.GasolinaDAO;
import com.zagcorp.my_trip.database.dao.HospedagemDAO;
import com.zagcorp.my_trip.database.dao.RefeicaoDAO;
import com.zagcorp.my_trip.database.dao.TarifaDAO;
import com.zagcorp.my_trip.database.dao.ViagemDAO;
import com.zagcorp.my_trip.database.model.ViagemModel;

import java.sql.SQLException;
import java.util.List;

public class LocalTripActivity extends AppCompatActivity {
    private Button btnContinuar;
    private FloatingActionButton btnVoltar;
    private EditText edtTitle, edtLocalTrip;
    private Spinner spinnerDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_trip);

        Intent it = getIntent();
        Long idViagem = it.getLongExtra("viagemId", 0);

        btnVoltar = findViewById(R.id.btnVoltar);
        btnContinuar = findViewById(R.id.btnContinuar);
        edtTitle = findViewById(R.id.edtTitle);
        edtLocalTrip = findViewById(R.id.edtLocalTrip);
        spinnerDuration = findViewById(R.id.spinnerDuration);

        String[] durations = {"1 dia", "2 dias", "3 dias", "4 dias", "5 dias", "1 semana", "2 semanas"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, durations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDuration.setAdapter(adapter);

        preencherCampos(idViagem);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LocalTripActivity.this);
                builder.setTitle("Confirmar Saída");
                builder.setMessage("Tem certeza que deseja sair?");

                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        excluirDados(idViagem);
                        Intent it = new Intent(LocalTripActivity.this, HomeActivity.class);
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
                String title = edtTitle.getText().toString();
                int totalViagens = 0;

                if (title.isEmpty()) {
                    Toast.makeText(LocalTripActivity.this, "Preencha o campo título", Toast.LENGTH_SHORT).show();
                    return;
                }

                String localTrip = edtLocalTrip.getText().toString();

                if (localTrip.isEmpty()) {
                    Toast.makeText(LocalTripActivity.this, "Preencha o campo local da viagem", Toast.LENGTH_SHORT).show();
                    return;
                }

                String duration = spinnerDuration.getSelectedItem().toString();

                if (duration.isEmpty()) {
                    Toast.makeText(LocalTripActivity.this, "Selecione a duração da viagem", Toast.LENGTH_SHORT).show();
                    return;
                }

                ViagemDAO dao = new ViagemDAO(getApplicationContext());

                try {
                    totalViagens = dao.verificaViagem(idViagem);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                ViagemModel viagem = new ViagemModel();

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LocalTripActivity.this);
                int userId = sharedPreferences.getInt("userId", 0);

                viagem.setUsuario(userId);
                viagem.setTitulo(title);
                viagem.setLocal(localTrip);
                viagem.setDuracao(duration);

                try {
                    long idInserido = 0;
                    if (totalViagens > 0) {
                        idInserido = dao.Edit(viagem, idViagem);
                        Toast.makeText(LocalTripActivity.this, "Viagem editada com sucesso", Toast.LENGTH_SHORT).show();
                    } else {
                        idInserido = dao.Insert(viagem);
                        Toast.makeText(LocalTripActivity.this, "Viagem cadastrada com sucesso", Toast.LENGTH_SHORT).show();
                    }
                    Intent it = new Intent(LocalTripActivity.this, GasolinaActivity.class);
                    if (totalViagens > 0) {
                        it.putExtra("viagemId", idViagem);
                    } else {
                        it.putExtra("viagemId", idInserido);
                    }
                    startActivity(it);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void excluirDados(long idViagem) {
        GasolinaDAO gasolinaDao = new GasolinaDAO(getApplicationContext());
        try {
            gasolinaDao.deleteByViagemId(idViagem);  // Excluir gasolina
        } catch (Exception e) {
            e.printStackTrace();
        }
        TarifaDAO tarifaDao = new TarifaDAO(getApplicationContext());
        try {
            tarifaDao.deleteByViagemId(idViagem);  // Excluir tarifa
        } catch (Exception e) {
            e.printStackTrace();
        }

        HospedagemDAO hospedagemDao = new HospedagemDAO(getApplicationContext());
        try {
            hospedagemDao.deleteByViagemId(idViagem);  // Excluir hospedagem
        } catch (Exception e) {
            e.printStackTrace();
        }

        RefeicaoDAO refeicaoDao = new RefeicaoDAO(getApplicationContext());
        try {
            refeicaoDao.deleteByViagemId(idViagem);  // Excluir refeicao
        } catch (Exception e) {
            e.printStackTrace();
        }

        EntretenimentoDAO entretenimentoDao = new EntretenimentoDAO(getApplicationContext());
        try {
            entretenimentoDao.deleteByViagemId(idViagem);  // Excluir entretenimento
        } catch (Exception e) {
            e.printStackTrace();
        }

        ViagemDAO viagemDao = new ViagemDAO(getApplicationContext());
        try {
            viagemDao.deleteByViagemId(idViagem);  // Excluir viagem
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void preencherCampos(long idViagem) {
        ViagemDAO dao = new ViagemDAO(getApplicationContext());
        try {
            ViagemModel viagem = (ViagemModel) dao.buscaViagemPorId(idViagem);
            if (viagem != null) {
                edtTitle.setText(viagem.getTitulo());
                edtLocalTrip.setText(viagem.getLocal());

                String[] durations = {"1 dia", "2 dias", "3 dias", "4 dias", "5 dias", "1 semana", "2 semanas"};
                for (int i = 0; i < durations.length; i++) {
                    if (durations[i].equals(viagem.getDuracao())) {
                        spinnerDuration.setSelection(i);
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
