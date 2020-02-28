package com.example.plandiabetes.Interface;

import com.example.plandiabetes.Class.Meal;

public interface MealActiItemClickListener {
    public void OnEdit(Meal meal,int index);
    public void OnDelete(Meal meal,int index);
}
