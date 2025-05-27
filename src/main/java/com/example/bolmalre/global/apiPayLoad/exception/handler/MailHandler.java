package com.example.bolmalre.global.apiPayLoad.exception.handler;


import com.example.bolmalre.global.apiPayLoad.code.BaseErrorCode;
import com.example.bolmalre.global.apiPayLoad.exception.GeneralException;

public class MailHandler extends GeneralException {
    public MailHandler(BaseErrorCode code) {
        super(code);
    }
}
