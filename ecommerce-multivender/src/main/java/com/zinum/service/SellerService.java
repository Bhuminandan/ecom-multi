package com.zinum.service;

import com.zinum.enums.AccountStatus;
import com.zinum.model.Seller;

import java.util.List;

public interface SellerService {

    Seller getSellerProfile(String jwt);

    Seller createSeller(Seller seller);

    Seller getSellerById(Long id);

    Seller getSellerByEmail(String email);

    List<Seller> getAllSellers(AccountStatus accountStatus);

    Seller updateSeller(Long id, Seller seller);

    void deleteSeller(Long id);

    Seller verifySellerEmail(String email, String code);

    Seller updateSellerAccountStatus(Long sellerId, AccountStatus accountStatus);

}
