package com.example.plandiabetes.Interface;

import com.example.plandiabetes.Class.Medication;

public interface MedicationItemCLickListener {
    public void onItemClick(Medication medication);
    public void onEdit(Medication medication, int index);
    public void onDelete(Medication medication,int index);
}
