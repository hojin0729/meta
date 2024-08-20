package com.metaro.metaro._core.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "METARO-000", "Internal server error"),
    EMPTY_EMAIL_MEMBER(HttpStatus.NOT_FOUND, "USER-001", "해당 이메일은 회원 가입 되지 않은 이메일입니다."),
    SAME_EMAIL(HttpStatus.CONFLICT, "USER-002", "이미 가입된 이메일입니다."),
    EMAIL_STRUCTURE(HttpStatus.FORBIDDEN,"USER-003","이메일 형식으로 작성해주세요"),
    INVALID_PASSWORD(HttpStatus.CONFLICT, "USER-004", "비밀번호가 유효하지 않습니다."),
    INVALID_EMAIL_CODE(HttpStatus.CONFLICT, "USER-005", "이메일 인증 코드가 일치하지 않습니다."),
    INVALID_EMAIL(HttpStatus.CONFLICT, "USER-006", "이메일이 인증되지 않았습니다."),
    EMAIL_NON_EXIST(HttpStatus.CONFLICT, "USER-007", "해당 이메일의 회원을 찾을 수 없습니다."),
    NO_SUCH_ALGORITHM(HttpStatus.INTERNAL_SERVER_ERROR, "EMAIL-001", "사용 가능한 암호화 알고리즘을 찾을 수 없습니다."),
    FAILED_VALIDATE_ACCESS_TOKEN(HttpStatus.EXPECTATION_FAILED, "TOKEN-001", "유효하지 않은 Access Token 입니다."),
    FAILED_VALIDATE__REFRESH_TOKEN(HttpStatus.EXPECTATION_FAILED, "TOKEN-002", "유효하지 않은 Refresh Token 입니다."),
    FAILED_GET_ACCESS_TOKEN(HttpStatus.EXPECTATION_FAILED, "TOKEN-003", "Access Token 을 가져오는데 실패했습니다."),
    IS_NOT_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "TOKEN-004", "Refresh Token 이 아닙니다."),
    FAILED_GET_KAKAO_PROFILE(HttpStatus.EXPECTATION_FAILED, "SOCIAL-001", "Kakao 유저 프로필을 가져오는데 실패했습니다."),
    FAILED_GET_RERFRESH_TOKEN(HttpStatus.BAD_REQUEST, "TOKEN-004", "Refresh Token 을 얻을 수 없습니다."),
    FAILED_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "AUTH-001", "인증에 실패하였습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "AUTH-002", "접근이 거부되었습니다."),
    DIFFERENT_IP_ADDRESS(HttpStatus.BAD_REQUEST, "AUTH-003", "기존 IP 주소와 다른 IP 주소에서의 요청입니다."),
    ANONYMOUS_USER(HttpStatus.UNAUTHORIZED, "AUTH-004", "익명의 유저가 접근하였습니다."),
    CANT_LOAD_MBTI_INTERIM_RESULT(HttpStatus.BAD_REQUEST, "MBTI-001", "중간 결과 없이 최종 결과를 가져올 수 없습니다."),
    FAILED_GET_MASCOT_BY_TYPE(HttpStatus.BAD_REQUEST, "MASCOT-001", "해당 MBTI Type 의 Mascot 를 가져올 수 없습니다."),
    FAILED_GET_MEMBER_BY_ID(HttpStatus.BAD_REQUEST, "MEMBER-001", "해당 ID의 Member 를 조회할 수 없습니다."),
    NOT_ALL_QUESTIONS_ANSWERED(HttpStatus.BAD_REQUEST, "MBTI-002", "질문에 대한 답변이 비어있습니다.");

    /*
        예시)
        SAME_EMAIL(HttpStatus.CONFLICT, "USER-001", "이미 가입한 이메일입니다.")
        EMAIL_STRUCTURE(HttpStatus.FORBIDDEN,"USER-002","이메일 형식으로 작성해주세요")

        이렇게 여기 정의하고 throw new ApplicationException(ErrorCode.SAME_EMAIL); 던지면 됨
    */

    private HttpStatus status;
    private String errorCode;
    private String message;
}
