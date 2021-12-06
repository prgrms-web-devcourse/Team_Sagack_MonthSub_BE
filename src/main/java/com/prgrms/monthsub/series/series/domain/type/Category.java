package com.prgrms.monthsub.series.series.domain.type;

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
