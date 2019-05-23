package pl.karol.foodieapp.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Optional;

@Controller
public class ItemController {

    private ItemRepository itemRepository;

    @Autowired
    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping("/dish/{name}")
    public String dish(@PathVariable String name, Model model) {
        Optional<Item> item = itemRepository.findByNameIgnoreCase(name.replaceAll("-", " "));
        item.ifPresent(it -> model.addAttribute("item", it));
        return item.map(it -> "item").orElse("redirect:/");
    }
}
