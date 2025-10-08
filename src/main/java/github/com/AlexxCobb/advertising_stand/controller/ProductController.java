package github.com.AlexxCobb.advertising_stand.controller;

import github.com.AlexxCobb.advertising_stand.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/stands")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/get")
    public String getProductDto(Model model) {
        var topProducts = productService.getPopularProducts();
        var discountProducts = productService.getDiscountProducts();
        var newProducts = productService.getNewProducts();
        model.addAttribute("topProducts", topProducts);
        model.addAttribute("discountProducts", discountProducts);
        model.addAttribute("newProducts", newProducts);
        return "products-for-stand";
    }
}