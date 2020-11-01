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

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermAdapterViewHolder> {

    private static final String TAG = "IgB:TermAdapter";
    private List<Term> terms = new ArrayList<>();
    private OnTermClickListener termListener;


    @NonNull
    @Override
    public TermAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.term, parent, false);

        return new TermAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TermAdapterViewHolder holder, int position) {

        holder.termName.setText(terms.get(position).getTitle());
        holder.termStartDate.setText(terms.get(position).getStartDate().toString());
        holder.termEndDate.setText(terms.get(position).getEndDate().toString());

        /*
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(context, TermActivity.class);
//                intent.putExtra("data", data[position]);
                intent.putExtra("terms", terms.get(position));
                context.startActivity(intent);
            }
        });

         */
    }

    @Override
    public int getItemCount() {
        return terms.size();
    }

    public Term getTermAt(int position) {
        return terms.get(position);
    }

    public void setTerms(List<Term> terms){
        Log.i(TAG, "setTerms got called <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        this.terms = terms;
        notifyDataSetChanged();
    }

    public class TermAdapterViewHolder extends RecyclerView.ViewHolder{
        private static final String TAG = "IgB:TermAdapterViewHol";
        private TextView termName;
        private TextView termStartDate;
        private TextView termEndDate;
        //ConstraintLayout mainLayout;

        public TermAdapterViewHolder(@NonNull View itemView){
            super(itemView);
            Log.i(TAG, "started TermAdapterViewHolder constructor");
            termName = itemView.findViewById(R.id.text_view_name);
            termStartDate = itemView.findViewById(R.id.text_view_start_date);
            termEndDate = itemView.findViewById(R.id.text_view_end_date);
            //mainLayout = itemView.findViewById(R.id.mainLayout);
            Log.i(TAG, "started TermAdapterViewHolder constructor");

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(termListener != null && position != RecyclerView.NO_POSITION) {
                        termListener.onItemClick(terms.get(position));
                    }
                }
            });
        }
    }

    public interface OnTermClickListener{
        void onItemClick(Term term);
    }

    public void setOnTermClickListener(OnTermClickListener listener){
        this.termListener = listener;
    }
}



