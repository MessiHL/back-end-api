package com.example.request_learn.controller;


import com.example.request_learn.entity.Comment;
import com.example.request_learn.entity.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/test")
public class requestController {
    private static String baseDir = "C:/Users/Administrator/Desktop/images";

    /**
     * GET：请求不带参数
     * @param comment
     * @return
     */
    @GetMapping("/get01")
    public String get01(String comment){
        return comment == null ?"no parameter":comment;
    }

    @GetMapping("/get02")
    public String get02(@RequestParam("comment") String comment){
        return comment;
    }

    @GetMapping("/get03")
    public String get03(
            @RequestParam("id") String id,
            @RequestParam("name") String name,
            @RequestParam("comment") String comment
     ){
        return "id:"+id+",name:"+name+",comment:"+comment;
    }

    /**
     *  使用对象对参数进行封装，这样在多个参数时，优势很明显。
     *  但是这里无法使用 @RequestParam注解，否则会出错。
     * */
    @GetMapping("/get04")
    public Comment get04(Comment comment){
        if(Objects.isNull(comment)){
            return null;
        } else {
            return comment;
        }
    }

    /**
     * 使用对象封装参数要求必须具有一个对象，所以可以使用 Map 来封装，这样可以减少对象的数量。
     * */
    @GetMapping("/get05")
    public Map<String,String>  get05(@RequestParam Map<String,String> map){
        map.forEach((k,v) ->{
            System.out.println(k + "-->" +v);
        });

        return map;
    }

    @GetMapping("/get06/{id}")
    public Comment getById(@PathVariable("id") String id){
        Comment comment = new Comment();
        comment.setId(id);
        comment.setName("name_"+id);
        comment.setComment("comment_" + id);
        return comment;
    }

    /**
     * 返回值为二进制
     * 其实这里可以使用 Files.readAllBytes()这个方法，这样就简单了。
     * 这里我就不改了，我习惯了使用这种环读取的方式，不过确实有点繁琐了。
     * */
    @GetMapping("/get07/{name}")
    public int getFile(@PathVariable("name") String name, HttpServletResponse response){
        int len = 0;
        try(OutputStream out = new BufferedOutputStream(response.getOutputStream())){
            try (InputStream in = new BufferedInputStream(new FileInputStream(new File(baseDir, name)))) {

                byte[] data = new byte[4*1024];
                while ((len = in.read(data)) != -1) {
                    out.write(data, 0, len);
                }
            }
            return len;
        }
        catch (IOException e){
            e.printStackTrace();
            return len;
        }
    }

    @PostMapping("/post01")
    public String post01(
            @RequestParam("id") String id,
            @RequestParam("name") String name,
            @RequestParam("comment") String comment
    ){
        return "id:"+id+",name:"+name+",comment:"+comment;
    }

    @PostMapping("/post02")
    public Map<String, String> post02(@RequestParam Map<String, String> map) {
        map.forEach((k, v) -> {
            System.out.println(k + " --> " + v);
        });
        return map;
    }

    @PostMapping("/post03")
    public Comment post03(@RequestBody Comment comment){
        if(Objects.isNull(comment)){
            return null;
        } else {
            return comment;
        }
    }

    @PostMapping ("/post04")
    public Comment[] post04(@RequestBody Comment[] comments){
        return comments;
    }

    @PostMapping ("/post05")
    public List<Comment> post05(@RequestBody List<Comment> comments){
        return comments;
    }

    /**
     * 传递二进制数据
     * */
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            try {
                file.transferTo(new File(baseDir, fileName)); // 对于 SpringBoot 中使用路径还是懵逼！
                return "success";
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "Fail";
    }

    /**
     * 表单数据：文本+二进制
     * */
    @PostMapping("/formWithFile")
    public String formWithFile(
            @RequestParam("id") String id,
            @RequestParam("name") String name,
            @RequestParam("file") MultipartFile file
    ) {
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            try {
                file.transferTo(new File(baseDir, fileName)); // 对于 SpringBoot 中使用路径还是懵逼！
                return "id:"+id+",name:"+name;
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "Fail";
    }

    /**
     * 表单数据：文本+二进制 封装成对象
     * @param user
     * @return
     */
    @PostMapping("/formWithFile2")
    public String formWithFile2(User user) {
        MultipartFile file = user.getFile();
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            try {
                file.transferTo(new File(baseDir, fileName)); // 对于 SpringBoot 中使用路径还是懵逼！
                return "id:"+user.getId()+",name:"+user.getName() + ",fileName:"+ fileName;
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "Fail";
    }

    /**
     * POST以二进制形式传递文件，通常的web表单是做不到的，但是ajax2.0以后是支持的，我们来尝试一下。
     * 注意它和 Multipart的区别，Multipart实际上不只包含文件本身的数据，还有文件的其它的信息，例如刚才获取的文件名。
     * 但是如果以二进制的形式传递，它就是完全的文件数据流，不包含任何其它信息，只有文件本身的二进制数据流。
     *
     * 使用这种形式，只能传输单个文件，无法传输多个文件，因为它只是文件本身的二进制数据，如果是多个的话，
     * 那么谁也别想从一个连续的二进制流中把图片切分出来了。
     * */
    @PostMapping("/binaryFile")
    public String binaryFile(@RequestBody byte[] fileData) {
        try {
            Files.write(Paths.get(baseDir, UUID.randomUUID().toString() + ".jpg"), fileData);
            return "success";
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

}
