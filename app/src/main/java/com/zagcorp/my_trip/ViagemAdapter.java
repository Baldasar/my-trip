package com.zagcorp.my_trip;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zagcorp.my_trip.database.dao.EntretenimentoDAO;
import com.zagcorp.my_trip.database.dao.GasolinaDAO;
import com.zagcorp.my_trip.database.dao.HospedagemDAO;
import com.zagcorp.my_trip.database.dao.RefeicaoDAO;
import com.zagcorp.my_trip.database.dao.TarifaDAO;
import com.zagcorp.my_trip.database.dao.ViagemDAO;
import com.zagcorp.my_trip.database.model.ViagemModel;

import java.util.List;

public class ViagemAdapter extends RecyclerView.Adapter<ViagemAdapter.ViewHolder> {

    private List<ViagemModel> viagemList;
    private Context context;
    private HomeActivity homeActivity;

    public ViagemAdapter(List<ViagemModel> viagemList, Context context, HomeActivity homeActivity) {
        this.viagemList = viagemList;
        this.context = context;
        this.homeActivity = homeActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viagem_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViagemModel viagem = viagemList.get(position);
        holder.textNomeViagem.setText(viagem.getTitulo());
        holder.textSubtitulo.setText("Local: " + viagem.getLocal());
        holder.novoSubtitulo.setText("Duração: " + viagem.getDuracao());
        holder.btnVerMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActicity.class);
                Integer id = (int) (long) viagem.getId();
                intent.putExtra("viagemId", id);
                context.startActivity(intent);
            }
        });

        holder.btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirmar Exclusão");
                builder.setMessage("Você realmente deseja excluir esta viagem?");

                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        excluirDados(viagem.getId());
                        Toast.makeText(context, "Viagem excluída com sucesso", Toast.LENGTH_SHORT).show();
                        if (homeActivity != null) {
                            homeActivity.recreate();
                        }
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                int myColor = context.getResources().getColor(R.color.primary);

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(myColor);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(myColor);
            }
        });


        holder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, LocalTripActivity.class);
                it.putExtra("viagemId", viagem.getId());
                it.putExtra("editar", true);
                context.startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() {
        return viagemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textNomeViagem, textSubtitulo, novoSubtitulo;
        Button btnVerMais, btnExcluir, btnEditar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textNomeViagem = itemView.findViewById(R.id.textNomeViagem);
            textSubtitulo = itemView.findViewById(R.id.textSubtitulo);
            novoSubtitulo = itemView.findViewById(R.id.novoSubtitulo);
            btnVerMais = itemView.findViewById(R.id.btnVerMais);
            btnExcluir = itemView.findViewById(R.id.btnExcluir);
            btnEditar = itemView.findViewById(R.id.btnEditar);
        }
    }

    private void excluirDados(long idViagem) {
        GasolinaDAO gasolinaDao = new GasolinaDAO(context.getApplicationContext());
        try {
            gasolinaDao.deleteByViagemId(idViagem);  // Excluir gasolina
        } catch (Exception e) {
            e.printStackTrace();
        }
        TarifaDAO tarifaDao = new TarifaDAO(context.getApplicationContext());
        try {
            tarifaDao.deleteByViagemId(idViagem);  // Excluir tarifa
        } catch (Exception e) {
            e.printStackTrace();
        }

        HospedagemDAO hospedagemDao = new HospedagemDAO(context.getApplicationContext());
        try {
            hospedagemDao.deleteByViagemId(idViagem);  // Excluir hospedagem
        } catch (Exception e) {
            e.printStackTrace();
        }

        RefeicaoDAO refeicaoDao = new RefeicaoDAO(context.getApplicationContext());
        try {
            refeicaoDao.deleteByViagemId(idViagem);  // Excluir refeicao
        } catch (Exception e) {
            e.printStackTrace();
        }

        EntretenimentoDAO entretenimentoDao = new EntretenimentoDAO(context.getApplicationContext());
        try {
            entretenimentoDao.deleteByViagemId(idViagem);  // Excluir entretenimento
        } catch (Exception e) {
            e.printStackTrace();
        }

        ViagemDAO viagemDao = new ViagemDAO(context.getApplicationContext());
        try {
            viagemDao.deleteByViagemId(idViagem);  // Excluir viagem
        } catch (Exception e) {
            e.printStackTrace();
        }

        Toast.makeText(context, "Viagem excluída com sucesso", Toast.LENGTH_SHORT).show();
        if (homeActivity != null) {
            homeActivity.recreate(); // Certifique-se de que homeActivity não é nulo antes de chamar recreate()
        }
    }
}
