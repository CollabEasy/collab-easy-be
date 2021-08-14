package com.collab.project.model.artist;


import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import lombok.*;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "artists")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Artist implements Serializable {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private String artistId;

  @NonNull
  @Column(nullable = false, unique = true)
  private String artistHandle;
  @NonNull
  @Column(nullable = false)
  private String firstName;

  private String lastName;
  @NonNull
  @Column(nullable = false, unique = true)
  private String email;

  Long phoneNumber;

  private String country;

  private String profilePicUrl;

  private String timezone;

  private String bio;

  private Integer age;

  private Timestamp lastActive;

  private String gender;

  private Timestamp createdAt;

  private Timestamp updatedAt;
}
