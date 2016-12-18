package softuniBlog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuniBlog.bindingModel.SearchBindingModel;
import softuniBlog.entity.*;
import softuniBlog.repository.*;

import javax.persistence.Transient;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
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
    @Autowired
    private TagRepository tagRepository;

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

        List<Category> categories = this.categoryRepository.findAll();
        List<Tag> tags = this.tagRepository.findAll().stream()
                .sorted((a, b) -> Integer.valueOf(b.getArticles().size()).compareTo(a.getArticles().size()))
                .limit(5)
                .collect(Collectors.toList());

        model.addAttribute("tags", tags);
        model.addAttribute("categories", categories);
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

        model.addAttribute("categories", categories);
        model.addAttribute("view", "home/news");

        return "base-layout";
    }

    @GetMapping("/search")
    public String search(Model model) {

        List<String> searchTypes = new ArrayList<>();

        searchTypes.add("Title");
        searchTypes.add("Content");
        searchTypes.add("Tags");
        searchTypes.add("Author");

        model.addAttribute("searchTypes", searchTypes);
        model.addAttribute("view", "home/search");

        return "base-layout";
    }

    @PostMapping("/search")
    @GetMapping("/search-results")
    public String searchProcess(SearchBindingModel searchBindingModel, Model model, RedirectAttributes redirectAttributes) {

        if (searchBindingModel.getSearchTypes() == null){
            redirectAttributes.addFlashAttribute("error", "At least 1 search type must be specified.");
            return "redirect:/search";
        }

        String searchText = searchBindingModel.getSearchText();
        List<String> searchTypes = searchBindingModel.getSearchTypes();

        List<Article> articles = GetSearchResults(searchText, searchTypes);

        model.addAttribute("articles", articles);
        model.addAttribute("view", "home/search-results");

        return "base-layout";
    }

    @Transient
    private List<Article> GetSearchResults(String searchText, List<String> searchTypes) {

        List<Article> articleResults = new ArrayList<>();
        List<Article> articles = this.articleRepository.findAll();

        for (String type : searchTypes) {

            if (type.equals("Title")) {
                for (Article article : articles) {

                    Integer index = article.getTitle().indexOf(searchText);

                    if (!index.equals(-1)){
                        articleResults.add(article);
                    }
                }
            }

            if (type.equals("Content")) {
                for (Article article : articles) {

                    Integer index = article.getContent().indexOf(searchText);

                    if (!index.equals(-1)){
                        articleResults.add(article);
                    }
                }
            }

            if (type.equals("Tags")) {
                List<Tag> tags = this.tagRepository.findAll();

                for (Tag tag: tags) {

                    if (tag.getName().equals(searchText)) {
                        articleResults.addAll(tag.getArticles());
                    }
                }
            }

            if (type.equals("Author")) {
                List<User> users = this.userRepository.findAll();

                for (User user : users) {

                    if (user.getFullName().equals(searchText)){
                        articleResults.addAll(user.getArticles());
                    }
                }
            }
        }

        Set<Article> distinctArticles = new HashSet<>();
        distinctArticles.addAll(articleResults);

        articleResults.clear();
        articleResults.addAll(distinctArticles);

        return articleResults;
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
