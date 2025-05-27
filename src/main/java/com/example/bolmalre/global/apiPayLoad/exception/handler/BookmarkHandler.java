package com.example.bolmalre.global.apiPayLoad.exception.handler;


import com.example.bolmalre.global.apiPayLoad.code.BaseErrorCode;
import com.example.bolmalre.global.apiPayLoad.exception.GeneralException;

public class BookmarkHandler extends GeneralException {
    public BookmarkHandler(BaseErrorCode code) {
        super(code);
    }
}
