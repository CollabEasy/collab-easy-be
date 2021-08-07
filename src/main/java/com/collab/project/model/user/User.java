package com.collab.project.model.user;


import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class User implements Serializable {
  @Id
  String userId;

  @NonNull
  @Column(nullable = false, unique = true)
  String userHandle;
  @NonNull
  @Column(nullable = false)
  String firstName;

  String lastName;
  @NonNull
  @Column(nullable = false, unique = true)
  String email;

  Long phoneNumber;

  String country;

  String profilePicUrl;

  String timezone;

  String bio;

  Integer age;

  Long lastActive;

  String gender;

  Long createdAt;

  Long updatedAt;
}
