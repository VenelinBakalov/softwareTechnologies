package softuniBlog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import softuniBlog.entity.*;
import softuniBlog.repository.ArticleRepository;
import softuniBlog.repository.CategoryRepository;
import softuniBlog.repository.PositionRepository;
import softuniBlog.repository.UserRepository;

import javax.persistence.Transient;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PositionRepository positionRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String index(Model model) {

        List<Article> articles = this.articleRepository.findAll();
        List<Article> latestFiveArticles = findLatestFive(articles);
        Article latestArticle = latestFiveArticles.get(0);

        List<Comment> comments = latestArticle.getComments().stream()
                .sorted((a, b) -> b.getDateAdded().compareTo(a.getDateAdded()))
                .collect(Collectors.toList());

        if (!(SecurityContextHolder.getContext().getAuthentication()
                instanceof AnonymousAuthenticationToken)) {

            UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();

            User userEntity = this.userRepository.findByEmail(principal.getUsername());

            model.addAttribute("user", userEntity);
        }

        model.addAttribute("latestFiveArticles", latestFiveArticles);
        model.addAttribute("latestArticle", latestArticle);
        model.addAttribute("comments", comments);
        model.addAttribute("view", "home/index");

        return "base-layout";
    }

    @RequestMapping("/error/403")
    public String accessDenied(Model model) {
        model.addAttribute("view", "error/403");

        return "base-layout";
    }

    @GetMapping("/category/{id}")
    public String listArticles(Model model, @PathVariable Integer id) {
        if (!this.categoryRepository.exists(id)) {
            return "redirect:/";
        }

        Category category = this.categoryRepository.findOne(id);
        Set<Article> articles = category.getArticles();

        model.addAttribute("category", category);
        model.addAttribute("articles", articles);
        model.addAttribute("view", "home/list-articles");

        return "base-layout";
    }

    @GetMapping("/team")
    public String listTeam(Model model) {

        List<Position> positions = this.positionRepository.findAll().stream()
                .filter(position -> !position.getName().equals("Guest"))
                .filter(position -> !position.getUsers().isEmpty())
                .collect(Collectors.toList());

        model.addAttribute("positions", positions);
        model.addAttribute("view", "home/team");

        return "base-layout";
    }

    @GetMapping("/news")
    public String listAllCategories(Model model) {

        List<Category> categories = this.categoryRepository.findAll();

        model.addAttribute("view", "home/news");
        model.addAttribute("categories", categories);

        return "base-layout";
    }

    @Transient
    private List<Article> findLatestFive(List<Article> articles) {

        articles = articles.stream()
                .sorted((a, b) -> b.getDateAdded().compareTo(a.getDateAdded()))
                .limit(5)
                .collect(Collectors.toList());

        return articles;
    }
}
