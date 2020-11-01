package com.example.f3app;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentAdapterViewHolder> {

    private static final String TAG = "IgB:AssessmentAdapter";
    private List<Assessment> assessments = new ArrayList<>();
    private OnAssessmentClickListener assessmentListener;

    @NonNull
    @Override
    public AssessmentAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.assessment, parent, false);

        return new AssessmentAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentAdapterViewHolder holder, int position) {
        holder.assessmentName.setText(assessments.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return assessments.size();
    }

    public Assessment getAssessmentAt(int position) {
        return assessments.get(position);
    }

    public void setAssessments(List<Assessment> assessments){
        Log.i(TAG, "setAssessments got called ");
        this.assessments = assessments;
        notifyDataSetChanged();
    }

    public class AssessmentAdapterViewHolder extends RecyclerView.ViewHolder{

        private static final String TAG = "IgB:AssmentAdapViewHold";
        private TextView assessmentName;

        public AssessmentAdapterViewHolder(@NonNull View itemView){
            super(itemView);
            Log.i(TAG, "started AssessmentAdapterViewHolder constructor");
            assessmentName = itemView.findViewById(R.id.text_view_name);

            Log.i(TAG, "started AssessmentAdapterViewHolder constructor");

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(assessmentListener != null && position != RecyclerView.NO_POSITION) {
                        assessmentListener.onItemClick(assessments.get(position));
                    }
                }
            });
        }
    }

    public interface OnAssessmentClickListener{
        void onItemClick(Assessment assessment);
    }

    public void setOnAssessmentClickListener(OnAssessmentClickListener listener){
        this.assessmentListener = listener;
    }
}

