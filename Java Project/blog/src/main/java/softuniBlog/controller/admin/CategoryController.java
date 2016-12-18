package softuniBlog.controller.admin;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuniBlog.bindingModel.CategoryBindingModel;
import softuniBlog.entity.Article;
import softuniBlog.entity.Category;
import softuniBlog.repository.ArticleRepository;
import softuniBlog.repository.CategoryRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("view", "admin/category/list");

        List<Category> categories = this.categoryRepository.findAll();

        categories = categories.stream()
                .sorted(Comparator.comparingInt(Category::getId))
                .collect(Collectors.toList());

        model.addAttribute("categories", categories);

        return "base-layout";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("view", "admin/category/create");

        return "base-layout";
    }

    @PostMapping("/create")
    public String createProcess(CategoryBindingModel categoryBindingModel, RedirectAttributes redirectAttributes) {

        if(StringUtils.isEmpty(categoryBindingModel.getName())) {
            redirectAttributes.addFlashAttribute("error", "The name of the category cannot be empty!");
            return "redirect:/admin/categories/create";
        }

        Category category = new Category(categoryBindingModel.getName());

        this.categoryRepository.saveAndFlush(category);

        return "redirect:/admin/categories/";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable Integer id, RedirectAttributes redirectAttributes) {

        if(!this.categoryRepository.exists(id)) {
            redirectAttributes.addFlashAttribute("error", "Such category doesn't exist!");
            return "redirect:/admin/categories/";
        }

        model.addAttribute("view", "admin/category/edit");

        Category category = this.categoryRepository.findOne(id);

        model.addAttribute("category", category);

        return "base-layout";
    }

    @PostMapping("/edit/{id}")
    public String editProcess(CategoryBindingModel categoryBindingModel, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
        if(!this.categoryRepository.exists(id)) {
            redirectAttributes.addFlashAttribute("error", "Such category doesn't exist!");
            return "redirect:/admin/categories/";
        }

        if(StringUtils.isEmpty(categoryBindingModel.getName())) {
            redirectAttributes.addFlashAttribute("error", "The name of the category cannot be empty!");
            return "redirect:/admin/categories/edit/" + id;
        }

        Category category = this.categoryRepository.findOne(id);

        category.setName(categoryBindingModel.getName());

        this.categoryRepository.saveAndFlush(category);

        return "redirect:/admin/categories/";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
        if (!this.categoryRepository.exists(id)) {
            redirectAttributes.addFlashAttribute("error", "Such category doesn't exist!");
            return "redirect:/admin/categories/";
        }

        Category category = this.categoryRepository.findOne(id);

        model.addAttribute("category", category);
        model.addAttribute("view", "admin/category/delete");

        return "base-layout";
    }

    @PostMapping("/delete/{id}")
    public String deleteProcess(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        if (!this.categoryRepository.exists(id)) {
            redirectAttributes.addFlashAttribute("error", "Such category doesn't exist!");
            return "redirect:/admin/categories/";
        }

        Category category = this.categoryRepository.findOne(id);

        for (Article article : category.getArticles()) {
            this.articleRepository.delete(article);
        }

        this.categoryRepository.delete(category);

        return "redirect:/admin/categories/";
    }
}
