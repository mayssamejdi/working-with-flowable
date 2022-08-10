package poc.inetum.flowable.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poc.inetum.flowable.domain.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
