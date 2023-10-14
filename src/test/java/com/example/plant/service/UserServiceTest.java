package com.example.plant.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.plant.domain.User;
import com.example.plant.dto.UserDto;
import com.example.plant.exception.PlantException;
import com.example.plant.repository.UserRepository;
import com.example.plant.type.ErrorCode;
import com.example.plant.type.UserStatus;
import com.example.plant.type.UserType;
import java.math.BigInteger;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  @Test
  @DisplayName("회원가입 성공")
  void successJoin() {
    // Given
    User user = User.builder()
        .loginId("loginId")
        .loginPassword("loginPassword")
        .nickName("nickName")
        .userType(UserType.GENERAL)
        .build();

    given(userRepository.save(any())).willReturn(user);
    // When
    UserDto saveUser = userService.userJoin(user.getLoginId(), user.getLoginPassword(), user.getNickName(),
        user.getUserType());

    // Then
    assertEquals(user.getLoginId(), saveUser.getLoginId());
    assertEquals(user.getLoginPassword(), saveUser.getLoginPassword());
    assertEquals(user.getUserType(), saveUser.getUserType());
    assertEquals(UserStatus.USED, saveUser.getUserStatus());
  }

  @Test
  @DisplayName("회원가입 실패 - 로그인아이디 중복")
  void failJoinDuplicateLoginId() {
    // Given
    User user = User.builder()
        .loginId("loginId")
        .loginPassword("loginPassword")
        .nickName("nickName")
        .userType(UserType.GENERAL)
        .build();

    given(userRepository.findByLoginIdAndUserStatus(anyString(), eq(UserStatus.USED)))
        .willReturn(Optional.of(user));

    // When
    PlantException exception = assertThrows(PlantException.class,
        () -> userService.userJoin(user.getLoginId(), user.getLoginPassword(), user.getNickName(), user.getUserType()));

    // Then
    assertEquals(ErrorCode.DUPLICATION_LOGINID, exception.getErrorCode());
  }

  @Test
  @DisplayName("회원가입 실패 - 닉네임 중복")
  void failJoinDuplicateNickName() {
    // Given
    User user = User.builder()
        .loginId("loginId")
        .loginPassword("loginPassword")
        .nickName("nickName")
        .userType(UserType.GENERAL)
        .build();

    given(userRepository.findByNickNameAndUserStatus(anyString(), eq(UserStatus.USED)))
        .willReturn(Optional.of(user));

    // When
    PlantException exception = assertThrows(PlantException.class,
        () -> userService.userJoin(user.getLoginId(), user.getLoginPassword(), user.getNickName(), user.getUserType()));

    // Then
    assertEquals(ErrorCode.DUPLICATION_NICKNAME, exception.getErrorCode());
  }

  @Test
  @DisplayName("사용자 닉네임 변경 성공")
  void successChangeNickName() {
    // Given
    User user = User.builder()
        .userId(BigInteger.ONE)
        .loginId("loginId")
        .loginPassword("loginPassword")
        .nickName("nickName")
        .userType(UserType.GENERAL)
        .userStatus(UserStatus.USED)
        .build();

    String newNickName = "newNickName";

    given(userRepository.findByUserId((BigInteger) any()))
        .willReturn(Optional.of(user));

    // When
    UserDto saveUser = userService.changeNickName(user.getUserId(), newNickName);

    // Then
    verify(userRepository, times(1)).save(user);

    assertEquals(user.getLoginId(), saveUser.getLoginId());
    assertEquals(newNickName, saveUser.getNickName());
  }

  @Test
  @DisplayName("사용자 닉네임 변경 실패 - 회원 상태가 UNUSED")
  void failChangeNickNameWithdrawnUser() {
    // Given
    User user = User.builder()
        .userId(BigInteger.ONE)
        .loginId("loginId")
        .loginPassword("loginPassword")
        .nickName("nickName")
        .userType(UserType.GENERAL)
        .userStatus(UserStatus.UNUSED)
        .build();

    String newNickName = "newNickName";

    given(userRepository.findByUserId((BigInteger) any()))
        .willReturn(Optional.of(user));

    // When
    PlantException exception = assertThrows(PlantException.class,
        () -> userService.changeNickName(user.getUserId(), newNickName));

    // Then
    assertEquals(ErrorCode.WITHDRAWN_USER, exception.getErrorCode());
  }

  @Test
  @DisplayName("사용자 닉네임 변경 실패 - 동일한 닉네임")
  void failChangeNickNameSameNickName() {
    // Given
    User user = User.builder()
        .userId(BigInteger.ONE)
        .loginId("loginId")
        .loginPassword("loginPassword")
        .nickName("nickName")
        .userType(UserType.GENERAL)
        .userStatus(UserStatus.USED)
        .build();

    String newNickName = "nickName";

    given(userRepository.findByUserId((BigInteger) any()))
        .willReturn(Optional.of(user));

    // When
    PlantException exception = assertThrows(PlantException.class,
        () -> userService.changeNickName(user.getUserId(), newNickName));

    // Then
    assertEquals(ErrorCode.SAME_NICKNAME, exception.getErrorCode());
  }

  @Test
  @DisplayName("회원 상태 변경 성공")
  void successChangeUserStatus() {
    // Given
    User user = User.builder()
        .userId(BigInteger.ONE)
        .loginId("loginId")
        .loginPassword("loginPassword")
        .nickName("nickName")
        .userType(UserType.GENERAL)
        .userStatus(UserStatus.USED)
        .build();

    given(userRepository.findByUserId((BigInteger) any()))
        .willReturn(Optional.of(user));

    // When
    UserDto saveUser = userService.changeUserStatus(user.getUserId(), UserStatus.UNUSED);

    // Then
    verify(userRepository, times(1)).save(user);

    assertEquals(user.getLoginId(), saveUser.getLoginId());
    assertEquals(UserStatus.UNUSED, saveUser.getUserStatus());
  }

  @Test
  @DisplayName("회원 상태 실패 - 동일한 회원 상태")
  void failChangeNickNameSameUserStatus() {
    // Given
    User user = User.builder()
        .userId(BigInteger.ONE)
        .loginId("loginId")
        .loginPassword("loginPassword")
        .nickName("nickName")
        .userType(UserType.GENERAL)
        .userStatus(UserStatus.UNUSED)
        .build();

    given(userRepository.findByUserId((BigInteger) any()))
        .willReturn(Optional.of(user));

    // When
    PlantException exception = assertThrows(PlantException.class,
        () -> userService.changeUserStatus(user.getUserId(), UserStatus.UNUSED));

    // Then
    assertEquals(ErrorCode.SAME_USER_STATUS, exception.getErrorCode());
  }

  @Test
  @DisplayName("개인 정보 조회")
  void getUserInfo() {
    // Given
    User user = User.builder()
        .userId(BigInteger.ONE)
        .loginId("loginId")
        .loginPassword("loginPassword")
        .nickName("nickName")
        .userType(UserType.GENERAL)
        .userStatus(UserStatus.USED)
        .build();

    given(userRepository.findByUserId((BigInteger) any()))
        .willReturn(Optional.of(user));

    // When
    User getUser = userService.userInfo(user.getUserId());

    // Then
    assertEquals(user.getUserId(), getUser.getUserId());
    assertEquals(user.getLoginPassword(), getUser.getLoginPassword());
    assertEquals(user.getUserType(), getUser.getUserType());
    assertEquals(user.getNickName(), getUser.getNickName());
    assertEquals(user.getUserStatus(), getUser.getUserStatus());
  }
}
