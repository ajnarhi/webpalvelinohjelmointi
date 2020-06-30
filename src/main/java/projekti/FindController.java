/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author ajnarhi
 */
@Controller
public class FindController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ConnectionRepository connectionRepository;

    //jos private User user olisi täällä niin kuka vaan näkisi edellisen haetun
    @GetMapping("/findconnections")
    public String findconnectionsMain() {

        return "findconnections";
    }

    @PostMapping("/findconnections")
    public String searchConnection(@RequestParam String name, Model model) {
Authentication auth = SecurityContextHolder.getContext().getAuthentication(); //user pitää hakea tässä uudelleen, että saadaan skillsi oikealle käyttäjälle. Muuten se voisi mennä välissä kirjautuneelle esim.
        String username = auth.getName();                                               //koska on käyttäjä kirjautuneena niin Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
        User user = userRepository.findByUsername(username);
        User searcheduser = userRepository.findByRealname(name);
        if(!user.getRealname().equals(searcheduser.getRealname())){
        model.addAttribute("user", searcheduser);

        return "findconnections";
        }else{
            return "triedtofindyourself";
        }
    }

    @PostMapping("/addconnection")
    public String addConnection(@RequestParam String name) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); //user pitää hakea tässä uudelleen, että saadaan skillsi oikealle käyttäjälle. Muuten se voisi mennä välissä kirjautuneelle esim.
        String username = auth.getName();                                               //koska on käyttäjä kirjautuneena niin Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
        User user = userRepository.findByUsername(username);//voidaan hakea juuri se käyttäjä.
        Connection connection = new Connection(false, userRepository.findByUsername(name), user); //jos olen jo lähettänyt pyynnön en voi lhettää toista pyyntöä
        Connection connectionOther=new Connection(true, user, userRepository.findByUsername(name)); // jos toinen tyyppi pyytänyt minua niin en voi tehdä uutta pyyntöä
        Connection connectionThird=new Connection(true, userRepository.findByUsername(name), user); //on olemassa jo hyväksytty yhteys. en voi enää liätä uutta
        if (!user.getConnections().contains(connection) && !user.getAskedconnections().contains(connectionOther) && !user.getConnections().contains(connectionThird)) {
             
            connectionRepository.save(connection);

            return "requestsent";
        }else{
            return "personalreadyonyourconnections";
        }
    }

    @PostMapping("/connectionapproval")
    public String connectionApproval(@RequestParam String accept, Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
        String username = auth.getName();                                               
        User user = userRepository.findByUsername(username);
        Connection connection = connectionRepository.getOne(id);
        
        for(Connection connectionOther:user.getConnections()){
            if(connectionOther.getWhoasked().equals(connection.getWhowasasked() )&& connectionOther.getWhowasasked().equals(connection.getWhoasked())){
                if(connectionOther.getAccepted()){
                    connectionRepository.delete(connection);
                    connection=connectionOther;
                }else{
                    connectionRepository.delete(connectionOther);
                }
            }
        }
        
        
        if (accept.equals("Add connection!")) {

            connection.setAccepted(Boolean.TRUE);
            connectionRepository.save(connection);
      
        }else{
            connectionRepository.delete(connection);
        }
        return "redirect:/profile";
    }

    @PostMapping("/cancelconnection")
    public String cancelConnection(@RequestParam String cancel, Long id) {
        Connection connection = connectionRepository.getOne(id);

        connectionRepository.delete(connection);

        return "redirect:/profile";
    }
}
