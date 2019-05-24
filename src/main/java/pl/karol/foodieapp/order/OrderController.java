package pl.karol.foodieapp.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.karol.foodieapp.common.Message;
import pl.karol.foodieapp.item.Item;
import pl.karol.foodieapp.item.ItemRepository;

import java.util.Optional;

@Controller
public class OrderController {

    private ClientOrder clientOrder;
    private ItemRepository itemRepository;
    private OrderRepository orderRepository;

    @Autowired
    public OrderController(ClientOrder clientOrder, ItemRepository itemRepository, OrderRepository orderRepository) {
        this.clientOrder = clientOrder;
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/order/add")
    public String addItemToOrder(@RequestParam Long itemId, Model model) {
        Optional<Item> item = itemRepository.findById(itemId);
        item.ifPresent(clientOrder::add);
        if(item.isPresent()) {
            model.addAttribute("message", new Message("Added", "Added to order: " + item.get().getName()));
        } else {
            model.addAttribute("message", new Message("Not added", "Loaded wrong id from menu: " + itemId));
        }
        return "message";
    }

    @GetMapping("/order")
    public String getCurrentOrder(Model model) {
        model.addAttribute("order", clientOrder.getOrder());
        model.addAttribute("sum", clientOrder
                .getOrder()
                .getItems().stream()
                .mapToDouble(Item::getPrice)
                .sum());
        return "order";
    }

    @PostMapping("/order/realize")
    public String realizeOrder(@RequestParam String address, @RequestParam String telephone, Model model) {
        Order order = clientOrder.getOrder();
        order.setAddress(address);
        order.setTelephone(telephone);
        orderRepository.save(order);
        clientOrder.clear();
        model.addAttribute("message", new Message("Thank You", "We are proceeding your order"));
        return "message";
    }
}
