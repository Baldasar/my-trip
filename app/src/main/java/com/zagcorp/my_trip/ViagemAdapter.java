package com.zagcorp.my_trip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zagcorp.my_trip.database.model.ViagemModel;

import java.util.List;

public class ViagemAdapter extends RecyclerView.Adapter<ViagemAdapter.ViewHolder> {

    private List<ViagemModel> viagemList;
    private Context context;

    public ViagemAdapter(List<ViagemModel> viagemList, Context context) {
        this.viagemList = viagemList;
        this.context = context;
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
        holder.textValor.setText("R$: 20.000,00");
        holder.btnVerMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return viagemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textNomeViagem, textSubtitulo, textValor;
        Button btnVerMais;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textNomeViagem = itemView.findViewById(R.id.textNomeViagem);
            textSubtitulo = itemView.findViewById(R.id.textSubtitulo);
            textValor = itemView.findViewById(R.id.textValor);
            btnVerMais = itemView.findViewById(R.id.btnVerMais);
        }
    }
}