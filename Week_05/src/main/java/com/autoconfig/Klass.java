package com.autoconfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yansq
 * @date 2020/11/21
 */
@Component
@Getter
@Setter
public class Klass {

    private List<Student> students;

    public void dong(){
        System.out.println(this.getStudents());
    }
}
