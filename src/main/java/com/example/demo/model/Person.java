package com.example.demo.model;

import lombok.*;
import org.omg.CORBA.DynAnyPackage.Invalid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.diagnostics.LoggingFailureAnalysisReporter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Administrator
 */
@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@Document(indexName = "at",type = "test_doc")
public class Person {
    private Integer id;
    private String name;

    public static void main(String[] args) {
        BigDecimal a = new BigDecimal("1500.00");
        BigDecimal b = new BigDecimal("1979");
        Double aa = new Double("1500.00");
        BigDecimal result = a.subtract(b);

        System.out.println(result);

        System.out.println(1|3);//或    001 | 011  == 011
        System.out.println(1^3);//异或  001 | 011 == 010
        System.out.println(1&3);//与    001 | 011 == 001

        LocalDate localDate = LocalDate.now();
        String table_no = localDate.format(DateTimeFormatter.ofPattern("yyyyMM"));
        String day = localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        System.out.println(table_no);
        System.out.println(day);
        List<Long> uidList = new ArrayList<>();
        uidList.add(1110L);
        uidList.add(2220L);
        uidList.add(3330L);
        uidList = uidList.subList(2,2);
        System.out.println(uidList);

        test(uidList);

        System.out.println(uidList);

        Person person = new Person(1,"测试");
        Person person2 = new Person(2,"测试2");
        Map<Integer,Person> map = new HashMap<>();
        map.put(1,person);
        map.put(2,person2);
        List<Person> listAll = new ArrayList<>();
        List<Person> list = new ArrayList<>();
        list.sort(new Comparator<Person>(){
            @Override
            public int compare(Person o1, Person o2) {
                Integer level1 = o1.getId() == null? 0 : o1.getId();
                Integer level2 = o2.getId() == null? 0 : o2.getId();
                return level1.compareTo(level2);
            }
        }.reversed());
        System.out.println("-------------------");
        System.out.println(list);
        list.add(map.get(1));
        list.add(map.get(2));

       listAll.addAll(list);
        System.out.println("---------***********----------");
        System.out.println(listAll);
        Person person22 = new Person(null,"测试");
        list.add(person22);
        listAll.addAll(list);
        System.out.println("---------***********----------");
        System.out.println(listAll);

        System.out.println(list);
        map.remove(1);
        System.out.println(list);
        System.out.println(map);

        System.out.println("---------------------------");


        System.out.println(list);
        //list.sort(Comparator.comparingInt(Person::getId).reversed());
        System.out.println(list);



        Integer idtt = person22.getId();
        System.out.println(idtt);

        System.out.println(getTodayStartTime());

        System.out.println(getTodayEndTime());

        System.out.println(getEndTime(1602651539097L,100));

    }
    public static void test( List<Long> uidList){
        List<Long> uidList2 = new ArrayList<>(uidList);
        List<Long> uid_online = new ArrayList<>();
        uid_online.add(111L);
        uidList2.retainAll(uid_online);
        System.out.println(uidList2);
    }

    public static Long getTodayStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
    public static Long getTodayEndTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static Long getEndTime(Long millis ,Integer year) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        calendar.add(Calendar.YEAR,100);
        return calendar.getTimeInMillis();

    }
}
