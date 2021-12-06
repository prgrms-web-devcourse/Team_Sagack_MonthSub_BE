package com.prgrms.monthsub.part.writer.domain;

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
@Table(name = "writer_likes")
public class WriterLikes extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", referencedColumnName = "id")
    private Writer writer;

    @Column(name = "user_id", columnDefinition = "BIGINT", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "likes_status", columnDefinition = "VARCHAR(50)")
    private LikesStatus likesStatus;


    public enum LikesStatus {

        Like,
        Nothing;

        public static LikesStatus of(String likesStatus) {
            return LikesStatus.valueOf(likesStatus.toUpperCase());
        }

    }


}
