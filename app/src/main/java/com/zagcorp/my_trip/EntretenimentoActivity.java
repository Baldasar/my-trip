package com.zagcorp.my_trip;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zagcorp.my_trip.database.dao.EntretenimentoDAO;
import com.zagcorp.my_trip.database.dao.RefeicaoDAO;
import com.zagcorp.my_trip.database.model.EntretenimentoModel;
import com.zagcorp.my_trip.database.model.RefeicaoModel;

import java.util.ArrayList;

public class EntretenimentoActivity extends AppCompatActivity {

    private LinearLayout layoutAtividades;
    private int contadorAtividades = 0;
    private ArrayList<Integer> atividadeIds = new ArrayList<>();
    private ArrayList<Integer> valorIds = new ArrayList<>();
    private Button btnContinuar, btnPularEtapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entretenimento);

        layoutAtividades = findViewById(R.id.layoutAtividades);
        ImageButton btnAdicionarAtividade = findViewById(R.id.btnAdicionarAtividade);

        Intent it = getIntent();
        Long idViagem = it.getLongExtra("viagemId", 0);

        adicionarNovaAtividade();

        btnAdicionarAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarNovaAtividade();
            }
        });

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarAtividades(idViagem);
            }
        });
    }

    private void adicionarNovaAtividade() {
        contadorAtividades++;

        // Cria nova divisão
        View divisao = new View(this);
        divisao.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                1)); // altura de 1dp para uma linha divisória
        divisao.setBackgroundColor(getResources().getColor(R.color.black));

        // Cria novo EditText para Atividade
        EditText novaAtividade = new EditText(this);
        novaAtividade.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        novaAtividade.setHint("Atividade");
        novaAtividade.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
        novaAtividade.setPadding(8, 8, 8, 8);
        int idAtividade = View.generateViewId();
        novaAtividade.setId(idAtividade);
        atividadeIds.add(idAtividade);

        // Cria novo EditText para Valor
        EditText novoValor = new EditText(this);
        novoValor.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        novoValor.setHint("Valor");
        novoValor.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        novoValor.setPadding(8, 8, 8, 8);
        int idValor = View.generateViewId();
        novoValor.setId(idValor);
        valorIds.add(idValor);

        // Adiciona os novos EditTexts ao layout
        layoutAtividades.addView(divisao);
        layoutAtividades.addView(novaAtividade);
        layoutAtividades.addView(novoValor);
    }

    private void salvarAtividades(long idViagem) {
        for (int i = 0; i < atividadeIds.size(); i++) {
            EditText atividade = findViewById(atividadeIds.get(i));
            EditText valor = findViewById(valorIds.get(i));

            String atividadeTexto = atividade.getText().toString();
            String valorTexto = valor.getText().toString();

            if (atividadeTexto.isEmpty() || valorTexto.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        EntretenimentoDAO dao = new EntretenimentoDAO(getApplicationContext());
        EntretenimentoModel entretenimento = new EntretenimentoModel();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(EntretenimentoActivity.this);

        for (int i = 0; i < atividadeIds.size(); i++) {
            EditText atividade = findViewById(atividadeIds.get(i));
            EditText valor = findViewById(valorIds.get(i));

            String atividadeTexto = atividade.getText().toString();
            String valorTexto = valor.getText().toString();

            entretenimento.setViagem(idViagem);
            entretenimento.setAtividade(atividadeTexto);
            entretenimento.setValor(Integer.parseInt(valorTexto));

            try {
                dao.Insert(entretenimento);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            Toast.makeText(EntretenimentoActivity.this, "Entretenimentos cadastrados com sucesso", Toast.LENGTH_SHORT).show();
            Intent it = new Intent(EntretenimentoActivity.this, HomeActivity.class);
            it.putExtra("viagemId", idViagem);
            startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}