package vn.dkt.laptopshop.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import vn.dkt.laptopshop.domain.Product;
import vn.dkt.laptopshop.service.ProductService;
import vn.dkt.laptopshop.service.UploadService;

@Controller
public class ProductController {
    private final ProductService productService;
    private final UploadService uploadService;

    public ProductController(ProductService productService, UploadService uploadService) {
        this.productService = productService;
        this.uploadService = uploadService;
    }

    @GetMapping("/admin/product")
    public String getProduct(Model model, @RequestParam("page") Optional<String> pageOptional) {
        int page = 1;
        try {
            if (pageOptional.isPresent()) {
                page = Integer.parseInt(pageOptional.get());
            }
        } catch (Exception e) {

        }
        final int pageSize = 4;
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Product> productsPage = this.productService.getAllProducts(pageable);
        List<Product> products = productsPage.getContent();
        model.addAttribute("products", products);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productsPage.getTotalPages());

        return "admin/product/show";
    }

    @GetMapping("admin/product/create")
    public String getCreateProduct(Model model) {
        model.addAttribute("newProduct", new Product());

        return "admin/product/create";
    }

    @PostMapping("admin/product/create")
    public String getPostProduct(@ModelAttribute("newProduct") @Valid Product newProduct, BindingResult bindingResult,
            @RequestParam("varFile") MultipartFile file) {

        if (bindingResult.hasErrors()) {
            return "admin/product/create";
        }
        String avatar = this.uploadService.handleSaveUploadFile(file, "product");
        newProduct.setImage(avatar);

        this.productService.handleSaveProduct(newProduct);
        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/{id}")
    public String getUserDetailPage(Model model, @PathVariable long id) {
        Product product = this.productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("id", id);
        return "admin/product/detail";
    }

    @GetMapping("/admin/product/delete/{id}")
    public String getDeleteProductPage(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        // User user = new User();
        // user.setId(id);
        model.addAttribute("newProduct", new Product());
        return "admin/product/delete";
    }

    @PostMapping("/admin/product/delete")
    public String postDeleteProduct(Model model, @ModelAttribute("newProduct") Product eric) {
        this.productService.handleDeleteProduct(eric.getId());
        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/update/{id}") // GET
    public String getUpdateProductPage(Model model, @PathVariable long id) {
        Product currentProduct = this.productService.getProductById(id);
        model.addAttribute("newProduct", currentProduct);
        return "admin/product/update";
    }

    @PostMapping("/admin/product/update")
    public String postUpdateProduct(@ModelAttribute("newProduct") @Valid Product var,
            BindingResult newProductBindingResult, @RequestParam("varFile") MultipartFile file) {

        if (newProductBindingResult.hasErrors()) {
            return "admin/product/update";
        }
        Product currentProduct = this.productService.getProductById(var.getId());

        if (currentProduct != null) {
            if (!file.isEmpty()) {
                String avatar = this.uploadService.handleSaveUploadFile(file, "product");
                currentProduct.setImage(avatar);
            }
            currentProduct.setDetailDesc(var.getDetailDesc());
            currentProduct.setFactory(var.getFactory());
            currentProduct.setName(var.getName());
            currentProduct.setQuantity(var.getQuantity());
            currentProduct.setSold(var.getSold());
            currentProduct.setTarget(var.getTarget());
            currentProduct.setShortDesc(var.getShortDesc());
            currentProduct.setPrice(var.getPrice());
            // bug here
            this.productService.handleSaveProduct(currentProduct);
        }
        return "redirect:/admin/product";
    }
}
