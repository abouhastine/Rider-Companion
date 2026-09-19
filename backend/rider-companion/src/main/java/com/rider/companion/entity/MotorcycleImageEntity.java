package com.rider.companion.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "motorcycle_images")
@Getter
@Setter
@NoArgsConstructor
public class MotorcycleImageEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "motorcycle_id", nullable = false, unique = true)
  private MotocycleEntity motorcycle;

  @Lob
  @Column(nullable = false)
  private byte[] content;

  @Column(nullable = false)
  private String contentType;

  @Column(nullable = false)
  private Long byteSize;

  @Column(nullable = false)
  private Instant updatedAt;
}
