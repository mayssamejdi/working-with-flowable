package poc.inetum.flowable.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import poc.inetum.flowable.domain.Meeting;
import poc.inetum.flowable.repository.MeetingRepository;
import poc.inetum.flowable.services.AdminService;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;
    @Autowired
    MeetingRepository meetingRepository;

    @GetMapping("/getListRdv")
    public List<Meeting> getList() {
        return this.adminService.getRdvList();
    }

    @PostMapping("/accept/{meetingId}")
    public void accept(@PathVariable long meetingId) {
        Meeting meeting = meetingRepository.getById(meetingId);
        meeting.setAccepted(true);
        meetingRepository.save(meeting);
    }

    @PostMapping("/refuse/{meetingId}")
    public void refuse(@PathVariable long meetingId) {
        Meeting meeting = meetingRepository.getById(meetingId);
        meeting.setAccepted(false);
        meetingRepository.save(meeting);
    }
}
