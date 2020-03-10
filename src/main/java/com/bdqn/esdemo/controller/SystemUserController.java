package com.bdqn.esdemo.controller;

import com.bdqn.esdemo.constant.SysConst;
import com.bdqn.esdemo.controller.vo.SystemUserVo;
import com.bdqn.esdemo.service.SystemUserService;
import com.bdqn.esdemo.utils.DateTimeUtil;
import com.bdqn.esdemo.utils.TimeUtils;
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
 * ClassName: {@link SystemUserController}
 *
 * @author xyf
 * description:  用户控制器
 * create time: 2020/3/9 17:02
 */
@RestController
public class SystemUserController {

    @Autowired
    private TransportClient client;

    @Autowired
    private SystemUserService systemUserService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * description: 查询people的索引的相关数据
     *
     * @param id
     * @return org.springframework.http.ResponseEntity
     * @date 2020/3/9 17:07
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
     * description: 导入数据(指定id)
     * @date  2020/3/10 14:32
     * @param
     * @return org.springframework.http.ResponseEntity
     */
    @PostMapping("/add1/people/man")
    @ResponseBody
    public ResponseEntity add1PeopleFromDb() {
        List<IndexResponse> results = new ArrayList<>();
        try {
//             从数据库中获取用户的数据
            List<SystemUserVo> systemUserVos = systemUserService.getUsers();
            if (systemUserVos != null || systemUserVos.size() > 0) {
                for (SystemUserVo systemUserVo : systemUserVos) {
                    XContentBuilder content = XContentFactory.jsonBuilder()
                            .startObject()
                            .field("name", systemUserVo.getName())
                            .field("country", systemUserVo.getCountry())
                            .field("age", systemUserVo.getAge())
                    .field("date",systemUserVo.getBirthday())
                    .endObject();
            IndexResponse result = this.client.prepareIndex("people", "man")
                    .setSource(content).setId(systemUserVo.getId()).get();
            results.add(result);
        }
        return new ResponseEntity(results.size(), HttpStatus.OK);
    }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    /**
     * description: 导入数据(指定id插入文档)
     * @date  2020/3/10 14:32
     * @param
     * @return org.springframework.http.ResponseEntity
     */
    @PostMapping("/add2/people/man/{id}")
    @ResponseBody
    public ResponseEntity add2PeopleFromDb(@PathVariable String id) {
        List<IndexResponse> results = new ArrayList<>();
        try {
//             从数据库中获取用户的数据
            SystemUserVo systemUserVo = systemUserService.getUsebById(id);
                if (systemUserVo != null) {
                    XContentBuilder content = XContentFactory.jsonBuilder()
                            .startObject()
                            .field("name", systemUserVo.getName())
                            .field("country", systemUserVo.getCountry())
                            .field("age", systemUserVo.getAge())
                            .field("date",systemUserVo.getBirthday())
                            .field("_id",id)
                            .endObject();
                    IndexResponse result = this.client.prepareIndex("people", "man")
                            .setSource(content).setId(systemUserVo.getId()).get();
                    results.add(result);
                return new ResponseEntity(results.size(), HttpStatus.OK);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
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
     * description:  删除人物信息
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
            //版本更新后的语法
//            result.add(hit.getSourceAsMap());
        }

        return new ResponseEntity(result, HttpStatus.OK);
    }
}

