package com.sporttourism.payload;

import com.sporttourism.entities.TripDifficulty;
import com.sporttourism.entities.TripType;
import javax.validation.constraints.NotNull;
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
public class SportTripInput {

  String tripGuideId;

  @NotNull
  String locationName;

  String tripDescription;

  @NotNull
  String tripDate;

  @NotNull
  TripDifficulty tripDifficulty;

  @NotNull
  TripType tripType;

  @NotNull
  Integer tripDuration;

  Integer maxGroupCount;

  Double cost;

//  Set<String> participantsIds;

}
