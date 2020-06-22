/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author ajnarhi
 */

@Controller
public class RegistrationController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    PasswordEncoder passwordEncoder;
    
    
    private User user;
    
     @GetMapping("/registration")
    public String registrationMain() {
        return "registration";
    }
    
      @GetMapping("/registrationcompleted")
    public String registrationCompleted() {
        return "registrationcompleted";
    }
    
//    @GetMapping("/signin")
//    public String signIn(){
//        
//        return "signin";
//    }
    
    @GetMapping("/profile")
    public String showProfile(Model model){
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         String username = auth.getName();
        
        user=userRepository.findByUsername(username);
        model.addAttribute("name", user.getRealname() );
        
        return "profile";
    }
    
    @PostMapping("/registration")
    public String createNewUser(@ModelAttribute User user){
        //tähän pitäisi saada salasanan salaus
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
    userRepository.save(user);
    return "redirect:/registrationcompleted";
}
    
//    @PostMapping("/signin")
//    public String signUserIn(@RequestParam String username, @RequestParam String password){
//    user=userRepository.findByUsernameAndPassword(username, password);
//   
//    return "redirect:/profile";
//}
}
