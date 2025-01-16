package com.example.bolmal.common.apiPayLoad.exception.handler;

import com.example.bolmal.common.apiPayLoad.code.BaseErrorCode;
import com.example.bolmal.common.apiPayLoad.exception.GeneralException;

public class MailHandler extends GeneralException {
    public MailHandler(BaseErrorCode code) {
        super(code);
    }
}
