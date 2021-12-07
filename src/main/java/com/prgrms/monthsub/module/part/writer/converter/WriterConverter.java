package com.prgrms.monthsub.module.part.writer.converter;

import com.prgrms.monthsub.module.part.user.converter.UserConverter;
import com.prgrms.monthsub.module.part.writer.domain.Writer;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.SeriesOneWithWriterResponse;
import org.springframework.stereotype.Component;

@Component
public class WriterConverter {

    private final UserConverter userConverter;

    public WriterConverter(UserConverter userConverter) {
        this.userConverter = userConverter;
    }

    public SeriesOneWithWriterResponse writerToSeriesOneWithWriterResponse(Writer writer) {
        return new SeriesOneWithWriterResponse(
            writer.getId(),
            writer.getFollowCount(),
            userConverter.userToSeriesOneWithUserResponse(writer.getUser())
        );
    }

}
