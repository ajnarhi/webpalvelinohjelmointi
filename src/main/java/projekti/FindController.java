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

        User user = userRepository.findByRealname(name);
        model.addAttribute("user", user);

        return "findconnections";
    }

    @PostMapping("/addconnection")
    public String addConnection(@RequestParam String name) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); //user pitää hakea tässä uudelleen, että saadaan skillsi oikealle käyttäjälle. Muuten se voisi mennä välissä kirjautuneelle esim.
        String username = auth.getName();                                               //koska on käyttäjä kirjautuneena niin Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
        User user = userRepository.findByUsername(username);                            //voidaan hakea juuri se käyttäjä.

        Connection connection = new Connection(false, userRepository.findByUsername(name), user); //false tarkoittaa, että ei vielä hyväksytty, kuka lisätään, kenelle lisätään
        connectionRepository.save(connection);

        return "requestsent";
    }

    @PostMapping("/connectionapproval")
    public String connectionApproval(@RequestParam String accept, Long id) {
        Connection connection = connectionRepository.getOne(id);
        if (accept.equals("Add connection!")) {

            connection.setAccepted(Boolean.TRUE);
            connectionRepository.save(connection);
        } else {
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
