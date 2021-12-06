package com.prgrms.monthsub.series.series.domain.type;

public enum SeriesStatus {

    SUBSCRIPTION_UNAVAILABLE,
    SUBSCRIPTION_AVAILABLE;

    public static SeriesStatus of(String seriesStatus) {
        return SeriesStatus.valueOf(seriesStatus.toUpperCase());
    }
}
