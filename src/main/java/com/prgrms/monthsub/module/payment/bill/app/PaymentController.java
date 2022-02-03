package com.prgrms.monthsub.module.payment.bill.app;

import com.prgrms.monthsub.common.security.jwt.JwtAuthentication;
import com.prgrms.monthsub.module.payment.bill.domain.exception.PaymentException.FailedPayment;
import com.prgrms.monthsub.module.payment.bill.dto.PaymentSeries;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@Tag(name = "Payment")
public class PaymentController {

  private final PaymentService paymentService;

  public PaymentController(
    PaymentService paymentService
  ) {
    this.paymentService = paymentService;
  }

  @GetMapping("/series/{id}")
  @Operation(summary = "결제 요청 시 시리즈 단건 조회")
  @Tag(name = "[화면]-결제")
  public PaymentSeries.Response getSeriesById(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @PathVariable Long id
  ) {
    return this.paymentService.getSeriesById(id);
  }

  @PostMapping("/series/{id}")
  @Operation(summary = "결제 완료 요청")
  @Tag(name = "[화면]-결제")
  public Object create(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @PathVariable Long id
  ) throws FailedPayment {
    return this.paymentService.pay(id, authentication.userId);
  }

}
