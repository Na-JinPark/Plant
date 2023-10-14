package com.example.plant.service;

import static com.example.plant.type.ErrorCode.PLANT_SAME_NICKNAME;

import com.example.plant.domain.Plant;
import com.example.plant.domain.User;
import com.example.plant.dto.PlantDto;
import com.example.plant.exception.PlantException;
import com.example.plant.repository.PlantRepository;
import com.example.plant.type.Status;
import java.math.BigInteger;
import java.util.Date;
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
  public PlantDto plantAdd(String nickName, String plantName, Date firstDate, BigInteger userId){

    User user = userService.userInfo(userId);

    validationPlant(nickName, userId);

    return PlantDto.fromEntity(
        plantRepository.save(Plant.builder()
            .nickName(nickName)
            .plantName(plantName)
            .firstDate(firstDate)
            .user(user)
            .build())
    );
  }

  private void validationPlant(String nickName, BigInteger userId){
    if(plantRepository.findByUser_UserIdAndNickNameAndPlantStatus(userId,nickName, Status.USED).isPresent()){
        throw new PlantException(PLANT_SAME_NICKNAME);
    }
  }
}
