package com.djeffing.spring_ai.configs.rateLimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class RateLimitAspect {

    // Map qui stocke tous les "seaux" (buckets) : clé = utilisateur (IP + endpoint), valeur = Bucket
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Around("@annotation(rateLimit)") // Exécuter ce code autour de toute méthode annotée avec "@RateLimit".
    public Object rateLimit(ProceedingJoinPoint pjp, RateLimit rateLimit) throws Throwable {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpServletResponse response =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();

        String clientIp = request.getRemoteAddr();
        String key = clientIp + ":" + pjp.getSignature().toShortString();

        // Crée un bucket si inexistant
        buckets.computeIfAbsent(key, k -> {
            Bandwidth limit = Bandwidth.classic(
                    rateLimit.limit(),
                    Refill.intervally(rateLimit.limit(), Duration.ofSeconds(rateLimit.duration()))
            );
            return Bucket4j.builder().addLimit(limit).build();
        });

        Bucket bucket = buckets.get(key);


        if (bucket.tryConsume(1)) { // Vérifier si on peut consommer 1 token
            long available = bucket.getAvailableTokens();
            long limit = rateLimit.limit();
            long reset = System.currentTimeMillis() / 1000 + rateLimit.duration();

            response.setHeader("X-RateLimit-Limit", String.valueOf(limit));
            response.setHeader("X-RateLimit-Remaining", String.valueOf(available));
            response.setHeader("X-RateLimit-Reset", String.valueOf(reset));

            return pjp.proceed();
        } else { // quota dépassé
            long reset = System.currentTimeMillis() / 1000 + rateLimit.duration();

            response.setHeader("X-RateLimit-Limit", String.valueOf(rateLimit.limit()));
            response.setHeader("X-RateLimit-Remaining", "0");
            response.setHeader("X-RateLimit-Reset", String.valueOf(reset));

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Rate limit exceeded");
            return null;
        }
    }
}

