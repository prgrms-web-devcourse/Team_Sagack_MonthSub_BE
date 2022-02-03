package com.prgrms.monthsub.module.series.series.app;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

import com.prgrms.monthsub.common.security.jwt.JwtAuthentication;
import com.prgrms.monthsub.module.series.series.dto.SeriesCommentEdit;
import com.prgrms.monthsub.module.series.series.dto.SeriesCommentList;
import com.prgrms.monthsub.module.series.series.dto.SeriesCommentPost;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/series/comments")
@Tag(name = "Series")
public class SeriesCommentController {

  private final SeriesCommentService seriesCommentService;

  public SeriesCommentController(SeriesCommentService seriesCommentService) {
    this.seriesCommentService = seriesCommentService;
  }

  @GetMapping
  @Operation(summary = "시리즈 댓글, 대댓글 조회(무한 스크롤)")
  @Tag(name = "[화면]-시리즈")
  public SeriesCommentList.Response getComments(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @RequestParam Long seriesId,
    @RequestParam @Positive Integer size,
    @RequestParam(required = false) Long lastId
  ) {
    return ofNullable(authentication)
      .map(authenticate -> this.seriesCommentService.getComments(
        of(authenticate.userId), seriesId, ofNullable(lastId), size
      ))
      .orElse(this.seriesCommentService.getComments(
        Optional.empty(), seriesId, ofNullable(lastId), size
      ));
  }

  @PostMapping
  @Operation(summary = "시리즈 댓글, 대댓글 작성")
  @Tag(name = "[화면]-시리즈")
  public SeriesCommentPost.Response postComment(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @Valid @RequestBody SeriesCommentPost.Request request
  ) {
    return this.seriesCommentService.saveComment(authentication.userId, request);
  }

  @PutMapping
  @Operation(summary = "시리즈 댓글, 대댓글 수정")
  @Tag(name = "[화면]-시리즈")
  public SeriesCommentEdit.Response editComment(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @Valid @RequestBody SeriesCommentEdit.Request request
  ) {
    return this.seriesCommentService.editComment(authentication.userId, request);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "시리즈 댓글, 대댓글 삭제")
  @Tag(name = "[화면]-시리즈")
  public void deleteComment(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @PathVariable Long id
  ) {
    this.seriesCommentService.deleteComment(authentication.userId, id);
  }

}
