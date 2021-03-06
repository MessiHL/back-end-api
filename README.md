# 参数传递方式总结

## 1 简介

项目说明：该项目主要是演示get、post请求的传参方式，主要使用postman进行测试

项目地址：https://github.com/MessiHL/back-end-api

参考资料：

​	1- [使用IDEA创建SpringBoot项目](https://blog.csdn.net/lom9357bye/article/details/69677120?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522164888711416780366553017%2522%252C%2522scm%2522%253A%252220140713.130102334.pc%255Fall.%2522%257D&request_id=164888711416780366553017&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~first_rank_ecpm_v1~rank_v31_ecpm-2-69677120.142^v5^pc_search_result_cache,157^v4^control&utm_term=idea+%E6%90%AD%E5%BB%BAspringboot%E9%A1%B9%E7%9B%AE&spm=1018.2226.3001.4187)

​	2- [SpringBoot前后端分离参数传递方式总结](https://blog.csdn.net/qq_40734247/article/details/110151421)

​	3-  [HTTP 菜鸟教程](https://www.runoob.com/http/http-messages.html)

## 2 GET传参

### 2.1 不带参数

```java
    @GetMapping("/get01")
    public String get01(String comment){
        return comment == null ?"no parameter":comment;
    }
```

![](G:\coder\D项目实战\back-end-api\images\get1.png)

### 2.2 单个键值对参数

```java
    @GetMapping("/get02")
    public String get02(@RequestParam("comment") String comment){
        return comment;
    }
```

![](G:\coder\D项目实战\back-end-api\images\get2.png)

### 2.3 多个键值对参数

```java
    @GetMapping("/get03")
    public String get03(
            @RequestParam("id") String id,
            @RequestParam("name") String name,
            @RequestParam("comment") String comment
     ){
        return "id:"+id+",name:"+name+",comment:"+comment;
    }
```

![](G:\coder\D项目实战\back-end-api\images\get3.png)

### 2.4 键值对映射对象

```java
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
```

![](G:\coder\D项目实战\back-end-api\images\get4.png)

### 2.5 键值对映射Map

```java
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
```

![](G:\coder\D项目实战\back-end-api\images\get5.png)

### 2.6 路径参数

```java
    @GetMapping("/get06/{id}")
    public Comment getById(@PathVariable("id") String id){
        Comment comment = new Comment();
        comment.setId(id);
        comment.setName("name_"+id);
        comment.setComment("comment_" + id);
        return comment;
    }
```

![](G:\coder\D项目实战\back-end-api\images\get6.png)

### 2.7 返回值为二进制

```java
    /**
     * 返回值为二进制
     * 其实这里可以使用 Files.readAllBytes()这个方法，这样就简单了。
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
```

![](G:\coder\D项目实战\back-end-api\images\get7.png)



## 3 POST 传参

### 3.1 多个键值对参数

```java
     @PostMapping("/post01")
    public String post01(
            @RequestParam("id") String id,
            @RequestParam("name") String name,
            @RequestParam("comment") String comment
    ){
        return "id:"+id+",name:"+name+",comment:"+comment;
    }
```

![](G:\coder\D项目实战\back-end-api\images\post1.png)

### 3.2 键值对映射Map

```java
     @PostMapping("/post02")
    public Map<String, String> post02(@RequestParam Map<String, String> map) {
        map.forEach((k, v) -> {
            System.out.println(k + " --> " + v);
        });
        return map;
    }
```

![](G:\coder\D项目实战\back-end-api\images\post2.png)

### 3.3 传递Json数据映射对象

```java
    @PostMapping("/post03")
    public Comment post03(@RequestBody Comment comment){
        if(Objects.isNull(comment)){
            return null;
        } else {
            return comment;
        }
    } 
```

![](G:\coder\D项目实战\back-end-api\images\post3.png)

### 3.4 Json数组映射对象数组

```java
    @PostMapping ("/post04")
    public Comment[] post04(@RequestBody Comment[] comments){
        return comments;
    }
```

![](G:\coder\D项目实战\back-end-api\images\post4.png)



### 3.5 json数组映射List

```java
    @PostMapping ("/post05")
    public List<Comment> post05(@RequestBody List<Comment> comments){
        return comments;
    }
```

![](G:\coder\D项目实战\back-end-api\images\post5.png)

### 3.6 传递二进制数据（文件）

```java
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
```

![](G:\coder\D项目实战\back-end-api\images\post6.png)

### 3.7 表单数据（文本+文件）

```java
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
```

![](G:\coder\D项目实战\back-end-api\images\post7.png)



### 3.8 表单数据，进一步封装成对象

```java
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
```

![](G:\coder\D项目实战\back-end-api\images\post8.png)



### 3.9 ajax2.0传递二进制数据

```java
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
```

![](G:\coder\D项目实战\back-end-api\images\post9.png)



## 4 HTTP

### 4.1 HTTP 请求方式

[菜鸟教程](https://www.runoob.com/http/http-methods.html)

HTTP1.0 定义了三种请求方法： GET, POST 和 HEAD 方法。

HTTP1.1 新增了六种请求方法：OPTIONS、PUT、PATCH、DELETE、TRACE 和 CONNECT 方法。

| 序号 | 方法    | 描述                                                         |
| :--- | :------ | :----------------------------------------------------------- |
| 1    | GET     | 请求指定的页面信息，并返回实体主体。                         |
| 2    | HEAD    | 类似于 GET 请求，只不过返回的响应中没有具体的内容，用于获取报头 |
| 3    | POST    | 向指定资源提交数据进行处理请求（例如提交表单或者上传文件）。数据被包含在请求体中。POST 请求可能会导致新的资源的建立和/或已有资源的修改。 |
| 4    | PUT     | 从客户端向服务器传送的数据取代指定的文档的内容。             |
| 5    | DELETE  | 请求服务器删除指定的页面。                                   |
| 6    | CONNECT | HTTP/1.1 协议中预留给能够将连接改为管道方式的代理服务器。    |
| 7    | OPTIONS | 允许客户端查看服务器的性能。                                 |
| 8    | TRACE   | 回显服务器收到的请求，主要用于测试或诊断。                   |
| 9    | PATCH   | 是对 PUT 方法的补充，用来对已知资源进行局部更新 。           |

### 4.2 HTTP 响应头信息

[菜鸟教程](https://www.runoob.com/http/http-header-fields.html)

| 应答头           | 说明                                                         |
| :--------------- | :----------------------------------------------------------- |
| Allow            | 服务器支持哪些请求方法（如GET、POST等）。                    |
| Content-Encoding | 文档的编码（Encode）方法。只有在解码之后才可以得到Content-Type头指定的内容类型。利用gzip压缩文档能够显著地减少HTML文档的下载时间。Java的GZIPOutputStream可以很方便地进行gzip压缩，但只有Unix上的Netscape和Windows上的IE 4、IE 5才支持它。因此，Servlet应该通过查看Accept-Encoding头（即request.getHeader("Accept-Encoding")）检查浏览器是否支持gzip，为支持gzip的浏览器返回经gzip压缩的HTML页面，为其他浏览器返回普通页面。 |
| Content-Length   | 表示内容长度。只有当浏览器使用持久HTTP连接时才需要这个数据。如果你想要利用持久连接的优势，可以把输出文档写入 ByteArrayOutputStream，完成后查看其大小，然后把该值放入Content-Length头，最后通过byteArrayStream.writeTo(response.getOutputStream()发送内容。 |
| Content-Type     | 表示后面的文档属于什么MIME类型。Servlet默认为text/plain，但通常需要显式地指定为text/html。由于经常要设置Content-Type，因此HttpServletResponse提供了一个专用的方法setContentType。 |
| Date             | 当前的GMT时间。你可以用setDateHeader来设置这个头以避免转换时间格式的麻烦。 |
| Expires          | 应该在什么时候认为文档已经过期，从而不再缓存它？             |
| Last-Modified    | 文档的最后改动时间。客户可以通过If-Modified-Since请求头提供一个日期，该请求将被视为一个条件GET，只有改动时间迟于指定时间的文档才会返回，否则返回一个304（Not Modified）状态。Last-Modified也可用setDateHeader方法来设置。 |
| Location         | 表示客户应当到哪里去提取文档。Location通常不是直接设置的，而是通过HttpServletResponse的sendRedirect方法，该方法同时设置状态代码为302。 |
| Refresh          | 表示浏览器应该在多少时间之后刷新文档，以秒计。除了刷新当前文档之外，你还可以通过setHeader("Refresh", "5; URL=http://host/path")让浏览器读取指定的页面。 注意这种功能通常是通过设置HTML页面HEAD区的＜META HTTP-EQUIV="Refresh" CONTENT="5;URL=http://host/path"＞实现，这是因为，自动刷新或重定向对于那些不能使用CGI或Servlet的HTML编写者十分重要。但是，对于Servlet来说，直接设置Refresh头更加方便。  注意Refresh的意义是"N秒之后刷新本页面或访问指定页面"，而不是"每隔N秒刷新本页面或访问指定页面"。因此，连续刷新要求每次都发送一个Refresh头，而发送204状态代码则可以阻止浏览器继续刷新，不管是使用Refresh头还是＜META HTTP-EQUIV="Refresh" ...＞。  注意Refresh头不属于HTTP 1.1正式规范的一部分，而是一个扩展，但Netscape和IE都支持它。 |
| Server           | 服务器名字。Servlet一般不设置这个值，而是由Web服务器自己设置。 |
| Set-Cookie       | 设置和页面关联的Cookie。Servlet不应使用response.setHeader("Set-Cookie", ...)，而是应使用HttpServletResponse提供的专用方法addCookie。参见下文有关Cookie设置的讨论。 |
| WWW-Authenticate | 客户应该在Authorization头中提供什么类型的授权信息？在包含401（Unauthorized）状态行的应答中这个头是必需的。例如，response.setHeader("WWW-Authenticate", "BASIC realm=＼"executives＼"")。 注意Servlet一般不进行这方面的处理，而是让Web服务器的专门机制来控制受密码保护页面的访问（例如.htaccess）。 |

### 4.3 HTTP 状态码

[菜鸟教程](https://www.runoob.com/http/http-status-codes.html)


下面是常见的 HTTP 状态码：

- 200 - 请求成功
- 301 - 资源（网页等）被永久转移到其它URL
- 404 - 请求的资源（网页等）不存在
- 500 - 内部服务器错误

**HTTP 状态码分类**

HTTP 状态码由三个十进制数字组成，第一个十进制数字定义了状态码的类型。响应分为五类：信息响应(100–199)，成功响应(200–299)，重定向(300–399)，客户端错误(400–499)和服务器错误 (500–599)：

| 分类 | 分类描述                                       |
| :--- | :--------------------------------------------- |
| 1**  | 信息，服务器收到请求，需要请求者继续执行操作   |
| 2**  | 成功，操作被成功接收并处理                     |
| 3**  | 重定向，需要进一步的操作以完成请求             |
| 4**  | 客户端错误，请求包含语法错误或无法完成请求     |
| 5**  | 服务器错误，服务器在处理请求的过程中发生了错误 |

**HTTP状态码列表:**

| 状态码 | 状态码英文名称                  | 中文描述                                                     |
| :----- | :------------------------------ | :----------------------------------------------------------- |
| 100    | Continue                        | 继续。客户端应继续其请求                                     |
| 101    | Switching Protocols             | 切换协议。服务器根据客户端的请求切换协议。只能切换到更高级的协议，例如，切换到HTTP的新版本协议 |
|        |                                 |                                                              |
| 200    | OK                              | 请求成功。一般用于GET与POST请求                              |
| 201    | Created                         | 已创建。成功请求并创建了新的资源                             |
| 202    | Accepted                        | 已接受。已经接受请求，但未处理完成                           |
| 203    | Non-Authoritative Information   | 非授权信息。请求成功。但返回的meta信息不在原始的服务器，而是一个副本 |
| 204    | No Content                      | 无内容。服务器成功处理，但未返回内容。在未更新网页的情况下，可确保浏览器继续显示当前文档 |
| 205    | Reset Content                   | 重置内容。服务器处理成功，用户终端（例如：浏览器）应重置文档视图。可通过此返回码清除浏览器的表单域 |
| 206    | Partial Content                 | 部分内容。服务器成功处理了部分GET请求                        |
|        |                                 |                                                              |
| 300    | Multiple Choices                | 多种选择。请求的资源可包括多个位置，相应可返回一个资源特征与地址的列表用于用户终端（例如：浏览器）选择 |
| 301    | Moved Permanently               | 永久移动。请求的资源已被永久的移动到新URI，返回信息会包括新的URI，浏览器会自动定向到新URI。今后任何新的请求都应使用新的URI代替 |
| 302    | Found                           | 临时移动。与301类似。但资源只是临时被移动。客户端应继续使用原有URI |
| 303    | See Other                       | 查看其它地址。与301类似。使用GET和POST请求查看               |
| 304    | Not Modified                    | 未修改。所请求的资源未修改，服务器返回此状态码时，不会返回任何资源。客户端通常会缓存访问过的资源，通过提供一个头信息指出客户端希望只返回在指定日期之后修改的资源 |
| 305    | Use Proxy                       | 使用代理。所请求的资源必须通过代理访问                       |
| 306    | Unused                          | 已经被废弃的HTTP状态码                                       |
| 307    | Temporary Redirect              | 临时重定向。与302类似。使用GET请求重定向                     |
|        |                                 |                                                              |
| 400    | Bad Request                     | 客户端请求的语法错误，服务器无法理解                         |
| 401    | Unauthorized                    | 请求要求用户的身份认证                                       |
| 402    | Payment Required                | 保留，将来使用                                               |
| 403    | Forbidden                       | 服务器理解请求客户端的请求，但是拒绝执行此请求               |
| 404    | Not Found                       | 服务器无法根据客户端的请求找到资源（网页）。通过此代码，网站设计人员可设置"您所请求的资源无法找到"的个性页面 |
| 405    | Method Not Allowed              | 客户端请求中的方法被禁止                                     |
| 406    | Not Acceptable                  | 服务器无法根据客户端请求的内容特性完成请求                   |
| 407    | Proxy Authentication Required   | 请求要求代理的身份认证，与401类似，但请求者应当使用代理进行授权 |
| 408    | Request Time-out                | 服务器等待客户端发送的请求时间过长，超时                     |
| 409    | Conflict                        | 服务器完成客户端的 PUT 请求时可能返回此代码，服务器处理请求时发生了冲突 |
| 410    | Gone                            | 客户端请求的资源已经不存在。410不同于404，如果资源以前有现在被永久删除了可使用410代码，网站设计人员可通过301代码指定资源的新位置 |
| 411    | Length Required                 | 服务器无法处理客户端发送的不带Content-Length的请求信息       |
| 412    | Precondition Failed             | 客户端请求信息的先决条件错误                                 |
| 413    | Request Entity Too Large        | 由于请求的实体过大，服务器无法处理，因此拒绝请求。为防止客户端的连续请求，服务器可能会关闭连接。如果只是服务器暂时无法处理，则会包含一个Retry-After的响应信息 |
| 414    | Request-URI Too Large           | 请求的URI过长（URI通常为网址），服务器无法处理               |
| 415    | Unsupported Media Type          | 服务器无法处理请求附带的媒体格式                             |
| 416    | Requested range not satisfiable | 客户端请求的范围无效                                         |
| 417    | Expectation Failed              | 服务器无法满足Expect的请求头信息                             |
|        |                                 |                                                              |
| 500    | Internal Server Error           | 服务器内部错误，无法完成请求                                 |
| 501    | Not Implemented                 | 服务器不支持请求的功能，无法完成请求                         |
| 502    | Bad Gateway                     | 作为网关或者代理工作的服务器尝试执行请求时，从远程服务器接收到了一个无效的响应 |
| 503    | Service Unavailable             | 由于超载或系统维护，服务器暂时的无法处理客户端的请求。延时的长度可包含在服务器的Retry-After头信息中 |
| 504    | Gateway Time-out                | 充当网关或代理的服务器，未及时从远端服务器获取请求           |
| 505    | HTTP Version not supported      | 服务器不支持请求的HTTP协议的版本，无法完成处理               |

### 4.4 HTTP content-type

[菜鸟教程](https://www.runoob.com/http/http-content-type.html)

**常见的媒体格式类型如下：**

- text/html ： HTML格式
- text/plain ：纯文本格式
- text/xml ： XML格式
- image/gif ：gif图片格式
- image/jpeg ：jpg图片格式
- image/png：png图片格式

**以application开头的媒体格式类型：**

- application/xhtml+xml ：XHTML格式
- application/xml： XML数据格式
- application/atom+xml ：Atom XML聚合格式
- application/json： JSON数据格式
- application/pdf：pdf格式
- application/msword ： Word文档格式
- application/octet-stream ： 二进制流数据（如常见的文件下载）
- application/x-www-form-urlencoded ： <form encType=””>中默认的encType，form表单数据被编码为key/value格式发送到服务器（表单默认的提交数据的格式）

**另外一种常见的媒体格式是上传文件之时使用的：**

- multipart/form-data ： 需要在表单中进行文件上传时，就需要使用该格式
