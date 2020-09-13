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

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.RecAdapterViewHolder> {

    String data[];
    Context context;

    public RecAdapter(Context ct, String terms[]) {
        data = terms;
        context = ct;
    }

    @NonNull
    @Override
    public RecAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);
        return new RecAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecAdapterViewHolder holder, int position) {
        holder.termName.setText(data[position]);
        holder.termDescription.setText("Description " + Integer.toString(position));
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(context, TermActivity.class);
                intent.putExtra("data", data[position]);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class RecAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView termName, termDescription;
        ConstraintLayout mainLayout;
        public RecAdapterViewHolder(@NonNull View itemView){
            super(itemView);
            termName = itemView.findViewById(R.id.term_name_txt);
            termDescription = itemView.findViewById(R.id.term_description_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}



