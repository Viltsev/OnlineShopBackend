package com.online_shop.onlineShop.Cart.services;

import com.online_shop.onlineShop.Products.model.Product;
import com.online_shop.onlineShop.Products.repo.ProductRepository;
import com.online_shop.onlineShop.User.repo.UserRepository;
import com.online_shop.onlineShop.User.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.online_shop.onlineShop.User.model.User;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImplementation implements CartService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public String saveToCart(Long id, String email) {
        Optional<Product> productOptional = getProductById(id);
        Optional<User> userOptional = userService.getByEmail(email);

        if (productOptional.isPresent() && userOptional.isPresent()) {
            Product product = productOptional.get();
            User user = userOptional.get();

            List<Product> cartList = user.getUserCart();
            cartList.add(product);
            user.setUserCart(cartList);

            userRepository.save(user);
            return "Product has been saved!";
        } else {
            return "There is not a such product or user!";
        }
    }

    @Override
    public List<Product> getCartItems(String email) {
        Optional<User> userOptional = userService.getByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getUserCart();
        } else {
            return null;
        }
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
}
