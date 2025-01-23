package com.example.stageup.ui.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stageup.R;
import com.example.stageup.data.model.Stage;

import java.util.List;

public class StageAdapter extends RecyclerView.Adapter<StageAdapter.StageViewHolder> {

    private final List<Stage> stages;

    public StageAdapter(List<Stage> stages) {
        this.stages = stages;
    }

    @NonNull
    @Override
    public StageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Liaison avec le fichier XML de l'item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stage, parent, false);
        return new StageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StageViewHolder holder, int position) {
        // Obtenir l'objet Stage correspondant
        Stage stage = stages.get(position);

        // Définir les valeurs dans les TextViews
        holder.titreTextView.setText(stage.getTitre());
        holder.descriptionTextView.setText(stage.getDescription());
        holder.lieuTextView.setText("Lieu : " + stage.getLieu());
        holder.dureeTextView.setText("Durée : " + stage.getDuree());
        holder.dateDebutTextView.setText("Date de début : " + stage.getDateDebut());
        holder.dateFinTextView.setText("Date de fin : " + stage.getDateFin());
    }

    @Override
    public int getItemCount() {
        // Le nombre total de stages
        return stages.size();
    }

    // Classe interne pour le ViewHolder
    static class StageViewHolder extends RecyclerView.ViewHolder {

        // Définition des TextViews
        final TextView titreTextView, descriptionTextView, lieuTextView, dureeTextView, dateDebutTextView, dateFinTextView;

        public StageViewHolder(@NonNull View itemView) {
            super(itemView);

            // Liaison des TextViews avec les IDs XML
            titreTextView = itemView.findViewById(R.id.titreTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            lieuTextView = itemView.findViewById(R.id.lieuTextView);
            dureeTextView = itemView.findViewById(R.id.dureeTextView);
            dateDebutTextView = itemView.findViewById(R.id.dateDebutTextView);
            dateFinTextView = itemView.findViewById(R.id.dateFinTextView);
        }
    }
}