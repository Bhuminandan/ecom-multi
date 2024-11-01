package com.zinum.service.Impl;

import com.zinum.Utils.OtpUtil;
import com.zinum.config.JwtProvider;
import com.zinum.dto.request.LoginReq;
import com.zinum.dto.request.SignupReq;
import com.zinum.dto.response.AuthRes;
import com.zinum.enums.UserRoles;
import com.zinum.model.Cart;
import com.zinum.model.Seller;
import com.zinum.model.User;
import com.zinum.model.VerificationCode;
import com.zinum.repository.CartRepository;
import com.zinum.repository.SellerRepository;
import com.zinum.repository.UserRepository;
import com.zinum.repository.VerificationCodeRepository;
import com.zinum.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtProvider jwtProvider;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailServiceImpl emailService;
    private final CustomUserServiceImpl customUserServiceImpl;
    private final SellerRepository sellerRepository;

    @Autowired
    public AuthServiceImpl(
               UserRepository userRepository,
               PasswordEncoder passwordEncoder,
               JwtProvider jwtProvider,
               CartRepository cartRepository,
               VerificationCodeRepository verificationCodeRepository,
               EmailServiceImpl emailService,
               CustomUserServiceImpl customUserServiceImpl,
               SellerRepository sellerRepository
    )
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cartRepository = cartRepository;
        this.jwtProvider = jwtProvider;
        this.verificationCodeRepository = verificationCodeRepository;
        this.emailService = emailService;
        this.customUserServiceImpl = customUserServiceImpl;
        this.sellerRepository = sellerRepository;
    }

    @Override
    public void sendOtpVerificationEmail(String email, UserRoles role) {
        String SIGNING_PREFIX = "signing_";
        String SELLER_PREFIX = "seller_";

        if(email.startsWith(SIGNING_PREFIX)) {
            email = email.replace(SIGNING_PREFIX, "");

            if(role.equals(UserRoles.ROLE_SELLER)) {
                Seller seller = sellerRepository.findByEmail(email);
                if(seller == null){
                    throw new RuntimeException("Seller not found");
                }
            } else {
                User user = userRepository.findByEmail(email);
                if(user == null) {
                    throw new RuntimeException("User not found");
                }
            }
        }

        VerificationCode isExist = verificationCodeRepository.findByEmail(email);
        if(isExist != null) {
            verificationCodeRepository.delete(isExist);
        }
        VerificationCode verificationCode = new VerificationCode();
        String Otp = OtpUtil.generateOtp();
        verificationCode.setCode(Otp);
        verificationCode.setEmail(email);
        verificationCodeRepository.save(verificationCode);

        String subject = "ECOM: OTP Verification";
        String body = "Your OTP for verification is: " + Otp + ". Please do not share it with anyone.";

        emailService.sendOtpVerificationEmail(email, Otp, subject, body);
    }

    @Override
    public String createUser(SignupReq signupReq) throws Exception {

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(signupReq.getEmail());
        if (verificationCode == null || !verificationCode.getCode().equals(signupReq.getOtp())) {
            throw new Exception("Verification code not found");
        }

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

    @Override
    public AuthRes signin(LoginReq loginReq) throws Exception {
        String username = loginReq.getEmail();
        String otp = loginReq.getOtp();

        Authentication authentication = authenticate(username, otp);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);

        AuthRes res = new AuthRes();
        res.setJwt(token);
        res.setMessage("User logged in successfully");
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roleName = authorities.isEmpty()? null : authorities.iterator().next().getAuthority();
        res.setRole(UserRoles.valueOf(roleName));
        return res;
    }



    private Authentication authenticate(String username, String otp) throws Exception {
        UserDetails userDetails = customUserServiceImpl.loadUserByUsername(username);

        if(userDetails == null) {
            throw new BadCredentialsException("Invalid email");
        }

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);
        if (verificationCode == null || !verificationCode.getCode().equals(otp)) {
            throw new BadCredentialsException("Invalid otp");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}
