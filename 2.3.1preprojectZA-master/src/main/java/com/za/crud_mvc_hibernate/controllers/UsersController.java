package com.za.crud_mvc_hibernate.controllers;


import com.za.crud_mvc_hibernate.model.User;
import com.za.crud_mvc_hibernate.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
//@RequestMapping("/users")
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    //    security 2.4.2
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {
        List<String> messages = new ArrayList<>();
        messages.add("Hello!");
        messages.add("I'm Spring MVC-SECURITY application");
        messages.add("5.2.12 version by dec'20 ");
        model.addAttribute("messages", messages);
        return "hello";
    }

    @GetMapping(value = "/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/user")
    public String getUserInfo(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("user", userService.getUserByName(auth.getName()));

        return "user/info";
    }

    @GetMapping("/admin")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());

        return "admin/all";
    }

    @GetMapping("/admin/add")
    public String newUser(Model model) {//@ModelAttribute("user") User user,
        model.addAttribute("user", new User());
        model.addAttribute("roles", userService.getAllRoles());

        return "admin/add";
    }

    @PostMapping("/admin/add")
    public String create(@ModelAttribute("user") User user) {
        userService.saveUser(user);

        return "redirect:/admin";
    }

    @GetMapping("/admin/edit/{id}")
    public String showUser(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        model.addAttribute("roles", userService.getAllRoles());

        return "admin/edit";
    }

    @PostMapping("/admin/edit")
    public String edit(@ModelAttribute("user") User user) {
        userService.editUser(user);

        return "redirect:/admin";
    }

    @GetMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") int id){
        userService.deleteUser(id);

        return "redirect:/admin";
    }
}