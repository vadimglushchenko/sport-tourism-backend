package com.sporttourism;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.google.gson.Gson;
import com.sporttourism.entities.User;
import com.sporttourism.payload.UserInput;
import com.sporttourism.repositories.UserRepository;
import com.sporttourism.service.UserService;
import java.util.Collection;
import java.util.Optional;
import org.jeasy.random.EasyRandom;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CloudFunctionApplication.class)
@WebAppConfiguration
@ActiveProfiles("local")
@TestPropertySource(properties = {
    "amazon.dynamodb.endpoint=http://localhost:8000/",
    "amazon.aws.accesskey=test1",
    "amazon.aws.secretkey=test231"})
public class UserTest {

  private DynamoDBMapper dynamoDBMapper;

  @Autowired
  private AmazonDynamoDB amazonDynamoDB;

  private EasyRandom generator;

  @Autowired
  UserRepository repository;

  @Autowired
  UserService userService;

  @Autowired
  Gson gson;

  @Before
  public void setup() {
    generator = new EasyRandom();

    dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

    CreateTableRequest tableRequest = dynamoDBMapper
        .generateCreateTableRequest(User.class);
    tableRequest.setProvisionedThroughput(
        new ProvisionedThroughput(1L, 1L));
    amazonDynamoDB.createTable(tableRequest);

    dynamoDBMapper.batchDelete(repository.findAll());
  }

  @After
  public void cleanDatabase() {
    dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

    DeleteTableRequest deleteTableRequest = dynamoDBMapper
        .generateDeleteTableRequest(User.class);
    amazonDynamoDB.deleteTable(deleteTableRequest);
  }

  @Test
  public void addUser() {
    UserInput userInput = generator.nextObject(UserInput.class);

    Optional<User> addedUser = userService.addUser(userInput);

    assertTrue(addedUser.isPresent());
    assertEquals(userInput.getEmail(), addedUser.get().getEmail());
  }

  @Test
  public void getUsersByUserName() {
    Optional<User> user = userService.addUser(generator.nextObject(UserInput.class));
    userService.addUser(generator.nextObject(UserInput.class));
    userService.addUser(generator.nextObject(UserInput.class));

    Optional<Iterable<User>> foundedUsers = userService.getUsersByName(user.get().getUserName().substring(1, 5));

    assertTrue(foundedUsers.isPresent());
    assertTrue(((Collection<User>) foundedUsers.get()).contains(user.get()));
  }

}
