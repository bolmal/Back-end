package com.example.bolmalre.common.apiPayLoad.exception.handler;

import com.example.bolmalre.common.apiPayLoad.code.BaseErrorCode;
import com.example.bolmalre.common.apiPayLoad.exception.GeneralException;

public class AlarmHandler extends GeneralException {
    public AlarmHandler(BaseErrorCode code) {
        super(code);
    }
}
