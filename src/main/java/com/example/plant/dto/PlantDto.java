package com.example.plant.dto;

import com.example.plant.domain.Plant;
import com.example.plant.type.Status;
import java.math.BigInteger;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlantDto {

  private BigInteger plantId;
  private String nickName;
  private String plantName;
  private String imgUrl;
  private Date firstDate;
  private Status plantStatus;
  private BigInteger userId;

  public static PlantDto fromEntity(Plant plant) {
    return PlantDto.builder()
        .plantId(plant.getPlantId())
        .nickName(plant.getNickName())
        .plantName(plant.getPlantName())
        .imgUrl(plant.getImgUrl())
        .firstDate(plant.getFirstDate())
        .plantStatus(plant.getPlantStatus())
        .userId(plant.getUser().getUserId())
        .build();
  }
}
