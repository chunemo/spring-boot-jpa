package com.qiqi.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qiqi.entity.User;
import com.qiqi.service.base.impl.BaseServiceImpl;

@Service("userService")
@Transactional
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {
	
}