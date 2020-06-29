/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author ajnarhi
 */
public interface ConnectionRepository extends JpaRepository<Connection, Long>  {
    
    List<Connection> findByWhowasaskedAndAccepted(User user, Boolean accepted);
    List<Connection> findByWhoaskedAndAccepted (User user, Boolean accepted);

    
}
