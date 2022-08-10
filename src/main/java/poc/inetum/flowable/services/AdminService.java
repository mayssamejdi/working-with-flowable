package poc.inetum.flowable.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poc.inetum.flowable.domain.Admin;
import poc.inetum.flowable.domain.Meeting;
import poc.inetum.flowable.repository.AdminRepository;
import poc.inetum.flowable.repository.MeetingRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class AdminService {

    @Autowired
    MeetingRepository meetingRepository;
    @Autowired
    AdminRepository adminRepository;

    public List<Meeting> getRdvList() {
        List<Meeting> allMeetings;
        allMeetings = this.meetingRepository.findAll();
        return allMeetings;
    }

    public void addAdmin(Admin admin) {
        this.adminRepository.save(admin);
    }

}
