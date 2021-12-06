package com.prgrms.monthsub.domain.enumType;

public enum LikesStatus {

    Like,
    Nothing;

    public static LikesStatus of(String likesStatus) {
        return LikesStatus.valueOf(likesStatus.toUpperCase());
    }

}