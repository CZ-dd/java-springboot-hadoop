package com.czdd.mapper;

import com.czdd.pojo.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    /*
     * @description:查询全部User信息
     * @param
            * @return: java.util.List<com.czdd.pojo.User>
            * @author: Cz_13
            * @time: 2024/11/17 16:45
     */
    List<User> list();

    /*
     * @description:删
     * @param id
            * @return: void
            * @author: Cz_13
            * @time: 2024/11/17 19:20
     */
    void deleteById(Integer id);

    /*
     * @description:增
     * @param user
            * @return: void
            * @author: Cz_13
            * @time: 2024/11/17 19:20
     */
    void insert(User user);

    /*
     * @description:查
     * @param id
            * @return: com.czdd.pojo.User
            * @author: Cz_13
            * @time: 2024/11/17 19:20
     */
    User getUserById(Integer id);

    /*
     * @description:改
     * @param id
     * @param user
            * @return: void
            * @author: Cz_13
            * @time: 2024/11/17 19:20
     */
    void update(Integer id, User user);

    Long count();

    List<User> page(Integer pageStart, Integer pageSize);
}
