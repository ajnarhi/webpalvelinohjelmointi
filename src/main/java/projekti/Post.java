/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.util.Date;
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
@Data 
@NoArgsConstructor 
@AllArgsConstructor
public class Post extends AbstractPersistable<Long> {
    
    @ManyToOne
    private User user;
    //private Date date;
    private String message;
    
    
}


//Kirjautuneet käyttäjät voivat lähettää postauksia yhteisellä sivulla. Postaussivulla näkyy yhteydessä olevien henkilöiden postaukset. 
//Jokaisesta viestistä näytetään viestin lähettäjän nimi, viestin lähetysaika, sekä viestin tekstimuotoinen sisältö.
//Viestit näytetään postauslistassa niiden saapumisjärjestyksessä siten, että postauslistassa näkyy aina korkeintaan 25 uusinta viestiä.