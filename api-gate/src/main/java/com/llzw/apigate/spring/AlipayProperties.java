package com.llzw.apigate.spring;

import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;

@Data
@ConfigurationProperties(prefix = "alipay")
// Without this may cause NoUniqueBeanDefinitionException
@Primary
public class AlipayProperties {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

  // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
  @NotNull
  public String appId;

  // 商户私钥，您的PKCS8格式RSA2私钥
  @NotNull
  public String merchantPrivateKey;

  // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥
  @NotNull
  public String alipayPublicKey;

  // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
  @NotNull
  public String notifyUrl;

  // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
  @NotNull
  public String returnUrl;

  // 签名方式
  @NotNull
  public String signType = "RSA2";

  // 字符编码格式
  @NotNull
  public String charset = "utf-8";

  // 支付宝网关
  @NotNull
  public String gatewayUrl;

//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
}