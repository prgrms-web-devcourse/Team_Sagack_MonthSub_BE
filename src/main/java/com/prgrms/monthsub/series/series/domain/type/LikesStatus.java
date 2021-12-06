package com.prgrms.monthsub.series.series.domain.type;

public enum LikesStatus {

    Like,
    Nothing;

    public static LikesStatus of(String likesStatus) {
        return LikesStatus.valueOf(likesStatus.toUpperCase());
    }

}
