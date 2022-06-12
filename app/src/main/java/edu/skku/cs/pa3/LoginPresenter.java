package edu.skku.cs.pa3;

public class LoginPresenter implements
        LoginContract.ContractForPresenter,
        LoginContract.ContractForModel.OnSuccessListener{
    private LoginContract.ContractForView view;
    private LoginContract.ContractForModel model;

    public LoginPresenter(LoginContract.ContractForView view,
                          LoginContract.ContractForModel model){
        this.view=view;
        this.model=model;
    }

    @Override
    public void onSuccess() {
        if(view!=null) view.toastMessage(model.getMsg());
    }

    @Override
    public void onLogInButtonTouched(String name, String pw) {
        model.login(this,name,pw);
    }

    @Override
    public void onRegisterButtonTouched(String name, String pw) {
        model.register(this,name,pw);
    }
}
