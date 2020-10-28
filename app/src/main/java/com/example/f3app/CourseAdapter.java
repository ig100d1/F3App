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

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseAdapterViewHolder> {

    private static final String TAG = "IgB:CourseAdapter";
    private List<Course> courses = new ArrayList<>();
    private OnCourseClickListener courseListener;

    @NonNull
    @Override
    public CourseAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.course, parent, false);

        return new CourseAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapterViewHolder holder, int position) {

        holder.courseName.setText(courses.get(position).getTitle());
        holder.courseStartDate.setText(courses.get(position).getStartDate().toString());
        holder.courseEndDate.setText(courses.get(position).getEndDate().toString());
        holder.courseStatus.setText(courses.get(position).getStatus().toString());

    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public Course getCourseAt(int position) {
        return courses.get(position);
    }

    public void setCourses(List<Course> courses){
        Log.i(TAG, "setCourses got called ");
        this.courses = courses;
        notifyDataSetChanged();
    }

    public class CourseAdapterViewHolder extends RecyclerView.ViewHolder{

        private static final String TAG = "IGOR:CourseAdapterVH";
        private TextView courseName;
        private TextView courseStartDate;
        private TextView courseEndDate;
        private TextView courseStatus;

        public CourseAdapterViewHolder(@NonNull View itemView){
            super(itemView);
            Log.i(TAG, "started CourseAdapterViewHolder constructor");
            courseName = itemView.findViewById(R.id.text_view_name);
            courseStartDate = itemView.findViewById(R.id.text_view_start_date);
            courseEndDate = itemView.findViewById(R.id.text_view_end_date);
            courseStatus= itemView.findViewById(R.id.text_view_status);

            Log.i(TAG, "started CourseAdapterViewHolder constructor");

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(courseListener != null && position != RecyclerView.NO_POSITION) {
                        courseListener.onItemClick(courses.get(position));
                    }
                }
            });
        }
    }

    public interface OnCourseClickListener{
        void onItemClick(Course course);
    }

    public void setOnCourseClickListener(OnCourseClickListener listener){
        this.courseListener = listener;
    }
}

