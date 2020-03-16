package com.sporttourism.entities;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import lombok.experimental.FieldDefaults;

@DynamoDBDocument
@Data
@Builder
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
