package com.prgrms.monthsub.module.series.series.converter;

import com.prgrms.monthsub.module.part.user.converter.UserConverter;
import com.prgrms.monthsub.module.part.user.domain.User;
import com.prgrms.monthsub.module.part.writer.converter.WriterConverter;
import com.prgrms.monthsub.module.part.writer.domain.Writer;
import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.dto.MyChannel;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class MyChannelConverter {

    private final UserConverter userConverter;

    private final WriterConverter writerConverter;

    private final SeriesConverter seriesConverter;

    public MyChannelConverter(UserConverter userConverter,
        WriterConverter writerConverter,
        SeriesConverter seriesConverter) {
        this.userConverter = userConverter;
        this.writerConverter = writerConverter;
        this.seriesConverter = seriesConverter;
    }

    public MyChannel.Response myChannelToResponse(User user,
        Writer writer,
        List<Writer> followingWriterList,
        List<Series> seriesLikeList,
        List<Series> seriesSubscribeList,
        List<Series> seriesPostList) {
        return new MyChannel.Response(
            userConverter.userToSeriesOneWithUserResponse(user),
            followingWriterList.size(),
            followingWriterList.stream().map(writerConverter::writerToMyChannelFollowWriterObject)
                .collect(Collectors.toList()),
            seriesLikeList.stream().map(seriesConverter::seriesToMyChannelLikeObject)
                .collect(Collectors.toList()),
            seriesSubscribeList.stream().map(seriesConverter::seriesToMyChannelSubscribeObject)
                .collect(Collectors.toList()),
            writer.getFollowCount(),
            seriesPostList.stream().map(seriesConverter::seriesToMyChannelSeriesObject)
                .collect(Collectors.toList())
        );
    }

    public MyChannel.Response myChannelToResponseWithoutWriter(User user,
        List<Writer> followingWriterList,
        List<Series> seriesLikeList,
        List<Series> seriesSubscribeList) {

        return new MyChannel.Response(
            userConverter.userToSeriesOneWithUserResponse(user),
            followingWriterList.size(),
            followingWriterList.stream().map(writerConverter::writerToMyChannelFollowWriterObject)
                .collect(Collectors.toList()),
            seriesLikeList.stream().map(seriesConverter::seriesToMyChannelLikeObject)
                .collect(Collectors.toList()),
            seriesSubscribeList.stream().map(seriesConverter::seriesToMyChannelSubscribeObject)
                .collect(Collectors.toList()),
            0,
            Collections.emptyList()
        );
    }


}
