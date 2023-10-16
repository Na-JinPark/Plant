package com.example.plant.service;

import static com.example.plant.type.ErrorCode.DUPLICATION_LOGINID;
import static com.example.plant.type.ErrorCode.DUPLICATION_NICKNAME;
import static com.example.plant.type.ErrorCode.INVALID_USER;
import static com.example.plant.type.ErrorCode.SAME_NICKNAME;
import static com.example.plant.type.ErrorCode.SAME_USER_STATUS;
import static com.example.plant.type.ErrorCode.WITHDRAWN_USER;

import com.example.plant.domain.User;
import com.example.plant.dto.UserDto;
import com.example.plant.exception.PlantException;
import com.example.plant.repository.UserRepository;
import com.example.plant.type.UserStatus;
import com.example.plant.type.UserType;
import java.math.BigInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  /*
   * 회원가입
   * 파라미터 : 사용자 아이디, 비밀번호, 닉네임, 유형
   * 정책 : 사용자 아이디, 닉네임 중복시 실패 응답
   */
  public UserDto userJoin(String loginId, String loginPassword, String nickName, UserType userType) {

    validationUserJoin(loginId, nickName);

    return UserDto.fromEntity(
        userRepository.save(User.builder()
            .loginId(loginId)
            .loginPassword(loginPassword)
            .nickName(nickName)
            .userType(userType)
            .build())
    );
  }

  /*
   * 사용자 닉네임 변경
   * 파라미터 : 사용자 아이디, 비밀번호, 닉네임, 유형
   * 정책 : 회원상태가 UNUSED 이거나 동일한 닉네임으로 변경하려는 경우 실패 응답
   */
  public UserDto changeNickName(BigInteger userId, String newNickName) {

    User user = getUser(userId);

    userStatusChk(user);
    if (user.getNickName().equals(newNickName)) {
      throw new PlantException(SAME_NICKNAME);
    }

    user.setNickName(newNickName);
    userRepository.save(user);

    return UserDto.fromEntity(user);
  }

  /*
   * 회원 상태 변경
   * 파라미터 : 사용자 아이디, 회원 상태
   * 정책 : 동일한 회원상태일 경우 실패 응답
   *       회원 탈퇴를 하더라도 한달안에 활성화 가능하도록 추가할 예정
   */
  public UserDto changeUserStatus(BigInteger userId, UserStatus userStatus) {

    User user = getUser(userId);
    if (user.getUserStatus().equals(userStatus)) {
      throw new PlantException(SAME_USER_STATUS);
    }

    user.setUserStatus(userStatus);
    userRepository.save(user);

    return UserDto.fromEntity(user);
  }

  /*
   * 사용자 정보 조회
   * 파라미터 : 로그인아이디
   */
  public User userInfo(BigInteger userId) {
    User user = getUser(userId);
    userStatusChk(user);
    return user;
  }

  private User getUser(BigInteger userId) {
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new PlantException(INVALID_USER));

    return user;
  }

  private void userStatusChk(User user) {
    if (user.getUserStatus().equals(UserStatus.UNUSED)) {
      throw new PlantException(WITHDRAWN_USER);
    }
  }

  private void validationUserJoin(String loginId, String nickName) {
    if (userRepository.findByLoginIdAndUserStatus(loginId, UserStatus.USED).isPresent()) {
      throw new PlantException(DUPLICATION_LOGINID);
    }
    if (userRepository.findByNickNameAndUserStatus(nickName, UserStatus.USED).isPresent()) {
      throw new PlantException(DUPLICATION_NICKNAME);
    }
  }

}
