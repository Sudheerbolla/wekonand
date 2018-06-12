package com.weekend.models;

import java.io.Serializable;

/**
 * Created by hb on 9/12/16.
 */

public class ChatDialogModelObject implements Serializable {

    public int occupantId;
    public String name, login, password, photo;

    public ChatDialogModelObject(String name, int occupantId, String login, String password, String photo) {
        this.name = name;
        this.login = login;
        this.occupantId = occupantId;
        this.password = password;
        this.photo = photo;
    }

}
