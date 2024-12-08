package com.example.Lab5.config;

import com.example.Lab5.Model.User;
import com.example.Lab5.repository.userrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;

@Service
public class MyUserDetailsService implements UserDetailsService {
   @Autowired
    private userrepository userRepositiry;

   @Override
    public UserDetails loadUserByUsername(String username)throws UsernameNotFoundException{
       User user=userRepositiry.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("user not found"));
       List<GrantedAuthority>authorities= Collections.singletonList(new SimpleGrantedAuthority(user.isAdmin()?"ADMIN":"USER"));
       return new org.springframework.security.core.userdetails.User(
               user.getUsername(),
               user.getPassword(),
               true,
               true,
               true,
               true,
                authorities
       );

   }

}