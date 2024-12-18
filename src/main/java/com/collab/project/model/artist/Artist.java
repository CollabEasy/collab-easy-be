package com.collab.project.model.artist;


import com.collab.project.security.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import java.sql.Date;
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

    private String state;
    private String city;

    private String profilePicUrl;

    private String timezone;

    private String bio;

    private Integer age;

    private Timestamp lastActive;

    private String gender;

    private Date dateOfBirth;

    @Column
    private String countryIso;

    @Column
    private String countryDial;

    @Column(name="created_at", updatable = false, insertable = false, nullable = false)
    private Timestamp createdAt;

    @Column(name="updated_at", updatable = false, insertable = false, nullable = false)
    private Timestamp updatedAt;

    @Column
    private Boolean newUser;

    @Column
    private Boolean basicInfoComplete;

    @Column
    Boolean isReferralDone;

    @Column
    private Boolean testUser;

    @Column
    private Boolean profileComplete;

    @Column
    @JsonIgnore
    private Integer profileBits;

    @Column
    private String referralCode;

    @Transient
    public Role getRole() {
        return Role.DEFAULT;
    }

    @Transient
    public Boolean areDetailsUpdated() {
        this.newUser = StringUtils.isEmpty(getPhoneNumber());
        return !StringUtils.isEmpty(getPhoneNumber());
    }

    public void setNewUser(Boolean newUser) {
        this.newUser = newUser;
    }

    public void setTestUser(Boolean testUser) {
        this.testUser = testUser;
    }

}
