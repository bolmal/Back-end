package com.example.bolmalre.common.apiPayLoad.exception.handler;


import com.example.bolmalre.common.apiPayLoad.code.BaseErrorCode;
import com.example.bolmalre.common.apiPayLoad.exception.GeneralException;

public class ArtistHandler extends GeneralException {
    public ArtistHandler(BaseErrorCode code) {
        super(code);
    }
}
