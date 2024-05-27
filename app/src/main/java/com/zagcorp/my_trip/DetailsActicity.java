package com.zagcorp.my_trip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zagcorp.my_trip.database.dao.GasolinaDAO;
import com.zagcorp.my_trip.database.dao.TarifaDAO;
import com.zagcorp.my_trip.database.model.GasolinaModel;
import com.zagcorp.my_trip.database.model.TarifaModel;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DetailsActicity extends AppCompatActivity {
    private FloatingActionButton btnVoltar;

    private RelativeLayout rl_gasolina,rl_tarifa_aerea;
    private TextView txt_total_km,txt_media_km_litro,txt_custo_litro,txt_qntd_veiculos, txt_valor_total;

    private TextView txt_custo_estimado_pessoa,txt_quantidade_pessoas, txt_valor_veiculo, txt_valor_total_tarifa_aerea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.primary));
        window.setNavigationBarColor(getResources().getColor(R.color.primary));

        Intent it = getIntent();
        long idViagem = it.getIntExtra("viagemId", 0);

        btnVoltar = findViewById(R.id.btnVoltar);
        rl_gasolina = findViewById(R.id.rl_gasolina);
        rl_tarifa_aerea = findViewById(R.id.rl_tarifa_aerea);
        txt_total_km = findViewById(R.id.txt_total_km);
        txt_media_km_litro = findViewById(R.id.txt_media_km_litro);
        txt_custo_litro = findViewById(R.id.txt_custo_litro);
        txt_qntd_veiculos = findViewById(R.id.txt_qntd_veiculos);
        txt_valor_total = findViewById(R.id.txt_valor_total);
        txt_custo_estimado_pessoa = findViewById(R.id.txt_custo_estimado_pessoa);
        txt_quantidade_pessoas = findViewById(R.id.txt_quantidade_pessoas);
        txt_valor_veiculo = findViewById(R.id.txt_valor_veiculo);
        txt_valor_total_tarifa_aerea = findViewById(R.id.txt_valor_total_tarifa_aerea);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(DetailsActicity.this, HomeActivity.class);
                startActivity(it);
            }
        });

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        GasolinaDAO dao = new GasolinaDAO(getApplicationContext());
        try {
            List<GasolinaModel> all = dao.buscaGasolina("" +idViagem);

            if(all.size() == 0) {
                rl_gasolina.setVisibility(View.GONE);
                return;
            }

            double totalKm = 0;
            double mediaKmLitro = 0;
            double custoLitro = 0;
            int qntdVeiculos = 0;
            double valorTotal = 0;

            for (GasolinaModel gasolina : all) {
                totalKm += gasolina.getKm();
                mediaKmLitro += gasolina.getKm_litro();
                custoLitro += gasolina.getCusto_medio();
                qntdVeiculos += gasolina.getQntd_veiculo();
                valorTotal += ((gasolina.getKm() * gasolina.getCusto_medio()) * gasolina.getKm_litro()) / gasolina.getQntd_veiculo();
            }

            if (!all.isEmpty()) {
                mediaKmLitro /= all.size();
            }

            txt_total_km.setText("Total estimado de quilômetros: " + totalKm);
            txt_media_km_litro.setText("Média de Km por litro: " + mediaKmLitro);
            String custoLitroFormatado = currencyFormat.format(custoLitro);
            txt_custo_litro.setText("Custo médio por litro: " + custoLitroFormatado);
            txt_qntd_veiculos.setText("Quantidade de veículos: " + qntdVeiculos);

            String valorTotalFormatado = currencyFormat.format(valorTotal);
            txt_valor_total.setText("Valor total: " + valorTotalFormatado);

        } catch (Exception e) {
            e.printStackTrace();
        }

        TarifaDAO daoTarifa = new TarifaDAO(getApplicationContext());

        try {
            List<TarifaModel> allTarifas = daoTarifa.buscaTarifa(""+ idViagem);

            if(allTarifas.size() == 0) {
                rl_tarifa_aerea.setVisibility(View.GONE);
                return;
            }

            long custo_pessoa = 0;
            int qntd_pessoa = 0;
            long custo_veiculo = 0;
            long valorTotal = 0;

            for (TarifaModel tarifa : allTarifas) {
                custo_pessoa += tarifa.getCusto_pessoa();
                qntd_pessoa += tarifa.getQtd_pessoa();
                custo_veiculo += tarifa.getCusto_veiculo();
                valorTotal += (tarifa.getCusto_pessoa() * tarifa.getQtd_pessoa()) + tarifa.getCusto_veiculo();
            }

            String custoPessoaFormatado = currencyFormat.format(custo_pessoa);
            txt_custo_estimado_pessoa.setText("Custo estimado por pessoa: " + custoPessoaFormatado);
            txt_quantidade_pessoas.setText("Quantidade de pessoas: " + qntd_pessoa);
            String custoVeiculoFormatado = currencyFormat.format(custo_veiculo);
            txt_valor_veiculo.setText("Valor do veículo: " + custoVeiculoFormatado);

            String valorTotalFormatado = currencyFormat.format(valorTotal);
            txt_valor_total_tarifa_aerea.setText("Valor total: " + valorTotalFormatado);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
