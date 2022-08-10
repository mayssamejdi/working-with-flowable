package poc.inetum.flowable.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poc.inetum.DTO.UserDto;

@Repository
public interface UserRepository extends JpaRepository<UserDto, Long> {
}
