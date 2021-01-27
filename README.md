# Spring 拓展
[![Build Status](https://travis-ci.com/raylax/spring-ext.svg?branch=master)](https://travis-ci.com/raylax/spring-ext)
[![codecov](https://codecov.io/gh/raylax/spring-ext/branch/master/graph/badge.svg?token=jeohIpN2RI)](https://codecov.io/gh/raylax/spring-ext)


# 数据绑定
## 支持url参数使用model绑定进行重映射
- org.inurl.spring.ext.bind.ModelRequestParamProcessor
- org.inurl.spring.ext.bind.ModelRequestParam

```java
// WebMvcConfiguration.java
@Configuration
public class WebMvcConfiguration {

    @Bean
    public ModelRequestParamProcessor modelRequestParamProcessor() {
        return new ModelRequestParamProcessor();
    }
    
}
// TestModel.java
public class TestModel {
    private String a;
    @ModelRequestParam("b")
    private String c;
    @ModelRequestParam(name = "y")
    private int z;
    //... setter and getter
}
// TestController.java
@RestController
public class TestController {
    @GetMapping("/test")
    public TestModel test(TestModel param) {
        return param;
    }
}
// GET /test?a=1&b=2&y=3 
// OUTPUT => { "a": "1", "c": "2", "z": 3 }
```