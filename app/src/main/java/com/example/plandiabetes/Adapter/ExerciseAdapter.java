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
import com.example.plandiabetes.Interface.exeItemClickListener;
import com.example.plandiabetes.ListView.ExerciseListView;
import com.example.plandiabetes.R;
import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {
    List<Exercise> exerciseList;
    Context context;
    private exeItemClickListener itemSelectedListener;
    public ExerciseAdapter(List<Exercise> exerciseList, Context context) {
        this.exerciseList = exerciseList;
        this.context = context;
        this.itemSelectedListener= (ExerciseListView) context;
    }

    @NonNull
    @Override
    public ExerciseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_cardview,parent,false);
        //ExerciseAdapter.ViewHolder holder=new ExerciseAdapter.ViewHolder(view);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseAdapter.ViewHolder holder, int position) {
        holder.edtdate.setText(exerciseList.get(position).getDate());
        holder.edtdistance.setText(""+exerciseList.get(position).getMinute()+"minutes");
        holder.edtname.setText(""+exerciseList.get(position).getType());
        holder.edttime.setText(exerciseList.get(position).getTime());

        holder.onEdit(itemSelectedListener);
        holder.onDelete(itemSelectedListener);

    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialRippleLayout exeCard;
        TextView edtdate,edttime,edtname,edtdistance;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edtdate=(TextView) itemView.findViewById(R.id.txt_exedate);
            edttime=(TextView)itemView.findViewById(R.id.txt_exetime);
            edtname=(TextView)itemView.findViewById(R.id.txt_exename);
            edtdistance=(TextView)itemView.findViewById(R.id.txt_exedist);
            exeCard= (MaterialRippleLayout) itemView.findViewById(R.id.mrl_exe);
        }
        public void onEdit(final exeItemClickListener listener){
            exeCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    listener.onEdit(exerciseList.get(position),position);
                }
            });
        }
        public void onDelete(final exeItemClickListener listener){
            exeCard.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position=getAdapterPosition();
                    listener.onDelete(exerciseList.get(position),position);
                    return true;
                }
            });
        }
    }
}
