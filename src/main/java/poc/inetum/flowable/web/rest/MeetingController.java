package poc.inetum.flowable.web.rest;

import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poc.inetum.flowable.domain.Meeting;
import poc.inetum.flowable.domain.Message;
import poc.inetum.flowable.repository.MeetingRepository;
import poc.inetum.flowable.services.MeetingService;

import javax.annotation.security.RolesAllowed;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/meeting")
@CrossOrigin(origins = "*")
public class MeetingController {
    @Autowired
    MeetingService meetingService;
    @Autowired
    MeetingRepository meetingRepository;

    private final RuntimeService runtimeService;
    private final HistoryService historyService;
    private boolean valid;
    private Date meetingDate;
    private Date now;

    public MeetingController(final RuntimeService runtimeService, final HistoryService historyService) {
        this.runtimeService = runtimeService;
        this.historyService = historyService;
    }


    @PostMapping("/send/{meetingId}")
    public ResponseEntity<Message> accepted(@PathVariable("meetingId") long meetingId) throws ParseException {
        Message message;
        Map<String, Object> variables = new HashMap<>();
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/mm/dd h:m");
        Meeting meeting = meetingService.getMeeting(meetingId);
        try {
            now = sdFormat.parse(meetingService.getLocalDate());
            meetingDate = sdFormat.parse(meeting.getDateMeeting());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (meetingDate != null) {
            if (meetingDate.before(now)) {
                valid = false;
            } else {
                valid = true;
            }
        }
        variables.put("meetingId", meetingId);
        variables.put("valid", valid);
        final ProcessInstance process = runtimeService.startProcessInstanceByKey("meetingProcess", variables);
        final HistoricVariableInstance hvi = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(process.getId())
                .variableName("result").singleResult();
        message = (Message) hvi.getValue();
        return ResponseEntity.ok(message);
    }

    @PostMapping(path = "/addMeeting")
    public Meeting addRDV(@RequestBody Meeting meeting) {
        return meetingRepository.save(meeting);
    }

    @PutMapping(path = "/update/{id}")
    public Meeting updateRDV(@RequestBody Meeting meeting, @PathVariable long id) {
        Meeting meetingToUpdate = meetingRepository.getById(id);
        meetingToUpdate.setDateMeeting(meeting.getDateMeeting());
        meetingToUpdate.setAccepted(meeting.isAccepted());
        return meetingRepository.save(meetingToUpdate);
    }

    @DeleteMapping(path = "/delete/{id}")
    public void deleteRDV(@PathVariable long id) {
        Meeting meetingToDelete = meetingRepository.getById(id);
        if (!(meetingToDelete.toString().isEmpty())) {
            meetingRepository.delete(meetingToDelete);
        }
    }
}
