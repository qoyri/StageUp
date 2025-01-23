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
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.stageup.R;
import com.example.stageup.data.model.Etudiant;

import java.util.List;

public class EtudiantAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Etudiant> etudiants;
    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    public EtudiantAdapter(List<Etudiant> etudiants) {
        this.etudiants = etudiants;
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_etudiant, parent, false);
            return new EtudiantViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EtudiantViewHolder) {
            Etudiant etudiant = etudiants.get(position);

            EtudiantViewHolder etudiantHolder = (EtudiantViewHolder) holder;
            etudiantHolder.nomPrenomTextView.setText(etudiant.getNom() + " " + etudiant.getPrenom());
            etudiantHolder.contactTextView.setText(etudiant.getContact());
            etudiantHolder.promoTextView.setText(etudiant.getPromo());
            etudiantHolder.reseauxSociauxTextView.setText(etudiant.getReseauxSociaux());

            Glide.with(holder.itemView.getContext())
                    .load(decodeBase64Image(etudiant.getImageData()))
                    .placeholder(new ColorDrawable(Color.LTGRAY))
                    .error(new ColorDrawable(Color.RED))
                    .centerCrop()
                    .into(etudiantHolder.imageView);
        }
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
        return etudiants.size();
    }

    static class EtudiantViewHolder extends RecyclerView.ViewHolder {
        TextView nomPrenomTextView, contactTextView, promoTextView, reseauxSociauxTextView;
        ImageView imageView;

        EtudiantViewHolder(@NonNull View itemView) {
            super(itemView);
            nomPrenomTextView = itemView.findViewById(R.id.nomPrenomTextView);
            contactTextView = itemView.findViewById(R.id.contactTextView);
            promoTextView = itemView.findViewById(R.id.promoTextView);
            reseauxSociauxTextView = itemView.findViewById(R.id.reseauxSociauxTextView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    public void addEtudiants(List<Etudiant> newEtudiants) {
        int startPosition = etudiants.size();
        etudiants.addAll(newEtudiants);
        notifyItemRangeInserted(startPosition, newEtudiants.size());
    }
}