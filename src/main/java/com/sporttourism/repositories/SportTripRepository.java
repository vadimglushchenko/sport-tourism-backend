package com.sporttourism.repositories;

import com.sporttourism.entities.SportTrip;
import java.util.Optional;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface SportTripRepository extends CrudRepository<SportTrip, String> {

  Optional<Iterable<SportTrip>> findByIsCompleted(Boolean isCompleted);

}
