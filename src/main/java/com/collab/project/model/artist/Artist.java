package com.collab.project.model.artist;


import com.collab.project.security.Role;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import java.util.Objects;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Transient;
import lombok.*;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import org.springframework.util.StringUtils;

@Entity
@Table(name = "artists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Artist implements Serializable {

    @Id
    private String artistId;

    @Column(nullable = true, unique = true)
    private String artistHandle;
    private String slug;
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

    @Transient
    private Boolean newUser = false;

    @Transient
    public Role getRole() {
        return Role.DEFAULT;
    }

    @Transient
    public Boolean areDetailsUpdated() {
        return !StringUtils.isEmpty(getPhoneNumber());
    }

    public Boolean getNewUser() {
        return newUser;
    }

    public void setNewUser(Boolean newUser) {
        this.newUser = newUser;
    }









}
