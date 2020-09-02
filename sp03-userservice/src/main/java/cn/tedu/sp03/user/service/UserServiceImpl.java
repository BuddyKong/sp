package cn.tedu.sp03.user.service;

import cn.tedu.sp01.pojo.User;
import cn.tedu.sp01.service.UserService;
import cn.tedu.web.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl  implements UserService {
    @Value("${sp.user-service.users}")
    private String userJson;

    @Override
    public User getUser(Integer userId) {
        log.info("users json string : " + userJson);
        List<User> list = JsonUtil.from(userJson, new TypeReference<List<User>>() {//TypeReference json工具api转换类型
        });
        for (User u : list) {
            if (u.getId().equals(userId)) {
                return u;
            }
        }
        return new User(userId, "name-" + userId, "pwd-" + userId);
    }
    @Override
    public void addScore(Integer userId, Integer score) {
        log.info("user "+userId+" - 增加积分 "+score);
    }
}
