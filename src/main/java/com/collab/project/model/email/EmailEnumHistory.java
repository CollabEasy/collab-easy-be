package com.collab.project.model.email;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name = "email_enum_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class EmailEnumHistory {

    @NotNull
    @Column(name = "email_enum")
    String emailEnum;

    @NotNull
    @Column(name = "last_sent")
    Timestamp lastSent;
}
