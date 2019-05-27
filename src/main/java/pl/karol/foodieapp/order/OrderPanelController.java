package pl.karol.foodieapp.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.karol.foodieapp.item.Item;

import java.util.List;
import java.util.Optional;

@Controller
public class OrderPanelController {

    private OrderRepository orderRepository;

    @Autowired
    public OrderPanelController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/panel/orders")
    public String getOrders(@RequestParam(required = false) OrderStatus status, Model model) {
        List<Order> orders;
        if(status == null)
            orders = orderRepository.findAll();
        else
            orders = orderRepository.findAllByStatus(status);
        model.addAttribute("orders", orders);
        return "panel/orders";
    }

    @GetMapping("/panel/order/{id}")
    public String getOrder(@PathVariable Long id, Model model) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(ord -> singleOrderPanel(ord, model))
                .orElse("redirect:/");
    }

    @PostMapping("/panel/order/{id}")
    public String changeOrderStatus(@PathVariable Long id, Model model) {
        Optional<Order> order = orderRepository.findById(id);
        order.ifPresent(ord -> {
            ord.setStatus(OrderStatus.nextStatus(ord.getStatus()));
            orderRepository.save(ord);
        });
        return order.map(ord -> singleOrderPanel(ord, model))
                .orElse("redirect:/");
    }

    private String singleOrderPanel(Order order, Model model) {
        model.addAttribute("order", order);
        model.addAttribute("sum", order.getItems().stream().mapToDouble(Item::getPrice).sum());
        return "panel/order";
    }
}
