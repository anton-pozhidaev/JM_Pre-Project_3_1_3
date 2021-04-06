package study.jm.pp313.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import study.jm.pp313.dao.RoleDao;
import study.jm.pp313.model.Role;
import study.jm.pp313.model.User;
import study.jm.pp313.service.UserService;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminsController {

    private UserService userService;
    private RoleDao roleDao;

    @Autowired
    public AdminsController(UserService userService, RoleDao roleDao) {
        this.userService = userService;
        this.roleDao = roleDao;
    }

    @GetMapping
    public String getList(@ModelAttribute User user,
                          Principal principal, Model model) {
        Set<Role> allRoles = new HashSet<>(roleDao.getAllRoles());
        model.addAttribute("userLogged", userService.findUserByUsername(principal.getName()));
        model.addAttribute("allRoles", allRoles);
        model.addAttribute("usersAll", userService.listUsers());
        return "users/users";
    }

    @Transactional
    @PostMapping
    public String create(@ModelAttribute("user") User user,
                         @RequestParam("rolesChecked") String[] rolesStrArray) {
        Set<Role> userRolesForCreate = new HashSet<>(Arrays.stream(rolesStrArray)
                .map(rStr -> roleDao.findByRoleName(rStr)).collect(Collectors.toList()));
        user.setRoles(userRolesForCreate);
        userService.add(user);
        return "redirect:/admin";
    }

    @Transactional
    @PatchMapping("/id/{id}")
    public String update(@ModelAttribute("user") User user,
                         @PathVariable("id") long id,
                         @RequestParam("rolesChecked") String[] rolesStrArray
    ) {
        Set<Role> userRolesForUpdate = new HashSet<>(Arrays.stream(rolesStrArray)
                .map(sR -> roleDao.findByRoleName(sR)).collect(Collectors.toList()));
        user.setRoles(userRolesForUpdate);
        userService.update(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/id/{id}")
    public String delete(@PathVariable("id") long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

/*
    @GetMapping("/id/{id}")
    public String show(@PathVariable("id") long id, Model model) {
        model.addAttribute("usr", userService.get(id));
        return "users/show";
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user, Model model) {
        Set<Role> allRoles = new HashSet<>(roleDao.getAllRoles());
        model.addAttribute("allRoles", allRoles);
        return "users/new";
    }

    @GetMapping("/id/{id}/edit")
    public String edit(Model model, @PathVariable("id") long id) {
        User userForUpdate = userService.get(id);
        model.addAttribute("user", userForUpdate);
        model.addAttribute("allRoles", new HashSet<>(roleDao.getAllRoles()));
        model.addAttribute("userRoles", new HashSet<>(userForUpdate.getRoles()
                .stream().map(Role::getRole).collect(Collectors.toSet())));
        return "users/edit";
    }
*/
}
