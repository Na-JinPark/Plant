package com.example.plant.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.plant.domain.Plant;
import com.example.plant.domain.User;
import com.example.plant.dto.PlantDto;
import com.example.plant.dto.UserDto;
import com.example.plant.exception.PlantException;
import com.example.plant.repository.PlantRepository;
import com.example.plant.repository.UserRepository;
import com.example.plant.type.ErrorCode;
import com.example.plant.type.Status;
import com.example.plant.type.UserStatus;
import com.example.plant.type.UserType;
import java.math.BigInteger;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlantServiceTest {

  @Mock
  private PlantRepository plantRepository;

  @Mock
  private UserService userService;

  @InjectMocks
  private PlantService plantService;

  @Test
  @DisplayName("관리 식물 등록 성공")
  void successPlantAdd() {
    // Given
    User user = User.builder()
        .userId(BigInteger.ONE)
        .loginId("loginId")
        .loginPassword("loginPassword")
        .nickName("nickName")
        .userType(UserType.GENERAL)
        .build();

    Plant plant = Plant.builder()
        .nickName("베리베리")
        .plantName("산세베리아")
        .firstDate(new Date(2023-04-05))
        .user(user)
        .build();

    given(userService.userInfo( any()))
        .willReturn(user);
    given(plantRepository.save(any())).willReturn(plant);
    // When
    PlantDto savePlant = plantService.plantAdd(plant.getNickName(), plant.getPlantName(), plant.getFirstDate(), user.getUserId());

    // Then
    assertEquals(plant.getNickName(), savePlant.getNickName());
    assertEquals(plant.getPlantName(), savePlant.getPlantName());
    assertEquals(plant.getFirstDate(), savePlant.getFirstDate());
    assertEquals(Status.USED, savePlant.getPlantStatus());
    assertEquals(user.getUserId(), savePlant.getUserId());
  }

  @Test
  @DisplayName("관리 식물 등록 실패 - 중복 별칭")
  void failPlantAddSameNickName() {
    // Given
    User user = User.builder()
        .userId(BigInteger.ONE)
        .loginId("loginId")
        .loginPassword("loginPassword")
        .nickName("nickName")
        .userType(UserType.GENERAL)
        .build();

    Plant plant = Plant.builder()
        .nickName("베리베리")
        .plantName("산세베리아")
        .firstDate(new Date(2023-04-05))
        .user(user)
        .build();

    given(userService.userInfo(any()))
        .willReturn(user);
    given(plantRepository.findByUser_UserIdAndNickNameAndPlantStatus(user.getUserId(), plant.getNickName(), Status.USED))
        .willReturn(Optional.of(plant));
    // When
    PlantException exception = assertThrows(PlantException.class,
        () -> plantService.plantAdd(plant.getNickName(), plant.getPlantName(), plant.getFirstDate(), user.getUserId()));
    // Then
    assertEquals(ErrorCode.PLANT_SAME_NICKNAME, exception.getErrorCode());
  }

  @Test
  @DisplayName("관리 식물 수정 성공")
  void successPlantUpdate() {
    // Given
    User user = User.builder()
        .userId(BigInteger.ONE)
        .loginId("loginId")
        .loginPassword("loginPassword")
        .nickName("nickName")
        .userType(UserType.GENERAL)
        .build();

    Plant plant = Plant.builder()
        .nickName("베리베리")
        .plantName("산세베리아")
        .firstDate(new Date(2023-04-05))
        .plantId(BigInteger.ONE)
        .user(user)
        .build();

    given(plantRepository.findById(any()))
        .willReturn(Optional.of(plant));

    // When
    PlantDto savePlant = plantService.plantUpdate("베리베리", null,null, BigInteger.ONE);

    // Then
    verify(plantRepository, times(1)).save(plant);

    assertEquals(plant.getNickName(), savePlant.getNickName());
    assertEquals(plant.getPlantName(), savePlant.getPlantName());
    assertEquals(plant.getFirstDate(), savePlant.getFirstDate());
  }

  @Test
  @DisplayName("관리 식물 수정 실패 - 식물 관리 여부가 UNUSED")
  void failPlantUpdateStatusUnused() {
    // Given
    User user = User.builder()
        .userId(BigInteger.ONE)
        .loginId("loginId")
        .loginPassword("loginPassword")
        .nickName("nickName")
        .userType(UserType.GENERAL)
        .build();

    Plant plant = Plant.builder()
        .nickName("베리베리")
        .plantName("산세베리아")
        .firstDate(new Date(2023-04-05))
        .plantId(BigInteger.ONE)
        .user(user)
        .plantStatus(Status.UNUSED)
        .build();

    given(plantRepository.findById(any()))
        .willReturn(Optional.of(plant));

    // When
    PlantException exception = assertThrows(PlantException.class,
        () -> plantService.plantUpdate("베리베리", null,null, BigInteger.ONE));

    // Then
    assertEquals(ErrorCode.UNUSED_PLANT_INFORMATION, exception.getErrorCode());
  }

  @Test
  @DisplayName("관리 식물 수정 실패 - 중복 별칭")
  void failPlantUpdateSameNickName() {
    // Given
    User user = User.builder()
        .userId(BigInteger.ONE)
        .loginId("loginId")
        .loginPassword("loginPassword")
        .nickName("nickName")
        .userType(UserType.GENERAL)
        .build();

    Plant plant = Plant.builder()
        .nickName("베리베리")
        .plantName("산세베리아")
        .firstDate(new Date(2023-04-05))
        .plantId(BigInteger.ONE)
        .user(user)
        .plantStatus(Status.USED)
        .build();

    given(plantRepository.findById(any()))
        .willReturn(Optional.of(plant));

    given(plantRepository.findByUser_UserIdAndNickNameAndPlantStatus(any(),any(),any()))
        .willReturn(Optional.of(plant));

    // When
    PlantException exception = assertThrows(PlantException.class,
        () -> plantService.plantUpdate("베리베리", null,null, BigInteger.ONE));

    // Then
    assertEquals(ErrorCode.PLANT_SAME_NICKNAME, exception.getErrorCode());
  }

  @Test
  @DisplayName("관리 식물 삭제 성공")
  void successPlantDelete() {
    // Given
    User user = User.builder()
        .userId(BigInteger.ONE)
        .loginId("loginId")
        .loginPassword("loginPassword")
        .nickName("nickName")
        .userType(UserType.GENERAL)
        .build();

    Plant plant = Plant.builder()
        .nickName("베리베리")
        .plantName("산세베리아")
        .firstDate(new Date(2023-04-05))
        .plantId(BigInteger.ONE)
        .user(user)
        .build();

    given(plantRepository.findById(any()))
        .willReturn(Optional.of(plant));

    // When
    PlantDto savePlant = plantService.plantDelete(plant.getPlantId());

    // Then
    verify(plantRepository, times(1)).save(plant);

    assertEquals(savePlant.getPlantStatus(), Status.UNUSED);
  }
}