package cn.edu.xmu.javaee.productdemoaop.mapperjpa;

import cn.edu.xmu.javaee.productdemoaop.mapper.generator.po.ProductPo;
import cn.edu.xmu.javaee.productdemoaop.mapper.manual.po.ProductAllPo;
import cn.edu.xmu.javaee.productdemoaop.mapperjpa.po.ProductJPAPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductByNameMapper_jpa extends JpaRepository<ProductPo, Long>{
    List<ProductPo> findByName(String name);
    List<ProductPo> findByGoodsId(Long goodsId);
}
