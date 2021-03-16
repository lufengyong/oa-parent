package com.openoa.admin.controller;

import com.openoa.admin.ultil.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @PostMapping("add")
    @ResponseBody
    R add()
    {
        return R.ok(0);
    }
}
