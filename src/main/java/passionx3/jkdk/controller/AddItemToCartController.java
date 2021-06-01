package passionx3.jkdk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import passionx3.jkdk.domain.Cart;
import passionx3.jkdk.domain.Online;
import passionx3.jkdk.service.jkdkFacade;

@Controller
@SessionAttributes({"sessionCart", "userSession"})
public class AddItemToCartController { 
	@Autowired
	private jkdkFacade jkdkStore;

	@ModelAttribute("sessionCart")
	public Cart createCart() {
		return new Cart();
	}
	
	@RequestMapping("/shop/addItemToCart.do")
	public ModelAndView handleRequest(
			@RequestParam("itemId") int itemId,
			@ModelAttribute("sessionCart") Cart cart ,
			SessionStatus status
			) throws Exception {
		status.setComplete();
		if (cart.containsItemId(itemId)) {
			// 처리
			// UI에 "이미 담겼음" 출력
			System.out.println("AddItemController- if: " + itemId);
			
		}
		else {
			// isInStock is a "real-time" property that must be updated
			// every time an item is added to the cart, even if other
			// item details are cached.
			Online item = this.jkdkStore.getOnlineItemById(itemId);
			System.out.println("AddItemController- else: " + item.getItemId());
			cart.addItem(item);
		}
		return new ModelAndView("Cart", "cart", cart);
	}
}