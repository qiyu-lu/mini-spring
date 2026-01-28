 # IOC
 ## 最简单的bean容器
 > 分支：step-01-simple-bean-container

定义一个简单的bean容器BeanFactory，内部包含一个map用以保存bean，只有注册bean和获取bean两个方法

测试：
```
public class SimpleBeanContainerTest {

	@Test
	public void testGetBean() throws Exception {
		BeanFactory beanFactory = new BeanFactory();
		beanFactory.registerBean("helloService", new HelloService());
		HelloService helloService = (HelloService) beanFactory.getBean("helloService");
		assertThat(helloService).isNotNull();
		assertThat(helloService.sayHello()).isEqualTo("hello");
	}

	class HelloService {
		public String sayHello() {
			System.out.println("hello");
			return "hello";
		}
	}
}
```

```
public interface BeanFactory {
    Object getBean(String name);
}
public class SimpleBeanFactory implements BeanFactory {
    private  final Map<String, Object> beanMap = new HashMap<String, Object>();

    public SimpleBeanFactory(){}
    public void registerBean(String name, Object bean){
        beanMap.put(name, bean);
    }

    public Object getBean(String name){
        return beanMap.get(name);
    }
}
```
上面的这个的不足之处：在注册时对象就new出来了，这会带来以下问题：
1. 无法控制创建时机
2. 无法统一管理构造方式
3. 无法进行扩展：例如依赖注入、AOP、生命周期回调等

而真正的spring不直接注册对象。


## BeanDefinition和BeanDefinitionRegistry
> 分支：bean-definition-and-bean-definition-registry
> **实现一个“最小可用的 IOC 容器核心”**

那么现在需要实现的是：我想统一管理对象的创建，而不是到处 new，难点在于 "不要提前 new 对象，只保存‘怎么 new'"

类名.class 是 Java 的类字面量（class literal），用来在编译期就拿到某个类的 Class 对象，而不需要先 new 出实例。
之前在测试中是使用的 `new HelloService()` 那么创建时就new了对象，而现在可以使用 `HelloService.class`，可以告诉容器，之后如果有人使用时使用这个class来系统管理的进行new。

这个容器要满足：

1. **不提前创建对象**
2. **根据定义创建对象**
3. **默认单例**
4. **统一由容器控制创建过程**

我们需要有以下几部分：
```text
BeanDefinition          —— Bean 的“说明书”
BeanDefinitionRegistry  —— 存说明书的地方
SingletonBeanRegistry   —— 存已经创建好的单例 Bean
BeanFactory             —— 对外提供 getBean 能力
DefaultListableBeanFactory —— 真正的容器（核心）
```



### 1️⃣ BeanDefinition（Bean 的定义信息）

描述 **“如何创建一个 Bean”**，但**不创建**，

必须包含的信息（当前章节）

* Bean 的 `Class` 类型

对外能力

* 提供 Bean 的类型信息

明确不负责

* ❌ 不创建对象
* ❌ 不缓存对象
* ❌ 不关心单例 / 多例

设计目的

> **把“怎么创建”和“什么时候创建”分离**

---

### 2️⃣ BeanDefinitionRegistry（定义注册表）

职责

> 负责 **管理 BeanDefinition**

提供能力

* 注册 BeanDefinition（name → definition）
* 根据 name 获取 BeanDefinition
* 判断是否存在某个 BeanDefinition

明确不负责

* ❌ 不创建 Bean
* ❌ 不缓存 Bean 实例

---

### 3️⃣ SingletonBeanRegistry（单例注册表）

职责

> 负责 **管理已经创建完成的单例 Bean 实例**

提供能力

* 注册一个单例对象
* 根据 name 获取单例对象
* 判断单例是否存在

明确不负责

* ❌ 不知道 Bean 是怎么创建的
* ❌ 不知道 BeanDefinition 是什么

---

### 4️⃣ BeanFactory（对外统一入口）

职责

> 向外界提供 **获取 Bean 的统一方式**

提供能力

* 根据 beanName 获取 Bean 实例

行为约定

* 如果 Bean 未创建，负责触发创建流程
* 返回的必须是完整可用的 Bean

明确不负责

* ❌ 不保存 BeanDefinition
* ❌ 不决定存储结构

> 👉 BeanFactory 是 **“使用者看到的唯一接口”**

---

### 5️⃣ DefaultListableBeanFactory（真正的容器）

> **这是本章节的核心**

身份

* BeanFactory
* BeanDefinitionRegistry
* SingletonBeanRegistry

职责

> **协调三个东西：**
>
> * BeanDefinition
> * Bean 实例
> * 创建时机

---

对外能力

* 注册 BeanDefinition
* 获取 Bean

---

#### 内部必须完成的流程（getBean 的行为规范）

getBean(name) 的职责描述

```text
1. 检查单例注册表
   - 如果存在：直接返回

2. 获取 BeanDefinition
   - 如果不存在：抛出异常

3. 根据 BeanDefinition 创建 Bean 实例
   - 使用反射
   - 使用无参构造

4. 将创建好的 Bean 注册为单例

5. 返回 Bean 实例
```

---

### 模块之间的协作关系（重点）

```text
使用者
  ↓
BeanFactory.getBean()
  ↓
DefaultListableBeanFactory
  ↓
┌───────────────────────────────┐
│ 1. SingletonBeanRegistry       │
│ 2. BeanDefinitionRegistry      │
│ 3. Bean 实例创建逻辑           │
└───────────────────────────────┘
```

### 实现代码

```java
public class BeanDefinition {
    private Class<?> beanClass;
    public BeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
    }
    public Class<?> getBeanClass(){
        return this.beanClass;
    }
}
```

```java
public interface BeanDefinitionRegistry {
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
    BeanDefinition getBeanDefinition(String beanName);
    boolean containsBeanDefinition(String beanName);
}
```

```java
public interface BeanFactory {
    Object getBean(String beanName);
}
```

```java
public interface SingletonBeanRegistry {
    void registerSingleton(String beanName, Object bean);
    Object getSingleton(String beanName);
    boolean containsSingleton(String beanName);
}
```

```java
public class DefaultListableBeanFactory
        implements
        BeanDefinitionRegistry,
        SingletonBeanRegistry,
        BeanFactory{

    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<String, BeanDefinition>();
    //单例池
    private Map<String, Object> singleMap = new HashMap<>();



    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition){
        beanDefinitionMap.put(beanName, beanDefinition);
    }
    public BeanDefinition getBeanDefinition(String beanName){
        return beanDefinitionMap.get(beanName);
    }
    public boolean containsBeanDefinition(String beanName){
        return beanDefinitionMap.containsKey(beanName);
    }
    public void registerSingleton(String beanName, Object bean){
        singleMap.put(beanName, bean);
    }
    public Object getSingleton(String beanName){
        return singleMap.get(beanName);
    }
    public boolean containsSingleton(String beanName){
        return singleMap.containsKey(beanName);
    }

    public Object getBean(String beanName) {
        // 1. 先从单例池取
        if (containsSingleton(beanName)) {
            System.out.println("单例池中有："+beanName);
            return getSingleton(beanName);
        }

        // 2. 再从 BeanDefinition 创建
        BeanDefinition beanDefinition = getBeanDefinition(beanName);
        if (beanDefinition != null) {
            try {
                Object bean = beanDefinition.getBeanClass()
                        .getDeclaredConstructor()
                        .newInstance();
                registerSingleton(beanName, bean);
                return bean;
            } catch (Exception e) {
                throw new RuntimeException("无法实例化 Bean: " + beanName, e);
            }
        }

        // 3. 都不存在 → 明确失败
        throw new RuntimeException("No such bean: " + beanName);
    }
}
```

### 优化
![](./assets/bean-definition-and-bean-definition-registry.png)

```text
BeanDefinition
—— Bean 的定义模型，描述“这个 Bean 是什么、该如何创建”

BeanDefinitionRegistry
—— BeanDefinition 的管理者，负责注册和获取 Bean 的定义信息

SingletonBeanRegistry
—— 单例 Bean 的缓存中心，负责存取已经创建完成的 Bean 实例

BeanFactory
—— 对外统一入口，只暴露 getBean 能力，不关心内部细节

AbstractBeanFactory
—— 定义 getBean 的统一流程模板，协调“查缓存 + 创建 Bean”

AbstractAutowireCapableBeanFactory
—— 负责 Bean 的具体创建过程，封装实例化与后续扩展点

DefaultListableBeanFactory
—— 最终可用的容器实现，整合定义管理与 Bean 创建能力
```

出现的bug：如果测试类中使用的是测试类的内部类就会发生报错，原因如下：

- Bean 是一个「测试类的内部类」,HelloService 不是 public ,它的访问级别是 package-private
- 创建 Bean 的代码在「另一个 package」,一个在 `cn.yuqi.mini.spring.beans.factory.support` ，而另一个在 `cn.yuqi.mini.spring.beans.factory`
- 使用的是反射创建对象:beanClass.getDeclaredConstructor().newInstance();反射 必须遵守 Java 的访问控制规则。
