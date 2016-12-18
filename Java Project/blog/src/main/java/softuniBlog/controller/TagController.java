package softuniBlog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuniBlog.entity.Tag;
import softuniBlog.repository.TagRepository;

@Controller
public class TagController {

    @Autowired
    private TagRepository tagRepository;

    @GetMapping("/tag/{name}")
    public String articlesWithTag(Model model, @PathVariable String name, RedirectAttributes redirectAttributes) {

        Tag tag = this.tagRepository.findByName(name);

        if (tag == null) {
            redirectAttributes.addFlashAttribute("error", "Such tag doesn't exist!");
            return "redirect:/";
        }

        model.addAttribute("view", "tag/articles");
        model.addAttribute("tag", tag);

        return "base-layout";
    }
}
