package sept.superfive.authmicroservice.repository;

import sept.superfive.authmicroservice.model.Role;
import sept.superfive.authmicroservice.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(String email);

    User getByUserID(int id);

    Boolean existsByEmail(String email);

    Boolean existsByRolesIn(ArrayList<Role> roles);
}