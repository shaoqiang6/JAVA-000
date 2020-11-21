package com.autoconfig;

import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author yansq
 * @date 2020/11/21
 */
@Component
@Getter
public class School {

    private List<Klass> classes;
    @PostConstruct
    public void init() {
        System.out.println("school init ...");
    }
}
