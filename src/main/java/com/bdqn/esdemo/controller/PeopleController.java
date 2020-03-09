package com.bdqn.esdemo.controller;

import com.bdqn.esdemo.constant.SysConst;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ClassName: {@link PeopleController}
 *
 * @author xyf
 * description:  用户控制器
 * create time: 2020/3/9 17:02
 */
@RestController
public class PeopleController {

    @Autowired
    private TransportClient client;

    @GetMapping("/")
    public String index() {
            return "index";
        }

        /**
         * description: 查询people的索引的相关数据
         * @date  2020/3/9 17:07
         * @param	id
         * @return org.springframework.http.ResponseEntity
         */
        @GetMapping("/get/people/man")
        @ResponseBody
        public ResponseEntity getPeople(@RequestParam(name = "id", defaultValue = "") String id) {
            if (id.isEmpty()) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
        GetResponse result = this.client.prepareGet("people", "man", id).get();
        if (!result.isExists()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(result.getSource(), HttpStatus.OK);
    }

    /**
     * description:  添加数据
     * create time: 2019/10/9 0009下午 11:19
     *
     * @ param [name, country, age, date]
     * @ return org.springframework.http.ResponseEntity
     */
    @PostMapping("/add/people/man")
    @ResponseBody
    public ResponseEntity addPeople(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "country") String country,
            @RequestParam(name = "age") int age,
            @RequestParam(name = "date")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                    Date date) {
        try {
            XContentBuilder content = XContentFactory.jsonBuilder()
                    .startObject()
                    .field("name", name)
                    .field("country", country)
                    .field("age", age)
                    .field("date", date.getTime())
                    .endObject();
            IndexResponse result = this.client.prepareIndex("people", "man")
                    .setSource(content).get();
            return new ResponseEntity(result.getId(), HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * description:  修改人物信息（通过id） 未完待续
     * create time: 2019/10/9 0009下午 11:26
     *
     * @ param [name, country, age, date]
     * @ return org.springframework.http.ResponseEntity
     */
    @PostMapping("/update/people/man")
    @ResponseBody
    public ResponseEntity updatePeople(
            @RequestParam(name = "id") String id,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "country", required = false) String country,
            @RequestParam(name = "age", required = false) int age,
            @RequestParam(name = "date", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                    Date date) {
        UpdateRequest updateRequest = new UpdateRequest("people", "man", id);

        try {
            XContentBuilder builder = XContentFactory.jsonBuilder()
                    .startObject();
            if (name != null) {
                builder.field("name", name);
            }
            if (country != null) {
                builder.field("country", country);
            }
            if (age > 0 && age <= SysConst.MAX_AGE) {
                builder.field("age", age);
            }
            if (date != null) {
                builder.field("date", date.getTime());
            }
            builder.endObject();
            updateRequest.doc(builder);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            UpdateResponse result = this.client.update(updateRequest).get();
            return new ResponseEntity(result.getResult().toString(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * description: TODO  删除人物信息
     * create time: 2019/10/9 0009下午 11:29
     *
     * @ param [id]
     * @ return org.springframework.http.ResponseEntity
     */
    @PostMapping("/del/people/man/{id}")
    @ResponseBody
    public ResponseEntity delPeople(
            @PathVariable(name = "id") String id) {
        DeleteResponse result = this.client.prepareDelete("people", "man", id).get();
        return new ResponseEntity(result.getResult().toString(), HttpStatus.OK);

    }


    /**
     * description:   复合查询 按姓名和国家 还有年龄的范围
     * create time: 2019/10/10 0010下午 9:32
     *
     * @ param [name, country, gtAge, ltAge]
     * @ return org.springframework.http.ResponseEntity
     */
    @PostMapping("/query/people/man")
    @ResponseBody
    public ResponseEntity query(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "country", required = false) String country,
            @RequestParam(name = "gt_age", defaultValue = "0") int gtAge,
            @RequestParam(name = "lt_age", required = false) Integer ltAge) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (name != null) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("name", name));
        }
        if (country != null) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("country", country));
        }
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("age").from(gtAge);
        if (ltAge != null && ltAge > 0) {
            rangeQueryBuilder.to(ltAge);
        }
        boolQueryBuilder.filter(rangeQueryBuilder);

        SearchRequestBuilder searchRequestBuilder = this.client.prepareSearch("people")
                .setTypes("man")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(boolQueryBuilder)
                .setFrom(0)
                .setSize(5);
        System.out.println(searchRequestBuilder);
        SearchResponse response = searchRequestBuilder.get();
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (SearchHit hit :
                response.getHits()) {
            result.add(hit.getSource());
        }

        return new ResponseEntity(result, HttpStatus.OK);
    }
}

