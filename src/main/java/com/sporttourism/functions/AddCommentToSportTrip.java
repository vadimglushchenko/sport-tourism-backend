package com.sporttourism.functions;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.sporttourism.entities.Comment;
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
public class AddCommentToSportTrip implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  SportTripService sportTripService;
  Gson gson;

  static final Logger LOGGER = LoggerFactory.getLogger(SportTripService.class);

  @Override
  public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent) {

    APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();

    try {
      Optional<Map<String, String>> pathParameters = Optional.ofNullable(apiGatewayProxyRequestEvent.getQueryStringParameters());
      pathParameters.ifPresentOrElse(parameters -> {

        Optional<String> sportTripId = Optional.ofNullable(parameters.get("id"));

        sportTripId.ifPresent(tripId -> {
          LOGGER.info("AddCommentToSportTrip function: pathParameter id: " + tripId);

          Comment comment = gson.fromJson(apiGatewayProxyRequestEvent.getBody(), Comment.class);
          LOGGER.info("AddCommentToSportTrip function: comment from body: " + comment.toString());

          Optional<SportTrip> updatedSportTrip = sportTripService.addCommentToSportTrip(tripId, comment);
          updatedSportTrip.ifPresent(sportTripWithNewComment -> responseEvent.setBody(gson.toJson(sportTripWithNewComment)));
        });
      }, () -> {
        LOGGER.info("AddCommentToSportTrip function: pathParameter id is emply or current id doesn't exist!");
        responseEvent.setBody("Empty sport trip id path parameter!");
      });
    } catch (Exception e) {
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
