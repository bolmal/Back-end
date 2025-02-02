package com.example.bolmalre.common.apiPayLoad.code.status;


import com.example.bolmalre.common.apiPayLoad.code.BaseErrorCode;
import com.example.bolmalre.common.apiPayLoad.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    MEMBER_IMAGE_COUNT_ERROR(HttpStatus.BAD_REQUEST,"IMAGE4003","프로필 이미지는 한 장만 등록 가능합니다"),
    MEMBER_IMAGE_EXIST(HttpStatus.BAD_REQUEST,"IMAGE4001","프로필 이미지가 이미 등록되어 있습니다"),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND,"IMAGE4002","이미지를 찾을 수 없습니다"),
    IMAGE_CONVERT_ERROR(HttpStatus.SERVICE_UNAVAILABLE,"IMAGE5001","이미지 변환 중 오류가 발생하였습니다"),
    IMAGE_REMOVE_ERROR(HttpStatus.SERVICE_UNAVAILABLE,"IMAGE5002","이미지 삭제 중 오류가 발생하였습니다"),
    IMAGE_UPLOAD_ERROR(HttpStatus.BAD_GATEWAY,"IMAGE5004","이미지 등록 중 오류가 발생하였습니다"),
    IMAGE_DOWNLOAD_ERROR(HttpStatus.SERVICE_UNAVAILABLE,"IMAGE5003","이미지 다운로드 중 오류가 발생하였습니다"),

    BOOKMARK_NOT_EXIST(HttpStatus.BAD_REQUEST,"BOOKMARK4003","찜한 아티스트가 존재하지 않습니다"),
    BOOKMARK_EXIST(HttpStatus.BAD_REQUEST,"BOOKMARK4002","이미 찜하고 있는 아티스트 입니다"),
    BOOKMARK_ACCOUNT_ZERO(HttpStatus.BAD_REQUEST,"BOOKMARK4001","찜 가능 횟수가 0입니다"),

    ARTIST_NOT_FOUND(HttpStatus.NOT_FOUND,"ARTIST4001","아티스트를 찾을 수 없습니다"),

    MAIL_NOT_VALID(HttpStatus.BAD_REQUEST,"MAIL4002","인증번호가 일치하지 않습니다"),
    MAIL_NOT_SEND(HttpStatus.NOT_FOUND,"MAIL4001","요청받지 못한 이메일 입니다"),

    MEMBER_PASSWORD_VALID(HttpStatus.BAD_REQUEST,"MEMBER4009","회원 비밀번호 검증에 실패하였습니다"),
    MEMBER_ALREADY_ACTIVE(HttpStatus.BAD_REQUEST,"MEMBER4008","회원이 이미 활성 상태입니다"),
    MEMBER_ALREADY_INACTIVE(HttpStatus.BAD_REQUEST,"MEMBER4007","회원이 이미 비활성 상태입니다"),
    MEMBER_PASSWORD_DUPLICATE(HttpStatus.BAD_REQUEST,"MEMBER4006","비밀번호가 변경되지 않았습니다"),
    MEMBER_NOT_INACTIVE(HttpStatus.BAD_REQUEST,"MEMBER4005","이미 비활성화 상태인 회원입니다"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,"MEMBER4004","회원을 찾을 수 없습니다"),
    MEMBER_PHONE_NUMBER_AUTHENTICATE(HttpStatus.BAD_REQUEST,"MEMBER4003","전화번호 인증에 실패하였습니다."),
    MEMBER_AGREEMENT(HttpStatus.BAD_REQUEST,"MEMBER4002","필수 약관에는 모두 동의를 해주셔야 합니다."),
    MEMBER_USERNAME_DUPLICATE(HttpStatus.BAD_REQUEST,"MEMBER4001","중복된 username입니다");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}