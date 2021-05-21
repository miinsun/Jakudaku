package passionx3.jkdk.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("serial")
public class Order implements Serializable {
	int orderId; 
	String orderDate; 
	int totalPrice; 
	String creditCard; 
	String expireDate;
	String cardType;
	int discountCost;
	int usedMileage;
	String userId;
	List<LineItem> lineItems;
		
	public Order() { }

	public Order(int orderId, String orderDate, int totalPrice, String creditCard, String expireDate, String cardType,
			int discountCost, int usedMileage, String userId) {
		this.orderId = orderId;
		this.orderDate = orderDate;
		this.totalPrice = totalPrice;
		this.creditCard = creditCard;
		this.expireDate = expireDate;
		this.cardType = cardType;
		this.discountCost = discountCost;
		this.usedMileage = usedMileage;
		this.userId = userId;
	}
	
	 public void initOrder(Account account, Cart cart) {
		    userId = account.getUserId();
		    
		    Calendar cal= Calendar.getInstance ( );
		    SimpleDateFormat sDate = new SimpleDateFormat("yy/MM/dd");
			cal.setTime(new Date());
			String today = sDate.format(cal.getTime());
		    orderDate = today;

		    totalPrice = (int) cart.getSubTotal();
		    
		    creditCard = "999 9999 9999 9999";
		    expireDate = "12/03";
		    cardType = "Visa";

		    Iterator<CartItem> i = cart.getAllCartItems();
		    while (i.hasNext()) {
		      CartItem cartItem = (CartItem) i.next();
		      addLineItem(cartItem);
		    }
		  }
	 
	  public void addLineItem(CartItem cartItem) {
		    LineItem lineItem = new LineItem(lineItems.size() + 1, cartItem);
		    addLineItem(lineItem);
		  }
	  
	  public void addLineItem(LineItem lineItem) {
		    lineItems.add(lineItem);
		  }
	  
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public int getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getCreditCard() {
		return creditCard;
	}
	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}
	public String getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public int getDiscountCost() {
		return discountCost;
	}
	public void setDiscountCost(int discountCost) {
		this.discountCost = discountCost;
	}
	public int getUsedMileage() {
		return usedMileage;
	}
	public void setUsedMileage(int usedMileage) {
		this.usedMileage = usedMileage;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<LineItem> getLineItems() {
		return lineItems;
	}
	public void setLineItems(List<LineItem> lineItems) {
		this.lineItems = lineItems;
	}
	
}
