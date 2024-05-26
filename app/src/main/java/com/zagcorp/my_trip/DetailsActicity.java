package com.zagcorp.my_trip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zagcorp.my_trip.database.dao.GasolinaDAO;
import com.zagcorp.my_trip.database.dao.HospedagemDAO;
import com.zagcorp.my_trip.database.dao.RefeicaoDAO;
import com.zagcorp.my_trip.database.dao.TarifaDAO;
import com.zagcorp.my_trip.database.dao.ViagemDAO;
import com.zagcorp.my_trip.database.model.GasolinaModel;
import com.zagcorp.my_trip.database.model.HospedagemModel;
import com.zagcorp.my_trip.database.model.RefeicaoModel;
import com.zagcorp.my_trip.database.model.TarifaModel;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DetailsActicity extends AppCompatActivity {
    private FloatingActionButton btnVoltar;

    private RelativeLayout rl_gasolina, rl_tarifa_aerea, rl_refeicoes, rl_hospedagem;
    private TextView txt_total_km, txt_media_km_litro, txt_custo_litro, txt_qntd_veiculos, txt_valor_total;

    private TextView txt_custo_estimado_pessoa, txt_quantidade_pessoas, txt_valor_veiculo, txt_valor_total_tarifa_aerea;

    private TextView txt_custo_estimado_refeicao, txt_quantidade_refeicoes, txt_quantidade_viajantes, txt_duracao_viagem, txt_valor_total_refeicoes;

    private TextView txt_custo_medio, txt_total_noites, txt_total_quartos, txt_valor_total_hospedagem;

    private TextView txt_valor_total_global;
    private Integer qntd_viajanes = 0;
    private long valorTotalGlobal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent it = getIntent();
        long idViagem = it.getIntExtra("viagemId", 0);

        btnVoltar = findViewById(R.id.btnVoltar);

        txt_valor_total_global = findViewById(R.id.txt_valor_total_global);

        rl_gasolina = findViewById(R.id.rl_gasolina);
        rl_tarifa_aerea = findViewById(R.id.rl_tarifa_aerea);
        rl_refeicoes = findViewById(R.id.rl_refeicoes);
        rl_hospedagem = findViewById(R.id.rl_hospedagem);

        txt_total_km = findViewById(R.id.txt_total_km);
        txt_media_km_litro = findViewById(R.id.txt_media_km_litro);
        txt_custo_litro = findViewById(R.id.txt_custo_litro);
        txt_qntd_veiculos = findViewById(R.id.txt_qntd_veiculos);
        txt_valor_total = findViewById(R.id.txt_valor_total);

        txt_custo_estimado_pessoa = findViewById(R.id.txt_custo_estimado_pessoa);
        txt_quantidade_pessoas = findViewById(R.id.txt_quantidade_pessoas);
        txt_valor_veiculo = findViewById(R.id.txt_valor_veiculo);
        txt_valor_total_tarifa_aerea = findViewById(R.id.txt_valor_total_tarifa_aerea);

        txt_custo_estimado_refeicao = findViewById(R.id.txt_custo_estimado_refeicao);
        txt_quantidade_refeicoes = findViewById(R.id.txt_quantidade_refeicoes);
        txt_quantidade_viajantes = findViewById(R.id.txt_quantidade_viajantes);
        txt_duracao_viagem = findViewById(R.id.txt_duracao_viagem);
        txt_valor_total_refeicoes = findViewById(R.id.txt_valor_total_refeicoes);

        txt_custo_medio = findViewById(R.id.txt_custo_medio);
        txt_total_noites = findViewById(R.id.txt_total_noites);
        txt_total_quartos = findViewById(R.id.txt_total_quartos);
        txt_valor_total_hospedagem = findViewById(R.id.txt_valor_total_hospedagem);

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
            List<GasolinaModel> all = dao.buscaGasolina("" + idViagem);
            if (all.isEmpty()) {
                rl_gasolina.setVisibility(View.GONE);
            } else {
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
                    valorTotal += ((gasolina.getKm() / gasolina.getCusto_medio()) * gasolina.getKm_litro()) / gasolina.getQntd_veiculo();
                    valorTotalGlobal += valorTotal;
                }

                txt_total_km.setText("Total estimado de quilômetros: " + totalKm);
                txt_media_km_litro.setText("Média de Km por litro: " + mediaKmLitro);
                String custoLitroFormatado = currencyFormat.format(custoLitro);
                txt_custo_litro.setText("Custo médio por litro: " + custoLitroFormatado);
                txt_qntd_veiculos.setText("Quantidade de veículos: " + qntdVeiculos);

                String valorTotalFormatado = currencyFormat.format(valorTotal);
                txt_valor_total.setText("Valor total: " + valorTotalFormatado);

                String valorTotalGlobalFormatado = currencyFormat.format(valorTotalGlobal);
                txt_valor_total_global.setText("Valor total: " + valorTotalGlobalFormatado);
            }
        } catch (Exception e) {
            rl_gasolina.setVisibility(View.GONE);
            e.printStackTrace();
        }

        TarifaDAO daoTarifa = new TarifaDAO(getApplicationContext());
        try {
            List<TarifaModel> allTarifas = daoTarifa.buscaTarifa("" + idViagem);
            if (allTarifas.isEmpty()) {
                rl_tarifa_aerea.setVisibility(View.GONE);
            } else {
                long custo_pessoa = 0;
                int qntd_pessoa = 0;
                long custo_veiculo = 0;
                long valorTotal = 0;

                for (TarifaModel tarifa : allTarifas) {
                    custo_pessoa += tarifa.getCusto_pessoa();
                    qntd_pessoa += tarifa.getQtd_pessoa();
                    custo_veiculo += tarifa.getCusto_veiculo();
                    valorTotal += (tarifa.getCusto_pessoa() * tarifa.getQtd_pessoa()) + tarifa.getCusto_veiculo();
                    valorTotalGlobal += valorTotal;
                }

                String custoPessoaFormatado = currencyFormat.format(custo_pessoa);
                txt_custo_estimado_pessoa.setText("Custo estimado por pessoa: " + custoPessoaFormatado);
                txt_quantidade_pessoas.setText("Quantidade de pessoas: " + qntd_pessoa);
                String custoVeiculoFormatado = currencyFormat.format(custo_veiculo);
                txt_valor_veiculo.setText("Valor do veículo: " + custoVeiculoFormatado);

                String valorTotalFormatado = currencyFormat.format(valorTotal);
                txt_valor_total_tarifa_aerea.setText("Valor total: " + valorTotalFormatado);

                String valorTotalGlobalFormatado = currencyFormat.format(valorTotalGlobal);
                txt_valor_total_global.setText("Valor total: " + valorTotalGlobalFormatado);
            }
        } catch (Exception e) {
            rl_tarifa_aerea.setVisibility(View.GONE);
            e.printStackTrace();
        }

        RefeicaoDAO daoRefeicao = new RefeicaoDAO(getApplicationContext());
        try {
            List<RefeicaoModel> allRefeicoes = daoRefeicao.buscaRefeicao("" + idViagem);
            if (allRefeicoes.isEmpty()) {
                rl_refeicoes.setVisibility(View.GONE);
            } else {
                ViagemDAO daoViagem = new ViagemDAO(getApplicationContext());
                String duracao_viagem = daoViagem.getDuracaoViagem(idViagem);

                double custo_refeicao = 0;
                int qntd_refeicoes = 0;
                int qntd_viajanes = 0;
                int duracao_viagem_int = getDays(duracao_viagem);
                long valorTotal = 0;

                for (RefeicaoModel refeicao : allRefeicoes) {
                    custo_refeicao += refeicao.getCusto_estimado();
                    qntd_refeicoes += refeicao.getQtd_refeicao();
                    qntd_viajanes += refeicao.getQtd_viajantes();
                    valorTotal += ((refeicao.getQtd_refeicao() * refeicao.getQtd_viajantes()) * refeicao.getCusto_estimado()) * duracao_viagem_int;
                    valorTotalGlobal += valorTotal;
                }

                String custoRefeicaoFormatado = currencyFormat.format(custo_refeicao);
                txt_custo_estimado_refeicao.setText("Custo estimado por refeição: " + custoRefeicaoFormatado);
                txt_quantidade_refeicoes.setText("Quantidade de refeições: " + qntd_refeicoes);
                txt_quantidade_viajantes.setText("Quantidade de viajantes: " + qntd_viajanes);
                txt_duracao_viagem.setText("Duração da viagem: " + duracao_viagem);
                String valorTotalFormatado = currencyFormat.format(valorTotal);
                txt_valor_total_refeicoes.setText("Valor total: " + valorTotalFormatado);

                String valorTotalGlobalFormatado = currencyFormat.format(valorTotalGlobal);
                txt_valor_total_global.setText("Valor total: " + valorTotalGlobalFormatado);
            }
        } catch (Exception e) {
            rl_refeicoes.setVisibility(View.GONE);
            e.printStackTrace();
        }

        HospedagemDAO daoHospedagem = new HospedagemDAO(getApplicationContext());
        try {
            List<HospedagemModel> allHospedagem = daoHospedagem.buscaHospedagem("" + idViagem);
            if (allHospedagem.isEmpty()) {
                rl_hospedagem.setVisibility(View.GONE);
            } else {
                double custo_medio = 0;
                int total_noites = 0;
                int total_quartos = 0;
                long valorTotal = 0;

                for (HospedagemModel hospedagem : allHospedagem) {
                    custo_medio += hospedagem.getCusto_noite();
                    total_noites += hospedagem.getQtd_noite();
                    total_quartos += hospedagem.getQtd_quarto();
                    valorTotal += (hospedagem.getCusto_noite() * hospedagem.getQtd_noite()) * hospedagem.getQtd_quarto();
                    valorTotalGlobal += valorTotal;
                }

                String custoMedioFormatado = currencyFormat.format(custo_medio);
                txt_custo_medio.setText("Custo médio por noite: " + custoMedioFormatado);
                txt_total_noites.setText("Total de noites: " + total_noites);
                txt_total_quartos.setText("Total de quartos: " + total_quartos);

                String valorTotalFormatado = currencyFormat.format(valorTotal);
                txt_valor_total_hospedagem.setText("Valor total: " + valorTotalFormatado);

                String valorTotalGlobalFormatado = currencyFormat.format(valorTotalGlobal);
                txt_valor_total_global.setText("Valor total: " + valorTotalGlobalFormatado);
            }
        } catch (Exception e) {
            rl_hospedagem.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }

    public static int getDays(String period) {
        int days;
        switch (period) {
            case "1 dia":
                days = 1;
                break;
            case "2 dias":
                days = 2;
                break;
            case "3 dias":
                days = 3;
                break;
            case "4 dias":
                days = 4;
                break;
            case "5 dias":
                days = 5;
                break;
            case "1 semana":
                days = 7;
                break;
            case "2 semanas":
                days = 14;
                break;
            default:
                throw new IllegalArgumentException("Período inválido: " + period);
        }
        return days;
    }
}
