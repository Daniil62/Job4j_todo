package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.service.ItemService;

import java.util.Optional;

@Controller
public class ItemController {

    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @GetMapping("/index/{status}")
    public String getItems(@PathVariable("status") int status, Model model) {
        switch (status) {
            case 0 :
                model.addAttribute("items", service.getUnperformed());
                model.addAttribute("list", "new");
                break;
            case 1 :
                model.addAttribute("items", service.getPerformed());
                model.addAttribute("list", "old");
                break;
            default:
                loadAllItems(model);
        }
        return "index";
    }

    @GetMapping("/addItemForm")
    public String addItem(Model model) {
        model.addAttribute("item", new Item());
        return "addItemForm";
    }

    @PostMapping("/createItem")
    public String createItem(@ModelAttribute Item item, Model model) {
        service.create(item);
        model.addAttribute("items", service.getUnperformed());
        model.addAttribute("list", "new");
        return "index";
    }

    @GetMapping("/itemDetails/{itemId}")
    public String itemDetails(Model model, @PathVariable("itemId") long id) {
        String result = "index";
        Optional<Object> optionalItem = service.findById(id);
        if (optionalItem.isPresent()) {
            model.addAttribute("item", optionalItem.get());
            result = "itemDetails";
        }
        return result;
    }

    @GetMapping("/editItemForm/{itemId}/{previous}")
    public String editItem(Model model,
                           @PathVariable("previous") String previous,
                           @PathVariable("itemId") long id) {
        String result = "editItemForm";
        Optional<Object> optionalItem = service.findById(id);
        if (optionalItem.isPresent()) {
            model.addAttribute("item", optionalItem.get());
            model.addAttribute("previous", previous);
        } else {
            loadAllItems(model);
            result = "index";
        }
        return result;
    }

    @PostMapping("/updateItem/{previous}")
    public String updateItem(@ModelAttribute Item item,
                             @PathVariable("previous") String previous,
                             Model model) {
        String result;
        service.update(item);
        if ("details".equals(previous)) {
            model.addAttribute("item", service
                    .findById(item.getId()).orElse(new Item()));
            result = "itemDetails";
        } else {
            loadAllItems(model);
            result = "index";
        }
        return result;
    }

    @PostMapping("/changeStatus/{itemId}/{itemStatus}")
    public String changeStatus(Model model,
                               @PathVariable("itemId") long id,
                               @PathVariable("itemStatus") String status) {
        service.changeStatus(id, "false".equals(status));
        model.addAttribute("item", service
                .findById(id).orElse(new Item()));
        return "itemDetails";
    }

    @PostMapping("/deleteItem/{itemId}")
    public String deleteItem(Model model, @PathVariable("itemId") long id) {
        service.delete(id);
        loadAllItems(model);
        return "index";
    }

    private void loadAllItems(Model model) {
        model.addAttribute("items", service.getAll());
        model.addAttribute("list", "all");
    }
}