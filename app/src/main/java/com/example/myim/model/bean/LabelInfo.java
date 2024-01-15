package com.example.myim.model.bean;

import androidx.annotation.NonNull;

public class LabelInfo {
    private String label;

    public String getLabel() {
        return label;
    }
    public LabelInfo(String name){
        this.label=name;
    }
    public void setLabel(String label) {
        this.label = label;
    }

    @NonNull
    @Override
    public String toString() {
        return label;
    }
}
