package com.example.bolmalre.common.apiPayLoad.exception.handler;


import com.example.bolmalre.common.apiPayLoad.code.BaseErrorCode;
import com.example.bolmalre.common.apiPayLoad.exception.GeneralException;

public class BookmarkHandler extends GeneralException {
    public BookmarkHandler(BaseErrorCode code) {
        super(code);
    }
}
