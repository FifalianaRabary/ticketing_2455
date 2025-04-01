package ticketingspring.controller;


import ticketingspring.model.PrixVolEnfant;
import ticketingspring.model.TypeSiege;
import ticketingspring.model.Vol;
import ticketingspring.service.PrixVolEnfantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.annotation.ComponentScan;


import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/prixVolEnfant")
@ComponentScan(basePackages = {"controller", "service", "model"})
public class PrixVolEnfantController {

    @Autowired
    private PrixVolEnfantService prixVolEnfantService;

    @GetMapping("/ajouter")
    public String afficherFormulaire(Model model) throws Exception {
        try {
            List<Vol> vols = prixVolEnfantService.getAllVols();
            List<TypeSiege> typesSiege = prixVolEnfantService.getAllTypeSiege();
            model.addAttribute("typesSiege", typesSiege);
            model.addAttribute("prixVolEnfant", new PrixVolEnfant());
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("message", "Erreur lors de la récupération des données.");
        }
        return "formulairePrixVolEnfant";
    }

    @PostMapping("/ajouter")
    public String ajouterPrixVolEnfant(@ModelAttribute PrixVolEnfant prixVolEnfant, Model model) throws Exception {
        try {
            prixVolEnfantService.save(prixVolEnfant);
            model.addAttribute("message", "Prix vol enfant ajouté avec succès !");
            return "redirect:/prixVolEnfant/ajouter";
        } catch (SQLException e) {
            model.addAttribute("message", "Erreur lors de l'ajout du prix vol enfant.");
            e.printStackTrace();
            return "formulairePrixVolEnfant";
        }
    }
}
