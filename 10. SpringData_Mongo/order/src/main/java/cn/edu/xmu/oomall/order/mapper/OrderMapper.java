package cn.edu.xmu.oomall.order.mapper;

import cn.edu.xmu.oomall.order.dao.bo.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface OrderMapper extends MongoRepository<Order, String>{

    @Query(value = "{'orderItems': { $elemMatch: { 'name': ?0 }}}")
    List<Order> findByOrderItemsName(String name, Pageable pageable);

    Order findFirstByOrderSn(String orderSn);
}
