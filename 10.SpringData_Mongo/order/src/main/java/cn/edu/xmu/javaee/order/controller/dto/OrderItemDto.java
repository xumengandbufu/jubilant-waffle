package cn.edu.xmu.javaee.order.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
public class OrderItemDto {

    private Long id;

    private Long onsaleId;

    private Integer quantity;

    private Long price;

    private Long discountPrice;

    private Long point;

    private String name;
}
