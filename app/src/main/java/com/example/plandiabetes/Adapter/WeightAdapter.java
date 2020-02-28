package com.example.plandiabetes.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.plandiabetes.Class.Weight;
import com.example.plandiabetes.Interface.MealActiItemClickListener;
import com.example.plandiabetes.Interface.WeightItemClickListener;
import com.example.plandiabetes.R;

import java.util.List;

public class WeightAdapter extends RecyclerView.Adapter<WeightAdapter.ViewHolder> {
    List<Weight> wlist;
    Context context;
    private WeightItemClickListener itemSelectedListener;

    public WeightAdapter(List<Weight> wlist, Context context) {
        this.wlist = wlist;
        this.context = context;
        this.itemSelectedListener= (WeightItemClickListener) context;
    }

    @NonNull
    @Override
    public WeightAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.weightcardview,parent,false);
        //ExerciseAdapter.ViewHolder holder=new ExerciseAdapter.ViewHolder(view);

        return new WeightAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeightAdapter.ViewHolder holder, int position) {

        holder.txtweight.setText(wlist.get(position).getWeight().toString()+" lb");
        holder.txttime.setText(wlist.get(position).getTime().toString());
        holder.txtdate.setText(wlist.get(position).getDate().toString());
        holder.onEdit(itemSelectedListener);
        holder.onDelete(itemSelectedListener);
    }

    @Override
    public int getItemCount() {
        return wlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtdate,txttime,txtweight;
        MaterialRippleLayout mrlweight;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtdate=itemView.findViewById(R.id.txt_wdate);
            txttime=itemView.findViewById(R.id.txt_wtime);
            txtweight=itemView.findViewById(R.id.txt_weight);
            mrlweight=itemView.findViewById(R.id.mrlweight);
        }
        public void onEdit(final WeightItemClickListener listener){
            mrlweight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    listener.OnEdit(wlist.get(position),position);
                }
            });
        }
        public void onDelete(final WeightItemClickListener listener) {
            mrlweight.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int positon = getAdapterPosition();
                    listener.OnDelete(wlist.get(positon), positon);
                    return true;
                }
            });
        }
    }
}
