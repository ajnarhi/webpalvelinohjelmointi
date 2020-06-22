/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author ajnarhi
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "Usertable")
public class User extends AbstractPersistable<Long>{
    
    private String username;
    private String password;
    private String realname;
    private String profileidentificationstring;
    
}
