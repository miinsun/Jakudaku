package passionx3.jkdk.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.util.WebUtils;

import passionx3.jkdk.domain.Account;

/**
 * @author Juergen Hoeller
 * @since 01.12.2003
 */
@Component
public class SignonInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler)
			throws Exception {
		Account userSession = 
			(Account) WebUtils.getSessionAttribute(request, "userSession");
		if (userSession == null) {
			String url = request.getRequestURL().toString(); 
			String query = request.getQueryString();
			ModelAndView modelAndView = new ModelAndView("SignonForm");
			if (query != null) {
				modelAndView.addObject("signonForwardAction", url+"?"+query);
			}
			else {
				modelAndView.addObject("signonForwardAction", url);
			}
			throw new ModelAndViewDefiningException(modelAndView);
		}
		else {
			return true;
		}
	}
}
