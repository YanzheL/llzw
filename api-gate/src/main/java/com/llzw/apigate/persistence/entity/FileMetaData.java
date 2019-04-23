package com.llzw.apigate.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class FileMetaData extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  protected Long id;

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
