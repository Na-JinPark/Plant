package com.example.plant.repository;

import com.example.plant.domain.User;
import com.example.plant.type.UserStatus;
import java.math.BigInteger;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, BigInteger> {

  Optional<User> findByLoginIdAndUserStatus(String loginId, UserStatus userStatus);

  Optional<User> findByNickNameAndUserStatus(String nickName, UserStatus userStatus);

  Optional<User> findByUserId(BigInteger userId);
}
