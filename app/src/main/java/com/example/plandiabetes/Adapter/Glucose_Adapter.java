package com.example.plandiabetes.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.plandiabetes.Activity.GlucoseActi;
import com.example.plandiabetes.Class.Glucose;
import com.example.plandiabetes.Interface.glucoseItemClickListener;
import com.example.plandiabetes.ListView.GlucolseListView;
import com.example.plandiabetes.R;

import java.util.List;


public class Glucose_Adapter extends RecyclerView.Adapter<Glucose_Adapter.ViewHolder> {
    private List<Glucose> glucoseList;
    private Context context;
    private glucoseItemClickListener itemClickListener;

    public Glucose_Adapter(List<Glucose> glucoseList, Context context) {
        this.glucoseList = glucoseList;
        this.context = context;
        this.itemClickListener=(GlucolseListView)context;
    }

    @NonNull
    @Override
    public Glucose_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.glucose_cardview,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Glucose_Adapter.ViewHolder holder, int position) {
        holder.txtgamount.setText(""+glucoseList.get(position).getGlucoseLevel()+" "+glucoseList.get(position).getUnit());
        holder.txtgdate.setText(""+glucoseList.get(position).getDate());
        holder.txtgperiod.setText(""+glucoseList.get(position).getPeriod());
        holder.txtgtime.setText(""+glucoseList.get(position).getTime());

        holder.onEdit(itemClickListener);
        holder.onDelete(itemClickListener);
    }

    @Override
    public int getItemCount() {
        return glucoseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialRippleLayout glucoseCardView;
        TextView txtgamount,txtgdate,txtgtime,txtgperiod;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtgamount=(TextView)itemView.findViewById(R.id.txt_exname);
            txtgdate=(TextView)itemView.findViewById(R.id.txt_exedate);
            txtgtime=(TextView)itemView.findViewById(R.id.txt_metime);
            txtgperiod=(TextView)itemView.findViewById(R.id.txt_mepower);
            glucoseCardView=(MaterialRippleLayout)itemView.findViewById(R.id.mrlglucose);
        }
        public void onEdit(final glucoseItemClickListener listener){
            if(listener!=null){
                glucoseCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position=getAdapterPosition();
                        listener.onEdit(glucoseList.get(position),position);
                    }
                });
            }
        }
        public void onDelete(final glucoseItemClickListener listener){
            if(listener!=null){
                glucoseCardView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int position=getAdapterPosition();
                        listener.onDelete(glucoseList.get(position),position);
                        return true;
                    }
                });
            }
        }
    }
}
