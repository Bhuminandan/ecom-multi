package com.zinum.service.Impl;

import com.zinum.enums.UserRoles;
import com.zinum.model.Seller;
import com.zinum.model.User;
import com.zinum.repository.SellerRepository;
import com.zinum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserServiceImpl  implements UserDetailsService {

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private static final String SELLER_PREFIX = "seller_";

    @Autowired
    public CustomUserServiceImpl(UserRepository userRepository, SellerRepository sellerRepository) {
        this.userRepository = userRepository;
        this.sellerRepository = sellerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username.startsWith(SELLER_PREFIX)){
            String sellerEmail = username.substring(SELLER_PREFIX.length());
            Seller seller = sellerRepository.findByEmail(sellerEmail);
            if(seller == null){
                throw new UsernameNotFoundException("Seller not found");
            }
            return buildUserDetails(seller.getEmail(), seller.getPassword(), seller.getRole());
        } else {
            User user = userRepository.findByEmail(username);
            if(user == null){
                throw new UsernameNotFoundException("User not found");
            }
            return buildUserDetails(user.getEmail(), user.getPassword(), user.getRole());
        }
    }

    private UserDetails buildUserDetails(String username, String password, UserRoles role) {
        if(role == null) {
            role = UserRoles.ROLE_CUSTOMER;
        }
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority("ROLE_" + role.toString()));
        return new org.springframework.security.core.userdetails.User(username, password, list);
    }
}
