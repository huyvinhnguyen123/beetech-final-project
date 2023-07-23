package com.beetech.finalproject.domain.service;

import com.beetech.finalproject.domain.entities.*;
import com.beetech.finalproject.domain.enums.Status;
import com.beetech.finalproject.domain.repository.CartDetailRepository;
import com.beetech.finalproject.domain.repository.CartRepository;
import com.beetech.finalproject.domain.repository.ProductRepository;
import com.beetech.finalproject.domain.repository.UserRepository;
import com.beetech.finalproject.utils.CustomGenerate;
import com.beetech.finalproject.web.dtos.cart.*;
import com.beetech.finalproject.web.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final JwtUtils jwtUtils;

    /**
     * Extract login id(email) from token when user authentication
     *
     * @param token - token from authentication
     * @return - user
     */
    private User extractUserFromToken(String token) {
        String loginId = jwtUtils.extractUsername(token);
        User user = userRepository.findByLoginId(loginId);
        log.info("Extract user from token success");
        return user;
    }


    /**
     * create new cart
     *
     * @return - cart
     */
    private Cart createCart() {
        Cart cart = new Cart();
        cart.setTotalPrice(0.0);
        cart.setVersionNo(1.0);
        cart.setToken(CustomGenerate.generateRandomString(20));
        cartRepository.save(cart);

        log.info("Save new cart success");
        return cart;
    }

    /**
     * Add new product to cart
     *
     * @param cartCreateDto - input cartCreateDto's properties
     * @return - cartRetrieveCreateDto
     */
    @Transactional
    public CartRetrieveCreateDto addProductToCart(CartCreateDto cartCreateDto, User user) {
        Cart cart = createCart();

        Product existingProduct = productRepository.findById(cartCreateDto.getProductId()).orElseThrow(
                ()->{
                    log.error("Not found this product");
                    return new NullPointerException("Not found this product: " + cartCreateDto.getProductId());
                }
        );
        log.info("Found product");

        User existingUser = user;

        cart.setUser(existingUser);
        cartRepository.save(cart);
        log.info("Update cart with user success");

        CartDetail cartDetail = new CartDetail();
        cartDetail.setCart(cart);
        cartDetail.setProduct(existingProduct);
        cartDetail.setQuantity(cartCreateDto.getQuantity());
        cartDetail.setPrice(existingProduct.getPrice());
        cartDetail.setTotalPrice(cartDetail.getPrice() * cartDetail.getQuantity());
        cartDetail.setStatusCode(Status.ACTIVE.getCode());
        cartDetailRepository.save(cartDetail);
        log.info("Save new cart detail success");

        cart.setTotalPrice(cartDetail.getTotalPrice());
        cart.setVersionNo(cart.getVersionNo() + 1);
        cartRepository.save(cart);
        log.info("save update cart success");

        CartRetrieveCreateDto cartRetrieveCreateDto = new CartRetrieveCreateDto();
        cartRetrieveCreateDto.setToken(cart.getToken());
        cartRetrieveCreateDto.setTotalPrice(cart.getTotalPrice());
        cartRetrieveCreateDto.setVersionNo(cart.getVersionNo());
        log.info("Add new product to cart success");

        return cartRetrieveCreateDto;
    }

    /**
     * Add new product to cart without login
     *
     * @param cartCreateDto - input cartCreateDto's properties
     * @return - cartRetrieveCreateDto
     */
    @Transactional
    public CartRetrieveCreateDto addProductToCartWithoutLogin(CartCreateDto cartCreateDto) {
        Cart cart = createCart();

        Product existingProduct = productRepository.findById(cartCreateDto.getProductId()).orElseThrow(
                ()->{
                    log.error("Not found this product");
                    return new NullPointerException("Not found this product: " + cartCreateDto.getProductId());
                }
        );
        log.info("Found product");

        log.info("Not authentication");
        Cart cartWithoutLogin = cartRepository.findByToken(cartCreateDto.getToken());
        log.info("Get new cart success");

        CartDetail cartDetail = new CartDetail();
        cartDetail.setCart(cartWithoutLogin);
        cartDetail.setProduct(existingProduct);
        cartDetail.setQuantity(cartCreateDto.getQuantity());
        cartDetail.setPrice(existingProduct.getPrice());
        cartDetail.setTotalPrice(cartDetail.getPrice() * cartDetail.getQuantity());
        cartDetail.setStatusCode(Status.ACTIVE.getCode());
        cartDetailRepository.save(cartDetail);
        log.info("Save new cart detail success");

        cartWithoutLogin.setTotalPrice(cartDetail.getTotalPrice());
        cartWithoutLogin.setVersionNo(cartWithoutLogin.getVersionNo() + 1);
        cartRepository.save(cartWithoutLogin);
        log.info("save update cart success");

        CartRetrieveCreateDto cartRetrieveCreateDto = new CartRetrieveCreateDto();
        cartRetrieveCreateDto.setToken(cart.getToken());
        cartRetrieveCreateDto.setTotalPrice(cart.getTotalPrice());
        cartRetrieveCreateDto.setVersionNo(cart.getVersionNo());
        log.info("Add new product to cart success");

        return cartRetrieveCreateDto;
    }

    /**
     * Sync cart after login
     *
     * @param token - input token
     * @return - cartRetrieveDto
     */
    @Transactional
    public CartRetrieveSyncDto syncCartAfterLogin(String token, User user) {
        CartRetrieveSyncDto cartRetrieveSyncDto = new CartRetrieveSyncDto();

        // Get old cart from user
        Cart oldCart = user.getCart();

        // Get current cart without login
        Cart cartWithoutLogin = cartRepository.findByToken(token);

        int totalQuantity = 0;

        // If old cart & current cart is null
        if(oldCart == null || cartWithoutLogin == null || oldCart == null && cartWithoutLogin == null) {
            log.info("cart is null");
        }

        // If old cart & current cart is not null
        if(oldCart != null && cartWithoutLogin != null) {
            for(CartDetail cartDetailWithoutLogin: cartWithoutLogin.getCartDetails()) {
                boolean IsMatch = false;
                for(CartDetail oldCartDetail: oldCart.getCartDetails()) {
                    if(cartDetailWithoutLogin.getProduct().getProductId().equals(oldCartDetail.getProduct().getProductId())) {
                        // Product id matches, update quantity
                        oldCartDetail.setQuantity(oldCartDetail.getQuantity() + cartDetailWithoutLogin.getQuantity());
                        oldCartDetail.setTotalPrice(oldCartDetail.getTotalPrice() + cartDetailWithoutLogin.getTotalPrice());
                        IsMatch = true;
                        log.info("Have same product - update quantity");

                        totalQuantity += oldCartDetail.getQuantity();
                        break;
                    }
                }
                if(!IsMatch) {
                    // Product id doesn't exist, create new cart detail
                    cartDetailWithoutLogin.setCart(oldCart);
                    oldCart.getCartDetails().add(cartDetailWithoutLogin);
                    log.info("Haven't same product - add new cartDetail inside old cart");

                    totalQuantity += cartDetailWithoutLogin.getQuantity();
                }
            }
            // Update old cart in the database
            cartRepository.save(oldCart);
            log.info("Save old cart success");
        }

        cartRetrieveSyncDto.setTotalQuantity(totalQuantity);

        // If current cart is exist and old cart is empty or null
        if (cartWithoutLogin != null && (oldCart == null || oldCart.getCartDetails().isEmpty())) {
            // User doesn't have an existing cart(old cart), update current cart
            cartWithoutLogin.setUser(user);
            cartWithoutLogin.setVersionNo(cartWithoutLogin.getVersionNo() + 1);
            cartRepository.save(cartWithoutLogin);
            log.info("Save current cart success");

            for(CartDetail cartDetail: cartWithoutLogin.getCartDetails()) {
                cartRetrieveSyncDto.setTotalQuantity(cartDetail.getQuantity());
            }
        }

        return cartRetrieveSyncDto;
    }

    /**
     * display cart
     *
     * @param token - input token
     * @return - cart
     */
    public CartRetrieveDto displayCart(String token, User user) {
        CartRetrieveDto cartRetrieveDto = new CartRetrieveDto();
        CartDetailRetrieveDto cartDetailRetrieveDto = new CartDetailRetrieveDto();

        List<CartDetailRetrieveDto> cartDetailRetrieveDtos = new ArrayList<>();

        User existingUser = user;
        if(existingUser == null) {
            log.info("Not authentication");
            Cart cartWithoutLogin = cartRepository.findByToken(token);
            cartRetrieveDto.setCartId(cartWithoutLogin.getCartId());
            cartRetrieveDto.setTotalPrice(cartWithoutLogin.getTotalPrice());
            cartRetrieveDto.setVersionNo(cartWithoutLogin.getVersionNo());

            for(CartDetail cartDetail: cartWithoutLogin.getCartDetails()) {
                cartDetailRetrieveDto.setCartDetailId(cartDetail.getCartDetailId());
                cartDetailRetrieveDto.setProductId(cartDetail.getProduct().getProductId());
                cartDetailRetrieveDto.setProductName(cartDetail.getProduct().getProductName());
                cartDetailRetrieveDto.setQuantity(cartDetail.getQuantity());
                cartDetailRetrieveDto.setPrice(cartDetail.getPrice());
                cartDetailRetrieveDto.setTotalPrice(cartDetail.getTotalPrice());

                for(ProductImage productImage: cartDetail.getProduct().getProductImages()) {
                    cartDetailRetrieveDto.setImagePath(productImage.getImageForProduct().getPath());
                    cartDetailRetrieveDto.setImageName(productImage.getImageForProduct().getName());
                }

                switch (cartDetail.getStatusCode()) {
                    case 1 -> cartDetailRetrieveDto.setStatus(Status.ACTIVE.getStatus());
                    case 2 -> cartDetailRetrieveDto.setStatus(Status.PENDING.getStatus());
                    case 3 -> cartDetailRetrieveDto.setStatus(Status.CHECKED_OUT.getStatus());
                    case 4 -> cartDetailRetrieveDto.setStatus(Status.EXPIRED.getStatus());
                    case 5 -> cartDetailRetrieveDto.setStatus(Status.ABANDONED.getStatus());
                    case 6 -> cartDetailRetrieveDto.setStatus(Status.SAVED.getStatus());
                    case 7 -> cartDetailRetrieveDto.setStatus(Status.DELETED.getStatus());
                    default -> {
                        log.error("Not found this status");
                        throw new RuntimeException("This status is not existed");
                    }
                }
                cartDetailRetrieveDtos.add(cartDetailRetrieveDto);
            }
            cartRetrieveDto.setCartDetailRetrieveDtos(cartDetailRetrieveDtos);
            log.info("Get cart without login success");
        } else {
            Cart cartLogin = existingUser.getCart();
            cartRetrieveDto.setCartId(cartLogin.getCartId());
            cartRetrieveDto.setTotalPrice(cartLogin.getTotalPrice());
            cartRetrieveDto.setVersionNo(cartLogin.getVersionNo());

            for(CartDetail cartDetail: cartLogin.getCartDetails()) {
                cartDetailRetrieveDto.setCartDetailId(cartDetail.getCartDetailId());
                cartDetailRetrieveDto.setProductId(cartDetail.getProduct().getProductId());
                cartDetailRetrieveDto.setProductName(cartDetail.getProduct().getProductName());
                cartDetailRetrieveDto.setQuantity(cartDetail.getQuantity());
                cartDetailRetrieveDto.setPrice(cartDetail.getPrice());
                cartDetailRetrieveDto.setTotalPrice(cartDetail.getTotalPrice());

                for(ProductImage productImage: cartDetail.getProduct().getProductImages()) {
                    cartDetailRetrieveDto.setImagePath(productImage.getImageForProduct().getPath());
                    cartDetailRetrieveDto.setImageName(productImage.getImageForProduct().getName());
                }

                switch (cartDetail.getStatusCode()) {
                    case 1 -> cartDetailRetrieveDto.setStatus(Status.ACTIVE.getStatus());
                    case 2 -> cartDetailRetrieveDto.setStatus(Status.PENDING.getStatus());
                    case 3 -> cartDetailRetrieveDto.setStatus(Status.CHECKED_OUT.getStatus());
                    case 4 -> cartDetailRetrieveDto.setStatus(Status.EXPIRED.getStatus());
                    case 5 -> cartDetailRetrieveDto.setStatus(Status.ABANDONED.getStatus());
                    case 6 -> cartDetailRetrieveDto.setStatus(Status.SAVED.getStatus());
                    case 7 -> cartDetailRetrieveDto.setStatus(Status.DELETED.getStatus());
                    default -> {
                        log.error("Not found this status");
                        throw new RuntimeException("This status is not existed");
                    }
                }
                cartDetailRetrieveDtos.add(cartDetailRetrieveDto);
            }
            cartRetrieveDto.setCartDetailRetrieveDtos(cartDetailRetrieveDtos);
            log.info("Get cart login success");
        }

        log.info("display cart success");
        return cartRetrieveDto;
    }

    /**
     * Get sum quantity in cart
     *
     * @param token - input token
     * @return - cartSumQuantityDto
     */
    public CartSumQuantityDto getTotalQuantityInCart(String token, User user) {
        User existingUser = user;
        if(existingUser == null) {
            log.info("Not authentication");
            Cart cartWithoutLogin = cartRepository.findByToken(token);

            CartSumQuantityDto cartSumQuantityDto = new CartSumQuantityDto();
            int totalQuantitySum = 0;
            for(CartDetail cartDetail: cartWithoutLogin.getCartDetails()) {
                totalQuantitySum += cartDetail.getQuantity();
            }
            cartSumQuantityDto.setTotalQuantity(totalQuantitySum);
            cartSumQuantityDto.setVersionNo(cartWithoutLogin.getVersionNo());

            log.info("Get total quantity in cart success");
            return cartSumQuantityDto;
        } else {
            Cart cartLogin = existingUser.getCart();

            CartSumQuantityDto cartSumQuantityDto = new CartSumQuantityDto();
            int totalQuantitySum = 0;
            for(CartDetail cartDetail: cartLogin.getCartDetails()) {
                totalQuantitySum += cartDetail.getQuantity();
            }
            cartSumQuantityDto.setTotalQuantity(totalQuantitySum);
            cartSumQuantityDto.setVersionNo(cartLogin.getVersionNo());

            log.info("Get total quantity in cart success");
            return cartSumQuantityDto;
        }
    }

    /**
     * update cart
     *
     * @param cartUpdateDto - input cartUpdateDto's properties
     * @return - cartRetrieveUpdateDto
     */
    @Transactional
    public CartRetrieveUpdateDto updateCart(CartUpdateDto cartUpdateDto, User user) {
        User existingUser = user;
        if(existingUser == null) {
            log.info("Not authentication");
            CartRetrieveUpdateDto cartRetrieveUpdateDto = new CartRetrieveUpdateDto();
            int totalQuantity = 0;
            double totalPrice = 0.0;

            Cart cartWithoutLogin = cartRepository.findByToken(cartUpdateDto.getToken());
            if(cartWithoutLogin.getVersionNo().equals(cartUpdateDto.getVersionNo())) {
                for(CartDetail cartDetail: cartWithoutLogin.getCartDetails()) {
                    if(cartDetail.getCartDetailId().equals(cartUpdateDto.getCartDetailId())) {
                        cartDetail.setQuantity(cartUpdateDto.getQuantity());
                        cartDetail.setTotalPrice(cartDetail.getPrice() * cartDetail.getQuantity());
                        cartDetailRepository.save(cartDetail);
                        log.info("Update cart detail success");

                        totalQuantity += cartDetail.getQuantity();
                        totalPrice +=  cartDetail.getTotalPrice();
                    }
                }
                cartWithoutLogin.setTotalPrice(totalPrice);
                cartWithoutLogin.setVersionNo(cartUpdateDto.getVersionNo() + 1);
                cartRepository.save(cartWithoutLogin);
                log.info("Update cart success");

                cartRetrieveUpdateDto.setTotalQuantity(totalQuantity);
            }
            return cartRetrieveUpdateDto;
        } else {
            CartRetrieveUpdateDto cartRetrieveUpdateDto = new CartRetrieveUpdateDto();
            int totalQuantity = 0;
            double totalPrice = 0.0;

            Cart cartLogin = existingUser.getCart();
            if(cartLogin.getVersionNo().equals(cartUpdateDto.getVersionNo())) {
                for(CartDetail cartDetail: cartLogin.getCartDetails()) {
                    if(cartDetail.getCartDetailId().equals(cartUpdateDto.getCartDetailId())) {
                        cartDetail.setQuantity(cartUpdateDto.getQuantity());
                        cartDetail.setTotalPrice(cartDetail.getPrice() * cartDetail.getQuantity());
                        cartDetailRepository.save(cartDetail);
                        log.info("Update cart detail success");

                        totalQuantity += cartDetail.getQuantity();
                        totalPrice +=  cartDetail.getTotalPrice();
                    }
                }
                cartLogin.setTotalPrice(totalPrice);
                cartLogin.setVersionNo(cartUpdateDto.getVersionNo() + 1);
                cartRepository.save(cartLogin);
                log.info("Update cart success");

                cartRetrieveUpdateDto.setTotalQuantity(totalQuantity);
            }
            return cartRetrieveUpdateDto;
        }
    }

    /**
     * Delete cart
     *
     * @param cartDeleteDto - input cartDeleteDto's properties
     * @return - cartRetrieveSyncDto
     */
    @Transactional
    public CartRetrieveSyncDto deleteCart(CartDeleteDto cartDeleteDto, User user) {
        CartRetrieveSyncDto cartRetrieveSyncDto = new CartRetrieveSyncDto();

        // Get cart while user login
        User existingUser = user;
        Cart cartLogin = existingUser.getCart();

        // Get cart without login
        Cart cartWithoutLogin = cartRepository.findByToken(cartDeleteDto.getToken());

        // Check if clearCart equal 1 then delete cart and cart detail
        if(cartDeleteDto.getClearCart() == 1){
            // Check if cart is null
            if(cartLogin == null && cartWithoutLogin == null) {
                log.error("Not found cart");
                throw new NullPointerException("Can't delete cart because can't get cart from user");
            }  else if(cartLogin != null) { // For cart login
                for(CartDetail cartDetail: cartLogin.getCartDetails()) {
                    if(cartDetail.getCartDetailId().equals(cartDeleteDto.getDetailId())){
                        cartDetailRepository.deleteById(cartDetail.getCartDetailId());
                        log.info("Delete detail cart while user login success");

                        cartRetrieveSyncDto.setTotalQuantity(cartDetail.getQuantity());
                    }
                }
                if(cartLogin.getVersionNo().equals(cartDeleteDto.getVersionNo())) {
                    cartRepository.deleteById(cartLogin.getCartId());
                    log.info("Delete cart while user login success");
                }
            } else if(cartWithoutLogin != null) { // For cart without login
                for(CartDetail cartDetail: cartWithoutLogin.getCartDetails()) {
                    if(cartDetail.getCartDetailId().equals(cartDeleteDto.getDetailId())){
                        cartDetailRepository.deleteById(cartDetail.getCartDetailId());
                        log.info("Delete detail cart without login success");

                        cartRetrieveSyncDto.setTotalQuantity(cartDetail.getQuantity());
                    }
                }
                if(cartWithoutLogin.getVersionNo().equals(cartDeleteDto.getVersionNo())) {
                    cartRepository.deleteById(cartLogin.getCartId());
                    log.info("Delete cart without login success");
                }
            }
        }

        // Check if clearCart equal 0 then delete cart detail and update cart
        if(cartDeleteDto.getClearCart() == 0) {
            List<CartDetail> cartDetailsToDelete = new ArrayList<>();
            if(cartLogin == null && cartWithoutLogin == null) { // Check if cart is null
                log.error("Not found cart");
                throw new NullPointerException("Can't delete cart because can't get cart from user");
            }else if(cartLogin != null && cartWithoutLogin == null) { // For cart login

                double totalPriceSum = 0.0;
                for(CartDetail cartDetail: cartLogin.getCartDetails()) {
                    totalPriceSum += cartDetail.getTotalPrice();
                    cartDetailsToDelete.add(cartDetail);
                    cartRetrieveSyncDto.setTotalQuantity(cartDetail.getQuantity());
                }

                // Delete the cart details outside the loop
                for (CartDetail cartDetail : cartDetailsToDelete) {
                    cartLogin.getCartDetails().remove(cartDetail);
                    cartDetail.setCart(null);
                    cartDetailRepository.delete(cartDetail);
                    log.info("Delete detail cart while user login success");
                }

                cartLogin.setTotalPrice(totalPriceSum);
                cartLogin.setVersionNo(cartDeleteDto.getVersionNo());
                cartRepository.save(cartLogin);
                log.info("update cart while user login success");

            } else if(cartLogin == null && cartWithoutLogin != null) { // For cart without login

                double totalPriceSum = 0.0;
                for(CartDetail cartDetail: cartWithoutLogin.getCartDetails()) {
                    totalPriceSum += cartDetail.getTotalPrice();
                    cartDetailsToDelete.add(cartDetail);
                    cartRetrieveSyncDto.setTotalQuantity(cartDetail.getQuantity());
                }

                // Delete the cart details outside the loop
                for (CartDetail cartDetail : cartDetailsToDelete) {
                    cartLogin.getCartDetails().remove(cartDetail);
                    cartDetail.setCart(null);
                    cartDetailRepository.delete(cartDetail);
                    log.info("Delete detail cart without login success");
                }

                cartLogin.setTotalPrice(totalPriceSum);
                cartWithoutLogin.setVersionNo(cartDeleteDto.getVersionNo());
                cartRepository.save(cartWithoutLogin);
                log.info("update cart without login success");
            }
        }
        return cartRetrieveSyncDto;
    }
}
