package com.zagcorp.my_trip;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zagcorp.my_trip.database.dao.GasolinaDAO;
import com.zagcorp.my_trip.database.model.GasolinaModel;

import java.util.List;

public class DetailsActicity extends AppCompatActivity {
    private FloatingActionButton btnVoltar;
    private TextView txt_total_km,txt_media_km_litro,txt_custo_litro,txt_qntd_veiculos, txt_valor_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent it = getIntent();
        Integer idViagem = it.getIntExtra("viagemId", 0);

        btnVoltar = findViewById(R.id.btnVoltar);
        txt_total_km = findViewById(R.id.txt_total_km);
        txt_media_km_litro = findViewById(R.id.txt_media_km_litro);
        txt_custo_litro = findViewById(R.id.txt_custo_litro);
        txt_qntd_veiculos = findViewById(R.id.txt_qntd_veiculos);
        txt_valor_total = findViewById(R.id.txt_valor_total);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(DetailsActicity.this, HomeActivity.class);
                startActivity(it);
            }
        });

        GasolinaDAO dao = new GasolinaDAO(getApplicationContext());
        try {
            List<GasolinaModel> all = dao.buscaGasolina("0");
            Toast.makeText(this, "" + idViagem, Toast.LENGTH_SHORT).show();
            String totalKm = "";
            String mediaKmLitro = "";
            String custoLitro = "";
            String qntdVeiculos = "";
            String valorTotal = "";

            for (GasolinaModel gasolina : all) {
                totalKm += gasolina.getKm();
                mediaKmLitro += gasolina.getCusto_medio();
                custoLitro += gasolina.getKm_litro();
                qntdVeiculos += gasolina.getQntd_veiculo();
                valorTotal += ((gasolina.getKm() * gasolina.getCusto_medio()) * gasolina.getKm_litro()) / gasolina.getQntd_veiculo();
            }

            txt_total_km.setText("Total estimado de quilômetros: " + totalKm);
            txt_media_km_litro.setText("Média de Km por litro: " + mediaKmLitro);
            txt_custo_litro.setText("Custo médio por litro: " + custoLitro);
            txt_qntd_veiculos.setText("Quantidade de veículos: " + qntdVeiculos);
            txt_valor_total.setText("Valor total: " + valorTotal);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
