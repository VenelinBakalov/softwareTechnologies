package softuniBlog.controller;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import softuniBlog.bindingModel.FileBindingModel;
import softuniBlog.bindingModel.PasswordEditBindingModel;
import softuniBlog.bindingModel.UserBindingModel;
import softuniBlog.entity.*;
import softuniBlog.repository.PositionRepository;
import softuniBlog.repository.RoleRepository;
import softuniBlog.repository.UserRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class UserController {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PositionRepository positionRepository;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("view", "user/register");

        return "base-layout";
    }

    @PostMapping("/register")
    public String registerProcess(UserBindingModel userBindingModel) {

        if (!userBindingModel.getPassword().equals(userBindingModel.getConfirmPassword())) {
            return "redirect:/register";
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        User user = new User(
                userBindingModel.getEmail(),
                userBindingModel.getFullName(),
                bCryptPasswordEncoder.encode(userBindingModel.getPassword())
        );

        Role userRole = this.roleRepository.findByName("ROLE_USER");

        user.addRole(userRole);

        Position userPosition = this.positionRepository.findByName("Guest");

        user.setPosition(userPosition);

        this.userRepository.saveAndFlush(user);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("view", "user/login");

        return "base-layout";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return "redirect:/login?logout";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profilePage(Model model) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        User user = this.userRepository.findByEmail(principal.getUsername());

        model.addAttribute("user", user);
        model.addAttribute("view", "user/profile");

        return "base-layout";
    }

    @GetMapping("/{id}/articles")
    @PreAuthorize("isAuthenticated()")
    public String listUserArticles(Model model, @PathVariable Integer id) {
        if (!this.userRepository.exists(id)) {
            return "redirect:/profile";
        }

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        User loggedUser = this.userRepository.findByEmail(principal.getUsername());

        User user = this.userRepository.findOne(id);

        if (!loggedUser.equals(user)) {
            return "redirect:/login";
        }

        List<Article> articles = (List<Article>) loggedUser.getArticles().stream()
                .sorted(Comparator.comparingInt(Article::getId))
                .collect(Collectors.toList());

        model.addAttribute("articles", articles);
        model.addAttribute("view", "user/list-articles");

        return "base-layout";
    }

    @GetMapping("/{id}/videos")
    @PreAuthorize("isAuthenticated()")
    public String listUserVideos(Model model, @PathVariable Integer id) {
        if (!this.userRepository.exists(id)) {
            return "redirect:/profile";
        }

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User loggedUser = this.userRepository.findByEmail(principal.getUsername());

        User user = this.userRepository.findOne(id);

        if (!loggedUser.equals(user)) {
            return "redirect:/login";
        }

        List<Video> videos = (List<Video>) loggedUser.getAuthorVideos().stream()
                .sorted(Comparator.comparingInt(Video::getId))
                .collect(Collectors.toList());

        model.addAttribute("videos", videos);
        model.addAttribute("view", "user/list-videos");

        return "base-layout";
    }

    @GetMapping("/{id}/edit-password")
    @PreAuthorize("isAuthenticated()")
    public String editPassword(Model model, @PathVariable Integer id) {
        if (!this.userRepository.exists(id)) {
            return "redirect:/profile";
        }

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User loggedUser = this.userRepository.findByEmail(principal.getUsername());

        User user = this.userRepository.findOne(id);

        if (!loggedUser.equals(user)) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("view", "user/edit-password");

        return "base-layout";
    }

    @PostMapping("/{id}/edit-password")
    @PreAuthorize("isAuthenticated()")
    public String editPasswordProcess(@PathVariable Integer id, PasswordEditBindingModel passwordEditBindingModel) {
        if (!this.userRepository.exists(id)) {
            return "redirect:/profile";
        }

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User loggedUser = this.userRepository.findByEmail(principal.getUsername());

        User user = this.userRepository.findOne(id);

        if (!loggedUser.equals(user)) {
            return "redirect:/login";
        }


        if (!StringUtils.isEmpty(passwordEditBindingModel.getPassword())
                && !StringUtils.isEmpty(passwordEditBindingModel.getConfirmPassword())) {

            if (passwordEditBindingModel.getPassword().equals(passwordEditBindingModel.getConfirmPassword())) {

                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

                user.setPassword(bCryptPasswordEncoder.encode(passwordEditBindingModel.getPassword()));
            }
        }

        this.userRepository.saveAndFlush(user);

        return "redirect:/profile";
    }

    @GetMapping("/{id}/profile-picture/upload")
    @PreAuthorize("isAuthenticated()")
    public String picture(Model model, @PathVariable Integer id) {
        if (!this.userRepository.exists(id)) {
            return "redirect:/profile";
        }

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User loggedUser = this.userRepository.findByEmail(principal.getUsername());

        User user = this.userRepository.findOne(id);

        if (!loggedUser.equals(user)) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("view", "user/picture/upload");

        return "base-layout";
    }

    @PostMapping("/{id}/profile-picture/upload")
    @PreAuthorize("isAuthenticated()")
    public String uploadPicture(@PathVariable Integer id, FileBindingModel fileBindingModel) {
        if (!this.userRepository.exists(id)) {
            return "redirect:/profile";
        }

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User loggedUser = this.userRepository.findByEmail(principal.getUsername());

        User user = this.userRepository.findOne(id);

        if (!loggedUser.equals(user)) {
            return "redirect:/login";
        }

        MultipartFile file = fileBindingModel.getPicture();

        if (file != null) {

            String originalFileName = file.getOriginalFilename();

            File imageFile = new File
                    ("D:\\javaBroadcast\\newsWebsiteProject\\blog\\src\\main\\resources\\static\\images\\", originalFileName);

            try {
                file.transferTo(imageFile);
                user.setImagePath(imageFile.getPath());

                this.userRepository.saveAndFlush(user);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "redirect:/profile";
    }
}

