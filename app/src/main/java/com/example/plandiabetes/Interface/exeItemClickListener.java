package com.example.plandiabetes.Interface;

import com.example.plandiabetes.Class.Exercise;

public interface exeItemClickListener {
    public void onItemSelected(Exercise exercise);
    public void onEdit(Exercise exercise,int index);
    public void onDelete(Exercise exercise,int index);
}
