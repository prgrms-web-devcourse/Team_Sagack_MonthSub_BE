package com.prgrms.monthsub.common.utils;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public class TimeUtil {

    public static String convertUploadDateListToUploadDateString(String[] uploadDateList) {
        return StringUtils.chop(
            Arrays.stream(uploadDateList).map(s -> s + "$").collect(Collectors.joining()));
    }

}
