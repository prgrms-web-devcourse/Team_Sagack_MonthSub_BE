package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.common.security.jwt.JwtAuthentication;
import com.prgrms.monthsub.module.series.series.dto.SeriesCommentEdit;
import com.prgrms.monthsub.module.series.series.dto.SeriesCommentPost;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/series/comment")
@Tag(name = "Series")
public class SeriesCommentController {

  private final SeriesCommentService seriesCommentService;

  public SeriesCommentController(SeriesCommentService seriesCommentService) {
    this.seriesCommentService = seriesCommentService;
  }

  @PostMapping
  @Operation(summary = "시리즈 댓글, 대댓글 작성")
  @Tag(name = "[화면]-시리즈")
  public SeriesCommentPost.Response postComment(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @Valid @RequestBody SeriesCommentPost.Request request
  ){
    return this.seriesCommentService.save(authentication.userId, request);
  }

  @PutMapping
  @Operation(summary = "시리즈 댓글, 대댓글 수정")
  @Tag(name = "[화면]-시리즈")
  public SeriesCommentEdit.Response editComment(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @Valid @RequestBody SeriesCommentEdit.Request request
  ){
    return this.seriesCommentService.edit(authentication.userId, request);
  }

}
