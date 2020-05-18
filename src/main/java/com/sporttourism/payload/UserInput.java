package com.sporttourism.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@With
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserInput {

  String userName;
  String phoneNumber;
  String email;
}
