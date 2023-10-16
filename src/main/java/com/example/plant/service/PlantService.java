package com.example.plant.service;

import static com.example.plant.type.ErrorCode.NO_PLANT_INFORMATION;
import static com.example.plant.type.ErrorCode.PLANT_SAME_NICKNAME;
import static com.example.plant.type.ErrorCode.UNUSED_PLANT_INFORMATION;

import com.example.plant.domain.Plant;
import com.example.plant.domain.User;
import com.example.plant.dto.PlantDto;
import com.example.plant.exception.PlantException;
import com.example.plant.repository.PlantRepository;
import com.example.plant.type.Status;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlantService {

  private final PlantRepository plantRepository;
  private final UserService userService;

  /*
   * 관리 식물 추가
   * 파라미터 : 식물 별칭, 종류, 사진, 키우기 시작한날짜, 사용자아이디
   * 정책 : 사용자는 여러 식물을 등록할 수 있으나 동일한 별칭으로 등록할 경우 실패 응답
   */
  public PlantDto plantAdd(String nickName, String plantName, Date firstDate, BigInteger userId) {

    User user = userService.userInfo(userId);

    chkPlantNickName(nickName, userId);

    return PlantDto.fromEntity(
        plantRepository.save(Plant.builder()
            .nickName(nickName)
            .plantName(plantName)
            .firstDate(firstDate)
            .user(user)
            .build())
    );
  }

  /*
   * 관리 식물 편집
   * 파라미터 : 식물 별칭, 종류, 사진, 키우기 시작한날짜, 식물 관리 여부
   * 정책 : 식물아이디가 존재 하지 않는 경우, 식물 관리 여부가 UNUSED인 경우, 변경할려는 별칭이 존재하는 경우 실패 응답
   */
  public PlantDto plantUpdate(String nickName, String plantName, Date firstDate, BigInteger plantId) {

    Plant plant = plantInfo(plantId);

    if (nickName != null) {
      chkPlantNickName(nickName, plant.getUser().getUserId());
      plant.setNickName(nickName);
    }
    if (plantName != null) {
      plant.setPlantName(plantName);
    }
    if (firstDate != null) {
      plant.setFirstDate(firstDate);
    }

    plantRepository.save(plant);

    return PlantDto.fromEntity(plant);
  }

  /*
   * 관리 식물 삭제
   * 파라미터 : 식물 아이디
   * 정책 : 식물아이디가 존재 하지 않는 경우, 식물 관리 여부가 이미 UNUSED인 경우 실패 응답
   */
  public PlantDto plantDelete(BigInteger plantId) {

    Plant plant = plantInfo(plantId);
    plant.setPlantStatus(Status.UNUSED);
    plantRepository.save(plant);

    return PlantDto.fromEntity(plant);
  }


  public Plant plantInfo(BigInteger plantId) {
    Plant plant = plantRepository.findById(plantId)
        .orElseThrow(() -> new PlantException(NO_PLANT_INFORMATION));
    if (!plantStatus(plant)) {
      throw new PlantException(UNUSED_PLANT_INFORMATION);
    }
    return plant;
  }

  public List<PlantDto> getPlantList(BigInteger userId) {
    User user = userService.userInfo(userId);

    List<Plant> plants = plantRepository
        .findByUserUserIdAndPlantStatus(user.getUserId(), Status.USED);

    return plants.stream()
        .map(PlantDto::fromEntity)
        .collect((Collectors.toList()));
  }

  private boolean plantStatus(Plant plant) {
    if (plant.getPlantStatus().equals(Status.USED)) {
      return true;
    } else {
      return false;
    }
  }

  private void chkPlantNickName(String nickName, BigInteger userId) {
    if (plantRepository.findByUserUserIdAndNickNameAndPlantStatus(userId, nickName, Status.USED).isPresent()) {
      throw new PlantException(PLANT_SAME_NICKNAME);
    }
  }
}
