package com.sporttourism;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.sporttourism.entities.Comment;
import com.sporttourism.entities.SportTrip;
import com.sporttourism.payload.SportTripInput;
import com.sporttourism.repositories.SportTripRepository;
import com.sporttourism.service.SportTripService;
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
public class SportTripTest {

  private DynamoDBMapper dynamoDBMapper;

  @Autowired
  private AmazonDynamoDB amazonDynamoDB;

  private EasyRandom generator;

  @Autowired
  SportTripRepository repository;

  @Autowired
  SportTripService sportTripService;

  @Autowired
  Gson gson;

  @Before
  public void setup() {
    generator = new EasyRandom();

    dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

    CreateTableRequest tableRequest = dynamoDBMapper
        .generateCreateTableRequest(SportTrip.class);
    tableRequest.setProvisionedThroughput(
        new ProvisionedThroughput(1L, 1L));
    amazonDynamoDB.createTable(tableRequest);

    dynamoDBMapper.batchDelete(repository.findAll());
  }

  @After
  public void cleanDatabase() {
    dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

    DeleteTableRequest deleteTableRequest = dynamoDBMapper
        .generateDeleteTableRequest(SportTrip.class);
    amazonDynamoDB.deleteTable(deleteTableRequest);
  }

  @Test
  public void addSportTrip() {
    SportTripInput sportTrip = generator.nextObject(SportTripInput.class);

    Optional<SportTrip> addedSportTrip = sportTripService.addSportTrip(sportTrip);

    assertTrue(addedSportTrip.isPresent());
    assertEquals(sportTrip.getLocationName(), addedSportTrip.get().getLocationName());
  }

  @Test
  public void addCommentToSportTrip() {
    SportTrip sportTrip = sportTripService.addSportTrip(generator.nextObject(SportTripInput.class)).get();

    Comment comment = generator.nextObject(Comment.class);
    Comment commentSecond = generator.nextObject(Comment.class);

    sportTripService.addCommentToSportTrip(sportTrip.getId(), comment);
    Optional<SportTrip> updatedSportTrip = sportTripService.addCommentToSportTrip(sportTrip.getId(), commentSecond);

    assertTrue(updatedSportTrip.isPresent());
    assertEquals(((Collection<Comment>) updatedSportTrip.get().getComments()).size(), 2);
    assertEquals(updatedSportTrip.get().getComments(), Lists.newArrayList(comment, commentSecond));
  }

  @Test
  public void getActiveSportTrips() {
    sportTripService.addSportTrip(generator.nextObject(SportTripInput.class));
    sportTripService.addSportTrip(generator.nextObject(SportTripInput.class));
    sportTripService.addSportTrip(generator.nextObject(SportTripInput.class));

    Optional<Iterable<SportTrip>> sportTripsByStatus = sportTripService.getSportTripsByIsCompleted(Boolean.FALSE);

    assertTrue(sportTripsByStatus.isPresent());
    assertEquals(((Collection<SportTrip>) sportTripsByStatus.get()).size(), 3);
  }

}
