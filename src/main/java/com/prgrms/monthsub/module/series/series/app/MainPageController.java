package com.prgrms.monthsub.module.series.series.app;


import com.prgrms.monthsub.module.series.series.dto.MainPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/main")
@Tag(name = "MainPage")
public class MainPageController {

  private final MainPageAssemble mainPageAssemble;

  public MainPageController(MainPageAssemble mainPageAssemble) {this.mainPageAssemble = mainPageAssemble;}

  @GetMapping
  @Operation(summary = "메인 페이지 조회")
  @Tag(name = "[화면]-메인 페이지")
  public MainPage.Response getMainPage() {
    return this.mainPageAssemble.getMainPage();
  }

}
