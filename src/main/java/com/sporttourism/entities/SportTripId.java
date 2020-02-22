package com.sporttourism.entities;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@With
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SportTripId {

  @DynamoDBHashKey(attributeName = "sportTripId")
  String sportTripId;

  @DynamoDBRangeKey
  TripType tripType;


}
