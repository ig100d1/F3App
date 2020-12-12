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

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteAdapterViewHolder> {

    private static final String TAG = "IgB:NoteAdapter";
    private List<Note> notes = new ArrayList<>();
    private OnNoteClickListener noteListener;

    @NonNull
    @Override
    public NoteAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.note, parent, false);

        return new NoteAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapterViewHolder holder, int position) {
        String title = notes.get(position).getTitle().trim();
        if (title.isEmpty()) {
            holder.noteNote.setText(notes.get(position).getNote());
        }else{
            holder.noteNote.setText(title);
        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public Note getNoteAt(int position) {
        return notes.get(position);
    }

    public void setNotes(List<Note> notes){
        Log.i(TAG, "setNotes got called ");
        this.notes = notes;
        notifyDataSetChanged();
    }

    public class NoteAdapterViewHolder extends RecyclerView.ViewHolder{

        private static final String TAG = "IgB:AssmentAdapViewHold";
        private TextView noteNote;

        public NoteAdapterViewHolder(@NonNull View itemView){
            super(itemView);
            Log.i(TAG, "started NoteAdapterViewHolder constructor");
            noteNote = itemView.findViewById(R.id.text_view_note);

            Log.i(TAG, "started NoteAdapterViewHolder constructor");

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(noteListener != null && position != RecyclerView.NO_POSITION) {
                        noteListener.onItemClick(notes.get(position));
                    }
                }
            });
        }
    }

    public interface OnNoteClickListener{
        void onItemClick(Note note);
    }

    public void setOnNoteClickListener(OnNoteClickListener listener){
        this.noteListener = listener;
    }
}

