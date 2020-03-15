package com.sporttourism.functions;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.sporttourism.entities.SportTrip;
import com.sporttourism.service.SportTripService;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetSportTrips implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  SportTripService sportTripService;
  Gson gson;

  static final Logger LOGGER = LoggerFactory.getLogger(SportTripService.class);

  @Override
  public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent) {
    APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();

    Optional<Map<String, String>> pathParameters = Optional.ofNullable(apiGatewayProxyRequestEvent.getQueryStringParameters());
    pathParameters.ifPresentOrElse(parameters -> {
      Optional<String> sportTripId = Optional.ofNullable(parameters.get("id"));

      sportTripId.ifPresent(tripId -> {
        LOGGER.info("GetActiveSportTrips function: pathParameter id: " + tripId);

        Optional<SportTrip> sportTrip = sportTripService.getSportTripById(tripId);
        sportTrip.ifPresent(newSportTrip -> responseEvent.setBody(gson.toJson(sportTrip)));
      });

      Optional<String> isSportTripCompleted = Optional.ofNullable(parameters.get("isCompleted"));

      isSportTripCompleted.ifPresent(isCompleted -> {
        LOGGER.info("GetActiveSportTrips function: pathParameter isCompleted: " + isCompleted);

        Optional<Iterable<SportTrip>> sportTripsFromService = sportTripService.getSportTripsByIsCompleted(Boolean.valueOf(isCompleted));
        sportTripsFromService.ifPresent(sportTrips -> {
          LOGGER.info("GetActiveSportTrips function: Sport trips from service: " + gson.toJson(sportTrips));
          responseEvent.setBody(gson.toJson(sportTrips));
        });
      });
    }, () -> {
      Optional<Iterable<SportTrip>> activeSportTrips = sportTripService.getSportTripsByIsCompleted(Boolean.FALSE);
      activeSportTrips.ifPresent(sportTrips -> LOGGER.info("GetActiveSportTrips function: Sport trips from service: " + gson.toJson(sportTrips)));
      activeSportTrips
          .ifPresent(sportTrips -> responseEvent.setBody(gson.toJson(sportTrips)));
    });

    responseEvent.setHeaders(Map.of("Access-Control-Allow-Origin", "*",
        "Access-Control-Allow-Credentials", "true"));
    responseEvent.setStatusCode(HttpStatus.OK.value());

    return responseEvent;
  }
}
