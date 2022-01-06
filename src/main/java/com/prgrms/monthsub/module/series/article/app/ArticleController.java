package com.prgrms.monthsub.module.series.article.app;

import com.prgrms.monthsub.common.security.jwt.JwtAuthentication;
import com.prgrms.monthsub.module.series.article.dto.ArticleEdit;
import com.prgrms.monthsub.module.series.article.dto.ArticleOne;
import com.prgrms.monthsub.module.series.article.dto.ArticlePost;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/articles")
@Tag(name = "Article")
public class ArticleController {

  private final ArticleAssemble articleAssemble;

  public ArticleController(
    ArticleAssemble articleAssemble
  ) {
    this.articleAssemble = articleAssemble;
  }

  @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  @Operation(summary = "아티클 작성")
  @Tag(name = "[화면]-아티클")
  public ArticlePost.Response postArticle(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @RequestPart MultipartFile file,
    @Valid @RequestPart ArticlePost.Request request
  ) {
    return this.articleAssemble.createArticle(file, request, authentication.userId);
  }

  @PutMapping(path = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  @Operation(summary = "아티클 수정")
  @Tag(name = "[화면]-아티클")
  public ArticleEdit.ChangeResponse editArticle(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @PathVariable Long id,
    @RequestPart(required = false) MultipartFile file,
    @Valid @RequestPart ArticleEdit.ChangeRequest request
  ) {
    return this.articleAssemble.editArticle(
      id, request, Optional.ofNullable(file), authentication.userId
    );
  }

  @GetMapping(path = "/{id}")
  @Operation(summary = "아티클 단건 조회")
  @Tag(name = "[화면]-아티클")
  public ArticleOne.Response getArticle(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @PathVariable Long id,
    @RequestParam(value = "seriesId", required = true) Long seriesId
  ) {
    return this.articleAssemble.getArticle(id, seriesId, authentication.userId);
  }

}

