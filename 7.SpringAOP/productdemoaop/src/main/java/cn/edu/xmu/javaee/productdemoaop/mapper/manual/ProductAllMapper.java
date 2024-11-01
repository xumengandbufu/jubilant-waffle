//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.javaee.productdemoaop.mapper.manual;

import cn.edu.xmu.javaee.productdemoaop.dao.bo.Product;
import cn.edu.xmu.javaee.productdemoaop.dao.bo.User;
import cn.edu.xmu.javaee.productdemoaop.mapper.generator.ProductPoSqlProvider;
import cn.edu.xmu.javaee.productdemoaop.mapper.generator.po.OnSalePo;
import cn.edu.xmu.javaee.productdemoaop.mapper.generator.po.ProductPo;
import cn.edu.xmu.javaee.productdemoaop.mapper.generator.po.ProductPoExample;
import cn.edu.xmu.javaee.productdemoaop.mapper.manual.po.ProductAllPo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

import java.util.List;

@Mapper
public interface ProductAllMapper {

    @SelectProvider(type=ProductPoSqlProvider.class, method="selectByExample")
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="sku_sn", property="skuSn", jdbcType=JdbcType.VARCHAR),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="original_price", property="originalPrice", jdbcType=JdbcType.BIGINT),
            @Result(column="weight", property="weight", jdbcType=JdbcType.BIGINT),
            @Result(column="barcode", property="barcode", jdbcType=JdbcType.VARCHAR),
            @Result(column="unit", property="unit", jdbcType=JdbcType.VARCHAR),
            @Result(column="origin_place", property="originPlace", jdbcType=JdbcType.VARCHAR),
            @Result(column="commission_ratio", property="commissionRatio", jdbcType=JdbcType.INTEGER),
            @Result(column="free_threshold", property="freeThreshold", jdbcType=JdbcType.BIGINT),
            @Result(column="status", property="status", jdbcType=JdbcType.SMALLINT),
            @Result(column="creator_id", property="creatorId", jdbcType=JdbcType.BIGINT),
            @Result(column="creator_name", property="creatorName", jdbcType=JdbcType.VARCHAR),
            @Result(column="modifier_id", property="modifierId", jdbcType=JdbcType.BIGINT),
            @Result(column="modifier_name", property="modifierName", jdbcType=JdbcType.VARCHAR),
            @Result(column="gmt_create", property="gmtCreate", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="gmt_modified", property="gmtModified", jdbcType=JdbcType.TIMESTAMP),
            @Result(property =  "onSaleList", javaType = List.class, many =@Many(select="selectLastOnSaleByProductId"), column = "id"),
            @Result(property =  "otherProduct", javaType = List.class, many =@Many(select="selectOtherProduct"), column = "goods_id")
    })
    List<ProductAllPo> getProductWithAll(ProductPoExample example);

    @Select({
            "select p.`id`, p.`goods_id`, p.`sku_sn`, p.`name`, p.`original_price`, p.`weight`, " +
            "p.`barcode`, p.`unit`, p.`origin_place`,p.`creator_id`, p.`creator_name`, p.`modifier_id`, " +
            "p.`modifier_name`, p.`gmt_create`, p.`gmt_modified`," +
            "os.`id`,os.`shop_id`, os.`product_id`, os.`price`, os.`begin_time`, os.`end_time`, os.`quantity`, " +
            "os.`type`, os.`max_quantity`, os.`invalid`" +
            "from goods_product p",
            "LEFT JOIN goods_onsale os ON p.`id` = os.`id`" +
            "LEFT JOIN goods_product ps ON ps.`goods_id` = p.`goods_id` AND ps.`id` <> p.`id`" +
            "where p.`id` = #{productId,jdbcType=BIGINT} and `begin_time` <= NOW() and `end_time` > NOW()"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="sku_sn", property="skuSn", jdbcType=JdbcType.VARCHAR),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="original_price", property="originalPrice", jdbcType=JdbcType.BIGINT),
            @Result(column="weight", property="weight", jdbcType=JdbcType.BIGINT),
            @Result(column="barcode", property="barcode", jdbcType=JdbcType.VARCHAR),
            @Result(column="unit", property="unit", jdbcType=JdbcType.VARCHAR),
            @Result(column="origin_place", property="originPlace", jdbcType=JdbcType.VARCHAR),
            @Result(column="commission_ratio", property="commissionRatio", jdbcType=JdbcType.INTEGER),
            @Result(column="free_threshold", property="freeThreshold", jdbcType=JdbcType.BIGINT),
            @Result(column="status", property="status", jdbcType=JdbcType.SMALLINT),
            @Result(column="creator_id", property="creatorId", jdbcType=JdbcType.BIGINT),
            @Result(column="creatorName", property="creatorName", jdbcType=JdbcType.VARCHAR),
            @Result(column="modifier_id", property="modifierId", jdbcType=JdbcType.BIGINT),
            @Result(column="modifier_name", property="modifierName", jdbcType=JdbcType.VARCHAR),
            @Result(column="gmt_create", property="gmtCreate", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="gmt_modified", property="gmtModified", jdbcType=JdbcType.TIMESTAMP),
            @Result(property =  "onSaleList", javaType = List.class, many =@Many(select="selectLastOnSaleByProductId"), column = "id"),
            @Result(property =  "otherProduct", javaType = List.class, many =@Many(select="selectOtherProduct"), column = "goods_id")
    })
    List<ProductAllPo> getProductJOINAll(Long productId);

    @Select({
            "select  p.`id`, p.`goods_id`, p.`sku_sn`, p.`name`, p.`original_price`, p.`weight`, " +
                    "p.`barcode`, p.`unit`, p.`origin_place`,p.`creator_id`, p.`creator_name`, p.`modifier_id`, " +
                    "p.`modifier_name`, p.`gmt_create`, p.`gmt_modified`" +
                    "from goods_product p " +
                    "where p.`name` = #{name}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="sku_sn", property="skuSn", jdbcType=JdbcType.VARCHAR),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="original_price", property="originalPrice", jdbcType=JdbcType.BIGINT),
            @Result(column="weight", property="weight", jdbcType=JdbcType.BIGINT),
            @Result(column="barcode", property="barcode", jdbcType=JdbcType.VARCHAR),
            @Result(column="unit", property="unit", jdbcType=JdbcType.VARCHAR),
            @Result(column="origin_place", property="originPlace", jdbcType=JdbcType.VARCHAR),
            @Result(column="commission_ratio", property="commissionRatio", jdbcType=JdbcType.INTEGER),
            @Result(column="free_threshold", property="freeThreshold", jdbcType=JdbcType.BIGINT),
            @Result(column="status", property="status", jdbcType=JdbcType.SMALLINT),
            @Result(column="creator_id", property="creatorId", jdbcType=JdbcType.BIGINT),
            @Result(column="creator_name", property="creatorName", jdbcType=JdbcType.VARCHAR),
            @Result(column="modifier_id", property="modifierId", jdbcType=JdbcType.BIGINT),
            @Result(column="modifier_name", property="modifierName", jdbcType=JdbcType.VARCHAR),
            @Result(column="gmt_create", property="gmtCreate", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="gmt_modified", property="gmtModified", jdbcType=JdbcType.TIMESTAMP),
            @Result(property =  "onSaleList", javaType = List.class, many =@Many(select="selectLastOnSaleByProductId"), column = "id"),
            @Result(property =  "otherProduct", javaType = List.class, many =@Many(select="selectOtherProduct"), column = "goods_id")
    })
    List<ProductAllPo> selectByJoinExample(String name);

    @Select({
            "select `creator_name`, `creator_id`, `id`",
            "from goods_product",
            "where `id` = #{productId,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column="creator_id", property="id", jdbcType=JdbcType.BIGINT),
            @Result(column="creator_name", property="name", jdbcType=JdbcType.VARCHAR),
    })
    User selectCreator(Long productId);

    @Select({
            "select `modifier_id`, `modifier_name`, `id` ",
            "from goods_product",
            "where `id` = #{productId,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column="modifier_id", property="id", jdbcType=JdbcType.BIGINT),
            @Result(column="modifier_name", property="name", jdbcType=JdbcType.VARCHAR),
    })
    User selectModifier(Long productId);

    @Select({
            "select",
            "`id`, `product_id`, `price`, `begin_time`, `end_time`, `quantity`, `max_quantity`, `creator_id`, ",
            "`creator_name`, `modifier_id`, `modifier_name`, `gmt_create`, `gmt_modified`",
            "from goods_onsale",
            "where `product_id` = #{productId,jdbcType=BIGINT} and `begin_time` <= NOW() and `end_time` > NOW()"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="product_id", property="productId", jdbcType=JdbcType.BIGINT),
            @Result(column="price", property="price", jdbcType=JdbcType.BIGINT),
            @Result(column="begin_time", property="beginTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="end_time", property="endTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="quantity", property="quantity", jdbcType=JdbcType.INTEGER),
            @Result(column="max_quantity", property="maxQuantity", jdbcType=JdbcType.INTEGER),
            @Result(column="creator_id", property="creatorId", jdbcType=JdbcType.BIGINT),
            @Result(column="creator_name", property="creatorName", jdbcType=JdbcType.VARCHAR),
            @Result(column="modifier_id", property="modifierId", jdbcType=JdbcType.BIGINT),
            @Result(column="modifier_name", property="modifierName", jdbcType=JdbcType.VARCHAR),
            @Result(column="gmt_create", property="gmtCreate", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="gmt_modified", property="gmtModified", jdbcType=JdbcType.TIMESTAMP)
    })
    List<OnSalePo> selectLastOnSaleByProductId(Long productId);

    @Select({
            "select",
            "`id`, `goods_id`, `sku_sn`, `name`, `original_price`, `weight`, ",
            "`barcode`, `unit`, `origin_place`, `creator_id`, `creator_name`, `modifier_id`, ",
            "`modifier_name`, `gmt_create`, `gmt_modified`",
            "from goods_product",
            "where `goods_id` = #{goodsId,jdbcType=BIGINT}"
    })

    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="sku_sn", property="skuSn", jdbcType=JdbcType.VARCHAR),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="original_price", property="originalPrice", jdbcType=JdbcType.BIGINT),
            @Result(column="weight", property="weight", jdbcType=JdbcType.BIGINT),
            @Result(column="barcode", property="barcode", jdbcType=JdbcType.VARCHAR),
            @Result(column="unit", property="unit", jdbcType=JdbcType.VARCHAR),
            @Result(column="origin_place", property="originPlace", jdbcType=JdbcType.VARCHAR),
            @Result(column="creator_id", property="creatorId", jdbcType=JdbcType.BIGINT),
            @Result(column="creator_name", property="creatorName", jdbcType=JdbcType.VARCHAR),
            @Result(column="modifier_id", property="modifierId", jdbcType=JdbcType.BIGINT),
            @Result(column="modifier_name", property="modifierName", jdbcType=JdbcType.VARCHAR),
            @Result(column="gmt_create", property="gmtCreate", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="gmt_modified", property="gmtModified", jdbcType=JdbcType.TIMESTAMP)
    })
    ProductPo selectOtherProduct(Long goodsId);
}
