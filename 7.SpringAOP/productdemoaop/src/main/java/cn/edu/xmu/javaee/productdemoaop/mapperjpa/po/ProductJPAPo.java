package cn.edu.xmu.javaee.productdemoaop.mapperjpa.po;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductJPAPo {
    @Id
    private Long id;
    private Long shopId;
    private Long goodsId;
    private Long categoryId;
    private Long templateId;
    private String skuSn;
    private String name;
    private Long originalPrice;
    private Long weight;
    private String barcode;
    private String unit;
    private String originPlace;
    private Long creatorId;
    private String creatorName;
    private Long modifierId;
    private String modifierName;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private Byte status;
    private Integer commissionRatio;
    private Long shopLogisticId;
    private Long freeThreshold;
}
