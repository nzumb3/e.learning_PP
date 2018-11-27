package de.tudarmstadt.informatik.tudas;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.tudarmstadt.informatik.tudas.model.Appointment;

public class AppointmentListAdapter extends RecyclerView.Adapter<AppointmentListAdapter.AppointmentViewHolder> {

    class AppointmentViewHolder extends RecyclerView.ViewHolder {
        private final TextView appointmentItemView;

        private AppointmentViewHolder(View itemView) {
            super(itemView);
            appointmentItemView = itemView.findViewById(R.id.textView);
        }
    }

    private final LayoutInflater mInflater;
    private List<Appointment> mWords; // Cached copy of words

    AppointmentListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public AppointmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new AppointmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AppointmentViewHolder holder, int position) {
        if (mWords != null) {
            Appointment current = mWords.get(position);
            holder.appointmentItemView.setText(current.toString());
        } else {
            // Covers the case of data not being ready yet.
            holder.appointmentItemView.setText("No Word");
        }
    }

    void setAppointment(List<Appointment> words){
        mWords = words;
        notifyDataSetChanged();
    }

        // getItemCount() is called many times, and when it is first called,
        // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mWords != null)
            return mWords.size();
        else return 0;
    }
}
