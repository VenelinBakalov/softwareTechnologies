package softuniBlog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import softuniBlog.bindingModel.VideoBindingModel;
import softuniBlog.entity.Article;
import softuniBlog.entity.User;
import softuniBlog.entity.Video;
import softuniBlog.repository.ArticleRepository;
import softuniBlog.repository.UserRepository;
import softuniBlog.repository.VideoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class VideoController {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/video/create")
    @PreAuthorize("isAuthenticated()")
    public String create(Model model) {

        List<Article> articles = this.articleRepository.findAll();

        List<User> users = this.userRepository.findAll().stream()
                .filter(user -> user.getPosition().getName().equals("Cameraman"))
                .collect(Collectors.toList());

        model.addAttribute("users", users);
        model.addAttribute("articles", articles);
        model.addAttribute("view", "video/create");

        return "base-layout";
    }

    @PostMapping("/video/create")
    @PreAuthorize("isAuthenticated()")
    public String createProcess(VideoBindingModel videoBindingModel) {

        if (StringUtils.isEmpty(videoBindingModel.getTitle()) || StringUtils.isEmpty(videoBindingModel.getFullUrl())){
            return "redirect:/video/create";
        }

        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        String url = getUrlForEmbeddedVideo(videoBindingModel.getFullUrl());

        User author = this.userRepository.findByEmail(user.getUsername());
        User cameraman = this.userRepository.findOne(videoBindingModel.getCameramanId());
        Article article = this.articleRepository.findOne(videoBindingModel.getArticleId());

        Video videoEntity = new Video(
                videoBindingModel.getTitle(),
                url,
                author,
                cameraman,
                article
        );

        this.videoRepository.saveAndFlush(videoEntity);
        return "redirect:/";
    }

    @GetMapping("/video/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String Edit(Model model, @PathVariable Integer id) {
        if (!this.videoRepository.exists(id)) {
            return "redirect:/";
        }

        Video video = this.videoRepository.findOne(id);

        if (!isUserAuthorOrAdmin(video)) {
            return "redirect:/profile";
        }

        List<Article> articles = this.articleRepository.findAll();

        List<User> users = this.userRepository.findAll().stream()
                .filter(user -> user.getPosition().getName().equals("Cameraman"))
                .collect(Collectors.toList());

        model.addAttribute("video", video);
        model.addAttribute("articles", articles);
        model.addAttribute("users", users);
        model.addAttribute("view", "video/edit");

        return "base-layout";
    }

    @PostMapping("/video/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editProcess(VideoBindingModel videoBindingModel, @PathVariable Integer id) {
        if (!this.videoRepository.exists(id)){
            return "redirect:/";
        }

        Video video = this.videoRepository.findOne(id);

        if (!isUserAuthorOrAdmin(video)) {
            return "redirect:/profile";
        }

        String url = getUrlForEmbeddedVideo(videoBindingModel.getFullUrl());
        Article article = this.articleRepository.findOne(videoBindingModel.getArticleId());
        User cameraman = this.userRepository.findOne(videoBindingModel.getCameramanId());

        video.setTitle(videoBindingModel.getTitle());
        video.setUrl(url);
        video.setCameraman(cameraman);
        video.setArticle(article);

        this.videoRepository.saveAndFlush(video);

        return "redirect:/profile";
    }

    @GetMapping("/video/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(Model model, @PathVariable Integer id) {
        if (!this.videoRepository.exists(id)){
            return "redirect:/";
        }

        Video video = this.videoRepository.findOne(id);

        if (!isUserAuthorOrAdmin(video)) {
            return "redirect:/profile";
        }

        List<Article> articles = this.articleRepository.findAll();

        List<User> users = this.userRepository.findAll().stream()
                .filter(user -> user.getPosition().getName().equals("Cameraman"))
                .collect(Collectors.toList());

        model.addAttribute("video", video);
        model.addAttribute("articles", articles);
        model.addAttribute("users", users);
        model.addAttribute("view", "video/delete");

        return "base-layout";
    }

    @PostMapping("/video/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteProcess(@PathVariable Integer id){
        if (!this.videoRepository.exists(id)){
            return "redirect:/";
        }

        Video video = this.videoRepository.findOne(id);

        if (!isUserAuthorOrAdmin(video)) {
            return "redirect:/profile";
        }

        this.videoRepository.delete(video);

        return "redirect:/profile";
    }

    private boolean isUserAuthorOrAdmin(Video video) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User user = this.userRepository.findByEmail(principal.getUsername());

        return user.isAdmin() || user.isAuthor(video);
    }

    private String getUrlForEmbeddedVideo(String fullUrl) {

        String standartUrlEnd = "watch?v=";
        Integer urlBeginIndex = fullUrl.indexOf(standartUrlEnd) + 8;
        String url = fullUrl.substring(urlBeginIndex);

        return url;
    }
}
