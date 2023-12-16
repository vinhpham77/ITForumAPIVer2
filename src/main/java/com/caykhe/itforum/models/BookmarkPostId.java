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
public class BookmarkPostId implements Serializable {
    @Serial
    private static final long serialVersionUID = -6914043965397034330L;
    @NotNull
    @Column(name = "bookmark_id", nullable = false)
    private Integer bookmarkId;

    @NotNull
    @Column(name = "target_id", nullable = false)
    private Integer targetId;

    @NotNull
    @Column(name = "type", nullable = false)
    private Boolean type = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BookmarkPostId entity = (BookmarkPostId) o;
        return Objects.equals(this.bookmarkId, entity.bookmarkId) &&
                Objects.equals(this.targetId, entity.targetId) &&
                Objects.equals(this.type, entity.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookmarkId, targetId, type);
    }

}