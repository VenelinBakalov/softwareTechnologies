package softuniBlog.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuniBlog.bindingModel.PositionBindingModel;
import softuniBlog.entity.Position;
import softuniBlog.entity.User;
import softuniBlog.repository.PositionRepository;
import softuniBlog.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/positions")
public class PositionController {

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String list(Model model){

        List<Position> positions = this.positionRepository.findAll();

        positions = positions.stream()
                .sorted(Comparator.comparingInt(Position::getId))
                .collect(Collectors.toList());

        model.addAttribute("positions", positions);
        model.addAttribute("view", "admin/position/list");

        return "base-layout";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("view", "admin/position/create");

        return "base-layout";
    }

    @PostMapping("/create")
    public String createProcess(PositionBindingModel positionBindingModel, RedirectAttributes redirectAttributes) {
        if (StringUtils.isEmpty(positionBindingModel.getName())) {
            redirectAttributes.addFlashAttribute("error", "The name of the position cannot be empty!");
            return "redirect:/admin/positions/create";
        }

        Position position = new Position(positionBindingModel.getName());

        this.positionRepository.saveAndFlush(position);

        return "redirect:/admin/positions/";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
        if (!this.positionRepository.exists(id)) {
            redirectAttributes.addFlashAttribute("error", "Such position doesn't exist!");
            return "redirect:/admin/positions/";
        }

        Position position = this.positionRepository.findOne(id);

        model.addAttribute("position", position);
        model.addAttribute("view", "admin/position/edit");

        return "base-layout";
    }

    @PostMapping("/edit/{id}")
    public String editProcess(@PathVariable Integer id, PositionBindingModel positionBindingModel, RedirectAttributes redirectAttributes) {
        if (!this.positionRepository.exists(id)) {
            redirectAttributes.addFlashAttribute("error", "Such position doesn't exist!");
            return "redirect:/admin/positions/";
        }

        Position position = this.positionRepository.findOne(id);

        if (position.getName().equals("Guest") || position.getName().equals("Cameraman")){
            redirectAttributes.addFlashAttribute("error", "Cannot edit this position!");
            return "redirect:/admin/positions/";
        }

        if (StringUtils.isEmpty(positionBindingModel.getName())) {
            redirectAttributes.addFlashAttribute("error", "The name of the position cannot be empty!");
            return "redirect:/admin/positions/edit/" + id;
        }

        position.setName(positionBindingModel.getName());

        this.positionRepository.saveAndFlush(position);

        return "redirect:/admin/positions/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        if (!this.positionRepository.exists(id)){
            redirectAttributes.addFlashAttribute("error", "Such position doesn't exist!");
            return "redirect:/admin/positions/";
        }

        Position position = this.positionRepository.findOne(id);

        model.addAttribute("position", position);
        model.addAttribute("view", "admin/position/delete");

        return "base-layout";
    }

    @PostMapping("/delete/{id}")
    public String deleteProcess(@PathVariable Integer id, RedirectAttributes redirectAttributes){
        if (!this.positionRepository.exists(id)){
            redirectAttributes.addFlashAttribute("error", "Such position doesn't exist!");
            return "redirect:/admin/positions/";
        }

        Position position = this.positionRepository.findOne(id);

        if (position.getName().equals("Guest") || position.getName().equals("Cameraman")){
            redirectAttributes.addFlashAttribute("error", "Cannot delete this position!");
            return "redirect:/admin/positions/";
        }

        List<User> users = this.userRepository.findAll().stream()
                .filter(u -> u.getPosition().getId().equals(id))
                .collect(Collectors.toList());

        for (User user : users) {
            user.setPosition(this.positionRepository.findByName("Guest"));
            this.userRepository.saveAndFlush(user);
        }

        this.positionRepository.delete(position);

        return "redirect:/admin/positions/";
    }
}
