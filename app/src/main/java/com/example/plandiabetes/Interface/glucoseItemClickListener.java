package com.example.plandiabetes.Interface;

import com.example.plandiabetes.Class.Glucose;
import com.example.plandiabetes.ListView.GlucolseListView;

public interface glucoseItemClickListener {
    public void onItemClick(Glucose glucose);
    public void onEdit(Glucose glucose, int index);
    public void onDelete(Glucose glucose,int index);
}
