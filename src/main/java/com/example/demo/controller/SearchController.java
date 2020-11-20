package com.example.demo.controller;

import com.example.demo.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.GetQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.web.bind.annotation.*;



/**
 * @author Administrator
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    /*@Autowired
    private ElasticsearchOperations elasticsearchOperations;*/


    private  ElasticsearchOperations elasticsearchOperations;

    public SearchController(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }



    @GetMapping("/person")
    public String save(@RequestParam(value = "id") Integer id,@RequestParam(value = "name") String name) {

        Person person = new Person(id,name);
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(person.getId().toString())
                .withObject(person)
                .build();
        IndexCoordinates at = IndexCoordinates.of("at");
        String documentId = elasticsearchOperations.index(indexQuery,at);
        //elasticsearchOperations.save(person);
        return documentId;
    }

    @GetMapping("/person/{id}")
    public Person findById1(@PathVariable("id")  Long id) {
        Person person = elasticsearchOperations
                .get(id.toString(), Person.class);
                //.queryForObject(GetQuery.getById(id.toString()), Person.class);
        System.out.println(person.toString());
        return person;
    }
}
