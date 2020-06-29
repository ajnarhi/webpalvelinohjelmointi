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
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.stereotype.Component;

/**
 *
 * @author ajnarhi
 */
@Entity
@Data @NoArgsConstructor @AllArgsConstructor

public class Skill extends AbstractPersistable<Long> {
    
    
    private String nameOfTheSkill;
    private Integer timesComplimented;
    @ManyToOne
    private User user;
}
