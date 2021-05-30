package passionx3.jkdk.dao.mybatis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import passionx3.jkdk.dao.LineItemDao;
import passionx3.jkdk.dao.OrderDao;
import passionx3.jkdk.dao.SequenceDao;
import passionx3.jkdk.dao.mybatis.mapper.AccountMapper;
import passionx3.jkdk.dao.mybatis.mapper.FundOrderMapper;
import passionx3.jkdk.dao.mybatis.mapper.FundingMapper;
import passionx3.jkdk.dao.mybatis.mapper.LineItemMapper;
import passionx3.jkdk.dao.mybatis.mapper.OnlineMapper;
import passionx3.jkdk.dao.mybatis.mapper.OrderMapper;
import passionx3.jkdk.domain.FundOrder;
import passionx3.jkdk.domain.LineItem;
import passionx3.jkdk.domain.Order;

@Repository
public class MybatisOrderDao implements OrderDao {

	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private FundOrderMapper fundOrderMapper;
	
	@Autowired
	private LineItemMapper lineItemMapper;

	@Autowired
	private AccountMapper accountMapper;
	
	@Autowired
	private LineItemDao lineItemDao;
	
	@Autowired
	private SequenceDao sequenceDao;
	
	@Autowired
	private OnlineMapper onlineMapper;
	
	@Autowired
	private FundingMapper fundingMapper;
	
	@Override
	@Transactional
	public Order getOrderByOrderId(int orderId) throws DataAccessException {
		Order order = orderMapper.getOrderByOrderId(orderId);
		if (order != null) {
			order.setLineItems(lineItemDao.getLineItemsByOrderId(orderId));
		}
	    return order;
	}	
	
	@Override
	@Transactional
	public int insertOrder(Order order) throws DataAccessException {  
    	order.setOrderId(sequenceDao.getOrderSequenceNextVal());
    	// order.setOrderId(-3);
    	// order 테이블에 insert
    	int result = orderMapper.insertOrder(order);
    	if (result == 0)
    		return 0;

    	// lineitem 테이블에 insert
    	for (LineItem lineItem : order.getLineItems()) {
    		lineItem.setOrderId(order.getOrderId());
    		lineItem.setLineItemId(sequenceDao.getLineItemSequenceNextVal());
    		// lineItem.setLineItemId(-3);
    		result = lineItemMapper.insertLineItem(lineItem);
    		if (result == 0)
    			return 0;
    	}
    	
    	//적립금 사용했다면
    	if (order.getUsedMileage() > 0) {
    		result = accountMapper.useMileage(order.getUsedMileage(), order.getUserId());
    		if (result == 0)
    			return 0;
    	}
    	
    	// 적립금 받기
    	result = accountMapper.getMileage(order.getEarningMileage(), order.getUserId());
    	if (result == 0)
			return 0;
    	
    	return 1;
	}

	@Override
	public List<Order> getOrdersByUserId(String userId) {
		return orderMapper.getOrdersByUserId(userId);
	}
	
	// 재아 파트
	@Override
	public Map<String, List<Order>> getLineItemsByUserId(String userId) {
		List<Order> onlineList = orderMapper.getOrdersByUserId(userId);
		List<FundOrder> fundingList = fundOrderMapper.getFundOrdersByUserId(userId);
		Map<String, List<Order>> lineItemMap = new LinkedHashMap<>();
		
		int i = 0, j = 0;
		
		while(i < onlineList.size() && j < fundingList.size()) {
			List<Order> orderList = new ArrayList<Order>();
			int orderId = onlineList.get(i).getOrderId();
			int fundOrderId = fundingList.get(j).getOrderId();

			try {
				SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date onlineDate = format.parse(onlineList.get(i).getOrderDate());
				Date fundingDate = format.parse(fundingList.get(j).getOrderDate());
						
				if(onlineDate.getTime() > fundingDate.getTime()) {
					Order order = orderMapper.getOrderByOrderId(orderId);
					
					List<LineItem> lineItemList = new ArrayList<LineItem>();
					for(int a = 0; a < lineItemMapper.getLineItemsByOrderId(orderId).size(); a++) { // lineItem에 item 객체 넣어주기							
						LineItem lineItem = lineItemMapper.getLineItemsByOrderId(orderId).get(a);
						lineItem.setOnline(onlineMapper.getOnlineItemById(lineItem.getItemId()));
						
						lineItemList.add(lineItem);
					}
					
					order.setLineItems(lineItemList);
					orderList.add(order);
					lineItemMap.put(onlineList.get(i).getOrderDate(), orderList);
					i++;
				}
				else {
					FundOrder fundOrder = fundOrderMapper.getFundOrderByOrderId(fundOrderId);
					
					List<LineItem> lineItemList = new ArrayList<LineItem>();
					for(int a = 0; a < lineItemMapper.getLineItemsByOrderId(fundOrderId).size(); a++) { // lineItem에 item 객체 넣어주기							
						LineItem lineItem = lineItemMapper.getLineItemsByOrderId(fundOrderId).get(a);
						lineItem.setFunding(fundingMapper.getFundingItemById(lineItem.getItemId()));
					
						lineItemList.add(lineItem);
					}
					
					fundOrder.setLineItems(lineItemList);
					orderList.add(fundOrder);
					lineItemMap.put(fundingList.get(j).getOrderDate(), orderList);
					
					i++;
					j++;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	
		if(i < onlineList.size()) {
			for(int k = i; k < onlineList.size(); k++) {
				List<Order> orderList = new ArrayList<Order>();
					System.out.println("이곳은 2의 online " + i);
					int orderId = onlineList.get(i).getOrderId();
					Order order = orderMapper.getOrderByOrderId(orderId);
				
				List<LineItem> lineItemList = new ArrayList<LineItem>();
				for(int a = 0; a < lineItemMapper.getLineItemsByOrderId(orderId).size(); a++) { // lineItem에 item 객체 넣어주기							
					LineItem lineItem = lineItemMapper.getLineItemsByOrderId(orderId).get(a);
					lineItem.setOnline(onlineMapper.getOnlineItemById(lineItem.getItemId()));
					
					lineItemList.add(lineItem);
				}
				
				order.setLineItems(lineItemList);
				orderList.add(order);
				lineItemMap.put(onlineList.get(i).getOrderDate(), orderList);
				i++;
			}
		}
		else {
			for(int k = j; k < fundingList.size(); k++) {
			
				List<Order> orderList = new ArrayList<Order>();
				System.out.println("이곳은 2의 funding " + j);
				int fundOrderId = fundingList.get(j).getOrderId();
				FundOrder fundOrder = fundOrderMapper.getFundOrderByOrderId(fundOrderId);
			
				List<LineItem> lineItemList = new ArrayList<LineItem>();
				for(int a = 0; a < lineItemMapper.getLineItemsByOrderId(fundOrderId).size(); a++) { // lineItem에 item 객체 넣어주기							
					LineItem lineItem = lineItemMapper.getLineItemsByOrderId(fundOrderId).get(a);
					lineItem.setFunding(fundingMapper.getFundingItemById(lineItem.getItemId()));
					
					lineItemList.add(lineItem);
				}
				
				fundOrder.setLineItems(lineItemList);
				orderList.add(fundOrder);
				lineItemMap.put(fundingList.get(j).getOrderDate(), orderList);
				
				j++;
			}
		}
	
		return lineItemMap;
	}

}
