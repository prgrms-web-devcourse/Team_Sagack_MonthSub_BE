package com.prgrms.monthsub.converter;

import com.prgrms.monthsub.domain.Writer;
import com.prgrms.monthsub.dto.response.SeriesGetWithWriterResponse;
import org.springframework.stereotype.Component;

@Component
public class WriterConverter {

    private final UserConverter userConverter;

    public WriterConverter(UserConverter userConverter) {
        this.userConverter = userConverter;
    }

    public SeriesGetWithWriterResponse writerToResponse(Writer writer) {
        return new SeriesGetWithWriterResponse(
            writer.getId(),
            writer.getFollowCount(),
            userConverter.userToSeriesGetWithUserResponse(writer.getUser())
        );
    }

}
