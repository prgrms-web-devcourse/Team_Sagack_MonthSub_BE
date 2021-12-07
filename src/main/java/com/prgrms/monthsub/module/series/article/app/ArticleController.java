package com.prgrms.monthsub.module.series.article.app;

import com.prgrms.monthsub.common.exception.model.ApiResponse;
import com.prgrms.monthsub.common.jwt.JwtAuthentication;
import com.prgrms.monthsub.module.series.article.dto.ArticlePost;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import javax.validation.Valid;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Tag(name = "Article")
public class ArticleController {

  private final ArticleAssemble articleAssemble;

  public ArticleController(
    ArticleAssemble articleAssemble
  ) {this.articleAssemble = articleAssemble;}

  @PostMapping(path = "/article", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  @Operation(summary = "아티클 작성")
  @Tag(name = "[화면]-아티클")
  public ApiResponse<ArticlePost.Response> postArticle(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @RequestPart(required = false) MultipartFile thumbnail,
    @Valid @RequestPart ArticlePost.Request request
  ) throws IOException {
    return ApiResponse.ok(
      HttpMethod.POST,
      articleAssemble.createArticle(thumbnail, request)
    );
  }

}
