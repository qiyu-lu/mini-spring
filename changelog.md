 # IOC
 ## 最简单的bean容器-代码分支：step-01-simple-bean-container

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


## BeanDefinition和BeanDefinitionRegistry-代码分支：step-02-bean-definition-and-bean-definition-registry
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
—— BeanDefinition 的管理者，负责注册和获取 Bean 的定义信息，即BeanDefinition

SingletonBeanRegistry
—— 单例 Bean 的缓存中心，负责存取已经创建完成的 Bean 实例

DefaultSingletonBeanRegistry
—— 实现 SingletonBeanRegistry ，对bean进行注册、获取。

BeanFactory
—— 对外统一入口，只暴露 getBean 能力，不关心内部细节

AbstractBeanFactory
—— 实现 BeanFactory ，继承 DefaultSingletonBeanRegistry ，
定义 getBean 的统一流程模板，协调“查缓存 + 创建 Bean”，但是不是完全实现， 
getBean 的实现 需要其继承父类 DefaultSingletonBeanRegistry 所实现的对bean的注册获取查询相关方法，
先查询 bean 单例池中是否有 对应 beanName 名字的bean，如果有，则从单例池中获取，
这些依赖于父类DefaultSingletonBeanRegistry的相关方法的实现；
如果没有则 需要根据 BeanDefinition 创建 Bean 实例，而这里不是在这个类中实现的，仅仅写了抽象方法，
进行了职责分离。
这个类只负责对于 getBean 方法中如果单例池中有的情况的逻辑的实现。

AbstractAutowireCapableBeanFactory
—— 负责 Bean 的具体创建过程，封装实例化与后续扩展点，继承 AbstractBeanFactory，
实现了 其父类中没有实现的 根据 BeanDefinition 创建 Bean 实例 的相关逻辑，
传入  beanName和beanDefinition进行实例化，然后将这个name和实例化后的bean存入单例池中，
这个是它继承的AbstractBeanFactory 继承的DefaultSingletonBeanRegistry的方法，将bean进行注册


DefaultListableBeanFactory
—— 最终可用的容器实现，整合定义管理与 Bean 创建能力，继承 AbstractAutowireCapableBeanFactory 和 
实现 BeanDefinitionRegistry接口的注册BeanDefinition的方法，
实现了AbstractBeanFactory的另一个用于 getBean 方法的抽象方法：getBeanDefinition，
在 BeanDefinition 池中获取 对应的BeanDefinition，因为 BeanDefinition池在这个类中进行创建。
```

```text
BeanDefinition
—— Bean 的定义模型，用于描述一个 Bean 是什么，以及该如何被创建。

BeanDefinitionRegistry
—— BeanDefinition 的注册与管理中心，负责按 beanName 存取 BeanDefinition。

SingletonBeanRegistry
—— 单例 Bean 的缓存抽象，定义单例 Bean 的注册与获取能力。

DefaultSingletonBeanRegistry
—— SingletonBeanRegistry 的默认实现，维护单例池，完成单例 Bean 的实际存取。

BeanFactory
—— 容器对外的统一入口，仅暴露 getBean 能力，屏蔽内部实现细节。

AbstractBeanFactory
—— BeanFactory 的抽象实现，继承 DefaultSingletonBeanRegistry，
定义 getBean 的标准流程：
优先从单例池中获取 Bean；若不存在，则委托子类根据 BeanDefinition 创建 Bean。
该类本身不关心 Bean 如何创建，只负责流程控制与职责拆分。

AbstractAutowireCapableBeanFactory
—— 负责 Bean 的具体创建过程，继承 AbstractBeanFactory，
实现根据 BeanDefinition 实例化 Bean 的逻辑，
并在创建完成后将 Bean 注册到单例池中。

DefaultListableBeanFactory
—— 最终可用的容器实现，
在继承 AbstractAutowireCapableBeanFactory 的同时实现 BeanDefinitionRegistry，
负责维护 BeanDefinition 池，
并提供 AbstractBeanFactory 所需的 getBeanDefinition 能力，
从而完整地打通 BeanDefinition 管理与 Bean 创建流程。
```

出现的bug：如果测试类中使用的是测试类的内部类就会发生报错，原因如下：

- Bean 是一个「测试类的内部类」,HelloService 不是 public ,它的访问级别是 package-private
- 创建 Bean 的代码在「另一个 package」,一个在 `cn.yuqi.mini.spring.beans.factory.support` ，而另一个在 `cn.yuqi.mini.spring.beans.factory`
- 使用的是反射创建对象:beanClass.getDeclaredConstructor().newInstance();反射 必须遵守 Java 的访问控制规则。

## Bean实例化策略InstantiationStrategy-代码分支：step-03-instantiation-strategy

现在bean是在AbstractAutowireCapableBeanFactory.doCreateBean方法中用beanClass.newInstance()来实例化，仅适用于bean有无参构造函数的情况。
`bean = beanClass.getDeclaredConstructor().newInstance();` getDeclaredConstructor()：只查找 参数列表为空 的构造方法,

![](./assets/instantiation-strategy.png)

针对bean的实例化，抽象出一个实例化策略的接口InstantiationStrategy，有两个实现类：
- SimpleInstantiationStrategy，使用bean的构造函数来实例化
- CglibSubclassingInstantiationStrategy，使用CGLIB动态生成子类

SimpleInstantiationStrategy 中使用构造函数进行实例化，那么就需要存储构造函数的参数，需要修改BeanDefinition，添加参数

### bug
在 中 cglib 实例化会抛出异常：
```java
        Object cglibBean = cglibInstantiationStrategy.instantiate(beanDefinition);
```
原因：
CGLIB 在干的是：
1. 生成 HelloService 的子类字节码
2. 加载这个子类
3. 通过 Enhancer.create() 调用父类构造器

CGLIB 实例化代码中加入 `enhancer.setCallback(NoOp.INSTANCE);` 

反射实例化
- ✔ 不生成子类
- ✔ 直接 new
- ❌ 无法拦截方法
- ❌ 无法做代理 / AOP

CGLIB 实例化
- ✔ 生成子类
- ✔ 可拦截方法（AOP 核心）
- ❌ 构造器必须可调用
- ❌ 不能是 final 类 / final 方法

👉 CGLIB 不是为了“方便 new”存在的
👉 它是为了“拦截方法”存在的

## 为bean填充属性-代码分支：step-04-populate-bean-with-property-values

在BeanDefinition中增加和bean属性对应的PropertyVales，实例化bean之后，为bean填充属性(AbstractAutowireCapableBeanFactory#applyPropertyValues)。

PropertyValue 表示 Bean 的“一个属性的赋值描述”
PropertyValues 表示 Bean 的“全部属性赋值描述的集合”

为什么要这样设计，而不是采用map，例如 `Map<String, Object>` ，
但这样不能自然地表达： 
这是字段注入还是 setter 注入？
这是字面量还是 Bean 引用？ 这个值是否需要类型转换？ 是否已完成注入？ 注入顺序是否重要？

应该按这个固定顺序来实现
1. instantiateBean   👉 调构造方法（new一个对象）
2. populateBean      👉 setXxx / 反射字段赋值
3. initializeBean    👉 aware / init-method

测试代码
```java
@Test
public void testPopulateBeanWithPropertyValues() throws Exception {
    DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
    PropertyValues  propertyValues = new PropertyValues();
    propertyValues.addPropertyValue(new PropertyValue("name", "小明"));
    propertyValues.addPropertyValue(new PropertyValue("age", 18));
    BeanDefinition beanDefinition = new BeanDefinition(Person.class);
    beanDefinition.setPropertyValues(propertyValues);

    beanFactory.registerBeanDefinition("person", beanDefinition);

    Person person = (Person) beanFactory.getBean("person");
    System.out.println(person);
    assertThat(person.getName()).isEqualTo("小明");
    assertThat(person.getAge()).isEqualTo(18);

}
```

在 AbstractAutowireCapableBeanFactory#applyPropertyValues 中的修改代码，在实例化之后进行填充。
```java
@Override
protected Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException{
    Object bean = doCreateBean(beanName, beanDefinition);
    applyPropertyValues(bean, beanDefinition);
    return bean;
}
protected void applyPropertyValues(Object bean, BeanDefinition beanDefinition) throws BeansException{
    PropertyValues  pvs = beanDefinition.getPropertyValues();
    if(pvs == null) return; //无参构造或者是采用默认值，不进行填充

    for(PropertyValue pv : pvs.getPropertyValueList()){
        String name = pv.getPropertyName();
        Object value = pv.getPropertyValue();

        try{
            Field field = bean.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(bean, value);
        } catch (Exception e){
            throw new BeansException("failed to set property " + name, e);
        }
    }
}
```

## 为bean注入bean-代码分支step-05-populate-bean-with-bean

增加BeanReference类，包装一个bean对另一个bean的引用。实例化beanA后填充属性时，若PropertyValue#value为BeanReference，引用beanB，则先去实例化beanB。 
由于不想增加代码的复杂度提高理解难度，暂时不支持循环依赖，后面会在高级篇中解决该问题。

在上一章 populate-bean-with-property-values 中实现的本质是： 值注入。
但在实际代码中的对象往往会出现另一个对象，即容器里“另一个 Bean”，

如果没有 BeanReference 时，会遇到的根本性困境：
若是写：
```java
propertyValues.addPropertyValue(
new PropertyValue("car", new Car())
);
```
这会带来三个致命问题：
1. 这个 Car 不受容器管理

    Person.car  ≠  IOC 容器中的 car Bean ，你会得到：两个 Car 实例，生命周期完全脱钩，无法统一管理

2. Bean 的定义顺序变得混乱

    Person -> Car 但： Car 什么时候创建？ 谁来创建？ 创建一次还是多次？ 这样写已经失去了“容器”的控制权

3. 你无法表达“依赖关系” 

    在 Spring 里： Person depends on Car，这是元数据层面的关系，而不是“代码层面的 new”。 如果你直接 new：容器根本不知道 Person 依赖 Car


```java
protected void applyPropertyValues(Object bean, BeanDefinition beanDefinition) throws BeansException{
    PropertyValues  pvs = beanDefinition.getPropertyValues();
    if(pvs == null) return; //无参构造或者是采用默认值，不进行填充

    for(PropertyValue pv : pvs.getPropertyValueList()){
        String name = pv.getPropertyName();
        Object value = pv.getPropertyValue();

        try{
            if(value instanceof BeanReference){
                value = getBean(((BeanReference) value).getBeanName());
            }
            Field field = bean.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(bean, value);
        } catch (Exception e){
            throw new BeansException("failed to set property " + name, e);
        }
    }
}
```
测试文件
```java
@Test
public void testPopulateBeanWithBean() throws Exception {
    DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

    //注册Car实例
    PropertyValues propertyValuesForCar = new PropertyValues();
    propertyValuesForCar.addPropertyValue(new PropertyValue("brand", "porsche"));
    BeanDefinition carBeanDefinition = new BeanDefinition(Car.class);
    carBeanDefinition.setPropertyValues(propertyValuesForCar);
    beanFactory.registerBeanDefinition("car", carBeanDefinition);

    //注册Person实例
    PropertyValues propertyValuesForPerson = new PropertyValues();
    propertyValuesForPerson.addPropertyValue(new PropertyValue("name", "derek"));
    propertyValuesForPerson.addPropertyValue(new PropertyValue("age", 18));
    //Person实例依赖Car实例
    propertyValuesForPerson.addPropertyValue(new PropertyValue("car", new BeanReference("car")));
    BeanDefinition beanDefinition = new BeanDefinition(Person.class);
    beanDefinition.setPropertyValues(propertyValuesForPerson);
    beanFactory.registerBeanDefinition("person", beanDefinition);

    Person person = (Person) beanFactory.getBean("person");
    System.out.println(person);
    assertThat(person.getName()).isEqualTo("derek");
    assertThat(person.getAge()).isEqualTo(18);
    Car car = person.getCar();
    assertThat(car).isNotNull();
    assertThat(car.getBrand()).isEqualTo("porsche");
}
```

## 资源和资源加载器-代码分支 step-06-resource-and-resource-loader