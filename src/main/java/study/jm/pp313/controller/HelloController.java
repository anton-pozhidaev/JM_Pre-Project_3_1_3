package study.jm.pp313.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import study.jm.pp313.service.UserService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HelloController {

    private UserService userService;

    @Autowired
    public HelloController(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    private void initMethod() {
        userService.addInitUsersToDB();
    }

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
