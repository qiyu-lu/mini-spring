package cn.yuqi.mini.springframework.beans.factory.support;

import cn.yuqi.mini.springframework.beans.BeansException;
import cn.yuqi.mini.springframework.beans.factory.config.BeanDefinition;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import java.util.Arrays;

public class CglibSubclassingInstantiationStrategy implements InstantiationStrategy{


    @Override
    public Object instantiate(BeanDefinition beanDefinition) throws BeansException {
        try{
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(beanDefinition.getBeanClass());

            enhancer.setCallback(NoOp.INSTANCE);//方法调用直接走父类，不做任何增强

            Object[] args = beanDefinition.getConstructorArgs();

            if(args == null || args.length == 0){
                System.out.println("CGLIB 无参实例化");
                return enhancer.create();
            }
            System.out.println("CGLIB 有参实例化");
            Class<?>[] paramTypes = Arrays.stream(args)
                    .map(Object::getClass)
                    .toArray(Class<?>[]::new);
            return enhancer.create(paramTypes, args);
        }
        catch (Exception e){
            throw new BeansException("CGLIB 实例化失败", e);
        }
    }
}
