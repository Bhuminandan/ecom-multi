package com.zinum.service.Impl;

import com.zinum.config.JwtProvider;
import com.zinum.dto.request.SignupReq;
import com.zinum.enums.UserRoles;
import com.zinum.model.Cart;
import com.zinum.model.User;
import com.zinum.repository.CartRepository;
import com.zinum.repository.UserRepository;
import com.zinum.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtProvider jwtProvider;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtProvider jwtProvider,
                           CartRepository cartRepository)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cartRepository = cartRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public String createUser(SignupReq signupReq) {

        User user = userRepository.findByEmail(signupReq.getEmail());
        if (user == null) {
            User createdUser = new User();
            createdUser.setEmail(signupReq.getEmail());
            createdUser.setRole(UserRoles.ROLE_CUSTOMER);
            createdUser.setPassword(passwordEncoder.encode(signupReq.getPassword()));
            createdUser.setFirstName(signupReq.getFirstName());
            createdUser.setLastName(signupReq.getLastName());
            createdUser.setMobile(signupReq.getMobile());

            user = userRepository.save(createdUser);

            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(UserRoles.ROLE_CUSTOMER.toString()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            signupReq.getEmail(),
            signupReq.getPassword(),
            authorities
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.generateToken(authentication);
    }
}
