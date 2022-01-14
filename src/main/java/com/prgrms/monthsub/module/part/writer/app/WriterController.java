package com.prgrms.monthsub.module.part.writer.app;

import static java.util.Optional.ofNullable;

import com.prgrms.monthsub.common.security.jwt.JwtAuthentication;
import com.prgrms.monthsub.module.part.writer.dto.WriterFollowEvent;
import com.prgrms.monthsub.module.part.writer.dto.WriterLikesList;
import com.prgrms.monthsub.module.part.writer.dto.WriterList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.constraints.Positive;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/writers")
@Tag(name = "Writer")
public class WriterController {
  private final WriterLikesService writerLikesService;
  private final WriterService writerService;

  public WriterController(
    WriterLikesService writerLikesService,
    WriterService writerService
  ) {
    this.writerLikesService = writerLikesService;
    this.writerService = writerService;
  }

  @PostMapping("/{id}/follow")
  @Operation(summary = "작가 팔로우 클릭 이벤트")
  @Tag(name = "[화면]-작가 채널")
  public WriterFollowEvent.Response followWriter(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @PathVariable Long id
  ) {
    return this.writerLikesService.likesEvent(authentication.userId, id);
  }

  @DeleteMapping("/{id}/follow")
  @Operation(summary = "작가 팔로우 취소 클릭 이벤트")
  @Tag(name = "[화면]-작가 채널")
  public WriterFollowEvent.Response cancelFollowWriter(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @PathVariable Long id
  ) {
    return this.writerLikesService.cancelLikesEvent(authentication.userId, id);
  }

  @GetMapping("/popular")
  @Operation(summary = "인기 작가 리스트")
  @Tag(name = "[화면]-메인 페이지")
  public WriterList.Response getPopularWriterList() {
    return this.writerService.findAll();
  }

  @GetMapping("/likes")
  @Operation(summary = "작가 팔로우 리스트(무한스크롤)")
  @Tag(name = "[화면]-채널")
  public WriterLikesList.Response getWritersLikesList(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @RequestParam(required = false) Long userId,
    @RequestParam(required = false) Long writerLikesLastId,
    @RequestParam @Positive Integer size
  ) {
    return this.writerLikesService.getWriterLikesList(
      (userId == null ? authentication.userId : userId),
      writerLikesLastId,
      size
    );
  }

}
