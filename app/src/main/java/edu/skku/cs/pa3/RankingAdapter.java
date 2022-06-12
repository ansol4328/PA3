package edu.skku.cs.pa3;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RankingAdapter extends BaseAdapter {
    private MapSelectionPresenter presenter;
    public RankingAdapter(MapSelectionPresenter presenter){
        this.presenter=presenter;
    }
    @Override
    public int getCount() {
        return presenter.getSize();
    }

    @Override
    public Object getItem(int i) {
        return presenter.getItem(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Context mContext=presenter.getContext();
        if(view==null){
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.ranking_info,viewGroup,false);
        }
        TextView rank = view.findViewById(R.id.rankingRank);
        TextView name = view.findViewById(R.id.rankingName);
        TextView sec = view.findViewById(R.id.rankingSec);
        UserRecord record = presenter.getItem(i);

        if(record.getName().equals(presenter.getName())) view.setBackgroundColor(0xFF99F691);
        rank.setText(record.getRank().toString());
        name.setText(record.getName());
        sec.setText(record.getSec().toString());
        return view;
    }
}
