package br.inatel.dm111promo.infrastructure.config;

import br.inatel.dm111promo.core.models.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class AppInterceptorRegistry implements WebMvcConfigurer {

    private static final List<String> ENDPOINTS_PATTERN = Arrays.asList(
            "/dm111/promotion**",
            "/dm111/promotions**",
            "/dm111/promotion/**",
            "/dm111/promotions/**"
    );

    private final AuthInterceptor authInterceptor;

    public AppInterceptorRegistry(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor).addPathPatterns(ENDPOINTS_PATTERN);
    }
}
