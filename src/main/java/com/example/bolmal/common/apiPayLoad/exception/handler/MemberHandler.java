package com.example.bolmal.common.apiPayLoad.exception.handler;

import com.example.bolmal.common.apiPayLoad.code.BaseErrorCode;
import com.example.bolmal.common.apiPayLoad.exception.GeneralException;

public class MemberHandler extends GeneralException {
    public MemberHandler(BaseErrorCode code) {
        super(code);
    }
}
