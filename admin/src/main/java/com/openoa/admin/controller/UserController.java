package com.openoa.admin.controller;

import com.openoa.admin.service.impl.AuthUserDetailServiceImpl;
import com.openoa.admin.ultil.R;
import com.openoa.admin.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private AuthUserDetailServiceImpl authUserDetailService;

    @PostMapping("add")
    @ResponseBody
    R add(@RequestBody UserVo userVo) {
        return R.ok(authUserDetailService.add(userVo));
    }
}
