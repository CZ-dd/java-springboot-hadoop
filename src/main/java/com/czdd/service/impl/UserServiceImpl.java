package com.czdd.service.impl;

import com.czdd.mapper.UserMapper;
import com.czdd.pojo.PageBean;
import com.czdd.pojo.User;
import com.czdd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> list() {
        return userMapper.list();
    }

    @Override
    public void delete(Integer id) {
        userMapper.deleteById(id);
    }

    @Override
    public void create(User user) {
        userMapper.insert(user);
    }

    @Override
    public User getUserById(Integer id) {
        return userMapper.getUserById(id);
    }

    @Override
    public void update(Integer id, User user) {
        userMapper.update(id, user);
    }

    @Override
    public PageBean page(Integer page, Integer pageSize) {
        Long total = userMapper.count();
        List<User> list = userMapper.page((page - 1) * pageSize, pageSize);
        return new PageBean(total, list);
    }
}
