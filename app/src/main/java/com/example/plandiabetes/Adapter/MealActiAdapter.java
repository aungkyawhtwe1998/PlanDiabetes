package com.example.plandiabetes.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.plandiabetes.Class.Exercise;
import com.example.plandiabetes.Class.Meal;
import com.example.plandiabetes.Interface.MealActiItemClickListener;
import com.example.plandiabetes.Interface.exeItemClickListener;
import com.example.plandiabetes.ListView.ExerciseListView;
import com.example.plandiabetes.ListView.MealListView;
import com.example.plandiabetes.R;

import java.util.List;

public class MealActiAdapter extends RecyclerView.Adapter<MealActiAdapter.ViewHolder> {
    List<Meal> mealList;
    Context context;
    private MealActiItemClickListener itemSelectedListener;
    public MealActiAdapter(List<Meal> mealList, Context context) {
        this.mealList = mealList;
        this.context = context;
        this.itemSelectedListener= (MealActiItemClickListener) context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_cardview,parent,false);
        //ExerciseAdapter.ViewHolder holder=new ExerciseAdapter.ViewHolder(view);

        return new MealActiAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txttotcarb.setText(mealList.get(position).getTotcarb().toString()+"Carbs");
        holder.txtdate.setText(mealList.get(position).getDate().toString());
        holder.txttime.setText(mealList.get(position).getTime().toString());
        holder.onDelete(itemSelectedListener);
        holder.onEdit(itemSelectedListener);
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txttotcarb,txtdate,txttime;
        MaterialRippleLayout cardmeal;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtdate=itemView.findViewById(R.id.txt_meallistdate);
            txttime=itemView.findViewById(R.id.txt_meallisttime);
            txttotcarb=itemView.findViewById(R.id.txt_mealTotCarb);
            cardmeal=itemView.findViewById(R.id.mrlmeal);

        }
        public void onEdit(final MealActiItemClickListener listener){
            cardmeal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    listener.OnEdit(mealList.get(position),position);
                }
            });
        }
        public void onDelete(final MealActiItemClickListener listener){
            cardmeal.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int positon=getAdapterPosition();
                    listener.OnDelete(mealList.get(positon),positon);
                    return true;
                }
            });
        }
    }
}
