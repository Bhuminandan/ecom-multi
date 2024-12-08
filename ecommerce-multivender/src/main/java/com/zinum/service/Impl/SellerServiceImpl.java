package com.zinum.service.Impl;

import com.zinum.config.JwtProvider;
import com.zinum.enums.AccountStatus;
import com.zinum.enums.UserRoles;
import com.zinum.exception.SellerException;
import com.zinum.model.Address;
import com.zinum.model.Seller;
import com.zinum.repository.AddressRepository;
import com.zinum.repository.SellerRepository;
import com.zinum.service.SellerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;

    public SellerServiceImpl(
            SellerRepository sellerRepository,
            JwtProvider jwtProvider,
            PasswordEncoder passwordEncoder,
            AddressRepository addressRepository
    ) {
        this.sellerRepository = sellerRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.addressRepository = addressRepository;
    }

    @Override
    public Seller getSellerProfile(String jwt) {
        String email = jwtProvider.getEmailFromJWT(jwt);
        return this.getSellerByEmail(email);
    }

    @Override
    public Seller createSeller(Seller seller) {
        log.info("Saving seller: {}", seller);
        Seller sellerExists = sellerRepository.findByEmail(seller.getEmail());
        if (sellerExists != null) {
            throw new RuntimeException("Seller already exists");
        }

        Seller newSeller = new Seller();
        newSeller.setFirstName(seller.getFirstName());
        newSeller.setLastName(seller.getLastName());
        newSeller.setEmail(seller.getEmail());
        newSeller.setMobile(seller.getMobile());
        newSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
        newSeller.setRole(UserRoles.ROLE_SELLER);
        newSeller.setGSTIN(seller.getGSTIN());
        newSeller.setBankDetails(seller.getBankDetails());
        newSeller.setBusinessDataDetails(seller.getBusinessDataDetails());

        Address pickupAddress = seller.getPickupAddress();
        if (pickupAddress != null) {
            pickupAddress.setSeller(newSeller);
            newSeller.setPickupAddress(pickupAddress);
        }

        log.info("Saving seller: {}", newSeller);
        return sellerRepository.save(newSeller);
    }

    @Override
    public Seller getSellerById(Long id) throws SellerException {
        return sellerRepository.findById(id).orElseThrow(() -> new SellerException("Seller not found"));
    }

    @Override
    public Seller getSellerByEmail(String email) {
        Seller seller = sellerRepository.findByEmail(email);
        if (seller == null) {
            throw new RuntimeException("Seller not found");
        }
        return seller;
    }

    @Override
    public List<Seller> getAllSellers(AccountStatus accountStatus) {
        return sellerRepository.findByAccountStatus(accountStatus);
    }

    @Override
    public Seller updateSeller(Long id, Seller seller) throws SellerException {

        Seller existingSeller = this.getSellerById(id);

        if(seller.getPickupAddress() != null){
            Address savedAddress = addressRepository.save(seller.getPickupAddress());
            existingSeller.setPickupAddress(savedAddress);
        }

        if(seller.getPassword() != null){
            existingSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
        }

        if (seller.getFirstName() != null) {
            existingSeller.setFirstName(seller.getFirstName());
        }

        if (seller.getLastName() != null) {
            existingSeller.setLastName(seller.getLastName());
        }

        if (seller.getMobile() != null) {
            existingSeller.setMobile(seller.getMobile());
        }

        if (seller.getGSTIN() != null) {
            existingSeller.setGSTIN(seller.getGSTIN());
        }

        if (seller.getBankDetails() != null) {
            existingSeller.setBankDetails(seller.getBankDetails());
        }

        if (seller.getBusinessDataDetails() != null && seller.getBusinessDataDetails().getBusinessName() != null) {
            existingSeller.setBusinessDataDetails(seller.getBusinessDataDetails());
        }

        if (seller.getBankDetails() != null
         && seller.getBankDetails().getAccountNumber() != null
         && seller.getBankDetails().getIfscCode() != null
         && seller.getBankDetails().getBankName() != null) {
            existingSeller.setBankDetails(seller.getBankDetails());
        }

        if(seller.getPickupAddress() != null
                && seller.getPickupAddress().getAddress() != null
                && seller.getPickupAddress().getCity() != null
                && seller.getPickupAddress().getState() != null
                && seller.getPickupAddress().getPinCode() != null
                && seller.getPickupAddress().getCountry() != null
        ) {
            existingSeller.setPickupAddress(seller.getPickupAddress());
        }
        return sellerRepository.save(existingSeller);
    }

    @Override
    public void deleteSeller(Long id) throws SellerException {
        Seller seller = this.getSellerById(id);
        sellerRepository.delete(seller);
    }

    @Override
    public Seller verifySellerEmail(String email, String code) {
        Seller seller = this.getSellerByEmail(email);
        seller.setVerified(true);
        seller.setAccountStatus(AccountStatus.ACTIVE);
        return sellerRepository.save(seller);
    }

    @Override
    public Seller updateSellerAccountStatus(Long sellerId, AccountStatus accountStatus) throws SellerException {
        Seller seller = this.getSellerById(sellerId);
        seller.setAccountStatus(accountStatus);
        return sellerRepository.save(seller);
    }
}
