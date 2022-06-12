package edu.skku.cs.pa3;

public interface LoginContract {
    interface ContractForView{
        void toastMessage(String msg);
    }
    interface ContractForModel{
        String getMsg();
        void register(OnSuccessListener listener, String name, String pw);
        void login(OnSuccessListener listener, String name, String pw);
        boolean checkValidity(String name, String pw);
        interface OnSuccessListener{
            void onSuccess();
        }
    }
    interface ContractForPresenter{
        void onLogInButtonTouched(String name, String pw);
        void onRegisterButtonTouched(String name, String pw);
    }
}
