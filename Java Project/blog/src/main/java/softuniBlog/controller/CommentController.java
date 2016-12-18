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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuniBlog.bindingModel.CommentBindingModel;
import softuniBlog.entity.Article;
import softuniBlog.entity.Comment;
import softuniBlog.entity.User;
import softuniBlog.repository.ArticleRepository;
import softuniBlog.repository.CommentRepository;
import softuniBlog.repository.UserRepository;

@Controller
public class CommentController {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @PostMapping("/article/{id}/add-comment")
    @PreAuthorize("isAuthenticated()")
    public String submitComment(@PathVariable Integer id, CommentBindingModel commentBindingModel, RedirectAttributes redirectAttributes) {
        if (!this.articleRepository.exists(id)) {
            return "redirect:/";
        }

        if (StringUtils.isEmpty(commentBindingModel.getContent())){
            redirectAttributes.addFlashAttribute("error", "The comment cannot be empty!");
            return "redirect:/article/" + id;
        }

        Article article = this.articleRepository.findOne(id);

        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User author = this.userRepository.findByEmail(user.getUsername());

        Comment comment = new Comment(
                commentBindingModel.getContent(),
                author,
                article
        );

        this.commentRepository.saveAndFlush(comment);

        return "redirect:/article/" + id;
    }

    @GetMapping("/comment/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String edit(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        if (!this.commentRepository.exists(id)) {
            redirectAttributes.addFlashAttribute("error", "Such comment doesn't exist!");
            return "redirect:/";
        }

        Comment comment = this.commentRepository.findOne(id);

        if (!isUserAuthorOrAdmin(comment)) {
            redirectAttributes.addFlashAttribute("error", "You are not authorized to edit this comment!");
            return "redirect:/article/" + comment.getArticle().getId();
        }

        model.addAttribute("comment", comment);
        model.addAttribute("view", "comment/edit");

        return "base-layout";
    }

    @PostMapping("/comment/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editProcess(@PathVariable Integer id, CommentBindingModel commentBindingModel, RedirectAttributes redirectAttributes) {
        if (!this.commentRepository.exists(id)) {
            redirectAttributes.addFlashAttribute("error", "Such comment doesn't exist!");
            return "redirect:/";
        }

        Comment comment = this.commentRepository.findOne(id);

        if (!isUserAuthorOrAdmin(comment)) {
            redirectAttributes.addFlashAttribute("error", "You are not authorized to edit this comment!");
            return "redirect:/article/" + comment.getArticle().getId();
        }

        if (StringUtils.isEmpty(commentBindingModel.getContent())){
            redirectAttributes.addFlashAttribute("error", "The comment cannot be empty!");
            return "redirect:/article/" + comment.getArticle().getId();
        }

        comment.setContent(commentBindingModel.getContent());

        this.commentRepository.saveAndFlush(comment);

        return "redirect:/article/" + comment.getArticle().getId();
    }

    @GetMapping("/comment/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        if (!this.commentRepository.exists(id)) {
            redirectAttributes.addFlashAttribute("error", "Such comment doesn't exist!");
            return "redirect:/";
        }

        Comment comment = this.commentRepository.findOne(id);

        if (!isUserAuthorOrAdmin(comment)) {
            redirectAttributes.addFlashAttribute("error", "You are not authorized to delete this comment!");
            return "redirect:/article/" + comment.getArticle().getId();
        }

        model.addAttribute("comment", comment);
        model.addAttribute("view", "comment/delete");

        return "base-layout";
    }

    @PostMapping("/comment/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteProcess(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        if (!this.commentRepository.exists(id)) {
            redirectAttributes.addFlashAttribute("error", "Such comment doesn't exist!");
            return "redirect:/";
        }

        Comment comment = this.commentRepository.findOne(id);

        if (!isUserAuthorOrAdmin(comment)) {
            redirectAttributes.addFlashAttribute("error", "You are not authorized to delete this comment!");
            return "redirect:/article/" + comment.getArticle().getId();
        }

        this.commentRepository.delete(id);

        return "redirect:/article/" + comment.getArticle().getId();
    }


    private boolean isUserAuthorOrAdmin(Comment comment) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User userEntity = this.userRepository.findByEmail(user.getUsername());

        return userEntity.isAdmin() || userEntity.isAuthor(comment);
    }
}
