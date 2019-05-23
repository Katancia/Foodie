package pl.karol.foodieapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.karol.foodieapp.item.Item;
import pl.karol.foodieapp.item.ItemRepository;

import java.util.List;

@Controller
public class HomeController {

    private ItemRepository itemRepository;

    @Autowired
    public HomeController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "home";
    }
}
