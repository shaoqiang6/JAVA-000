package com.autoconfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * @author yansq
 * @date 2020/11/18
 */
@Component
@Setter
@Getter
public class Student {
    private String name;
    private Integer age;

    public Student() {
        System.out.println("student ...." + toString());
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
