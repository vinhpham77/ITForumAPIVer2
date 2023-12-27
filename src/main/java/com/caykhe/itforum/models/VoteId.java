package com.caykhe.itforum.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class VoteId implements Serializable {
    @Serial
    private static final long serialVersionUID = 9026443096854345784L;

    private Integer targetId;
    private Boolean type = false;
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        VoteId entity = (VoteId) o;
        return Objects.equals(this.targetId, entity.targetId) &&
                Objects.equals(this.type, entity.type) &&
                Objects.equals(this.user, entity.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetId, type, user);
    }

}