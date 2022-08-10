package poc.inetum.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserDto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private int statusCode;
    private String status;

}
