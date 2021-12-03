package com.prgrms.monthsub.domain.enumType;

public enum Category {

    POEM,
    NOVEL,
    INTERVIEW,
    ESSAY,
    CRITIQUE,
    ETC;

    public static Category of(String category) {
        return Category.valueOf(category.toUpperCase());
    }
}
