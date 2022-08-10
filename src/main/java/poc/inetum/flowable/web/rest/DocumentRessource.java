package poc.inetum.flowable.web.rest;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import poc.inetum.DTO.DocDTO;
import poc.inetum.DTO.ResponseMessage;
import poc.inetum.flowable.services.CmisService;
import poc.inetum.flowable.services.FilesStorageService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/")
@CrossOrigin(origins = "http://localhost:4200")
public class DocumentRessource {

    private final CmisService cmisService;
    private final FilesStorageService storageService;


    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            storageService.save(file);
            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            cmisService.documentUpload();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }

    }

    @GetMapping("/documents")
    public ResponseEntity<List<DocDTO>> getListFiles() {

        List<DocDTO> listOfDocs = this.cmisService.getSiteContent();
        return new ResponseEntity<>(listOfDocs, HttpStatus.OK);

    }


}
