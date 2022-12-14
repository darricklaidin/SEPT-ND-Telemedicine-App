package sept.superfive.authmicroservice.repository;

import sept.superfive.authmicroservice.model.Role;
import sept.superfive.authmicroservice.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleName roleName);

    Boolean existsByName(RoleName roleName);
}
