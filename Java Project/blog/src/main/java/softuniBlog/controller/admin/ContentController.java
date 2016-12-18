package softuniBlog.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import softuniBlog.entity.Article;
import softuniBlog.entity.Video;
import softuniBlog.repository.ArticleRepository;
import softuniBlog.repository.VideoRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/content")
public class ContentController {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private VideoRepository videoRepository;

    @GetMapping("/articles")
    public String listArticles(Model model) {

        List<Article> articles = this.articleRepository.findAll();

        articles = articles.stream()
                .sorted(Comparator.comparingInt(Article::getId))
                .collect(Collectors.toList());

        model.addAttribute("articles", articles);
        model.addAttribute("view", "admin/content/articles");

        return "base-layout";
    }

    @GetMapping("/videos")
    public String listVideos(Model model) {

        List<Video> videos = this.videoRepository.findAll();

        videos = videos.stream()
                .sorted(Comparator.comparingInt(Video::getId))
                .collect(Collectors.toList());

        model.addAttribute("videos", videos);
        model.addAttribute("view", "admin/content/videos");

        return "base-layout";
    }
}
