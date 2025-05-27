package com.example.bolmalre.global.apiPayLoad.exception.handler;

import com.example.bolmalre.global.apiPayLoad.code.BaseErrorCode;
import com.example.bolmalre.global.apiPayLoad.exception.GeneralException;

public class ImageHandler extends GeneralException {
    public ImageHandler(BaseErrorCode code) {
        super(code);
    }
}
