package com.naharavirtualwallet.virtualwallet.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FiatTransactionRepository extends CrudRepository {

    List findByFromAccountNumber(Long fromAccountNumber);
    List findByToAccountNumber(Long toAccountNumber);
}
