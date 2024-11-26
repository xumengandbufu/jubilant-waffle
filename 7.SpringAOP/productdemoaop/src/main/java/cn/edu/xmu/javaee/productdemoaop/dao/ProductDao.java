//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.javaee.productdemoaop.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.productdemoaop.dao.bo.OnSale;
import cn.edu.xmu.javaee.productdemoaop.dao.bo.Product;
import cn.edu.xmu.javaee.productdemoaop.dao.bo.User;
import cn.edu.xmu.javaee.productdemoaop.mapper.generator.po.OnSalePo;
import cn.edu.xmu.javaee.productdemoaop.mapperjpa.OnSaleMapper_jpa;
import cn.edu.xmu.javaee.productdemoaop.mapperjpa.ProductByNameMapper_jpa;
import cn.edu.xmu.javaee.productdemoaop.mapper.generator.ProductPoMapper;
import cn.edu.xmu.javaee.productdemoaop.mapper.generator.po.ProductPo;
import cn.edu.xmu.javaee.productdemoaop.mapper.generator.po.ProductPoExample;
import cn.edu.xmu.javaee.productdemoaop.mapper.manual.ProductAllMapper;
import cn.edu.xmu.javaee.productdemoaop.mapper.manual.po.ProductAllPo;
import cn.edu.xmu.javaee.productdemoaop.mapperjpa.po.ProductJPAPo;
import cn.edu.xmu.javaee.productdemoaop.util.CloneFactory;
import cn.edu.xmu.javaee.productdemoaop.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Ming Qiu
 **/

@Service
@Repository
public class ProductDao {
    private final static String KEY = "P%d";
    private final static String OTHER_KEY = "PO%d";

    private int timeout;
    private final ProductPoMapper productPoMapper;
    private RedisUtil redisUtil;

    private final static Logger logger = LoggerFactory.getLogger(ProductDao.class);

    private OnSaleDao onSaleDao;

    private ProductAllMapper productAllMapper;

    ProductByNameMapper_jpa productByNameMapper;
    OnSaleMapper_jpa onSaleMapper;

    @Autowired
    public ProductDao(OnSaleMapper_jpa onSaleMapper, ProductByNameMapper_jpa productByNameMapper, ProductPoMapper productPoMapper, RedisUtil redisUtil, OnSaleDao onSaleDao, ProductAllMapper productAllMapper) {
        this.productPoMapper = productPoMapper;
        this.redisUtil = redisUtil;
        this.onSaleDao = onSaleDao;
        this.productAllMapper = productAllMapper;
        this.productByNameMapper = productByNameMapper;
        this.onSaleMapper = onSaleMapper;
    }

    /**
     * 用GoodsPo对象找Goods对象
     * @param name
     * @return  Goods对象列表，带关联的Product返回
     */
    public List<Product> retrieveProductByName(String name, boolean all) throws BusinessException {
        List<Product> productList = new ArrayList<>();
        ProductPoExample example = new ProductPoExample();
        ProductPoExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(name);
        List<ProductPo> productPoList = productPoMapper.selectByExample(example);
        for (ProductPo po : productPoList){
            Product product = null;
            if (all) {
                product = this.retrieveFullProduct(po);
            } else {
                product = CloneFactory.copy(new Product(), po);
            }
            productList.add(product);
        }
        logger.debug("retrieveProductByName: productList = {}", productList);
        return productList;
    }

    /**
     * 用GoodsPo对象找Goods对象
     * @param  productId
     * @return  Goods对象列表，带关联的Product返回
     */
    public Product retrieveProductByID(Long productId, boolean all) throws BusinessException {
        Product product = null;
        ProductPo productPo = productPoMapper.selectByPrimaryKey(productId);
        if (null == productPo){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, "产品id不存在");
        }
        if (all) {
            product = this.retrieveFullProduct(productPo);
        } else {
            product = CloneFactory.copy(new Product(), productPo);
        }

        logger.debug("retrieveProductByID: product = {}",  product);
        return product;
    }


    private Product retrieveFullProduct(ProductPo productPo) throws DataAccessException{
        assert productPo != null;
        Product product =  CloneFactory.copy(new Product(), productPo);
        List<OnSale> latestOnSale = onSaleDao.getLatestOnSale(productPo.getId());
        product.setOnSaleList(latestOnSale);

        List<Product> otherProduct = this.retrieveOtherProduct(productPo);
        product.setOtherProduct(otherProduct);

        return product;
    }

    private List<Product> retrieveOtherProduct(ProductPo productPo) throws DataAccessException{
        assert productPo != null;

        ProductPoExample example = new ProductPoExample();
        ProductPoExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdEqualTo(productPo.getGoodsId());
        criteria.andIdNotEqualTo(productPo.getId());
        List<ProductPo> productPoList = productPoMapper.selectByExample(example);
        return productPoList.stream().map(po->CloneFactory.copy(new Product(), po)).collect(Collectors.toList());
    }

    /**
     * 创建Goods对象
     * @param product 传入的Goods对象
     * @return 返回对象ReturnObj
     */
    public Product createProduct(Product product, User user) throws BusinessException{

        Product retObj = null;
        product.setCreator(user);
        product.setGmtCreate(LocalDateTime.now());
        ProductPo po = CloneFactory.copy(new ProductPo(), product);
        int ret = productPoMapper.insertSelective(po);
        retObj = CloneFactory.copy(new Product(), po);
        return retObj;
    }

    /**
     * 修改商品信息
     * @param product 传入的product对象
     * @return void
     */
    public void modiProduct(Product product, User user) throws BusinessException{
        product.setGmtModified(LocalDateTime.now());
        product.setModifier(user);
        ProductPo po = CloneFactory.copy(new ProductPo(), product);
        int ret = productPoMapper.updateByPrimaryKeySelective(po);
        if (ret == 0 ){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST);
        }
    }

    /**
     * 删除商品，连带规格
     * @param id 商品id
     * @return
     */
    public void deleteProduct(Long id) throws BusinessException{
        int ret = productPoMapper.deleteByPrimaryKey(id);
        if (ret == 0) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST);
        }
    }

    public List<Product> findProductByName_manual(String name) throws BusinessException {
        List<Product> productList;
        ProductPoExample example = new ProductPoExample();
        ProductPoExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(name);
        List<ProductAllPo> productPoList = productAllMapper.getProductWithAll(example);
        productList =  productPoList.stream().map(o->CloneFactory.copy(new Product(), o)).collect(Collectors.toList());
        logger.debug("findProductByName_manual: productList = {}", productList);
        return productList;
    }

    /**
     * 用GoodsPo对象找Goods对象
     * @param  productId
     * @return  Goods对象列表，带关联的Product返回
     */
    public Product findProductByID_manual(Long productId) throws BusinessException {
        Product product = null;
        ProductPoExample example = new ProductPoExample();
        ProductPoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(productId);
        List<ProductAllPo> productPoList = productAllMapper.getProductWithAll(example);

        if (productPoList.size() == 0){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, "产品id不存在");
        }
        product = CloneFactory.copy(new Product(), productPoList.get(0));
        logger.debug("findProductByID_manual: product = {}", product);
        return product;
    }

    public Product findProductById_Join(Long id) throws BusinessException {
        Product product = null;
        List<ProductAllPo> pap = productAllMapper.getProductJOINAll(id);
        if (pap.size() == 0){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, "产品id不存在");
        }
        product = CloneFactory.copy(new Product(), pap.get(0));
        logger.debug("findProductByID_manual: product = {}", product);
        return product;
    }

    public List<Product> findProductByName_Join(String name) throws BusinessException{
        List<Product> productList;
        List<ProductAllPo> productPoList = productAllMapper.selectByJoinExample(name);
        productList =  productPoList.stream().map(o->CloneFactory.copy(new Product(), o)).collect(Collectors.toList());
        logger.debug("findProductByName_join: productList = {}", productList);
        return productList;
    }

    public List<Product> findProductByName_JPA(String name) throws BusinessException{
        List<Product> productList = new ArrayList<>();
        List<ProductPo> productAllList = productByNameMapper.findByName(name);
        for(ProductPo productPo : productAllList){
            List<ProductPo> otherproducts = productByNameMapper.findByGoodsId(productPo.getGoodsId());
            List<Product> otherproductList = otherproducts.stream().map(o->CloneFactory.copy(new Product(), o)).collect(Collectors.toList());
            Product product =  CloneFactory.copy(new Product(), productPo);
            List<OnSalePo> onSalePos = onSaleMapper.findByproductId(product.getId());
            List<OnSale> onSaleList = onSalePos.stream().map(po-> CloneFactory.copy(new OnSale(), po)).collect(Collectors.toList());
            product.setOtherProduct(otherproductList);
            product.setOnSaleList(onSaleList);
            productList.add(product);
        }
        logger.debug("findProductByName_join: productList = {}", productList);
        return productList;
    }

    @Cacheable(value = "users", key = "#id")

    public Product findProductById_redis(Long id) throws RuntimeException {
        logger.debug("findValidById: id = {}", id);
        String key = String.format(KEY, id);
        Product bo;
        if (this.redisUtil.hasKey(key)) {
            bo = (Product) this.redisUtil.get(key);

        }else {
            Optional<String> redisKey=Optional.of(key);
            bo = this.findBo(id);
            redisKey.ifPresent(key1 -> redisUtil.set(key1, (Serializable) bo, timeout));
        }
        return bo;
    }
    private Product findBo(Long productId) {
        Optional<ProductPo> ret = this.productByNameMapper.findById(productId);
        if (ret.isPresent()) {
            ProductPo po = ret.get();
            String key = String.format(KEY, productId);
            Product bo = CloneFactory.copy(new Product(), po);
            return bo;
        } else {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "产品", productId));
        }
    }
}
