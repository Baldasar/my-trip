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
import com.zagcorp.my_trip.database.dao.ViagemDAO;
import com.zagcorp.my_trip.database.model.ViagemModel;

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
        Boolean editar = it.getBooleanExtra("editar", false);

        btnVoltar = findViewById(R.id.btnVoltar);
        btnContinuar = findViewById(R.id.btnContinuar);
        edtTitle = findViewById(R.id.edtTitle);
        edtLocalTrip = findViewById(R.id.edtLocalTrip);
        spinnerDuration = findViewById(R.id.spinnerDuration);

        if (editar) {
            try {
                ViagemDAO dao = new ViagemDAO(getApplicationContext());
                List<ViagemModel> viagens = dao.buscaViagensPorId(idViagem);

                for ( ViagemModel viagem : viagens ) {
                    edtTitle.setText(viagem.getTitulo());
                    edtLocalTrip.setText(viagem.getLocal());
                    spinnerDuration.setSelection(((ArrayAdapter) spinnerDuration.getAdapter()).getPosition(viagem.getDuracao()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String[] durations = {"1 dia", "2 dias", "3 dias", "4 dias", "5 dias", "1 semana", "2 semanas"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, durations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDuration.setAdapter(adapter);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LocalTripActivity.this);
                builder.setTitle("Confirmar Saída");
                builder.setMessage("Tem certeza que deseja sair?");

                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
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
                ViagemModel viagem = new ViagemModel();

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LocalTripActivity.this);
                int userId = sharedPreferences.getInt("userId", 0);

                viagem.setUsuario(userId);
                viagem.setTitulo(title);
                viagem.setLocal(localTrip);
                viagem.setDuracao(duration);

                try {
                    if(editar) {
                        ViagemModel viagemEdit = new ViagemModel();
                        viagemEdit.setId(idViagem);
                        viagemEdit.setUsuario(userId);
                        viagemEdit.setTitulo(title);
                        viagemEdit.setLocal(localTrip);
                        viagemEdit.setDuracao(duration);
                        int rowsAffected = dao.update(viagemEdit);

                        Toast.makeText(LocalTripActivity.this, "Viagem atualizada com sucesso", Toast.LENGTH_SHORT).show();
                        Intent it = new Intent(LocalTripActivity.this, GasolinaActivity.class);
                        it.putExtra("viagemId", idViagem);
                        it.putExtra("editar", true);
                        startActivity(it);
                    } else {
                        long idInserido = dao.Insert(viagem);
                        Toast.makeText(LocalTripActivity.this, "Viagem cadastrada com sucesso", Toast.LENGTH_SHORT).show();
                        Intent it = new Intent(LocalTripActivity.this, GasolinaActivity.class);
                        it.putExtra("viagemId", idInserido);
                        startActivity(it);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
