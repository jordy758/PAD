package hva.groepje12.quitsmokinghabits.util;

import android.content.Context;
import android.media.Image;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import hva.groepje12.quitsmokinghabits.R;
import hva.groepje12.quitsmokinghabits.model.Game;

/**
 * Created by lucas on 5-5-2017.
 */

public class GameInfoAdapter extends ArrayAdapter<Game> {
    private ArrayList<Game> gameList;
    public GameInfoAdapter(Context context, ArrayList<Game> games) {
        super(context, 0, games);
        this.gameList = games;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Game game = getItem(position);


        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_recommend_games, parent, false);
        }
        // Lookup view for data population
        TextView gameName = (TextView) convertView.findViewById(R.id.gameName);
        TextView gameDescription = (TextView) convertView.findViewById(R.id.gameDescription);
        ImageView gameIcon = (ImageView) convertView.findViewById(R.id.gameIcon);
        // Populate the data into the template view using the data object
        gameName.setText(game.getGameName());
        gameDescription.setText(game.getGameDescription());
        gameIcon.setImageDrawable(game.getGameIcon());
        // Return the completed view to render on screen
        return convertView;
    }


    @Override
    public int getCount() {
        return gameList.size();
    }

    public Game getItem(int position){
        return gameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
