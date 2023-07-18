package com.beetech.finalproject.domain.service;

import com.beetech.finalproject.domain.entities.Cart;
import com.beetech.finalproject.domain.entities.CartDetail;
import com.beetech.finalproject.domain.entities.Product;
import com.beetech.finalproject.domain.entities.User;
import com.beetech.finalproject.domain.repository.CartDetailRepository;
import com.beetech.finalproject.domain.repository.CartRepository;
import com.beetech.finalproject.domain.repository.ProductRepository;
import com.beetech.finalproject.domain.repository.UserRepository;
import com.beetech.finalproject.utils.CustomGenerate;
import com.beetech.finalproject.web.dtos.cart.*;
import com.beetech.finalproject.web.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
     * Add new product to cart
     *
     * @param cartCreateDto - input cartCreateDto's properties
     * @return - cartRetrieveCreateDto
     */
    @Transactional
    public CartRetrieveCreateDto addProductToCart(CartCreateDto cartCreateDto) {
        User existingUser = extractUserFromToken(cartCreateDto.getToken());
        if(existingUser == null) {
            log.error("Not found this user");
            throw new UsernameNotFoundException("User not found: " + existingUser);
        }

        String cartToken = CustomGenerate.generateRandomString(20);

        Cart cart = existingUser.getCart();
        if(cart == null) {
            log.info("User don't have cart");
            cart = new Cart();
            cart.setUser(existingUser);
            cart.setTotalPrice(0.0);
            cart.setVersionNo(1.0);
            cart.setToken(cartToken);
            cartRepository.save(cart);
            log.info("Save new cart success");
        }

        Product existingProduct = productRepository.findById(cartCreateDto.getProductId()).orElseThrow(
                ()->{
                    log.error("Not found this product");
                    return new NullPointerException("Not found this product: " + cartCreateDto.getProductId());
                }
        );
        log.info("Found product");

        CartDetail cartDetail = new CartDetail();
        cartDetail.setCart(cart);
        cartDetail.setProduct(existingProduct);
        cartDetail.setQuantity(cartCreateDto.getQuantity());
        cartDetail.setPrice(existingProduct.getPrice());
        cartDetail.setTotalPrice(cartDetail.getPrice() * cartDetail.getQuantity());
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
     * Sync cart after login
     *
     * @param cartSyncDto - input cartSyncSto
     * @return - cartRetrieveDto
     */
    public CartRetrieveSyncDto syncCartAfterLogin(CartSyncDto cartSyncDto) {
        CartRetrieveSyncDto cartRetrieveSyncDto = new CartRetrieveSyncDto();

        // Get old cart from user
        User user = extractUserFromToken(cartSyncDto.getAuthenticationToken());
        Cart oldCart = user.getCart();

        // Get current cart without login
        Cart cartWithoutLogin = cartRepository.findByToken(cartSyncDto.getCartToken());

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

                        cartRetrieveSyncDto.setTotalQuantity(oldCartDetail.getQuantity());
                        break;
                    }
                }
                if(!IsMatch) {
                    // Product id doesn't exist, create new cart detail
                    cartDetailWithoutLogin.setCart(oldCart);
                    oldCart.getCartDetails().add(cartDetailWithoutLogin);
                    log.info("Haven't same product - add new cartDetail inside old cart");

                    cartRetrieveSyncDto.setTotalQuantity(cartDetailWithoutLogin.getQuantity());
                }
            }
            // Update old cart in the database
            cartRepository.save(oldCart);
            log.info("Save old cart success");
        }

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
     * Delete cart
     *
     * @param cartDeleteDto - input cartDeleteDto's properties
     * @return - cartRetrieveSyncDto
     */
    @Transactional
    public CartRetrieveSyncDto deleteCart(CartDeleteDto cartDeleteDto) {
        CartRetrieveSyncDto cartRetrieveSyncDto = new CartRetrieveSyncDto();

        // Get cart while user login
        User user = extractUserFromToken(cartDeleteDto.getAuthenticationToken());
        Cart cartLogin = user.getCart();

        // Get cart without login
        Cart cartWithoutLogin = cartRepository.findByToken(cartDeleteDto.getToken());

        // Check if clearCart equal 1 then delete cart and cart detail
        if(cartDeleteDto.getClearCart() == 1){
            // Check if cart is null
            if(cartLogin == null && cartWithoutLogin == null) {
                log.error("Not found cart");
                throw new NullPointerException("Can't delete cart because can't get cart from user");
            }  else if(cartLogin != null && cartWithoutLogin == null) { // For cart login
                for(CartDetail cartDetail: cartLogin.getCartDetails()) {
                    cartDetailRepository.deleteById(cartDetail.getCartDetailId());
                    log.info("Delete detail cart while user login success");

                    cartRetrieveSyncDto.setTotalQuantity(cartDetail.getQuantity());
                }
                cartRepository.deleteById(cartLogin.getCartId());
                log.info("Delete cart while user login success");
            } else if(cartLogin == null && cartWithoutLogin != null) { // For cart without login
                for(CartDetail cartDetail: cartWithoutLogin.getCartDetails()) {
                    cartDetailRepository.deleteById(cartDetail.getCartDetailId());
                    log.info("Delete detail cart without login success");

                    cartRetrieveSyncDto.setTotalQuantity(cartDetail.getQuantity());
                }
                cartRepository.deleteById(cartWithoutLogin.getCartId());
                log.info("Delete cart without login success");
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
