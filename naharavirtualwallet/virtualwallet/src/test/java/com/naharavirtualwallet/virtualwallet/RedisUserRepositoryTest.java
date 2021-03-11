package com.naharavirtualwallet.virtualwallet;

import com.naharavirtualwallet.virtualwallet.model.User;
import com.naharavirtualwallet.virtualwallet.model.Wallet;
import com.naharavirtualwallet.virtualwallet.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.test.context.junit4.SpringRunner;

@DataRedisTest

public class RedisUserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    public void testAdd(){

    }
}
