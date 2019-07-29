package com.quanwc.controller;

import com.quanwc.service.MongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * @author quanwenchao
 * @date 2019/4/2 20:35:41
 */
@Controller
public class StudentController {

    @Autowired
    private MongoService mongoService;

    @PostMapping("/save")
    public String save() {


        HashMap<String, String> stringStringHashMap = new HashMap<>();

        mongoService.save();
        return "success";
    }

    @PostMapping("/save2")
    public String save2() {
        mongoService.save2();
        return "success";
    }

    @PostMapping("/save3")
    public String save3() {
        mongoService.save3();
        return "success";
    }
}
