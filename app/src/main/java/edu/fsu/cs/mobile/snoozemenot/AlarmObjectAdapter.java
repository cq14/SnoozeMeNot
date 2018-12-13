package edu.fsu.cs.mobile.snoozemenot;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

public class AlarmObjectAdapter extends RecyclerView.Adapter<AlarmObjectAdapter.MyViewHolder>{

    private List<AlarmObject> alarmObjectList;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title, time, ampm;
        public Switch alarmSwitch;
        public MyViewHolder(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.textView);
            time = (TextView) view.findViewById(R.id.time_card_text);
            alarmSwitch = (Switch) view.findViewById(R.id.alarm_switch);
            ampm = (TextView) view.findViewById(R.id.AM_PM_card_text);
        }
    }

    public AlarmObjectAdapter(List<AlarmObject> alarmObjectList){
        this.alarmObjectList = alarmObjectList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.alarm_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        AlarmObject alarmObject = alarmObjectList.get(i);
        myViewHolder.time.setText(alarmObject.getTime());
        myViewHolder.title.setText(alarmObject.getTitle());
        myViewHolder.alarmSwitch.setChecked(alarmObject.getOn());
        myViewHolder.ampm.setText(alarmObject.getAmPM());
    }

    @Override
    public int getItemCount() {
        return alarmObjectList.size();
    }
}
