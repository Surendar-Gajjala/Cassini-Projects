package com.cassinisys.plm.config;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import org.springframework.context.MessageSource;

import java.util.List;
import java.util.Locale;

public class MessageResolverMethod implements TemplateMethodModelEx {
    private MessageSource messageSource;
    private Locale locale;

    public MessageResolverMethod(MessageSource messageSource, Locale locale) {
        this.messageSource = messageSource;
        this.locale = locale;
    }


    @Override
    public Object exec(List arguments) throws TemplateModelException {
        if (arguments.size() != 1) {
            throw new TemplateModelException("Wrong number of arguments");
        }
        String code = arguments.get(0).toString();
        if (code == null || code.isEmpty()) {
            throw new TemplateModelException("Invalid code value '" + code + "'");
        }
        return messageSource.getMessage(code, null, locale);
    }
}