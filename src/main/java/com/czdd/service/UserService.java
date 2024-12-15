package com.czdd.service;

import com.czdd.pojo.PageBean;
import com.czdd.pojo.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    /*
     * @description:查询全部User信息
            * @return: java.util.List<com.czdd.pojo.User>
            * @author: Cz_13
            * @time: 2024/11/17 16:43
     */
    List<User> list();

    /*
     * @description:删
     * @param id
            * @return: void
            * @author: Cz_13
            * @time: 2024/11/17 19:14
     */
    void delete(Integer id);

    /*
     * @description:增
     * @param user
            * @return: void
            * @author: Cz_13
            * @time: 2024/11/17 19:14
     */
    void create(User user);

    /*
     * @description:通过id查询
     * @param id
            * @return: com.czdd.pojo.User
            * @author: Cz_13
            * @time: 2024/11/17 19:14
     */
    User getUserById(Integer id);

    /*
     * @description:改
     * @param id
     * @param user
            * @return: void
            * @author: Cz_13
            * @time: 2024/11/17 19:15
     */
    void update(Integer id, User user);

    PageBean page(Integer page, Integer pageSize);
}
