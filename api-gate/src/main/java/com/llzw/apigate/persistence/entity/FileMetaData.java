package com.llzw.apigate.persistence.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@AllArgsConstructor
public class FileMetaData implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  protected Long id;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  protected Date createdAt;

  // SHA-256 Hash value, length = 64
  @Column(nullable = false, updatable = false, unique = true, length = 64)
  protected String hash;

  protected int referenceCount;

  @Column(nullable = false, updatable = false, length = 30)
  protected String mimeType;

  /**
   * @return Whether <tt>referenceCount</tt> reaches zero after this operation.
   */
  public boolean decreaseReferenceCount() {
    if (referenceCount > 0) {
      --referenceCount;
    }
    return referenceCount == 0;
  }

  public int increaseReferenceCount() {
    return ++referenceCount;
  }
}
