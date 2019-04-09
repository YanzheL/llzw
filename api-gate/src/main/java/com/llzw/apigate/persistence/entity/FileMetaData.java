package com.llzw.apigate.persistence.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@AllArgsConstructor
public class FileMetaData implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  @Setter(AccessLevel.NONE)
  protected Long id;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  protected Date createdAt;

  // SHA-256 Hash value, length = 64
  @Column(nullable = false, updatable = false, length = 64)
  protected String hash;

  protected int referrenceCount;

  @Column(nullable = false, updatable = false)
  protected String mimetype;
}
