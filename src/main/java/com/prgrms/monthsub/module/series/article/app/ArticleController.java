package com.prgrms.monthsub.module.series.article.app;

import com.prgrms.monthsub.common.exception.model.ApiResponse;
import com.prgrms.monthsub.common.jwt.JwtAuthentication;
import com.prgrms.monthsub.module.series.article.dto.ArticleEdit;
import com.prgrms.monthsub.module.series.article.dto.ArticlePost;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import javax.validation.Valid;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RequestMapping("/article")
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
  public ApiResponse<ArticlePost.Response> postArticle(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @RequestPart MultipartFile thumbnail,
    @Valid @RequestPart ArticlePost.Request request
  ) throws IOException {
    return ApiResponse.ok(
      HttpMethod.POST,
      articleAssemble.createArticle(thumbnail, request)
    );
  }

  @PutMapping(path = "/{id}")
  @Operation(summary = "아티클 텍스트 정보 수정")
  @Tag(name = "[화면]-아티클")
  public ApiResponse<ArticleEdit.TextChangeResponse> editArticle(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @PathVariable Long id,
    @Valid @RequestBody ArticleEdit.TextChangeRequest request
  ) throws IOException {
    return ApiResponse.ok(
      HttpMethod.POST,
      articleAssemble.editArticle(id, request)
    );
  }

  @PatchMapping(path = "/{id}/thumbnail", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  @Operation(summary = "아티클 썸네일 이미지 업데이트")
  @Tag(name = "[화면]-아티클")
  public ApiResponse<String> registerImage(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @PathVariable Long id,
    @RequestPart MultipartFile image,
    @Valid @RequestPart Long seriesId
  ) throws IOException {
    return ApiResponse.ok(
      HttpMethod.PATCH,
      this.articleAssemble.changeThumbnail(image, seriesId, id, authentication.userId)
    );
  }

}
