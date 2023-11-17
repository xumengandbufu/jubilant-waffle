package cn.edu.xmu.javaee.order.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class OrderDto {

    private String id;

    private String orderSn;

    private String consignee;

    private String address;

    private String mobile;

    private String message;

    private List<OrderItemDto> orderItems;
}
