package com.caykhe.itforum.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Embeddable
public class SeriesPostId implements Serializable {

    @Serial
    private static final long serialVersionUID = 2552993135297632248L;

    @NotNull
    @Column(name = "series_id", nullable = false)
    private Integer seriesId;

    @NotNull
    @Column(name = "post_id", nullable = false)
    private Integer postId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SeriesPostId entity = (SeriesPostId) o;
        return Objects.equals(this.postId, entity.postId) &&
                Objects.equals(this.seriesId, entity.seriesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, seriesId);
    }

}