package com.llzw.apigate.persistence.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@MappedSuperclass
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class Token {
  protected static final int EXPIRATION = 60 * 24;

  @Id
  @GeneratedValue
  @Setter(AccessLevel.NONE)
  protected Long id;

  protected String token;

  @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
  @JoinColumn(nullable = false, name = "user_id")
  protected User user;

  protected Date expiryDate;

  public Token(final String token) {
    this.token = token;
    this.expiryDate = calculateExpiryDate(EXPIRATION);
  }

  public Token(final String token, final User user) {
    this(token);
    this.user = user;
  }

  protected static Date calculateExpiryDate(final int expiryTimeInMinutes) {
    final Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(new Date().getTime());
    cal.add(Calendar.MINUTE, expiryTimeInMinutes);
    return new Date(cal.getTime().getTime());
  }

  public void setToken(final String token) {
    this.token = token;
    this.expiryDate = calculateExpiryDate(EXPIRATION);
  }
}
