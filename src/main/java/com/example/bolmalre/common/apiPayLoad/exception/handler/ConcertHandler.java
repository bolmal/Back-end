package com.example.bolmalre.common.apiPayLoad.exception.handler;

import com.example.bolmalre.common.apiPayLoad.code.BaseErrorCode;
import com.example.bolmalre.common.apiPayLoad.exception.GeneralException;

public class ConcertHandler extends GeneralException {
    public ConcertHandler(BaseErrorCode code) {
        super(code);
    }
}
