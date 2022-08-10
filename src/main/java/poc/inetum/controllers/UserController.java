package poc.inetum.controllers;


import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.AccessTokenResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poc.inetum.DTO.UserDto;
import poc.inetum.flowable.repository.UserRepository;


import javax.annotation.security.RolesAllowed;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    private String authServerUrl = "http://localhost:8180/auth";
    private String realm = "anme";

   //this signin method is for the password grant_type
    @PostMapping(path = "/signin-with-password")
    public ResponseEntity<?> signin(@RequestBody UserDto userDto) {
        Map<String, Object> clientCredentials = new HashMap<>();
        clientCredentials.put("secret", "ZKpsrDqj4d9QMzyH5uVKYVUcYFt6KQ0z");
        clientCredentials.put("grant_type", "password");
        Configuration configuration = new Configuration(authServerUrl, realm, "springboot-with-password-grant_type", clientCredentials, null);
        AuthzClient authzClient = AuthzClient.create(configuration);
        AccessTokenResponse response = authzClient.obtainAccessToken(userDto.getFirstname(), userDto.getPassword());
        return ResponseEntity.ok(response);
    }

    //this signin method is for the client_credentials grant_type
    @PostMapping(path = "/signin-with-client_credentials")
    public ResponseEntity<?> signin1(@RequestBody String clientSecret) {
        Map<String, Object> clientCredentials = new HashMap<>();
        clientCredentials.put("secret", clientSecret);
        clientCredentials.put("grant_type", "client_credentials");
        Configuration configuration = new Configuration(authServerUrl, realm,"springboot-with-client_credentials-grant_type", clientCredentials, null);
        AuthzClient authzClient = AuthzClient.create(configuration);
        AccessTokenResponse response = authzClient.obtainAccessToken();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<UserDto> save(@RequestBody UserDto userDto){
        return ResponseEntity.ok(userRepository.save(userDto));

    }

    @GetMapping("/listUsers")
    public ResponseEntity<List<UserDto>> findAll(){
        return ResponseEntity.ok(userRepository.findAll());

    }
}
