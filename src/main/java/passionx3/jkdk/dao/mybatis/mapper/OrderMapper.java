package passionx3.jkdk.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import passionx3.jkdk.domain.LineItem;
import passionx3.jkdk.domain.Order;
<<<<<<< HEAD

=======
>>>>>>> branch 'develop' of https://github.com/KangHyojin1401/Jakudaku.git
@Mapper
public interface OrderMapper {

	@Select("SELECT orderDate, totalPrice, creditCard, expireDate, cardType, userId, discountCost, usedMileage"
			+ "	FROM Orders WHERE orderId = #{orderId}")
	Order getOrder(int orderId);

	@Select("INSERT INTO Orders (orderId, orderDate, totalPrice, creditCard, expireDate, cardType, userId, discountCost, usedMileage)"
			+ "	VALUES(#{orderId}, #{orderDate}, #{totalPrice}, #{creditCard}, #{expireDate}, #{cardType}, #{userId}, #{discountCost}, #{usedMileage})")
	int insertOrder(Order order);
	
	@Select("SELECT ORDERID, ORDERDATE, TOTALPRICE, CREDITCARD, EXPIREDATE, CARDTYPE, USEDMILEAGE, DISCOUNTCOST, USERID"
			+ " FROM ORDERS WHERE USERID = #{userId} ORDER BY ORDERDATE")
	List<Order> getOrdersByUserId(String userId);
	Map<String, List<Order>> getLineItemsByUserId(String userId);
}
