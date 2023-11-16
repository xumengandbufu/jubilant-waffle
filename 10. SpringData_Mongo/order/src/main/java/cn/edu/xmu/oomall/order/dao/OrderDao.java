//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.dao;

import cn.edu.xmu.oomall.order.dao.bo.Order;
import cn.edu.xmu.oomall.order.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {

    private OrderMapper orderMapper;

    @Autowired
    public OrderDao(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }


    public Order insert(Order order){
        return this.orderMapper.insert(order);
    }
}
