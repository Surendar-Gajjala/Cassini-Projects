package com.cassinisys.platform.util;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletContext;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Component class for email templates
 * 
 * @author lakshmi
 *
 */
@Component
public class HTMLTemplateUtils {

	@Transactional
	public static String getTemplateAsString(String templatePath,
			ServletContext servletContext) {

		try {
			InputStream is = servletContext.getResourceAsStream(templatePath);
			String strLine = "";
			StringBuilder strHtml = new StringBuilder("");
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					"UTF-8"));
			while ((strLine = br.readLine()) != null) {
				strHtml.append(strLine);
			}
			br.close();
			return strHtml.toString();
		} catch (Exception e) {

			e.printStackTrace();
			return "";
		}

	}

}
