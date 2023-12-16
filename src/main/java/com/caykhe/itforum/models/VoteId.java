package com.caykhe.itforum.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class VoteId implements Serializable {
    private static final long serialVersionUID = 9026443096854345784L;
    @NotNull
    @Column(name = "target_id", nullable = false)
    private Integer targetId;

    @NotNull
    @Column(name = "type", nullable = false)
    private Boolean type = false;

    @Size(max = 50)
    @NotNull
    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        VoteId entity = (VoteId) o;
        return Objects.equals(this.targetId, entity.targetId) &&
                Objects.equals(this.type, entity.type) &&
                Objects.equals(this.username, entity.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetId, type, username);
    }

}