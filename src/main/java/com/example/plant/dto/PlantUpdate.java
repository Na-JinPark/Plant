package com.example.plant.dto;

import com.example.plant.dto.PlantAdd.Response;
import com.example.plant.type.Status;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PlantUpdate {
  @Getter
  public static class Request {

    private String nickName;
    private String plantName;
    private Date firstDate;
    @NotNull
    private BigInteger plantId;
  }

  @Getter
  @Builder
  public static class Response {

    private String nickName;
    private String plantName;
    private String imgUrl;
    private Date firstDate;
    private Status plantStatus;

    public static PlantUpdate.Response from(PlantDto plantDto) {
      return PlantUpdate.Response.builder()
          .nickName(plantDto.getNickName())
          .plantName(plantDto.getPlantName())
          .imgUrl(plantDto.getImgUrl())
          .firstDate(plantDto.getFirstDate())
          .plantStatus(plantDto.getPlantStatus())
          .build();
    }
  }
}
