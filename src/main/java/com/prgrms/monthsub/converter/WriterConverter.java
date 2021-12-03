package com.prgrms.monthsub.converter;

import com.prgrms.monthsub.domain.Writer;
import com.prgrms.monthsub.dto.response.SeriesOneWithWriterResponse;
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
