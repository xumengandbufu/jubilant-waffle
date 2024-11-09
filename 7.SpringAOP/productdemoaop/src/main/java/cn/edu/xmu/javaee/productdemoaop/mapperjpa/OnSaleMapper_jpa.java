package cn.edu.xmu.javaee.productdemoaop.mapperjpa;

import cn.edu.xmu.javaee.productdemoaop.mapper.generator.po.OnSalePo;
import cn.edu.xmu.javaee.productdemoaop.mapper.generator.po.ProductPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OnSaleMapper_jpa extends JpaRepository<OnSalePo, Long> {  //<>方括号里的参数是指定表的类和主键
    List<OnSalePo> findByproductId(Long productId);
}
