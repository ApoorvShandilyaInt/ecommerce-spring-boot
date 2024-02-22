package UpIn.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import UpIn.dto.CartDto;
import UpIn.dto.CheckoutDto;
import UpIn.dto.DeliveryDto;
import UpIn.dto.OrderDto;
import UpIn.dto.OrderItemDto;
import UpIn.dto.UserDtoLogin;
import UpIn.entity.Cart;
import UpIn.entity.Checkout;
import UpIn.entity.Delivery;
import UpIn.entity.Order;
import UpIn.entity.OrderItem;
import UpIn.entity.Product;
import UpIn.entity.User;
import UpIn.service.CartService;
import UpIn.service.CheckoutService;
import UpIn.service.DeliveryService;
import UpIn.service.OrderService;
import UpIn.service.ProductService;
import UpIn.service.UserService;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@RequestMapping("/user")
@RestController
public class UserControllerr 
{

    private final UserService userService;
    private final ProductService productService;
    private final CartService cartService;
    private final CheckoutService checkoutService;
    private final DeliveryService deliveryService;
    private OrderService orderService;
    
    private final Map<String, User> loggedInUsers = new HashMap<>();

    // User Registration
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.createUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    // Retrieve User by ID
    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long userId) {
        User user = userService.getUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // Retrieve All Users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUser();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Update User
    @PutMapping("{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long userId, @RequestBody User user) {
        user.setId(userId);
        User updatedUser = userService.updateUser(user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    // Delete User
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>("User Successfully deleted!", HttpStatus.OK);
    }

    // User Login
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserDtoLogin userDtoLogin) {
        Optional<User> optionalUser = userService.authenticate(userDtoLogin.getEmail(), userDtoLogin.getPassword());
        if (optionalUser.isPresent()) {
            String sessionId = generateSessionId();
            loggedInUsers.put(sessionId, optionalUser.get());
            return ResponseEntity.ok(sessionId);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Email or Password");
        }
    }

    // Logout
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String sessionId) {
        loggedInUsers.remove(sessionId);
        return ResponseEntity.ok("Logged out successfully");
    }

    // Create Product
    @PostMapping("/product")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.createProduct(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    // Retrieve Product by ID
    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long productId) {
        Product product = productService.getProductById(productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    // Retrieve All Products
    @GetMapping("/product")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProduct();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // Update Product
    @PutMapping("/product/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long productId, @RequestBody Product product) {
        product.setId(productId);
        Product updatedProduct = productService.updateProduct(product);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    // Delete Product
    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>("Product successfully deleted!", HttpStatus.OK);
    }

    // Add to Cart
    @PostMapping("/cart/{productId}")
    public ResponseEntity<Cart> addToCart(@RequestHeader("Authorization") String sessionId, @PathVariable Long productId, @RequestBody CartDto cartDto) {
        User user = loggedInUsers.get(sessionId);
        if (user != null) {
            Cart cartItem = cartService.addToCart(user.getId(), productId, cartDto.getQuantity());
            return new ResponseEntity<>(cartItem, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    // Retrieve Cart
    @GetMapping("/cart/{userId}")
    public ResponseEntity<List<Cart>> getCartDetails(@PathVariable Long userId) {
        List<Cart> cartItems = cartService.getCartItems(userId);
        return ResponseEntity.ok(cartItems);
    } 

    // Update Cart
    @PutMapping("/cart/{cartId}")
    public ResponseEntity<Cart> updateCart(@RequestHeader("Authorization") String sessionId, @PathVariable Long cartId, @RequestBody CartDto cartDto) {
        User user = loggedInUsers.get(sessionId);
        if (user != null) {
            Cart updatedCartItem = cartService.updateCart(cartId, cartDto);
            return new ResponseEntity<>(updatedCartItem, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    // Delete Cart Item
    @DeleteMapping("/cart/{cartId}")
    public ResponseEntity<Void> deleteCartItem(@RequestHeader("Authorization") String sessionId, @PathVariable Long cartId) {
        User user = loggedInUsers.get(sessionId);
        if (user != null) {
            cartService.deleteCartItem(cartId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private String generateSessionId() {
        return UUID.randomUUID().toString();
    }

    // Add delivery address
    @PostMapping("/delivery/address")
    public ResponseEntity<Delivery> storeOrUpdateDeliveryAddress(@RequestHeader("Authorization") String sessionId, @RequestBody DeliveryDto deliveryDto)
    {
        User user = loggedInUsers.get(sessionId);
        if (user != null) {
            Delivery savedDelivery = deliveryService.saveOrUpdateDelivery(deliveryDto, user.getId());
            return new ResponseEntity<>(savedDelivery, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
   

    // Checkout endpoint

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutDto> checkout(@RequestHeader("Authorization") String sessionId, @RequestBody Map<String, String> requestBody) {
        User user = loggedInUsers.get(sessionId);
        if (user != null) {
            String checkoutConfirmation = requestBody.get("checkout");
            if ("true".equals(checkoutConfirmation)) {
                Delivery delivery = deliveryService.getDeliveryByUserId(user.getId());
                Checkout checkout = checkoutService.processCheckout(user.getId(), checkoutConfirmation);
                CheckoutDto checkoutDto = buildCheckoutDto(checkout, delivery);
                return ResponseEntity.ok(checkoutDto);
            } else {
                return ResponseEntity.badRequest().body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private CheckoutDto buildCheckoutDto(Checkout checkout, Delivery delivery) {
        CheckoutDto checkoutDto = new CheckoutDto();
        checkoutDto.setName(checkout.getUser().getFullName());
        checkoutDto.setMobileNumber(checkout.getUser().getMobileNumber());
        checkoutDto.setDateTime(LocalDateTime.now());
        checkoutDto.setBillId(checkout.getBillId());
        if (delivery != null)
        {
            checkoutDto.setAddress(delivery.getAddress());
        }
        List<CartDto> cartItemDtos = new ArrayList<>();
        int totalPrice = 0;
        for (Cart cart : checkout.getCartItems())
        {
            CartDto cartItemDto = new CartDto();
            cartItemDto.setProductName(cart.getProduct().getProductName());
            cartItemDto.setProductPrice(cart.getProduct().getPrice());
            cartItemDto.setQuantity(cart.getQuantity());
            cartItemDto.setTotalPrice(cart.getTotalPrice());
            cartItemDtos.add(cartItemDto);
            totalPrice += cart.getTotalPrice();
        }
        checkoutDto.setCartItems(cartItemDtos);
        checkoutDto.setTotalPrice(totalPrice);
        return checkoutDto;
    }

    
    @PostMapping("/order")
    public ResponseEntity<OrderDto> placeOrder(@RequestHeader("Authorization") String sessionId, @RequestBody Map<String, Object> requestBody) {
        User user = loggedInUsers.get(sessionId);
        if (user != null) {
            String billId = (String) requestBody.get("billId");
            int totalAmount = (int) requestBody.get("totalAmount");

            // Fetch the checkout details for the user
            Checkout checkout = checkoutService.getCheckoutByUserId(user.getId());

            if (checkout == null || !checkout.getBillId().equals(billId)) {
                // Invalid checkout details
                return ResponseEntity.badRequest().build();
            }

            List<Cart> cartItems = cartService.getCartItems(user.getId());

        // Calculate the total price of the items in the cart
        int totalCartPrice = calculateTotalCartPrice(cartItems);

        // Check if the entered total amount matches the total price of the cart
        if (totalCartPrice != totalAmount) {
            return ResponseEntity.badRequest().build();
        }
                Order order = orderService.placeOrder(user.getId(), billId, totalAmount);
                OrderDto orderDto = buildOrderDto(order);
                return ResponseEntity.ok(orderDto);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }

    private OrderDto buildOrderDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setMessage("Order Placed Successful");
        orderDto.setOrderNumber(order.getOrderNumber());
        orderDto.setName(order.getUser().getFullName());
        orderDto.setMobileNumber(order.getUser().getMobileNumber());
        Delivery delivery = deliveryService.getDeliveryByUserId(order.getUser().getId());
        if (delivery != null) {
            orderDto.setDeliveryAddress(delivery.getAddress());
        }
        // Populate order items
        List<OrderItemDto> orderItemDtos = convertOrderItemsToDto(order.getOrderItems());
        orderDto.setOrderItems(orderItemDtos);
        orderDto.setTotalAmountPaid(order.getTotalAmountPaid());
        return orderDto;
    }
    
    private List<OrderItemDto> convertOrderItemsToDto(List<OrderItem> orderItems) {
        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            OrderItemDto orderItemDto = new OrderItemDto();
            // Populate order item details
            orderItemDto.setProductName(orderItem.getProduct().getProductName());
            orderItemDto.setQuantity(orderItem.getQuantity());
            orderItemDto.setPrice(orderItem.getPrice());
            // Optionally calculate total price per item
            orderItemDto.setTotalPrice(orderItem.getQuantity() * orderItem.getPrice());
            orderItemDtos.add(orderItemDto);
        }
        return orderItemDtos;
    }

    public int calculateTotalCartPrice(List<Cart> cartItems) {
        int total = 0;
        for (Cart cartItem : cartItems) {
            total += cartItem.getTotalPrice(); 
        }
        return total;
    }
}

           



