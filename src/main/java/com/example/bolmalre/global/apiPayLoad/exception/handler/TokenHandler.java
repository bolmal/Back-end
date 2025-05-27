package com.example.bolmalre.global.apiPayLoad.exception.handler;

import com.example.bolmalre.global.apiPayLoad.code.BaseErrorCode;
import com.example.bolmalre.global.apiPayLoad.exception.GeneralException;

public class TokenHandler extends GeneralException {
    public TokenHandler(BaseErrorCode code) {
        super(code);
    }
}
