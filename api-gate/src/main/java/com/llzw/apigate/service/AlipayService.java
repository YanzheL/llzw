package com.llzw.apigate.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayResponse;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.llzw.apigate.persistence.entity.Payment;
import com.llzw.apigate.service.error.PaymentVendorException;
import com.llzw.apigate.spring.AlipayProperties;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AlipayService implements PaymentVendorService {

  private final Logger LOGGER = LoggerFactory.getLogger(getClass());

  @Value("${alipay.returnUrl}")
  String returnUrl;

  @Value("${alipay.notifyUrl}")
  String notifyUrl;

  String alipayPublicKey;

  String signType;

  String charset;

  AlipayClient alipayClient;

  public AlipayService(
      @Value("${alipay.gatewayUrl}") String gatewayUrl,
      @Value("${alipay.appId}") String appId,
      @Value("${alipay.merchantPrivateKey}") String merchantPrivateKey,
      @Value("${alipay.charset}") String charset,
      @Value("${alipay.alipayPublicKey}") String alipayPublicKey,
      @Value("${alipay.signType}") String signType
  ) {
    this.alipayPublicKey = alipayPublicKey;
    this.charset = charset;
    this.signType = signType;
    //获得初始化的AlipayClient
    alipayClient = new DefaultAlipayClient(
        gatewayUrl,
        appId, merchantPrivateKey, "json", charset,
        alipayPublicKey, signType
    );
  }

  public String pay(Payment payment) throws PaymentVendorException {
    //设置请求参数
    AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
    alipayRequest.setReturnUrl(AlipayProperties.returnUrl);
    alipayRequest.setNotifyUrl(AlipayProperties.notifyUrl);
    //商户订单号，商户网站订单系统中唯一订单号，必填
    String out_trade_no = String.valueOf(payment.getOrder().getId());
    //付款金额，必填
    String total_amount = String.valueOf(payment.getTotalAmount());
    //订单名称，必填
    String subject = payment.getSubject();
    //商品描述，可空
    String body = payment.getDescription();
    AlipayTradePayModel model = new AlipayTradePayModel();
    model.setOutTradeNo(out_trade_no);
    model.setTotalAmount(total_amount);
    model.setSubject(subject);
    model.setBody(body);
    model.setProductCode("FAST_INSTANT_TRADE_PAY");
    alipayRequest.setBizModel(model);
    try {
      //请求
      AlipayResponse response = alipayClient.pageExecute(alipayRequest, "GET");
      String orderString = response.getBody();
      return orderString;
    } catch (AlipayApiException e) {
      throw new PaymentVendorException(e.getMessage());
    }
  }

  @Override
  public boolean verifySignature(Map<String, String> params) {
    try {
      boolean signVerified = AlipaySignature
          .rsaCheckV2(params, alipayPublicKey, charset, signType);
      return signVerified;
    } catch (AlipayApiException e) {
      LOGGER.warn(e.getErrMsg());
      return false;
    }
  }
}
