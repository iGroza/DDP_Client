package ru.kirill.ag.ddp;

public class UsernameAuth extends PasswordAuth
{
	//создает объект типа логин:пароль для подключения к серверу Meteor
	//используется в качестве входных параметров медота 'login'
    public UsernameAuth(String username, String pw) {
        super(pw);
        assert(username != null);
        this.user.put("username", username);
    }
}
