package edu.skku.cs.pa3;

import android.content.Context;
import android.util.Log;

public class MapSelectionPresenter implements
    MapSelectionContract.ContractForPresenter,
    MapSelectionContract.ContractForModel.OnSuccessListener{

    private MapSelectionContract.ContractForView view;
    private MapSelectionContract.ContractForModel model;

    public MapSelectionPresenter(MapSelectionContract.ContractForView view,
                                 MapSelectionContract.ContractForModel model){
        this.view=view;
        this.model=model;
    }
    @Override
    public void onSuccess() {
        if(view!=null) view.updateView(model.getLevel(),model.getRecords());
    }

    @Override
    public void onStart() {
        if(view!=null) view.startGame(model.getLevel());
    }

    @Override
    public void init() {
        model.init(this);
    }

    @Override
    public void onNextButtonTouched() {
        model.nextTouched(this);
    }

    @Override
    public void onPrevButtonTouched() {
        model.prevTouched(this);
    }

    @Override
    public void onStartButtonTouched() {
        model.startTouched(this);
    }

    @Override
    public void sendUserName(String name) {
        model.setName(name);
    }

    @Override
    public String getName() {
        return model.getName();
    }

    @Override
    public void setContext(Context context) {
        model.setContext(context);
    }

    @Override
    public Context getContext() {
        return model.getContext();
    }

    @Override
    public UserRecord getItem(int i) {
        return model.getItem(i);
    }

    @Override
    public Integer getSize() {
        return model.getRecords().size();
    }

    @Override
    public Integer getLevel() {
        return model.getLevel();
    }
}
