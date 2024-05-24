package com.zagcorp.my_trip;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EntreterimentoActivity extends AppCompatActivity {

    private LinearLayout layoutAtividades;
    private int contadorAtividades = 0;
    private ArrayList<Integer> atividadeIds = new ArrayList<>();
    private ArrayList<Integer> valorIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entreterimento);

        layoutAtividades = findViewById(R.id.layoutAtividades);
        ImageButton btnAdicionarAtividade = findViewById(R.id.btnAdicionarAtividade);

        btnAdicionarAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarNovaAtividade();
            }
        });
    }

    private void adicionarNovaAtividade() {
        contadorAtividades++;

        // Cria novo EditText para Atividade
        EditText novaAtividade = new EditText(this);
        novaAtividade.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        novaAtividade.setHint("Digite a atividade");
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
        novoValor.setHint("Digite o valor");
        novoValor.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        novoValor.setPadding(8, 8, 8, 8);
        int idValor = View.generateViewId();
        novoValor.setId(idValor);
        valorIds.add(idValor);

        // Adiciona os novos EditTexts ao layout
        layoutAtividades.addView(novaAtividade);
        layoutAtividades.addView(novoValor);
    }

    // Método exemplo para acessar e manipular os campos adicionados
    private void acessarCamposAdicionados() {
        for (int i = 0; i < atividadeIds.size(); i++) {
            EditText atividade = findViewById(atividadeIds.get(i));
            EditText valor = findViewById(valorIds.get(i));

            // Realize as operações desejadas com os campos, por exemplo:
            String atividadeTexto = atividade.getText().toString();
            String valorTexto = valor.getText().toString();

            // Faça algo com os valores, como exibir em logs ou salvar em um banco de dados
            System.out.println("Atividade: " + atividadeTexto + ", Valor: " + valorTexto);
        }
    }
}