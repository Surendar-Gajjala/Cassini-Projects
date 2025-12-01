package com.cassinisys.platform.exceptions;

import org.springframework.ui.ModelMap;

/**
 * Created by reddy on 12/16/14.
 */
public class APIError extends ModelMap {

	private static final long serialVersionUID = 1L;

	private String code = ErrorCodes.GENERAL.toString();
	private String message = "Unknown error";

	public APIError() {
		addAttribute("error", true);
		addAttribute("code", this.code);
		addAttribute("message", this.message);
	}

	public APIError(String code, String message) {
		addAttribute("error", true);
		addAttribute("code", code);
		addAttribute("message", message);
	}

	public APIError(String message) {
		addAttribute("error", true);
		addAttribute("code", this.code);
		addAttribute("message", message);
	}
}
