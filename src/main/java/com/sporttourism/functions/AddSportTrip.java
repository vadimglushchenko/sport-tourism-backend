package com.sporttourism.functions;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.sporttourism.entities.SportTrip;
import com.sporttourism.payload.SportTripInput;
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
public class AddSportTrip implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  SportTripService sportTripService;
  Gson gson;

  static final Logger LOGGER = LoggerFactory.getLogger(SportTripService.class);

  @Override
  public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent) {

    SportTripInput sportTripInput;
    APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();

    try {
      LOGGER.info("AddSportTrip function: Request body: " + apiGatewayProxyRequestEvent.getBody());

      sportTripInput = gson.fromJson(apiGatewayProxyRequestEvent.getBody(), SportTripInput.class);
      LOGGER.info("AddSportTrip function: Parsed sport trip input: " + sportTripInput.toString());
      Optional<SportTrip> addedSportTrip = sportTripService.addSportTrip(sportTripInput);

      addedSportTrip.ifPresentOrElse(
          sportTrip -> responseEvent.setBody(gson.toJson(sportTrip)),
          () -> responseEvent.setBody("Incorrect sport trip input!")
      );
    } catch (NullPointerException e) {
      e.printStackTrace();
    }

    responseEvent.setHeaders(Map.of("Access-Control-Allow-Origin", "*",
        "Access-Control-Allow-Credentials", "true", "Access-Control-Allow-Methods", "GET,HEAD,OPTIONS,POST,PUT",
        "Access-Control-Allow-Headers",
        "Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers"));
    responseEvent.setStatusCode(HttpStatus.OK.value());
    return responseEvent;
  }
}
