package com.example.plant.controller;

import com.example.plant.dto.PlantAdd;
import com.example.plant.dto.PlantDto;
import com.example.plant.dto.UserJoin;
import com.example.plant.service.PlantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PlantController {
  private final PlantService plantService;

  /*
   * 식물등록 api
   * 파라미터 : 식물 별칭, 종류, 식물 사진, 키우기시작한 날자, 사용자아이디
   * 성공응답 : 식물 별칭, 종류, 식물 사진, 키우기시작한 날자, 사용자아이디
   */
  @PostMapping("/plant/add")
  public PlantAdd.Response plantAdd(
      @RequestBody @Valid PlantAdd.Request request
  ) {
    return PlantAdd.Response.from(
        plantService.plantAdd(
            request.getNickName(),
            request.getPlantName(),
            request.getFirstDate(),
            request.getUserId())
    );
  }

}
