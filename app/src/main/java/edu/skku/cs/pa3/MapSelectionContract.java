package edu.skku.cs.pa3;

import android.content.Context;

import java.util.ArrayList;

public interface MapSelectionContract {
    interface ContractForView{
        void updateView(Integer level, ArrayList<UserRecord> records);
        void startGame(Integer level);
    }
    interface ContractForModel{
        Integer getLevel();
        String getName();
        ArrayList<UserRecord> getRecords();
        void setName(String name);
        void nextTouched(MapSelectionContract.ContractForModel.OnSuccessListener listener);
        void prevTouched(MapSelectionContract.ContractForModel.OnSuccessListener listener);
        void startTouched(MapSelectionContract.ContractForModel.OnSuccessListener listener);
        void setRecords(MapSelectionContract.ContractForModel.OnSuccessListener listener);
        void init(MapSelectionContract.ContractForModel.OnSuccessListener listener);
        interface OnSuccessListener{
            void onSuccess();
            void onStart();
        }
        UserRecord getItem(int i);
        void setContext(Context context);
        Context getContext();
    }
    interface ContractForPresenter{
        void init();
        void onNextButtonTouched();
        void onPrevButtonTouched();
        void onStartButtonTouched();
        void sendUserName(String name);
        String getName();
        void setContext(Context context);
        Context getContext();
        UserRecord getItem(int i);
        Integer getSize();
        Integer getLevel();
    }
}
