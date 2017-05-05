package hva.groepje12.quitsmokinghabits.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import hva.groepje12.quitsmokinghabits.R;
import hva.groepje12.quitsmokinghabits.model.Game;

public class GameInfoAdapter extends ArrayAdapter<Game> {

    private TextView gameName;
    private TextView gameDescription;
    private ImageView gameIcon;

    private Game game;

    private ArrayList<Game> gameList;
    public GameInfoAdapter(Context context, ArrayList<Game> gameList) {
        super(context, 0, gameList);
        this.gameList = gameList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_recommend_games, parent, false);
        }

        game = getItem(position);

        gameName = (TextView) convertView.findViewById(R.id.gameName);
        gameDescription = (TextView) convertView.findViewById(R.id.gameDescription);
        gameIcon = (ImageView) convertView.findViewById(R.id.gameIcon);

        gameName.setText(game.getGameName());
        gameDescription.setText(game.getGameDescription());
        gameIcon.setImageDrawable(game.getGameIcon());

        return convertView;
    }

    @Override
    public int getCount() {
        return gameList.size();
    }

    @Override
    public Game getItem(int position){
        return gameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
