package com.example.bolmalre.common.apiPayLoad.exception.handler;

import com.example.bolmalre.common.apiPayLoad.code.BaseErrorCode;
import com.example.bolmalre.common.apiPayLoad.exception.GeneralException;

public class TokenHandler extends GeneralException {
    public TokenHandler(BaseErrorCode code) {
        super(code);
    }
}
