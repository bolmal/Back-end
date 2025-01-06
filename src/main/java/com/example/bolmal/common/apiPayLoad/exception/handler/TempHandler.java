package com.example.bolmal.common.apiPayLoad.exception.handler;


import com.example.bolmal.common.apiPayLoad.code.BaseErrorCode;
import com.example.bolmal.common.apiPayLoad.exception.GeneralException;

public class TempHandler extends GeneralException {

    public TempHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}