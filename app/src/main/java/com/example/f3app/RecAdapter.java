package com.example.f3app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.RecAdapterViewHolder> {

    private List<Term> terms = new ArrayList<Term>();
    Context context;


    @NonNull
    @Override
    public RecAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.term, parent, false);
        return new RecAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecAdapterViewHolder holder, int position) {

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

    public void setTerms(List<Term> terms){
        this.terms = terms;
        notifyDataSetChanged();
    }

    public class RecAdapterViewHolder extends RecyclerView.ViewHolder{
        private TextView termName;
        private TextView termStartDate;
        private TextView termEndDate;
        ConstraintLayout mainLayout;

        public RecAdapterViewHolder(@NonNull View itemView){
            super(itemView);
            termName = itemView.findViewById(R.id.text_view_name);
            termStartDate = itemView.findViewById(R.id.text_view_start_date);
            termEndDate = itemView.findViewById(R.id.text_view_end_date);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}



