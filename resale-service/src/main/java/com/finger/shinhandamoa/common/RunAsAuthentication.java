package com.finger.shinhandamoa.common;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.security.auth.Subject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

/**
 * RunAs 인증
 * 
 * @author wisehouse@finger.co.kr
 */
public class RunAsAuthentication implements Authentication {
    private Authentication authentication;
    
    private Stack<Authentication> mementoStack;

    public RunAsAuthentication(Authentication authentication) {
        if(authentication instanceof RunAsAuthentication) {
            this.authentication = ((RunAsAuthentication) authentication).authentication;
            this.mementoStack = ((RunAsAuthentication) authentication).mementoStack;
        } else {
            this.authentication = authentication;
            this.mementoStack = new Stack<>();
        }
    }
    
    public void runAs(Authentication authentication) {
        this.mementoStack.push(this.authentication);
        this.authentication = authentication;
    }
    
    public void exitRunAs() {
        if(this.mementoStack.isEmpty()) {
            return;
        }
        
        this.authentication = this.mementoStack.pop();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        
        if(this.mementoStack.isEmpty()) {
            return authorities;
        }
        
        final SimpleGrantedAuthority runAsGrantedAuthority = new SimpleGrantedAuthority("ROLE_RUN_AS");
        
        final List<GrantedAuthority> resultList = new ArrayList<>();
        resultList.addAll(authorities);
        resultList.add(runAsGrantedAuthority);
        
        return resultList;
    }

    @Override
    public Object getCredentials() {
        return authentication.getCredentials();
    }

    @Override
    public Object getDetails() {
        return authentication.getDetails();
    }

    @Override
    public Object getPrincipal() {
        return authentication.getPrincipal();
    }

    @Override
    public boolean isAuthenticated() {
        return authentication.isAuthenticated();
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        authentication.setAuthenticated(isAuthenticated);
    }

    @Override
    public boolean equals(Object another) {
        return authentication.equals(another);
    }

    @Override
    public String toString() {
        return authentication.toString();
    }

    @Override
    public int hashCode() {
        return authentication.hashCode();
    }

    @Override
    public String getName() {
        return authentication.getName();
    }

    @Override
    public boolean implies(Subject subject) {
        return authentication.implies(subject);
    }
}
