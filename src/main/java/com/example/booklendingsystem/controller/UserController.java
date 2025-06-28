package com.example.booklendingsystem.controller;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Value;

import com.example.booklendingsystem.model.User;
import com.example.booklendingsystem.repository.UserRepository;
import com.example.booklendingsystem.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.web.servlet.ModelAndView;  // 添加ModelAndView类的导入
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostConstruct
    public void init() {
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    @Autowired
    private UserRepository userRepository   ;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/online-count")
    public Long getOnlineUserCount() {
        return userService.countAllByIsLoggedIn();
    }
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }
    
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id,@RequestBody User user) {
        userService.deleteUser(id,user);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> userData) {
        String username = userData.get("username");
        String password = userData.get("password");

        // 验证数据
        if (username == null  || password == null ||
            username.isEmpty() || password.isEmpty()) {
            return ResponseEntity.badRequest().body("缺少必要字段");
        }

        // 创建新用户
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);  // 实际应加密存储
        newUser.setCreatedAt(java.time.LocalDateTime.now());
        newUser.setEmail(username + "@qq.com");
        newUser.setNickname(username);
        User savedUser = userService.saveUser(newUser);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> credentials, HttpSession session) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return ResponseEntity.badRequest().body("Username and password are required");
        }

        boolean flag = userService.findByUsernameAndPassword(username, password);
        
        if (flag) {
            // 从数据库获取完整用户信息
            List<User> users = userService.getAllUsers().stream()
                .filter(u -> u.getUsername().equals(username))
                .toList();
                
            if (!users.isEmpty()) {
                // 将用户信息存入session
                session.setAttribute("user", users.get(0));
                
                // 更新最后登录时间（需要User实体支持）
                // 这里暂时注释掉，直到User类实现setLastLogin方法
                User user = users.get(0);
                 user.setLastLogin(java.time.LocalDateTime.now());
                 userService.saveUser(user);
            }
            
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate(); // 清除 Session
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile")
    public ModelAndView getUserProfile(HttpSession session) {  // 修改为使用ModelAndView
        // 从session中获取用户信息（示例）
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            return new ModelAndView("redirect:/login.html");  // 未登录则跳转
        }
        
        return new ModelAndView("user-profile", "user", user);  // 使用ModelAndView传递数据
    }
    @PostMapping(path = "/uploadAvatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadAvatar(@RequestParam("avatar") MultipartFile file, HttpSession session) {
        if (file.isEmpty()) {
            return "文件为空，请选择一个文件上传。";
        }
        try {
            // 创建上传目录（如果不存在）
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                // 创建父目录链以避免路径问题
                dir.mkdirs();
            }

            // 读取图片并转换为JPG格式
            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            // 如果不是JPG格式则进行转换
            if (!file.getOriginalFilename().toLowerCase().endsWith(".jpg")) {
                BufferedImage jpgImage = new BufferedImage(
                    originalImage.getWidth(),
                    originalImage.getHeight(),
                    BufferedImage.TYPE_INT_RGB);
                jpgImage.createGraphics().drawImage(originalImage, 0, 0, Color.WHITE, null);
                
                // 保存为JPG格式
                String fileName = System.currentTimeMillis() + ".jpg";
                File targetFile = new File(dir, fileName);
                ImageIO.write(jpgImage, "jpg", targetFile);
                
                // 更新用户头像路径
                User user = (User) session.getAttribute("user");
                user.setAvatar(fileName);
                userRepository.save(user);
                return "文件上传成功: " + fileName;
            } else {
                // 如果已经是JPG格式，直接保存
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                File targetFile = new File(dir, fileName);
                file.transferTo(targetFile);
                
                // 更新用户头像路径
                User user = (User) session.getAttribute("user");
                user.setAvatar(fileName);
                userRepository.save(user);
                return "文件上传成功: " + fileName;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "文件上传失败: " + e.getMessage();
        }
    }
}
