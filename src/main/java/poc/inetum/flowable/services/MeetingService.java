package poc.inetum.flowable.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poc.inetum.flowable.domain.Meeting;
import poc.inetum.flowable.domain.Message;
import poc.inetum.flowable.repository.MeetingRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Transactional
public class MeetingService {

    private MeetingRepository meetingRepository;

    public MeetingService(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    public Meeting getMeeting(Long id) {
        Optional<Meeting> meeting = this.meetingRepository.findById(id);
        if (meeting.isPresent())
            return meeting.get();
        return null;
    }

    public Message refused(Meeting meeting) {
        return new Message("Meeting Refused. " + meeting.getDateMeeting() + " passed !!!");
    }

    public Message accepted(Meeting meeting) {
        return new Message("Your Meeting on " + meeting.getDateMeeting() + " will be treated soon ");
    }

    public String getLocalDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/mm/dd h:m");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}

