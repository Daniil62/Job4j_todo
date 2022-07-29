package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.AccountService;
import ru.job4j.todo.service.ItemService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class AccountController {

    private final AccountService accountService;
    private final ItemService itemService;

    public AccountController(AccountService service, ItemService itemService) {
        this.accountService = service;
        this.itemService = itemService;
    }

    @GetMapping("/formRegistration")
    public String createUser(Model model, HttpSession session) {
        addUserAttribute(model, session);
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute User user, Model model,
                               HttpServletRequest req) {
        boolean validate = accountService.validate(user);
        Optional<User> regUser = validate ? accountService.add(user) : Optional.empty();
        String result = "index";
        if (regUser.isEmpty()) {
            model.addAttribute(validate ? "error" : "empty", true);
            result = "registration";
        } else {
            HttpSession session = req.getSession();
            session.setAttribute("user", regUser.get());
            model.addAttribute("items", itemService.getAll());
            model.addAttribute("list", "all");
        }
        return result;
    }

    @GetMapping("/loginPage")
    public String loginPage(Model model,
                            @RequestParam(name = "fail", required = false)
                                    Boolean fail) {
        model.addAttribute("fail", fail != null);
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user,
                        HttpServletRequest req, Model model) {
        String result = "redirect:/index/2";
        Optional<User> userDb = accountService
                .findByLoginAndPassword(user.getLogin(), user.getPassword());
        if (userDb.isEmpty()) {
            result = "redirect:/loginPage?fail=true";
        } else {
            HttpSession session = req.getSession();
            session.setAttribute("user", userDb.get());
            model.addAttribute("items", itemService.getAll());
            model.addAttribute("list", "all");
        }
        return result;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/index/2";
    }

    private void addUserAttribute(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        model.addAttribute("user", user);
    }
}
