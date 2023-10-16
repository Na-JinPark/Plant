package com.example.plant.domain;

import com.example.plant.type.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;
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
@Table(name = "plant")
@EntityListeners(AuditingEntityListener.class)
public class Plant {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private BigInteger plantId;

  private String nickName;

  private String plantName;

  private String imgUrl;

  private Date firstDate;

  @Default
  @Enumerated(EnumType.STRING)
  private Status plantStatus = Status.USED;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @CreatedDate
  private LocalDateTime createdTime;
  @LastModifiedDate
  private LocalDateTime updatedTime;
}
