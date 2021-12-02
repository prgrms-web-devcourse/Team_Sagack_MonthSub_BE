package com.prgrms.monthsub.domain.enumType;

public enum SeriesStatus {

    SUBSCRIPTION_UNAVAILABLE,
    SUBSCRIPTION_AVAILABLE;

    public static SeriesStatus of(String seriesStatus) {
        return SeriesStatus.valueOf(seriesStatus);
    }
}
