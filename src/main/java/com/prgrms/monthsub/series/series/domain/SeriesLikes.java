package com.prgrms.monthsub.series.series.domain;

import com.prgrms.monthsub.common.domain.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "series_likes")
public class SeriesLikes extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT")
    private Long id;

    @Column(name = "user_id", columnDefinition = "BIGINT", nullable = false)
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id", referencedColumnName = "id")
    private Series series;

    @Enumerated(EnumType.STRING)
    @Column(name = "likes_status", columnDefinition = "VARCHAR(50)")
    private LikesStatus likesStatus;

    public LikesStatus changeLikeStatus() {
        this.likesStatus =
            this.likesStatus.equals(LikesStatus.Like) ? LikesStatus.Nothing : LikesStatus.Like;
        return this.likesStatus;
    }

    public enum LikesStatus {

        Like,
        Nothing;

        public static LikesStatus of(String likesStatus) {
            return LikesStatus.valueOf(likesStatus.toUpperCase());
        }

    }

}
