package com.example.plant.repository;

import com.example.plant.domain.Plant;
import com.example.plant.domain.User;
import com.example.plant.type.Status;
import java.math.BigInteger;
import java.util.BitSet;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantRepository extends JpaRepository<Plant, BigInteger> {

  Optional<Plant> findByUserUserIdAndNickNameAndPlantStatus(BigInteger userId, String nickName, Status plantStatus);

  List<Plant> findByUserUserIdAndPlantStatus(BigInteger userId, Status plantStatus);
}
