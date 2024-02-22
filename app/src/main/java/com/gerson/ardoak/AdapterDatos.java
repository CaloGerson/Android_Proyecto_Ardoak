package com.gerson.ardoak;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public class AdapterDatos extends RecyclerView.Adapter<AdapterDatos.ViewHolderDatos> {
    ArrayList<Vino> listaVino;

    public AdapterDatos(ArrayList<Vino> listaVino) {
        this.listaVino = listaVino;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHolderDatos(view);
    }

    // En el método onBindViewHolder de AdapterDatos
    @Override
    public void onBindViewHolder(@NonNull AdapterDatos.ViewHolderDatos holder, int position) {
        holder.etiNombre.setText(listaVino.get(position).getNombre());
        Log.d("uri", "Local uri: " + listaVino.get(position).getImagePath());
        holder.etiOrigen.setText(listaVino.get(position).getOrigen());
        holder.etiPrecio.setText(String.valueOf(listaVino.get(position).getPrecio()));

        Uri imageUri = Uri.parse(listaVino.get(position).getImagePath());
        if (imageUri != null) {
            Glide.with(holder.foto.getContext())
                    .load(imageUri)
                    .error(R.drawable.mi_imagen)
                    .into(holder.foto);
        } else {
            holder.foto.setImageResource(R.drawable.mi_imagen);
        }

    }




    @Override
    public int getItemCount() {
        return listaVino.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView etiNombre, etiOrigen, etiPrecio;
        ImageView foto;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            etiNombre = itemView.findViewById(R.id.itemListTextViewNombre);
            etiOrigen = itemView.findViewById(R.id.itemListTextViewOrigen);
            etiPrecio = itemView.findViewById(R.id.itemListTextViewPrecio);
            foto = itemView.findViewById(R.id.itemListImagenViewFoto);
        }
    }


}
