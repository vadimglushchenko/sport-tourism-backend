/*
package com.sporttourism.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Builder
public class User extends BaseEntity {

  @NonNull
  @Size(max = 40)
  String userName;

  @NonNull
  @Size(max = 20)
  String phone;

  @NonNull
  @Size(max = 40)
  @Email
  String email;

  @NonNull
  @Size(max = 100)
  String password;
}
*/
