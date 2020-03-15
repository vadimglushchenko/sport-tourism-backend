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
public class CompleteSportTrip implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  SportTripService sportTripService;
  Gson gson;

  static final Logger LOGGER = LoggerFactory.getLogger(SportTripService.class);

  @Override
  public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent) {

    String responseBody = "Incorrect sport trip id";

    try {
      LOGGER.info("CompleteSportTrip function: Request body: " + apiGatewayProxyRequestEvent.getBody());

      String sportTripId = gson.fromJson(apiGatewayProxyRequestEvent.getBody(), String.class);
      LOGGER.info("CompleteSportTrip function: Parsed sport trip id: " + sportTripId);
      Optional<SportTrip> completedSportTrip = sportTripService.completeSportTrip(sportTripId);

      if (completedSportTrip.isPresent()) {
        responseBody = gson.toJson(completedSportTrip.get());
      }
    } catch (NullPointerException e) {
      e.printStackTrace();
    }

    APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();
    responseEvent.setHeaders(Map.of("Access-Control-Allow-Origin", "*",
        "Access-Control-Allow-Credentials", "true", "Access-Control-Allow-Methods", "GET,HEAD,OPTIONS,POST,PUT",
        "Access-Control-Allow-Headers",
        "Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers"));
    responseEvent.setStatusCode(HttpStatus.OK.value());
    responseEvent.setBody(responseBody);
    return responseEvent;
  }
}
