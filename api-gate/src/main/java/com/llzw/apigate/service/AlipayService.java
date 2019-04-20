package com.llzw.apigate.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.llzw.apigate.message.error.PaymentVendorException;
import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.message.error.TradeNotFoundPaymentVendorException;
import com.llzw.apigate.persistence.entity.Payment;
import com.llzw.apigate.spring.AlipayProperties;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlipayService implements PaymentVendorService {

  private final Logger LOGGER = LoggerFactory.getLogger(getClass());

  private AlipayProperties properties;

  private AlipayClient alipayClient;

  @Autowired
  public AlipayService(AlipayProperties properties) {
    this.properties = properties;
    alipayClient = new DefaultAlipayClient(
        properties.gatewayUrl,
        properties.appId, properties.merchantPrivateKey, "json", properties.charset,
        properties.alipayPublicKey, properties.signType
    );
  }

  public String pay(Payment payment) throws PaymentVendorException {
    //设置请求参数
    AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
    request.setReturnUrl(properties.returnUrl);
    request.setNotifyUrl(properties.notifyUrl);
    //商户订单号，商户网站订单系统中唯一订单号，必填
    String outTradeNo = String.valueOf(payment.getOrder().getId());
    String totalAmount = String.valueOf(payment.getTotalAmount());
    String subject = payment.getSubject();
    String body = payment.getDescription();
    AlipayTradePagePayModel model = new AlipayTradePagePayModel();
    model.setOutTradeNo(outTradeNo);
    model.setTotalAmount(totalAmount);
    model.setSubject(subject);
    model.setBody(body);
    model.setProductCode("FAST_INSTANT_TRADE_PAY");
    model.setTimeoutExpress("15m");
    request.setBizModel(model);
    try {
      AlipayTradePagePayResponse response = alipayClient.pageExecute(request, "GET");
      if (response.isSuccess()) {
        return response.getBody();
      }
      throw new PaymentVendorException(response.getSubCode());
    } catch (AlipayApiException e) {
      throw new PaymentVendorException(e.getMessage());
    }
  }

  @Override
  public boolean verifySignature(Map<String, String> params) {
    try {
      boolean signVerified = AlipaySignature
          .rsaCheckV2(params, properties.alipayPublicKey, properties.charset, properties.signType);
      return signVerified;
    } catch (AlipayApiException e) {
      LOGGER.warn(e.getErrMsg());
      return false;
    }
  }

  @Override
  public Map<String, String> query(Long orderId) throws RestApiException {
    AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();
    String outTradeNo = String.valueOf(orderId);
    AlipayTradeQueryModel model = new AlipayTradeQueryModel();
    model.setOutTradeNo(outTradeNo);
    alipayRequest.setBizModel(model);
    try {
      AlipayTradeQueryResponse response = alipayClient.execute(alipayRequest);
      String subCode = response.getSubCode();
      if (response.isSuccess()) {
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String, String> map = mapper
//            .readValue(response.getBody(), new TypeReference<Map<String, String>>() {
//            });
        Map<String, String> map = new HashMap<>();
        map.put("trade_no", response.getTradeNo());
        map.put("out_trade_no", response.getOutTradeNo());
        map.put("total_amount", response.getTotalAmount());
        map.put("trade_status", response.getTradeStatus());
        return map;
      } else if (subCode.equals("ACQ.TRADE_NOT_EXIST")) {
        throw new TradeNotFoundPaymentVendorException(subCode);
      }
      throw new PaymentVendorException(subCode);
    } catch (AlipayApiException e) {
      throw new PaymentVendorException(e.getMessage());
    }
  }
}
