package cn.edu.xmu.javaee.productdemoaop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import cn.edu.xmu.javaee.core.jpa.SelectiveUpdateJpaRepositoryImpl;

@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.javaee.core",
		"cn.edu.xmu.javaee.productdemoaop"})
@MapperScan("cn.edu.xmu.javaee.productdemoaop.mapper")
@EnableJpaRepositories(value = "cn.edu.xmu.javaee.core.jpa", repositoryBaseClass = SelectiveUpdateJpaRepositoryImpl.class,basePackages = "cn.edu.xmu.javaee.productdemoaop.mapperjpa")
@EntityScan("cn.edu.xmu.javaee.productdemoaop.mapper.generator.po") // 指定实体扫描路径

public class ProductDemoAOPApplication {


	public static void main(String[] args) {
		SpringApplication.run(ProductDemoAOPApplication.class, args);
	}

}
