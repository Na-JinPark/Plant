package com.example.plant.controller;

import com.example.plant.domain.Plant;
import com.example.plant.domain.User;
import com.example.plant.dto.PlantAdd;
import com.example.plant.dto.PlantDto;
import com.example.plant.dto.PlantInfo;
import com.example.plant.dto.PlantUpdate;
import com.example.plant.dto.UserJoin;
import com.example.plant.service.PlantService;
import jakarta.validation.Valid;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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

  /*
   * 식물편집 api
   * 파라미터 : 식물 별칭, 종류, 식물 사진, 키우기시작한 날자, 식물 아이디
   * 성공응답 : 식물 별칭, 종류, 식물 사진, 키우기시작한 날자, 식물 관리 여부
   */
  @PostMapping("/plant/update")
  public PlantUpdate.Response plantUpdate(
      @RequestBody @Valid PlantUpdate.Request request
  ) {
    return PlantUpdate.Response.from(
        plantService.plantUpdate(
            request.getNickName(),
            request.getPlantName(),
            request.getFirstDate(),
            request.getPlantId())
    );
  }

  /*
   * 관리 식물 삭제 api
   * 파라미터 : 식물 별칭, 종류, 식물 사진, 키우기시작한 날자, 식물 아이디
   * 성공응답 : 식물 별칭, 종류, 식물 사진, 키우기시작한 날자, 식물 관리 여부
   */
  @PostMapping("/plant/delete")
  public PlantInfo.Response plantDelete(
      @RequestBody @Valid PlantInfo.Request request
  ) {
    return PlantInfo.Response.from(
        plantService.plantDelete(request.getPlantId())
    );
  }

  /*
   * 관리 식물 조회 api
   * 파라미터 : 식물 아이디
   * 성공응답 : 식물 별칭, 종류, 식물 사진, 키우기시작한 날자, 식물 관리 여부
   */
  @GetMapping("/plant/{plantId}")
  public PlantInfo.Response plantInfo(
      @PathVariable BigInteger plantId
  ) {
    return PlantInfo.Response.from(
        PlantDto.fromEntity(plantService.plantInfo(plantId))
    );
  }

  /*
   * 관리 식물리스트 조회 api
   * 파라미터 : 사용자 아이디
   * 성공응답 : 식물 별칭, 종류, 식물 사진, 키우기시작한 날자, 식물 관리 여부
   */
  @GetMapping("/plant/list/{userId}")
  public List<PlantDto> plantList(
      @PathVariable BigInteger userId
  ){
    return plantService.getPlantList(userId)
        .stream().map(plantDto ->
            PlantDto.builder()
                .plantId(plantDto.getPlantId())
                .nickName(plantDto.getNickName())
                .plantName(plantDto.getPlantName())
                .imgUrl(plantDto.getImgUrl())
                .firstDate(plantDto.getFirstDate())
                .plantStatus(plantDto.getPlantStatus())
                .userId(plantDto.getUserId())
                .build())
        .collect(Collectors.toList());
  }
}
