/*
 * Copyright (C) 2016 eKing Technology, Inc. All Rights Reserved.
 */
package com.qiqi.config.spring.crypt;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jasypt 加密, 配置文件加密
 * https://github.com/ulisesbocchio/jasypt-spring-boot
 *
 * @author zengfan
 */
@Configuration
public class JasyptEncryptorConfig {
    /**
     * 自定义加密、解密的类
     **/
    @Bean(name = "jasyptStringEncryptor")
    static public StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword("password");     // 加密密码
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }
}