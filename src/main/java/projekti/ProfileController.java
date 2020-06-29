/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.util.List;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author ajnarhi
 */
@Controller
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    ConnectionRepository connectionRepository;
    
    
    @GetMapping("/profile/{id}")
    public String personalPageForOthersToView(Model model,@PathVariable String id){
        
        User userForOthersToView=userRepository.findByProfileidentificationstring(id);
        model.addAttribute("name", userForOthersToView.getRealname());
        model.addAttribute("id",userForOthersToView.getProfileidentificationstring() );
        model.addAttribute("skills", userForOthersToView.getSkills());
        
       
        
        
        return "profilepageforotherstoview";
    }
    
    
    @PostMapping("/profile/{userid}")
    public String personalPageForOthersToView(@RequestParam Long id, @PathVariable String userid){
        User userForOthersToView=userRepository.findByProfileidentificationstring(userid);
        Skill skill=skillRepository.getOne(id);
        int compliments=skill.getTimesComplimented();
        compliments++;
        skill.setTimesComplimented(compliments);
        skillRepository.save(skill);
        
        
        return "redirect:/profile/{userid}";
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userRepository.findByUsername(username);
        List<Connection> connections = connectionRepository.findByWhowasaskedAndAccepted(user, Boolean.TRUE);
        connections.addAll(connectionRepository.findByWhoaskedAndAccepted(user, Boolean.TRUE));
        model.addAttribute("name", user.getRealname());
        model.addAttribute("skills", user.getSkills());

        model.addAttribute("waitingforotherpersonsapproval", connectionRepository.findByWhoaskedAndAccepted(user, Boolean.FALSE));
        model.addAttribute("waitingforyourapproval", connectionRepository.findByWhowasaskedAndAccepted(user, Boolean.FALSE));
        model.addAttribute("approvedconnections", connections);

        return "profile";
    }

    @PostMapping("/profile")
    public String addSkillToUser(@RequestParam String skill) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); //user pitää hakea tässä uudelleen, että saadaan skillsi oikealle käyttäjälle. Muuten se voisi mennä välissä kirjautuneelle esim.
        String username = auth.getName();                                               //koska on käyttäjä kirjautuneena niin Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
        User user = userRepository.findByUsername(username);                            //voidaan hakea juuri se käyttäjä.

        Skill newSkill = new Skill(skill,0, user);
        skillRepository.save(newSkill);
        skillRepository.flush();

        return "redirect:/profile";

    }

}
