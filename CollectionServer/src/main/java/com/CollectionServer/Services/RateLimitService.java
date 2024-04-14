package com.CollectionServer.Services;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import io.github.bucket4j.Bucket;
import org.springframework.web.servlet.HandlerInterceptor;

@Service
public class RateLimitService
{
    @Autowired
    AdminNotificationService adminNotificationService;
    //Handle requests with standard API limit
    private ConcurrentHashMap<String, Bucket> standardBuckets = new ConcurrentHashMap<String, Bucket>();
    //Handle requests with stricter API limit (password login)
    private ConcurrentHashMap<String, Bucket> loginBuckets = new ConcurrentHashMap<String, Bucket>();

    static final long LOGIN_BUCKET_CAPACITY = 15;
    static final long STANDARD_BUCKET_CAPACITY = 200;

    static final Bandwidth loginBandwidth = Bandwidth.classic(LOGIN_BUCKET_CAPACITY, Refill.intervally(LOGIN_BUCKET_CAPACITY, Duration.ofMinutes(1)));
    static final Bandwidth standardBandwidth = Bandwidth.classic(STANDARD_BUCKET_CAPACITY, Refill.intervally(STANDARD_BUCKET_CAPACITY, Duration.ofMinutes(1)));

    public boolean filterRequest(HttpServletRequest request, HttpServletResponse response)
    {
        boolean output = false;
        Bucket currentBucket = null;

        //Retrieve bucket based on request type
        if(request.getServletPath().equals("/login"))
        {
            currentBucket = loginBuckets.get(request.getRemoteAddr());
            if(currentBucket == null)
            {
                currentBucket = Bucket.builder().addLimit(loginBandwidth).build();
                loginBuckets.put(request.getRemoteAddr(), currentBucket);
            }
        }
        else
        {
            currentBucket = standardBuckets.get(request.getRemoteAddr());
            if(currentBucket == null)
            {
                currentBucket = Bucket.builder().addLimit(standardBandwidth).build();
                standardBuckets.put(request.getRemoteAddr(), currentBucket);
            }
        }

        ConsumptionProbe probe = currentBucket.tryConsumeAndReturnRemaining(1);
        if(probe.isConsumed())
        {
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            output = true;
        }
        else
        {
            adminNotificationService.submitLog(AdminNotificationService.LogLevel.WARN, RateLimitService.class.toString(), request.getRemoteAddr() + " has exceeded API rate limit");
            output = false;
        }

        return output;
    }

    public RateLimitInterceptor getInterceptor()
    {
        return new RateLimitInterceptor(this);
    }

    public class RateLimitInterceptor implements HandlerInterceptor
    {
        private RateLimitService rateLimitService;

        public RateLimitInterceptor(RateLimitService rateLimitService)
        {
            this.rateLimitService = rateLimitService;
        }

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
        {
            boolean output = false;

            if(rateLimitService.filterRequest(request, response))
            {
                output = true;
            }
            else
            {
                response.sendError(429, "API rate limit exceeded");
                output = false;
            }

            return output;
        }
    }
}
