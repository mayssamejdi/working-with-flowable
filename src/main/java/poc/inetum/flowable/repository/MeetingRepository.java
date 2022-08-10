package poc.inetum.flowable.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poc.inetum.flowable.domain.Meeting;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
}
