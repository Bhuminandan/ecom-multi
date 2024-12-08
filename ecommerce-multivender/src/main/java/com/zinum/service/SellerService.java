package com.zinum.service;

import com.zinum.enums.AccountStatus;
import com.zinum.exception.SellerException;
import com.zinum.model.Seller;

import java.util.List;

public interface SellerService {

    Seller getSellerProfile(String jwt);

    Seller createSeller(Seller seller);

    Seller getSellerById(Long id) throws SellerException;

    Seller getSellerByEmail(String email);

    List<Seller> getAllSellers(AccountStatus accountStatus);

    Seller updateSeller(Long id, Seller seller) throws SellerException;

    void deleteSeller(Long id) throws SellerException;

    Seller verifySellerEmail(String email, String code);

    Seller updateSellerAccountStatus(Long sellerId, AccountStatus accountStatus) throws SellerException;

}
