package com.example.web;

import io.reactivex.rxjava3.core.*;

public class HttpUtilsRxJava {

    public static Observable<String> getResponseLinesRx(String urlString) throws Exception {
        return Observable.fromStream(HttpUtils.getResponseLines(urlString));
    }

}
