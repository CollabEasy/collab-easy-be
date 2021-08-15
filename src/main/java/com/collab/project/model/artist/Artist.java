package com.collab.project.model.artist;


import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import io.jsonwebtoken.lang.Strings;
import lombok.*;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Locale;

@Entity
@Table(name = "artists")
@Getter
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Artist implements Serializable {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private String artistId;

  @NonNull
  @Column(nullable = false)
  private String firstName;

  private String lastName;
  @NonNull
  @Column(nullable = false, unique = true)
  private String email;

  private String slug;

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

  public Artist() {
      updateSlug();
  }

  public Artist(String artistId, @NonNull String firstName, String lastName, @NonNull String email, String slug, Long phoneNumber, String country, String profilePicUrl, String timezone, String bio, Integer age, Timestamp lastActive, String gender, Timestamp createdAt, Timestamp updatedAt) {
    this.artistId = artistId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    updateSlug();
    this.phoneNumber = phoneNumber;
    this.country = country;
    this.profilePicUrl = profilePicUrl;
    this.timezone = timezone;
    this.bio = bio;
    this.age = age;
    this.lastActive = lastActive;
    this.gender = gender;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public void setFirstName(@NonNull String firstName) {
    this.firstName = firstName;
    updateSlug();
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
    updateSlug();
  }

  private void updateSlug() {
    if (this.firstName == null) return;
    if (this.lastName == null || this.lastName.isEmpty()) {
      this.slug = this.firstName.toLowerCase(Locale.ROOT);
      return;
    }
    this.slug = Strings.replace(firstName.toLowerCase(Locale.ROOT) + " " + lastName.toLowerCase(Locale.ROOT), " ", "-");
  }
}
