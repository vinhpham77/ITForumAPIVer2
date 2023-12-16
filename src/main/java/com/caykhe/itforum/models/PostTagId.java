package com.caykhe.itforum.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class PostTagId implements Serializable {
    @Serial
    private static final long serialVersionUID = -3995321831031202909L;
    @NotNull
    @Column(name = "post_id", nullable = false)
    private Integer postId;

    @NotNull
    @Column(name = "tag_id", nullable = false)
    private Integer tagId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PostTagId entity = (PostTagId) o;
        return Objects.equals(this.tagId, entity.tagId) &&
                Objects.equals(this.postId, entity.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagId, postId);
    }

}