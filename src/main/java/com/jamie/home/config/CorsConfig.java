package com.jamie.home.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

   @Bean
   public CorsFilter corsFilter() {
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      CorsConfiguration config = new CorsConfiguration();
      config.setAllowCredentials(true);
      config.addAllowedOriginPattern("*");
      config.addAllowedHeader("*");
      config.addAllowedMethod("*");

      source.registerCorsConfiguration("/member/**", config);
      source.registerCorsConfiguration("/main/**", config);
      source.registerCorsConfiguration("/banner/**", config);
      source.registerCorsConfiguration("/notice/**", config);
      source.registerCorsConfiguration("/faq/**", config);
      source.registerCorsConfiguration("/contact/**", config);
      source.registerCorsConfiguration("/interpreter/**", config);
      source.registerCorsConfiguration("/booking/**", config);
      return new CorsFilter(source);
   }

}
