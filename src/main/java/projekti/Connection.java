/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author ajnarhi
 */
@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Connection extends AbstractPersistable<Long> {
    
    private Boolean accepted;
    @ManyToOne
    private User whowasasked;
    @ManyToOne
    private User whoasked;
    
}
