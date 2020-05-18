package com.sporttourism.functions;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.sporttourism.entities.User;
import com.sporttourism.service.UserService;
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
public class GetUsers implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  UserService userService;
  Gson gson;

  static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);


  @Override
  public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent) {
    APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();

    try {
      Optional<Map<String, String>> pathParameters = Optional.ofNullable(apiGatewayProxyRequestEvent.getQueryStringParameters());
      pathParameters.ifPresent(parameters -> {
        Optional<String> userName = Optional.ofNullable(parameters.get("userName"));

        userName.ifPresent(userNameValue -> {
          LOGGER.info("GetUsers function: pathParameter userName: " + userNameValue);

          Optional<Iterable<User>> usersFromService = userService.getUsersByName(userNameValue);
          usersFromService.ifPresent(sportTrips -> {
            LOGGER.info("GetActiveSportTrips function: Sport trips from service: " + gson.toJson(sportTrips));
            responseEvent.setBody(gson.toJson(sportTrips));
          });
        });
      });
    } catch (Exception e) {
      e.printStackTrace();
    }

    responseEvent.setHeaders(Map.of("Access-Control-Allow-Origin", "*",
        "Access-Control-Allow-Credentials", "true"));
    responseEvent.setStatusCode(HttpStatus.OK.value());

    return responseEvent;
  }
}
