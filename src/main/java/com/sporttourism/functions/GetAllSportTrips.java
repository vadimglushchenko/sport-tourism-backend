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
public class GetAllSportTrips implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  SportTripService sportTripService;
  Gson gson;

  static final Logger LOGGER = LoggerFactory.getLogger(SportTripService.class);

  @Override
  public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent) {
    Optional<Iterable<SportTrip>> allSportTrips = sportTripService.getAllSportTrips();
    allSportTrips.ifPresent(sportTrips -> LOGGER.info("Sport trips from service: " + sportTrips.toString()));

    APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();
    responseEvent.setHeaders(Map.of("Access-Control-Allow-Origin", "*",
        "Access-Control-Allow-Credentials", "true"));
    responseEvent.setStatusCode(HttpStatus.OK.value());
    allSportTrips
        .ifPresent(sportTrips -> responseEvent.setBody(gson.toJson(sportTrips)));

    return responseEvent;
  }
}
