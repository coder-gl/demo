package com.example.demo.cfg;

import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.cache.ehcache.EhCacheManagerUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import sun.plugin.javascript.navig.LinkArray;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Configuration
public class EhCacheConfig {


    @Bean
    public CacheManager cacheManager() {
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("preConfigured",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Object.class,
                                ResourcePoolsBuilder.heap(1000L))
                                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(20))))
                .build();
        cacheManager.init();
        return cacheManager;
    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);

        list = list.subList(0,2);
        System.out.println(list);

        String s = "(()())(())";
        removeOuterParentheses(s);

        ListNode node2 = new ListNode(2,null);
        ListNode node1 = new ListNode(1,node2);
        ListNode node0 = new ListNode(0,node1);
        kthToLast(node0 , 2);



        String decu = "邂逅开放时间[20201112-20201115] ";
        int sIndex = decu.indexOf("[");
        int eIndex = decu.indexOf("]");
        String id = decu.substring(sIndex+1, eIndex);
        System.out.println(id);

    }

    public static String removeOuterParentheses(String S) {
        StringBuilder sb = new StringBuilder();
        int level = 0;
        for( char c : S.toCharArray()){
            if (c == ')'){
                --level;
            }
            if (level >= 1){
                sb.append(c);
            }
            if (c == '('){
                ++level;
            }
        }
        return sb.toString();
    }

    static int  size;
    public static int kthToLast(ListNode head, int k) {

        int [] num = new int[10];
        int max = (int) (Math.pow(10,3)-1);//10的三次方

        if (head == null){
            return 0;
        }

        int value = kthToLast(head.next, k);
        if (++size == k){
            return head.val;
        }

        return value;
    }

    public static class ListNode {
      int val;
      ListNode next;
      ListNode(int x) { val = x; }
      ListNode(int x,ListNode next) {
          this.val = x;
          this.next = next;
      }
  }

}
