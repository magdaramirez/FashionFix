package mx.edu.potros.fashionfix;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

public class OutfitAdapter extends RecyclerView.Adapter<OutfitAdapter.OutfitViewHolder> {
    private List<Outfit> outfits;

    // Constructor que recibe la lista de outfits
    public OutfitAdapter(List<Outfit> outfits) {
        this.outfits = outfits;
    }

    // Método para inflar el layout del elemento de outfit
    @Override
    public OutfitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.outfit, parent, false);
        return new OutfitViewHolder(itemView);
    }

    // Método para vincular los datos de un conjunto de outfit a las vistas
    @Override
    public void onBindViewHolder(OutfitViewHolder holder, int position) {
        Outfit outfit = outfits.get(position);
        cargarImagenDesdeUrl(outfit.getBottom(), holder.imageViewPantalon);
        cargarImagenDesdeUrl(outfit.getTop(), holder.imageViewCamisa);
        cargarImagenDesdeUrl(outfit.getShoes(), holder.imageViewZapatos);
    }


    // Método para obtener la cantidad de elementos en la lista de outfits
    @Override
    public int getItemCount() {
        return outfits.size();
    }

    // Clase ViewHolder que representa cada elemento de outfit en el RecyclerView
    public class OutfitViewHolder extends RecyclerView.ViewHolder {
        // Declara las vistas del elemento de outfit aquí
        // Por ejemplo:
        public ImageView imageViewPantalon;
        public ImageView imageViewCamisa;
        public ImageView imageViewZapatos;

        public OutfitViewHolder(View itemView) {
            super(itemView);
            // Inicializa las vistas del elemento de outfit aquí
            // Por ejemplo:
            imageViewPantalon = itemView.findViewById(R.id.imageView_Bottom);
            imageViewCamisa = itemView.findViewById(R.id.imageView_Top);
            imageViewZapatos = itemView.findViewById(R.id.imageView_Shoes);
        }
    }

    // Método para cargar una imagen desde una URL y establecerla en un ImageView
    private void cargarImagenDesdeUrl(String imageUrl, final ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        imageView.setImageDrawable(resource);
                    }

                    @Override
                    public void onLoadCleared(Drawable placeholder) {
                        // No es necesario hacer nada aquí
                    }
                });
    }
}
