package com.zimadtestproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zimadtestproject.api.Animal;
import com.zimadtestproject.observer.AnimalDataRepository;

import java.util.ArrayList;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.AnimalViewHolder> {

    private List<Animal> animalList = new ArrayList<>();
    private Context context;

    RecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public static class AnimalViewHolder extends RecyclerView.ViewHolder {
        TextView animalText;
        TextView counter;
        ImageView animalImage;

        public AnimalViewHolder(View itemView) {
            super(itemView);
            counter = itemView.findViewById(R.id.animal_count);
            animalText = itemView.findViewById(R.id.animal_text_view);
            animalImage = itemView.findViewById(R.id.animal_image_view);
        }
    }

    @Override
    public RecyclerViewAdapter.AnimalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.animal_item_view, parent, false);
        AnimalViewHolder animalViewHolder = new AnimalViewHolder(view);

        return animalViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.AnimalViewHolder holder, final int position) {
        final String itemPosition = String.valueOf(position + 1);
        holder.counter.setText(itemPosition);
        holder.animalText.setText(animalList.get(position).getTitle());
        Picasso.with(context).load(animalList.get(position).getImageUrl()).resize(80, 60).into(holder.animalImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimalDataRepository.getInstance().setAnimal(animalList.get(position));
                Log.d("===>", "Item click click ...");
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return animalList.size();
    }


    public void setItems(List<Animal> items) {
        animalList.clear();
        animalList.addAll(items);
    }
}
