package softuniBlog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

    private String getUrlForEmbeddedVideo(String fullUrl) {

        String standartUrlEnd = "watch?v=";
        Integer urlBeginIndex = fullUrl.indexOf(standartUrlEnd) + 8;
        String url = fullUrl.substring(urlBeginIndex);

        return url;
    }
}
