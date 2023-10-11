package com.example.plant.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  DUPLICATION_LOGINID("현재 사용중인 아이디 입니다."),
  DUPLICATION_NICKNAME("현재 사용중인 닉네임 입니다."),
  INVALID_SERVER_ERROR("내부 서버 오류가 발생 했습니다."),
  INVALID_REQUEST("잘못된 요청 입니다."),
  WITHDRAWN_USER("탈퇴한 사용자 입니다."),
  INVALID_USER("존재하지 않는 사용자 입니다."),
  SAME_USER_STATUS("회원 상태가 변경할려는 값과 동일합니다."),
  SAME_NICKNAME("닉네임이 변경할려는 값과 동일합니다.");

  private final String description;
}
