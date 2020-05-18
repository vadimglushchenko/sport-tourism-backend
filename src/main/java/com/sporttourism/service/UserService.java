package com.sporttourism.service;

import com.sporttourism.entities.User;
import com.sporttourism.payload.UserInput;
import com.sporttourism.repositories.UserRepository;
import java.util.ArrayList;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

  static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

  UserRepository userRepository;

  public Optional<Iterable<User>> getUsersByName(String userNamePart) {
    Optional<Iterable<User>> users = Optional.of(new ArrayList<>());
    try {
      users = userRepository.findByUserNameContaining(userNamePart);
    } catch (Exception e) {
      LOGGER.error("UserService#getUsersByName: List of users is empty!");
      e.printStackTrace();
    }

    LOGGER.debug("UserService#getUsersByName output: " + users.toString());
    return users;
  }

  public Optional<User> getUserById(String userId) {
    Optional<User> user = Optional.empty();
    LOGGER.trace("UserService#getUserById: id " + userId);

    try {
      user = userRepository.findById(userId);
    } catch (Exception e) {
      LOGGER.error("UserService#getUserById: User with " + userId + " don't exist!");
      e.printStackTrace();
    }

    LOGGER.debug("UserService#getUserById output: " + user.toString());

    return user;
  }


  public Optional<User> addUser(UserInput userInput) {
    Optional<User> addedUser = Optional.empty();

    LOGGER.trace("UserService#addUser: userInput " + userInput.toString());

    User createdUser = User.builder()
        .email(userInput.getEmail())
        .phoneNumber(userInput.getPhoneNumber())
        .userName(userInput.getUserName())
        .build();

    try {
      addedUser = Optional.of(userRepository.save(createdUser));
    } catch (Exception e) {
      LOGGER.error("UserService#addUser: not valid user input!");
      e.printStackTrace();
    }

    LOGGER.debug("UserService#addUser output: " + addedUser.toString());

    return addedUser;
  }
}
