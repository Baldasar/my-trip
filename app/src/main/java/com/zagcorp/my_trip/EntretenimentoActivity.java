package com.zagcorp.my_trip;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zagcorp.my_trip.database.dao.EntretenimentoDAO;
import com.zagcorp.my_trip.database.model.EntretenimentoModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EntretenimentoActivity extends AppCompatActivity {

    private LinearLayout layoutAtividades;
    private int contadorAtividades = 0;
    private ArrayList<Integer> atividadeIds = new ArrayList<>();
    private ArrayList<Integer> valorIds = new ArrayList<>();
    private Button btnContinuar, btnPularEtapa, btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entretenimento);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.primary));
        window.setNavigationBarColor(getResources().getColor(R.color.primary));

        layoutAtividades = findViewById(R.id.layoutAtividades);
        ImageButton btnAdicionarAtividade = findViewById(R.id.btnAdicionarAtividade);

        Intent it = getIntent();
        Long idViagem = it.getLongExtra("viagemId", 0);

        btnVoltar = findViewById(R.id.btnVoltar);
        btnContinuar = findViewById(R.id.btnContinuar);
        btnPularEtapa = findViewById(R.id.btnPularEtapa);

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

        btnPularEtapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                excluirAtividadesExistentes(idViagem);
                Intent it = new Intent(EntretenimentoActivity.this, HomeActivity.class);
                it.putExtra("viagemId", idViagem);
                startActivity(it);
            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // This will close the current activity and go back to the previous one
            }
        });

        // Carrega as atividades de entretenimento existentes para a viagem
        carregarAtividades(idViagem);
    }

    private void carregarAtividades(long idViagem) {
        EntretenimentoDAO dao = new EntretenimentoDAO(getApplicationContext());
        List<EntretenimentoModel> atividades = new ArrayList<>();

        try {
            atividades = dao.buscaEntretenimento(String.valueOf(idViagem));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (atividades.isEmpty()) {
            // Se não houver atividades, adicionar uma nova vazia
            adicionarNovaAtividade();
        } else {
            // Se houver atividades, preencher os campos
            for (EntretenimentoModel atividade : atividades) {
                adicionarNovaAtividadeComDados(atividade.getAtividade(), String.valueOf(atividade.getValor()));
            }
        }
    }

    private void adicionarNovaAtividade() {
        adicionarNovaAtividadeComDados("", "");
    }

    private void adicionarNovaAtividadeComDados(String atividadeTexto, String valorTexto) {
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
        novaAtividade.setText(atividadeTexto);
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
        novoValor.setText(valorTexto);
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
        excluirAtividadesExistentes(idViagem);
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

    private void excluirAtividadesExistentes(long idViagem) {
        EntretenimentoDAO dao = new EntretenimentoDAO(getApplicationContext());
        try {
            dao.deleteByViagemId(idViagem);  // Excluir atividades existentes
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}