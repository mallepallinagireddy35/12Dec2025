/**
 * 
 */
package com.nic.svpnpa;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
/*import org.springframework.boot.autoconfigure.web.servlet.error.ErrorPage;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorPageRegistrar;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorPageRegistry;*/
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class ErrorConfiguration implements ErrorPageRegistrar {

    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        registry.addErrorPages(
            new ErrorPage(HttpStatus.NOT_FOUND, "/customerror"),
            new ErrorPage(HttpStatus.FORBIDDEN, "/customerror"),
            new ErrorPage(HttpStatus.UNAUTHORIZED, "/customerror")
            // Add more error pages as needed
        );
    }
}