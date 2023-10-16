package com.example.plant.domain;

import com.example.plant.type.UserStatus;
import com.example.plant.type.UserType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigInteger;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private BigInteger userId;

  private String loginId;

  private String loginPassword;

  @Enumerated(EnumType.STRING)
  private UserType userType;

  private String nickName;

  @Default
  @Enumerated(EnumType.STRING)
  private UserStatus userStatus = UserStatus.USED;

  @CreatedDate
  private LocalDateTime createdTime;
  @LastModifiedDate
  private LocalDateTime updatedTime;

}
