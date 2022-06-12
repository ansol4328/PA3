package edu.skku.cs.pa3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class MineMapAdapter extends BaseAdapter {
    private GamePresenter presenter;

    public MineMapAdapter(GamePresenter presenter){
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
        Integer level=presenter.getLevel();
        Context mContext= presenter.getContext();
        if(view==null){
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(level==0) view=layoutInflater.inflate(R.layout.large_cell,viewGroup,false);
            else view=layoutInflater.inflate(R.layout.small_cell,viewGroup,false);
        }
        Integer cellInfo = presenter.getItem(i);
        ImageView cell;
        if(level==0) cell=view.findViewById(R.id.largeCell);
        else cell=view.findViewById(R.id.smallCell);

        if(cellInfo==0) cell.setImageResource(R.drawable.zero);
        else if(cellInfo==1) cell.setImageResource(R.drawable.one);
        else if(cellInfo==2) cell.setImageResource(R.drawable.two);
        else if(cellInfo==3) cell.setImageResource(R.drawable.three);
        else if(cellInfo==4) cell.setImageResource(R.drawable.four);
        else if(cellInfo==5) cell.setImageResource(R.drawable.five);
        else if(cellInfo==6) cell.setImageResource(R.drawable.six);
        else if(cellInfo==7) cell.setImageResource(R.drawable.seven);
        else if(cellInfo==8) cell.setImageResource(R.drawable.eight);
        else if(cellInfo==9) cell.setImageResource(R.drawable.button);
        else if(cellInfo==-1) cell.setImageResource(R.drawable.mine);
        else if(cellInfo==10) cell.setImageResource(R.drawable.flag);
        else if(cellInfo==11) cell.setImageResource(R.drawable.wrong);

        return view;
    }
}
