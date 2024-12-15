package com.czdd.controller;

import com.czdd.pojo.PageBean;
import com.czdd.pojo.Result;
import com.czdd.pojo.User;
import com.czdd.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;


    /*
     * @description:查询
     * @param
            * @return: com.czdd.pojo.Result
            * @author: Cz_13
            * @time: 2024/11/17 16:58
     */
    /* 被分页查询取代
    @GetMapping
    public Result list(){

        List<User> userList = userService.list();

        return Result.success(userList);
    }
    */


    /*
     * @description:删除
     * @param id
            * @return: com.czdd.pojo.Result
            * @author: Cz_13
            * @time: 2024/11/17 16:59
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        userService.delete(id);
        return Result.success();
    }

    /*
     * @description: 改操作, 以json格式提交数据
     * @param id
            * @return: com.czdd.pojo.Result
            * @author: Cz_13
            * @time: 2024/11/17 17:10
     */
    @PutMapping("/{id}")
    public Result update(@PathVariable Integer id, @RequestBody User user){
        userService.update(id, user);
        return Result.success();
    }

    /*
     * @description: 通过id查询
     * @param id
            * @return: com.czdd.pojo.Result
            * @author: Cz_13
            * @time: 2024/11/17 17:28
     */
    @GetMapping("/read/{id}")
    public Result read(@PathVariable Integer id){
        User user = userService.getUserById(id);
        return Result.success(user);
    }

    /*
     * @description: 新增
     * @param user
            * @return: com.czdd.pojo.Result
            * @author: Cz_13
            * @time: 2024/11/17 19:10
     */
    @PostMapping
    public Result create(@RequestBody User user){
        userService.create(user);
        return Result.success();
    }

    /*
     * @description:分页查询
     * @param page      页码
     * @param pageSize  每页大小
            * @return: com.czdd.pojo.Result
            * @author: Cz_13
            * @time: 2024/11/17 19:35
     */
    @GetMapping
    public Result page(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "5") Integer pageSize){
        PageBean pageBean = userService.page(page, pageSize);
        return Result.success(pageBean);
    }
}
