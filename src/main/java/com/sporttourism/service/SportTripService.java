package com.sporttourism.service;

import com.sporttourism.entities.SportTrip;
import com.sporttourism.payload.SportTripInput;
import com.sporttourism.repositories.SportTripRepository;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SportTripService {

  static final Logger LOGGER = LoggerFactory.getLogger(SportTripService.class);

  SportTripRepository sportTripRepository;

  public Optional<Iterable<SportTrip>> getSportTripsByIsCompleted(Boolean isCompleted) {
    Optional<Iterable<SportTrip>> sportTrips = Optional.of(new ArrayList<>());
    try {
      sportTrips = sportTripRepository.findByIsCompleted(isCompleted);
    } catch (Exception e) {
      LOGGER.error("SportTripService#getActiveSportTrips: List of sport trips is empty!");
      e.printStackTrace();
    }

    LOGGER.debug("SportTripService#getActiveSportTrips output: " + sportTrips.toString());
    return sportTrips;
  }

  public Optional<SportTrip> getSportTripById(String sportTripId) {
    Optional<SportTrip> sportTrip = Optional.empty();
    LOGGER.trace("SportTripService#getSportTripById: id " + sportTripId);

    try {
      sportTrip = sportTripRepository.findById(sportTripId);
    } catch (Exception e) {
      LOGGER.error("SportTripService#getSportTripById: SportTrip with " + sportTripId + " don't exist!");
      e.printStackTrace();
    }

    LOGGER.debug("SportTripService#getSportTripById output: " + sportTrip.toString());

    return sportTrip;
  }

  public Optional<SportTrip> addSportTrip(SportTripInput sportTripInput) {
    Optional<SportTrip> adderSportTrip = Optional.empty();

    LOGGER.trace("SportTripService#addSportTrip: sportTripInput " + sportTripInput.toString());

    SportTrip createdSportTrip = SportTrip.builder()
        .tripGuideId(sportTripInput.getTripGuideId())
        .locationName(sportTripInput.getLocationName())
        .tripDescription(sportTripInput.getTripDescription())
        .tripDate(sportTripInput.getTripDate())
        .tripDifficulty(sportTripInput.getTripDifficulty().toString())
        .tripType(sportTripInput.getTripType().toString())
        .tripDuration(sportTripInput.getTripDuration())
        .maxGroupCount(sportTripInput.getMaxGroupCount())
        .cost(sportTripInput.getCost())
        .isCompleted(Boolean.FALSE)
        .build();

    try {
      adderSportTrip = Optional.of(sportTripRepository.save(createdSportTrip));
    } catch (Exception e) {
      LOGGER.error("SportTripService#addSportTrip: not valid sport trip input!");
      e.printStackTrace();
    }

    LOGGER.debug("SportTripService#addSportTrip output: " + adderSportTrip.toString());

    return adderSportTrip;
  }

  public Optional<SportTrip> completeSportTrip(String sportTripId) {
    LOGGER.trace("SportTripService#completeSportTrip: id=" + sportTripId);

    AtomicReference<SportTrip> completedSportTrip = new AtomicReference<>();

    try {
      sportTripRepository.findById(sportTripId)
          .map(sportTrip -> {
            sportTrip.setIsCompleted(true);
            completedSportTrip.set(sportTripRepository.save(sportTrip));
            return completedSportTrip.get();
          }).orElseThrow(NullPointerException::new);
    } catch (Exception e) {
      LOGGER.error("SportTripService#completeSportTrip: SportTrip with " + sportTripId + " don't exist!");
      e.printStackTrace();
    }

    LOGGER.debug("SportTripService#completeSportTrip: output: " + completedSportTrip.get().toString());

    return Optional.of(completedSportTrip.get());
  }
}
