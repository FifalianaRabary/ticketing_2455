package ticketingspring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping("/test")
public class TestController {

    @GetMapping("/test")
    public String afficher(Model model) throws Exception {
       
        return "test";
    }

    
}
