package com.weekend.server;

/**
 * Created by hb on 20-May-16.
 */
public interface IParser<T> {
    //On success
    void successResponse(int requestCode, T response);

    //On error
    void errorResponse(int requestCode, Throwable t);

    //No internet
    void noInternetConnection(int requestCode);

}
