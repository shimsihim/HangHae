package io.hhplus.tdd.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    CMM_BUSINESS_EXCEPTION(HttpStatus.BAD_REQUEST, "C0001" , "비즈니스 에러"),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U0001" , "유저를 찾을 수 없습니다."),
    USER_POINT_MUST_POSITIVE(HttpStatus.BAD_REQUEST, "U0002" , "포인트 충전 및 사용은 양수만 가능합니다."),
    USER_POINT_NOT_ENOUGH(HttpStatus.BAD_REQUEST, "U0003" , "잔액이 부족합니다.");


    private HttpStatus status;
    private String errMsg;
    private String errCode;

    ErrorCode(HttpStatus status , String errCode , String errMsg){
        this.status = status;
        this.errCode = errCode;
        this.errMsg = errMsg;
    }
}
