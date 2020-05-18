package com.sporttourism.repositories;

import com.sporttourism.entities.User;
import java.util.Optional;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface UserRepository extends CrudRepository<User, String> {

  Optional<Iterable<User>> findByUserNameContaining(String userNamePart);

}
