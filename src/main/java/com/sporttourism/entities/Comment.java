/*
package com.sporttourism.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "sportTrip")
@Builder
public class Comment extends BaseEntity {

  private String commentText;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User author;
  private Long date;

  @ManyToOne
  @JoinColumn(name = "sport_trip_id")
  @JsonIgnore
  private SportTrip sportTrip;
}
*/
