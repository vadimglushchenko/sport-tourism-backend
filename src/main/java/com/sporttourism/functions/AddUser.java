package com.sporttourism.functions;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.sporttourism.entities.User;
import com.sporttourism.payload.UserInput;
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
public class AddUser implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  UserService userService;
  Gson gson;

  static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

  @Override
  public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent) {

    UserInput userInput;
    APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();

    try {
      LOGGER.info("AddUser function: Request body: " + apiGatewayProxyRequestEvent.getBody());

      userInput = gson.fromJson(apiGatewayProxyRequestEvent.getBody(), UserInput.class);
      LOGGER.info("AddUser function: Parsed user input: " + userInput.toString());
      Optional<User> addedUser = userService.addUser(userInput);

      addedUser.ifPresentOrElse(
          user -> responseEvent.setBody(gson.toJson(user)),
          () -> responseEvent.setBody("Incorrect user input!")
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
