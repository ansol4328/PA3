package edu.skku.cs.pa3;

import android.content.Context;

import java.util.ArrayList;

public interface GameContract {
    interface ContractForView{
        void updateRemain(Integer rem);
        void updateTime(Integer t);
        void updateUserMap();
        void toastFinish(String msg);
    }
    interface ContractForModel{
        void openCell(Integer idx, GameContract.ContractForModel.onSuccessListener listener);
        void openMultiCell(Integer idx, GameContract.ContractForModel.onSuccessListener listener);
        void calculateTime(GameContract.ContractForModel.onSuccessListener listener);
        void uploadRecord(GameContract.ContractForModel.onSuccessListener listener);
        void createMineMap(GameContract.ContractForModel.onSuccessListener listener);
        void setFlag(int i, GameContract.ContractForModel.onSuccessListener listener);
        void removeFlag(int i, GameContract.ContractForModel.onSuccessListener listener);
        interface onSuccessListener{
            void updateTime();
            void updateUserMap();
            void toastFinish();
            void updateRemain();
        }
        ArrayList<Integer> getMapInfo();
        Integer getRemain();
        Integer getTime();
        String getMsg();
        boolean getFinished();
        Integer getSize();
        Integer getItem(int i);
        void setContext(Context context);
        Context getContext();
        Integer getLevel();
        void open(int i);
    }
    interface ContractForPresenter{
        void upRemain(int i);
        void downRemain(int i);
        void startTime();
        void createMineMap();
        void initRemain();
        Integer getSize();
        Integer getItem(int i);
        void setContext(Context context);
        Context getContext();
        Integer getLevel();
        void openCell(int i);
        void openMultiCell(int i);
        boolean getFinished();
    }
}
