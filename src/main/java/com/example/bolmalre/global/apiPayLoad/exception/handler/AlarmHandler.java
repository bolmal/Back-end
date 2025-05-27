package com.example.bolmalre.global.apiPayLoad.exception.handler;

import com.example.bolmalre.global.apiPayLoad.code.BaseErrorCode;
import com.example.bolmalre.global.apiPayLoad.exception.GeneralException;

public class AlarmHandler extends GeneralException {
    public AlarmHandler(BaseErrorCode code) {
        super(code);
    }
}
