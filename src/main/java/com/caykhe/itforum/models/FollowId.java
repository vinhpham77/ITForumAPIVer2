package com.caykhe.itforum.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FollowId implements Serializable {
    @Serial
    private static final long serialVersionUID = 1932590682598799897L;
    
    private User follower;
    private User followed;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FollowId entity = (FollowId) o;
        return Objects.equals(this.follower, entity.follower) &&
                Objects.equals(this.followed, entity.followed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(follower, followed);
    }

}