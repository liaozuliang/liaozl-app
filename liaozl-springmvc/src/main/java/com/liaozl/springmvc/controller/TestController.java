package com.liaozl.springmvc.controller;

import com.liaozl.springmvc.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/test")
public class TestController extends BaseController {

    @RequestMapping("index/{type}")
    public String index(ModelMap map, @PathVariable String type, HttpServletRequest request, HttpSession session) {
        List<User> userList = new ArrayList<User>();

        userList.add(new User(1L, "aa", 11, Arrays.asList(new String[]{"aa"}), new Date()));
        userList.add(new User(2L, "123", 22, Arrays.asList(new String[]{"123"}), new Date()));
        userList.add(new User(3L, "北京", 33, Arrays.asList(new String[]{"北京"}), new Date()));
        userList.add(new User(4L, "aa123北京", 44, Arrays.asList(new String[]{"aa", "123", "北京"}), new Date()));

        request.setAttribute("userList", userList);
        session.setAttribute("userList", userList);

        if ("jsp".equals(type)) {
            return "forward:/test/jspList";
        } else if ("ftl".equals(type)) {
            return "forward:/test/ftlList";
        } else {
            return "redirect:/test/jsonList";
        }
    }

    @RequestMapping("jsonList")
    @ResponseBody
    public User jsonTestList(ModelMap map) {
        map.put("type", "jsonTestList");
        return new User(5L, "aa123北京", 55, Arrays.asList(new String[]{"aa", "123", "北京"}), new Date());
    }

    @RequestMapping("strTest")
    @ResponseBody
    public String strTest(ModelMap map) {
        return "ok";
    }

    @RequestMapping("mapTest")
    @ResponseBody
    public Map<String, Object> mapTest(ModelMap map) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("int", 1);
        rtnMap.put("string", "哈哈");
        rtnMap.put("long", 2L);
        rtnMap.put("date", new Date());

        return rtnMap;
    }

    @RequestMapping("viewOrderTest")
    public String viewOrderTest(ModelMap map) {
        return "/test/ViewOrderTest";
    }

    /**
     * 跳转到jsp页面
     *
     * @param map
     * @return
     */
    @RequestMapping("jspList")
    public String jspTestList(ModelMap map, HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("userList") == null) {
            System.out.println("===========jspList create userList===========");

            List<User> userList = new ArrayList<User>();

            userList.add(new User(1L, "aa", 11, Arrays.asList(new String[]{"aa"}), new Date()));
            userList.add(new User(2L, "123", 22, Arrays.asList(new String[]{"123"}), new Date()));
            userList.add(new User(3L, "北京", 33, Arrays.asList(new String[]{"北京"}), new Date()));
            userList.add(new User(4L, "aa123北京", 44, Arrays.asList(new String[]{"aa", "123", "北京"}), new Date()));

            map.put("userList", userList);
        }

        return "/test/JspTestList";
    }

    /**
     * 跳转到freemarker页面
     *
     * @param map
     * @return
     */
    @RequestMapping("ftlList")
    public String ftlTestList(ModelMap map, HttpSession session) {
        if (session.getAttribute("userList") == null) {
            System.out.println("===========ftlList create userList===========");

            List<User> userList = new ArrayList<User>();

            userList.add(new User(1L, "aa", 11, Arrays.asList(new String[]{"aa"}), new Date()));
            userList.add(new User(2L, "123", 22, Arrays.asList(new String[]{"123"}), new Date()));
            userList.add(new User(3L, "北京", 33, Arrays.asList(new String[]{"北京"}), new Date()));
            userList.add(new User(4L, "aa123北京", 44, Arrays.asList(new String[]{"aa", "123", "北京"}), new Date()));

            map.put("userList", userList);
        }

        return "/test/FtlTestList";
    }
}
