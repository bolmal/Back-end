package com.example.bolmalre.common.apiPayLoad.exception.handler;

import com.example.bolmalre.common.apiPayLoad.code.BaseErrorCode;
import com.example.bolmalre.common.apiPayLoad.exception.GeneralException;

public class ImageHandler extends GeneralException {
    public ImageHandler(BaseErrorCode code) {
        super(code);
    }
}
