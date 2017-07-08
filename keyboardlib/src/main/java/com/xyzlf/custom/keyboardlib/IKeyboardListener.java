package com.xyzlf.custom.keyboardlib;

public interface IKeyboardListener {

    void onPasswordChange(String pwd);

    void onPasswordComplete(String pwd);

    void onForgetPwd();

}
