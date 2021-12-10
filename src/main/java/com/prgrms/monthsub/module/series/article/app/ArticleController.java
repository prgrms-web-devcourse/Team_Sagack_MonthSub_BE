package com.prgrms.monthsub.module.series.article.app;

import com.prgrms.monthsub.common.jwt.JwtAuthentication;
import com.prgrms.monthsub.module.series.article.dto.ArticleEdit;
import com.prgrms.monthsub.module.series.article.dto.ArticleOne;
import com.prgrms.monthsub.module.series.article.dto.ArticlePost;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    @RequestPart MultipartFile thumbnail,
    @Valid @RequestPart ArticlePost.Request request
  ) {
    return this.articleAssemble.createArticle(thumbnail, request);
  }

  @PutMapping(path = "/{id}")
  @Operation(summary = "아티클 텍스트 정보 수정")
  @Tag(name = "[화면]-아티클")
  public ArticleEdit.TextChangeResponse editArticle(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @PathVariable Long id,
    @Valid @RequestBody ArticleEdit.TextChangeRequest request
  ) {
    return this.articleAssemble.editArticle(id, request);
  }

  @GetMapping(path = "/{id}")
  @Operation(summary = "아티클 단건 조회")
  @Tag(name = "[화면]-아티클")
  public ArticleOne.Response editArticle(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @PathVariable Long id
  ) {
    return this.articleAssemble.getArticleOne(id);
  }

  @PatchMapping(path = "/{id}/thumbnail", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  @Operation(summary = "아티클 썸네일 이미지 업데이트")
  @Tag(name = "[화면]-아티클")
  public String registerImage(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @PathVariable Long id,
    @RequestPart MultipartFile image,
    @Valid @RequestPart Long seriesId
  ) {
    return this.articleAssemble.changeThumbnail(image, seriesId, id, authentication.userId);
  }

}
