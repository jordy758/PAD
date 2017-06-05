package hva.groepje12.quitsmokinghabits.util;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import hva.groepje12.quitsmokinghabits.R;
import hva.groepje12.quitsmokinghabits.model.Goal;

public class GoalsAdapter extends ArrayAdapter<Goal> {
    public GoalsAdapter(Context context, ArrayList<Goal> goals) {
        super(context, 0, goals);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Goal goal = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_goal, parent, false);
        }

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);

        tvTitle.setPaintFlags(0);
        tvDescription.setPaintFlags(0);

        tvTitle.setText(goal.getGoal());
        tvDescription.setText("Voor dit doel is: " + goal.getFormattedPrice() + " euro nodig!");

        if (goal.getAchievedAt() != null) {
            tvTitle.setPaintFlags(tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvDescription.setPaintFlags(tvDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        return convertView;
    }
}
