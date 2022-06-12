package edu.skku.cs.pa3;

import android.content.Context;

public class GamePresenter implements
    GameContract.ContractForPresenter,
    GameContract.ContractForModel.onSuccessListener{

    private GameContract.ContractForView view;
    private GameContract.ContractForModel model;

    public GamePresenter(GameContract.ContractForView view,
                         GameContract.ContractForModel model){
        this.view=view;
        this.model=model;
    }

    @Override
    public void updateTime() {
        if(view!=null) view.updateTime(model.getTime());
    }

    @Override
    public void updateUserMap() {
        if(view!=null) view.updateUserMap();
    }

    @Override
    public void toastFinish() {
        if(view!=null) view.toastFinish(model.getMsg());
    }

    @Override
    public void updateRemain() {
        if(view!=null) view.updateRemain(model.getRemain());
    }

    @Override
    public void upRemain(int i) {
        model.removeFlag(i,this);
    }

    @Override
    public void downRemain(int i) {
        model.setFlag(i,this);
    }

    @Override
    public void startTime() {
        model.calculateTime(this);
    }

    @Override
    public void createMineMap() {
        model.createMineMap(this);
    }

    @Override
    public void initRemain() {
        if(view!=null) view.updateRemain(model.getRemain());
    }

    @Override
    public Integer getSize() {
        return model.getSize();
    }

    @Override
    public Integer getItem(int i) {
        return model.getItem(i);
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
    public Integer getLevel() {
        return model.getLevel();
    }

    @Override
    public void openCell(int i) {
        model.openCell(i,this);
    }

    @Override
    public void openMultiCell(int i) {
        model.openMultiCell(i,this);
    }

    @Override
    public boolean getFinished() {
        return model.getFinished();
    }
}
