package com.example.plandiabetes.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.plandiabetes.Activity.SearchFood;
import com.example.plandiabetes.Class.Foods;
import com.example.plandiabetes.Interface.RecyclerItemSelectedListener;
import com.example.plandiabetes.R;

import java.util.List;

public class Foodadapter extends RecyclerView.Adapter<Foodadapter.ViewHolder> {
    private Context context;
    private List<Foods> foodsList;
    private RecyclerItemSelectedListener itemSelectedListener;

    public Foodadapter(Context context, List<Foods> foodsList) {
        this.context = context;
        this.foodsList = foodsList;
        itemSelectedListener=(SearchFood)context;
    }

    @NonNull
    @Override
        public Foodadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.food_cardview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Foodadapter.ViewHolder holder, int position) {
    holder.txtfood.setText((position+1)+". "+foodsList.get(position).getName());
    holder.txtdes.setText(foodsList.get(position).getServingSize()+"gram = "+foodsList.get(position).getCal()+"Cal, "+foodsList.get(position).getCarbs()+"Carbs, "+foodsList.get(position).getSugar()+"sugar.");
    String type=foodsList.get(position).getType();
    if(type.equals("ဟင်းသီးဟင်းရွက်"))
    {
        holder.imgfodd.setImageResource(R.drawable.ic_salad);
    }
    if (type.equals("ေဖျာ်ရည်"))
    {
        holder.imgfodd.setImageResource(R.drawable.ic_diet);
    }
    if (type.equals("အသားငါး"))
    {
        holder.imgfodd.setImageResource(R.drawable.ic_fish);
    }
    if (type.equals("အသီး"))
    {
        holder.imgfodd.setImageResource(R.drawable.ic_farm);
    }
    if (type.equals("ပဲအမျိုးမျိုး"))
    {
        holder.imgfodd.setImageResource(R.drawable.ic_beans);
    }
    if (type.equals("နို့ထွက်ပစ္စည်း နှင့် ဥအမျိုးမျိုး"))
    {
        holder.imgfodd.setImageResource(R.drawable.ic_dairy);
    }
    if (type.equals("ထမင်းနှင့်ဂျုံ"))
    {
        holder.imgfodd.setImageResource(R.drawable.ic_rice);
    }
    holder.onClick(itemSelectedListener);

    holder.cardView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            itemSelectedListener.onItemSelected(foodsList.get(position));
        }
    });
    }

    @Override
    public int getItemCount() {
        return foodsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgfodd;
        TextView txtfood,txtdes;
        MaterialRippleLayout cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgfodd = itemView.findViewById(R.id.img_food);
            txtfood = itemView.findViewById(R.id.txt_food);
            txtdes=itemView.findViewById(R.id.txt_des);
            cardView = itemView.findViewById(R.id.card_food);


        }
        public void onClick(final RecyclerItemSelectedListener listener){
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    listener.OnClick(foodsList.get(position),position);

                }
            });
        }


        /*public void updateList(List<Foods> newFood){
            foodsList.clear();
            foodsList.addAll(newFood);
            notifyDataSetChanged();
        }*/
    }
}
