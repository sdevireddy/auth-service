package com.zen.auth.filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ResponseLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponseCopier responseCopier = new HttpServletResponseCopier((HttpServletResponse) response);
        chain.doFilter(request, responseCopier);
        responseCopier.flushBuffer();
        byte[] copy = responseCopier.getCopy();
        System.out.println("🔍 Response: " + new String(copy, response.getCharacterEncoding()));
    }
}

