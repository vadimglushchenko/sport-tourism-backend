package com.sporttourism;

import static org.junit.Assert.assertTrue;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.sporttourism.entities.SportTrip;
import com.sporttourism.entities.TripDifficulty;
import com.sporttourism.entities.TripType;
import com.sporttourism.repositories.SportTripRepository;
import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Ignore;
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
    "amazon.aws.secretkey=test231" })
public class SportTripRepositoryTest {

  private DynamoDBMapper dynamoDBMapper;

  @Autowired
  private AmazonDynamoDB amazonDynamoDB;

  @Autowired
  SportTripRepository repository;

  @Before
  public void setup() {
    dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

    CreateTableRequest tableRequest = dynamoDBMapper
        .generateCreateTableRequest(SportTrip.class);
    tableRequest.setProvisionedThroughput(
        new ProvisionedThroughput(1L, 1L));
    amazonDynamoDB.createTable(tableRequest);

    dynamoDBMapper.batchDelete(repository.findAll());
  }

  @Ignore
  @Test
  public void addSportTrip() {
    SportTrip sportTrip = SportTrip.builder()
        .id("1")
        .cost(1000.)
        .locationName("Krim")
        .maxGroupCount(12)
        .tripDate("15 May, 2020")
        .tripDifficulty(TripDifficulty.TWO.toString())
        .tripDuration(15)
        .tripType(TripType.HIKING.toString())
        .isFinished(false)
        .isRemoved(false)
        .build();
    repository.save(sportTrip);

    List<SportTrip> result
        = (List<SportTrip>) repository.findAll();

    assertTrue("Not empty", result.size() > 0);
//    assertTrue("Contains item with expected cost",
//        result.get(0).getCost().equals(EXPECTED_COST));
  }
}
