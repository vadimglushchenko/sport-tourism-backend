package com.sporttourism.payload;

import com.sporttourism.entities.TripDifficulty;
import com.sporttourism.entities.TripType;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SportTripInput {

  @NotNull
  String locationName;
  String description;
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

//  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "sportTrip")
//  private Set<Comment> comments;

  Boolean isFinished;
  Boolean isRemoved;
}
