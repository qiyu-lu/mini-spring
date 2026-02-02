package cn.yuqi.mini.springframework.service;

/**
 * @author derekyi
 * @date 2020/11/22
 */
public class HelloService {
    public HelloService() {}
    public HelloService(String name){
        System.out.println("HelloService 类的有参构造+" + name);
    }
	public String sayHello() {
		System.out.println("hello");
		return "hello";
	}
}
