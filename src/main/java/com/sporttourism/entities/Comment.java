package com.sporttourism.entities;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.FieldDefaults;

@DynamoDBDocument
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@With
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {

  @DynamoDBAttribute
  String commentText;
  @DynamoDBAttribute
  String authorUsername;
  @DynamoDBAttribute
  Long date;
}
