package com.example.stageup.ui.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.stageup.R;
import com.example.stageup.data.model.Entreprise;

import java.util.List;

public class EntrepriseAdapter extends RecyclerView.Adapter<EntrepriseAdapter.EntrepriseViewHolder> {

    private List<Entreprise> entreprises;
    private OnEntrepriseClickListener listener;

    // Interface pour le clic
    public interface OnEntrepriseClickListener {
        void onEntrepriseClick(Entreprise entreprise);
    }

    public EntrepriseAdapter(List<Entreprise> entreprises, OnEntrepriseClickListener listener) {
        this.entreprises = entreprises;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EntrepriseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_entreprise, parent, false);
        return new EntrepriseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EntrepriseViewHolder holder, int position) {
        Entreprise entreprise = entreprises.get(position);

        holder.nomTextView.setText(entreprise.getNom());
        holder.adresseTextView.setText(entreprise.getAdresse());
        holder.contactTextView.setText(entreprise.getContact());

        Glide.with(holder.itemView.getContext())
                .load(decodeBase64Image(entreprise.getImageData()))
                .placeholder(new ColorDrawable(Color.LTGRAY))
                .error(new ColorDrawable(Color.RED))
                .centerCrop()
                .into(holder.imageView);

        // Définir le clic
        holder.itemView.setOnClickListener(v -> listener.onEntrepriseClick(entreprise));
    }

    private Bitmap decodeBase64Image(String base64String) {
        if (base64String != null && !base64String.isEmpty()) {
            byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return entreprises.size();
    }

    static class EntrepriseViewHolder extends RecyclerView.ViewHolder {
        TextView nomTextView, adresseTextView, contactTextView;
        ImageView imageView;

        EntrepriseViewHolder(@NonNull View itemView) {
            super(itemView);
            nomTextView = itemView.findViewById(R.id.nomTextView);
            adresseTextView = itemView.findViewById(R.id.adresseTextView);
            contactTextView = itemView.findViewById(R.id.contactTextView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}