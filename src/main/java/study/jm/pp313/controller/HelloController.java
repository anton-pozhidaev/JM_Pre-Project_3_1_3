package study.jm.pp313.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import study.jm.pp313.dao.RoleDao;
import study.jm.pp313.model.Role;
import study.jm.pp313.model.User;
import study.jm.pp313.service.UserService;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class HelloController {

    private UserService userService;
    private RoleDao roleDao;

    @Autowired
    public HelloController(UserService userService, RoleDao roleDao) {
        this.userService = userService;
        this.roleDao = roleDao;
    }

    @PostConstruct
    private void initMethod() {
        userService.addInitUsersToDB();
    }

    @GetMapping(value = "/admin")
    public String adminTemplate(Principal principal, Model model) {
        model.addAttribute("userLogged", userService.findUserByUsername(principal.getName()));
        return "users";
    }

/*
    @GetMapping(value = "/admin")
    public String adminTemplate(@ModelAttribute User user,
                                Principal principal, Model model) {
        Set<Role> allRoles = new HashSet<>(roleDao.getAllRoles());
        model.addAttribute("userLogged", userService.findUserByUsername(principal.getName()));
        model.addAttribute("allRoles", allRoles);
        model.addAttribute("usersAll", userService.listUsers());
        return "users";
    }
*/

    @GetMapping(value = "/hello")
    public String printWelcome(ModelMap model) {
        List<String> messages = new ArrayList<>();
        messages.add("Hello!");
        messages.add("I'm Spring MVC application");
        messages.add("5.2.12 version by feb'21");
//        messages.add("yoyoyo buuaa");
        model.addAttribute("messages", messages);
        return "index";
    }

    @GetMapping(value = "/login")
    public String loginPage() {
        return "login";
    }
}
