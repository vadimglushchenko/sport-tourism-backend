package com.sporttourism.service;

import com.sporttourism.entities.SportTrip;
import com.sporttourism.entities.TripDifficulty;
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

  public Optional<Iterable<SportTrip>> getAllSportTrips() {
    Optional<Iterable<SportTrip>> sportTrips = Optional.of(new ArrayList<>());
    try {
      sportTrips = (Optional.of(sportTripRepository.findAll()));
    } catch (Exception e) {
      LOGGER.error("getAllSportTrips: List of sport trips is empty!");
    }

    LOGGER.debug("getAllSportTrips output: " + sportTrips.toString());
    return sportTrips;
  }

  public Optional<SportTrip> getSportTripById(String sportTripId) {
    Optional<SportTrip> sportTrip = Optional.empty();
    LOGGER.trace("getSportTripById: " + sportTripId);

    try {
      sportTrip = sportTripRepository.findById(sportTripId);
    } catch (Exception e) {
      LOGGER.error("getSportTripById: SportTrip with " + sportTripId + " don't exist!");
    }

    LOGGER.debug("getSportTripById output: " + sportTrip.toString());

    return sportTrip;
  }

  public Optional<Iterable<SportTrip>> getSportTripsByTripDifficultyAndTripDuration(TripDifficulty tripDifficulty, Integer tripDuration) {
    Optional<Iterable<SportTrip>> sportTrips = Optional.of(new ArrayList<>());

    LOGGER.trace("getSportTripsByTripDifficultyAndTripDuration: tripDifficulty " + tripDifficulty + " tripDuration " + tripDuration);

    try {
      sportTrips = sportTripRepository.findByTripDifficultyAndTripDuration(tripDifficulty, tripDuration);
    } catch (Exception e) {
      LOGGER.error(
          "getSportTripsByTripDifficultyAndTripDuration: SportTrips with tripDifficulty " + tripDifficulty + " and tripDuration " + tripDuration
              + " don't exist!");
    }

    LOGGER.debug("getSportTripsByTripDifficultyAndTripDuration output: " + sportTrips.toString());

    return sportTrips;
  }

  public Optional<SportTrip> addSportTrip(SportTripInput sportTripInput) {
    Optional<SportTrip> adderSportTrip = Optional.empty();

    LOGGER.trace("addSportTrip: sportTripInput " + sportTripInput.toString());

    SportTrip createdSportTrip = SportTrip.builder()
        .locationName(sportTripInput.getLocationName())
        .tripDate(sportTripInput.getTripDate())
        .tripDifficulty(sportTripInput.getTripDifficulty().toString())
        .tripType(sportTripInput.getTripType().toString())
        .tripDuration(sportTripInput.getTripDuration())
        .maxGroupCount(sportTripInput.getMaxGroupCount())
        .cost(sportTripInput.getCost())
//        .comments(sportTripInput.getComments())
        .isFinished(sportTripInput.getIsFinished())
        .isRemoved(sportTripInput.getIsRemoved())
        .build();

    try {
      adderSportTrip = Optional.of(sportTripRepository.save(createdSportTrip));
    } catch (Exception e) {
      LOGGER.error(
          "addSportTrip: not valid sport trip input!");
    }

    LOGGER.debug("addSportTrip output: " + adderSportTrip.toString());

    return adderSportTrip;
  }

  public Optional<SportTrip> deactivateSportTrip(String sportTripId) {
    LOGGER.trace("deactivateSportTrip: " + sportTripId);

    AtomicReference<SportTrip> deactivatedSportTrip = new AtomicReference<>();

    try {
      sportTripRepository.findById(sportTripId)
          .map(sportTrip -> {
            sportTrip.setIsRemoved(true);
            deactivatedSportTrip.set(sportTripRepository.save(sportTrip));
            return deactivatedSportTrip.get();
          }).orElseThrow(NullPointerException::new);
    } catch (Exception e) {
      LOGGER.error("deactivatedSportTrip: SportTrip with " + sportTripId + " don't exist!");
    }

    LOGGER.debug("deactivatedSportTrip output: " + deactivatedSportTrip.get().toString());

    return Optional.of(deactivatedSportTrip.get());
  }

  public void deleteSportTrip(String sportTripId) {
    LOGGER.trace("deleteSportTrip: " + sportTripId);

    try {
      sportTripRepository.deleteById(sportTripId);
    } catch (Exception e) {
      LOGGER.error("deleteSportTrip: SportTrip with " + sportTripId + " don't exist!");
    }
  }
}
