package com.example.plandiabetes.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.plandiabetes.Class.Medication;
import com.example.plandiabetes.Interface.MedicationItemCLickListener;
import com.example.plandiabetes.ListView.MedicationListView;
import com.example.plandiabetes.R;

import java.util.List;

public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.ViewHolder> {
    List<Medication> medicationList;
    Context context;
    private MedicationItemCLickListener itemCLickListener;

    public MedicationAdapter(List<Medication> medicationList, Context context) {
        this.medicationList = medicationList;
        this.context = context;
        this.itemCLickListener=(MedicationListView)context;
    }

    @NonNull
    @Override
    public MedicationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.medication_cardview,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MedicationAdapter.ViewHolder holder, int position) {
        holder.txtmedPower.setText(medicationList.get(position).getNote());
        holder.txtmedName.setText(medicationList.get(position).getName());
        holder.txtdate.setText(medicationList.get(position).getDate());
        holder.txttime.setText(medicationList.get(position).getTime());

        holder.Ediit(itemCLickListener);
        holder.onDelete(itemCLickListener);
    }

    @Override
    public int getItemCount() {
        return medicationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialRippleLayout mrlmedi;
        TextView txtdate,txttime,txtmedName,txtmedPower;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mrlmedi=(MaterialRippleLayout)itemView.findViewById(R.id.mrlmedi);
            txtdate=(TextView)itemView.findViewById(R.id.txt_medate);
            txttime=(TextView)itemView.findViewById(R.id.txt_metime);
            txtmedName=(TextView)itemView.findViewById(R.id.txt_mename);
            txtmedPower=(TextView)itemView.findViewById(R.id.txt_mepower);
        }
        public void Ediit(final MedicationItemCLickListener listener){
            if(listener!=null){
                mrlmedi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int positon=getAdapterPosition();
                        listener.onEdit(medicationList.get(positon),positon);
                    }
                });
            }
        }
        public void onDelete(final MedicationItemCLickListener listener){
            if(listener!=null){
                mrlmedi.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int positon=getAdapterPosition();
                        listener.onDelete(medicationList.get(positon),positon);
                        return true;
                    }
                });
            }
        }
    }
}
