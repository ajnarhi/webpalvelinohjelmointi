/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author ajnarhi
 */
@Controller
public class SigninController {
    @Autowired
    UserRepository userRepository;
    
    

    @GetMapping("/signin")
    public String signIn(){
        
        return "signin";
    }
    
    @GetMapping("/signoutshow")
    public String signOut(){
        
        return "signout";
    }
    
}
