package com.sporttourism.functions;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.sporttourism.entities.SportTrip;
import com.sporttourism.payload.SportTripInput;
import com.sporttourism.service.SportTripService;
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
    String responseBody = "Incorrect sport trip input";

    try {
      sportTripInput = gson.fromJson(apiGatewayProxyRequestEvent.getBody(), SportTripInput.class);
      LOGGER.info("SportTripInput: " + sportTripInput.toString());

      Optional<SportTrip> addedSportTrip = sportTripService.addSportTrip(sportTripInput);

      if (addedSportTrip.isPresent()) {
        responseBody = gson.toJson(addedSportTrip.get());
      }
    } catch (NullPointerException e) {
      e.printStackTrace();
    }

    APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();
    responseEvent.setStatusCode(HttpStatus.OK.value());
    responseEvent.setBody(responseBody);
    return responseEvent;
  }
}
