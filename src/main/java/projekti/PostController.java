/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
public class PostController {
    
      @Autowired
    private UserRepository userRepository;
    
    
    @Autowired
    PostRepository postRepository;
    
      @Autowired
    ConnectionRepository connectionRepository;
    
    @GetMapping("/posts")
    public String showPosts(Model model){
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userRepository.findByUsername(username);
        
        
        
        List<Connection> connections = connectionRepository.findByWhowasaskedAndAccepted(user, Boolean.TRUE);
        
        connections.addAll(connectionRepository.findByWhoaskedAndAccepted(user, Boolean.TRUE));
        List<User>usersWithConnection=new ArrayList<>();
        usersWithConnection.add(user);
        for (Connection conn:connections){
            usersWithConnection.add(conn.getWhoasked());
            usersWithConnection.add(conn.getWhowasasked());
        }
       
        
        model.addAttribute("posts", postRepository.findByUserIn(usersWithConnection) );
        return "posts";
    }
    
    @PostMapping("/posts")
    public String addPostToDB(@RequestParam String message){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userRepository.findByUsername(username);
        LocalDateTime date=LocalDateTime.now();
        Post post=new Post(date,message, user);
        postRepository.save(post);
        
        return "redirect:/posts";
    }
    
    
}
