/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

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
import org.springframework.validation.FieldError;
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

    @Autowired
    AuthenticationManager authenticationManager;

    @GetMapping("/registration")
    public String registrationMain(@ModelAttribute User user) {
        return "registration";
    }

    @GetMapping("/registrationcompleted")
    public String registrationCompleted() {
        return "registrationcompleted";
    }

    

    @PostMapping("/registration")
    public String createNewUser(@Valid @ModelAttribute User user, BindingResult bindingResult,HttpSession session) {
        
        
        if (userRepository.findByProfileidentificationstring(user.getProfileidentificationstring())!=null) {
            FieldError error = new FieldError("user", "profileidentificationstring",
                                      "This identification string is already in use.");
    bindingResult.addError(error);
}
              if (userRepository.findByUsername(user.getUsername())!=null) {
            FieldError error = new FieldError("user", "username",
                                      "This username is already in use.");
    bindingResult.addError(error);
}

        
        
         if(bindingResult.hasErrors()) {
        return "registration";
    }
        String usersUnsecuredPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        userRepository.flush();//tässä käyttäjä ihan oikeasti viedään tietokantaan (debuggauksen seurauksena alempi koodi ei toiminut ilman tätä

        UsernamePasswordAuthenticationToken tokenForAuthentication = new UsernamePasswordAuthenticationToken(user.getUsername(), usersUnsecuredPassword); //säiliö joka sisältää käyttäjänimi ja salasana
        Authentication auth = authenticationManager.authenticate(tokenForAuthentication); //tunnistautuminen käyttäjälle, käynee customuserservicellä tarkistamassa, että käyttäjä on.
        //Manager tietää, etät säiliö annetaan sellaiselle käsittelijälle, joka käsittelee salasanoja ja käyttäjänimiä

        SecurityContext sc = SecurityContextHolder.getContext(); //haetaan olio joka pitää tämänhetkiestä kirjautumisesta huolta. Säilöö kirjautumistietoja pidemmällä ajalla.
        sc.setAuthentication(auth);                             //asetetaan tunnistautuneelle käyttäjälle lupa profiiliin menemiseen. Tämä sc tietää nyt, että tällä käyttäjällä on lupa mennä profiiliin.(pidemmän ajan)

        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc); //palvelimelle lähetetään käytätjän vontext_key jolla palvelin tietää, että kenen tiedot haetaan. Sessio ei häviä muuta kuin poistamalla västeet. 
        //Sessio ei ole sama kuin kirjautumiskerta. Tähän sessioon tallentuu, että kirjautuneena on tämä tietty tyyppi. 
        //ja myöhemmin se luetaan automaattisesti, että kuka on kirjautuneena Springin toimesta.

        return "redirect:/registrationcompleted";
    }
    
    
    
    
}
