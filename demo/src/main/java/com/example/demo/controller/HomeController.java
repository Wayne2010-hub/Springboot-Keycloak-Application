package com.example.demo.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;
import java.util.List;

@Controller
public class HomeController {

    private static final String REALM_ACCESS_CLAIM = "realm_access";
    private static final String ROLES_CLAIM = "roles";

    @SuppressWarnings("unchecked")
    @GetMapping("/home")
    public String getHomePage(Model model) {

        final DefaultOidcUser auth = (DefaultOidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //Vorname
        String firstname = auth.getUserInfo().getGivenName();
        //Nachname
        String lastname = auth.getUserInfo().getFamilyName();
        //Geburtstag
        String birthdate = auth.getUserInfo().getBirthdate();
        //Email
        String email = auth.getUserInfo().getEmail();
        //Straße
        String street = auth.getUserInfo().getLocale();
        //Ort
        //String town

        String username = auth.getUserInfo().getPreferredUsername();
        model.addAttribute("username", username);
        model.addAttribute("firstname", firstname);
        model.addAttribute("lastname", lastname);
        model.addAttribute("birthdate", birthdate);
        model.addAttribute("email", email);
        model.addAttribute("street", street);

        //var realmAccess = auth.getClaimAsMap(REALM_ACCESS_CLAIM);
        var roles = (Collection<String>) ((Collection<?>) auth.getClaimAsMap(REALM_ACCESS_CLAIM).get(ROLES_CLAIM)).stream().toList();
        List<String> NuPRoles = roles.stream()
                .filter(role -> role.startsWith("role")).toList();
        if (NuPRoles.isEmpty()) {
            model.addAttribute("role", "Keine Rolle zugewiesen!");
            return "home";
        }
        model.addAttribute("role", NuPRoles);
        return "home";
    }
}
