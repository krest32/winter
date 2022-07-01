package com.krest.others.service.impl;

import com.alibaba.fastjson.JSON;

import com.krest.others.entity.ResultVo;
import com.krest.others.entity.SearchVo;
import com.krest.others.service.EsService;
import com.krest.utils.entity.EsProductModel;
import com.krest.utils.response.R;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: krest
 * @Date: 2020/11/13 11:52
 * @Description:
 */
@Service
public class EsServiceImpl implements EsService {

    private static Integer pageSize=1;



    @Autowired
    @Qualifier("elasticsearchClient")
    private RestHighLevelClient restHighLevelClient;


    /**
     *   商品上架保存到ES中
     */
    @Override
    public R saveProduct(EsProductModel esProductModel) throws IOException {
        // BulkRequest 可以用来批量保存数据
        BulkRequest bulkRequest = new BulkRequest();
        //找到需要的索引库，在es中建立索引和映射关系，需要提前在Kibana中进行操作
        IndexRequest indexRequest = new IndexRequest("products");
        //将对象转化为JSON格式
        String jsonString = JSON.toJSONString(esProductModel);
        //设置 Id
        indexRequest.id(esProductModel.getId());
        //将 Json信息放入请求体重
        indexRequest.source(jsonString, XContentType.JSON);
        //发送请求
        bulkRequest.add(indexRequest);

        BulkResponse response = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);

        //返回信息
        System.out.println("bulkRequest："+bulkRequest.toString());
        System.out.println("上传数量"+response.getItems()+",是否失败:"+response.hasFailures());

        return R.ok();
    }

    //根据Id删除ES中的信息
    @Override
    public R deleteProduct(String id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("products",id);
        DeleteResponse delete = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        RestStatus status = delete.status();
        System.out.println(status);

        return R.ok();
    }

    /**
     *     根据条件查询数据
     **/
    @Override
    public R searchProduct(Long page,Long limit,SearchVo searchVo) throws IOException {

        searchVo.setPageNumber(page);
        searchVo.setSizeNumber(limit);

        // 建立检索的条件
        SearchRequest searchRequest = buildSearch(searchVo);

        // 执行检索
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        // 返回执行检索的结果，进行封装
        ResultVo resultVo = buildSearchResponse(response,searchVo);

        System.out.println(response);

        return R.ok().data("result",resultVo);
    }


    private ResultVo buildSearchResponse(SearchResponse response, SearchVo searchVo) {
        ResultVo resultVo = new ResultVo();
        System.out.println("searchVo:"+searchVo);

        //1. 封装产品信息到List集合中
        SearchHits hits = response.getHits();
        List<EsProductModel> productList = new ArrayList<>();
        if(hits.getHits()!=null && hits.getHits().length>0){
            for (SearchHit hit : hits.getHits()) {
                String sourceAsString = hit.getSourceAsString();
                EsProductModel product = JSON.parseObject(sourceAsString, EsProductModel.class);
                productList.add(product);
            }
        }
        resultVo.setProducts(productList);

        //从聚合信息中得到所有的Brand信息
        List<ResultVo.BrandVo> brandVoList = new ArrayList<>();
        ParsedStringTerms bandIdagg = response.getAggregations().get("brand_id_agg");
        for(Terms.Bucket bucket : bandIdagg.getBuckets()) {

            ResultVo.BrandVo brandVo = new ResultVo.BrandVo();
            brandVo.setBrandId(bucket.getKeyAsString());

            ParsedStringTerms brand_title_agg = bucket.getAggregations().get("brand_title_agg");
            String keyAsString = brand_title_agg.getBuckets().get(0).getKeyAsString();
            brandVo.setBrandTitle(keyAsString);

            brandVoList.add(brandVo);
        }
        resultVo.setBrandList(brandVoList);

        //得到所有的Catelog信息
        List<ResultVo.CataLogVo> cataLogList = new ArrayList<>();
        ParsedStringTerms catalogAgg = response.getAggregations().get("cateLog_id_Agg");
        List<? extends Terms.Bucket> buckets = catalogAgg.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            ResultVo.CataLogVo cataLogVo = new ResultVo.CataLogVo();
            cataLogVo.setCateLogId((String)bucket.getKey());
            //然后再得到子聚合的信息 catelogName
            ParsedStringTerms cateLog_title_Agg = bucket.getAggregations().get("cateLog_title_Agg");
            String keyAsString = cateLog_title_Agg.getBuckets().get(0).getKeyAsString();
            cataLogVo.setCateLogTtitle(keyAsString);
            cataLogList.add(cataLogVo);
        }
        resultVo.setCataLogList(cataLogList);



        //封装聚合中的信息
        List<ResultVo.attrVo> attrVoList = new ArrayList<>();
        ParsedNested attrList_agg = response.getAggregations().get("attrList_agg");
        ParsedStringTerms attrGroupId_agg = attrList_agg.getAggregations().get("attrGroupId_agg");
        //然后对AttrGoupId的聚合进行遍历
        for (Terms.Bucket bucket : attrGroupId_agg.getBuckets()) {
            ResultVo.attrVo attrVo = new ResultVo.attrVo();
            ParsedStringTerms attrGroupTitle_agg = bucket.getAggregations().get("attrGroupTitle_agg");
            String attrGroupTitle = attrGroupTitle_agg.getBuckets().get(0).getKeyAsString();
            attrVo.setAttrGroupId(bucket.getKeyAsString());
            attrVo.setAttrGroupTitle(attrGroupTitle);

            ParsedStringTerms attrTitle_agg = attrGroupTitle_agg.getBuckets().get(0).getAggregations().get("attrTitle_agg");

            List<String> attrtitle=attrTitle_agg.getBuckets().stream().map(item -> {
                    String itemKeyAsString = item.getKeyAsString();
                    return itemKeyAsString;
                }).collect(Collectors.toList());


            attrVo.setAttrTitleList(attrtitle);
            attrVoList.add(attrVo);
        }

        resultVo.setAttrList(attrVoList);

        //封转当前页慢
        resultVo.setPageNum(searchVo.getPageNumber());

        //封装总的纪录数
        long value = hits.getTotalHits().value;
        resultVo.setTotal(value);

        //封装总页面
        int totalPages = value%pageSize ==0 ?(int)value/pageSize:((int)value/pageSize)+1;
        resultVo.setTotalPages((long) totalPages);

        return resultVo;
    }


    private SearchRequest buildSearch(SearchVo searchVo) {

        //  指定检索的索引库
        SearchRequest searchRequest = new SearchRequest("products");

        //  构建检索条件的对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //  对关键字进行检索
        //构建多条件查询对象
        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
        // must 条件 模糊匹配,检查是否有关键字
        if(!StringUtils.isEmpty(searchVo.getKeyWords())){
            System.out.println("searchVo"+searchVo);
            // 对标题进行检索
            boolQuery.must(QueryBuilders.matchQuery("title",searchVo.getKeyWords()));
        }

        // 检查是否有品牌Id的List
        if(searchVo.getBrandId()!=null && searchVo.getBrandId().size() > 0){
            //searchParam.getBandId() 是一个list集合，所以使用termsQuery
            boolQuery.filter(QueryBuilders.termsQuery("brandId",searchVo.getBrandId()));
        }
        // 检查是否有一级分类的id
        if((searchVo.getOneSortId()!= null)&& (!StringUtils.isEmpty(searchVo.getOneSortId()))){
            boolQuery.filter(QueryBuilders.termQuery("oneSortId",searchVo.getOneSortId()));
        }
        // 检查是否二级分类的id
        if((searchVo.getTwoSortId()!= null) && (!StringUtils.isEmpty(searchVo.getTwoSortId()))){
            boolQuery.filter(QueryBuilders.termQuery("twoSortId",searchVo.getTwoSortId()));
        }

        // 检查是否有三级分类的id
        if((searchVo.getThreeSortId()!= null) && (!StringUtils.isEmpty(searchVo.getThreeSortId()))){
            boolQuery.filter(QueryBuilders.termQuery("threeSortId",searchVo.getThreeSortId()));
        }

        // 安装库存进行过滤
        searchVo.setHasStock(true);

        boolQuery.filter(QueryBuilders.termsQuery("hasStock",searchVo.getHasStock()));

        // 按照属性进行查询
        if(searchVo.getAttrSearch() !=null && searchVo.getAttrSearch().size()>0){
            // attr的属性可能会传递多个,参数形式1- 5寸：8寸，2 - 8G：16G(id-属性){需要与前端进行约定}
            for (String attr : searchVo.getAttrSearch()) {
                BoolQueryBuilder nestedQueryBuilder = QueryBuilders.boolQuery();

                String[] attrs = attr.split("-");
                //得到attrGroupId
                String attrGroupId = attrs[0];
                //得到attr属性Id
                String[] attrId = attrs[1].split(":");
                //进行参数匹配，为嵌入式的查询匹配名称，后面的聚合中会使用到,name:列表的名字加属性名称

                nestedQueryBuilder.must(QueryBuilders.termQuery("attrList.attrGroupId",attrGroupId));

                nestedQueryBuilder.must(QueryBuilders.termsQuery("attrList.id",attrId));
                //第一个参数：属性的名称，第二个参数查询条件，我们设置为nestedQueryBuilder，第三个查询结果是否参与评分，我们选择null
                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrList", nestedQueryBuilder, ScoreMode.None);
                //每一个都羊属性都要形成于一个Filter进行过滤
                boolQuery.filter(nestedQuery);
            }
        }

        // 检索价格区间
        if(!StringUtils.isEmpty(searchVo.getPrice())){
            /** 查询条件的样子：1—500/-500/500-
             *  "range": {
             *           "FIELD": {
             *             "gte": 10,
             *             "lte": 20
             *           }
             */
            RangeQueryBuilder Price= new RangeQueryBuilder("price");
            //对传递的字符串进行分割成为数组
            String[] s = searchVo.getPrice().split("-");
            if(s.length==2){
                Price.gte(s[0]).lte(s[1]);
            }else if(s.length==1){
                if(searchVo.getPrice().startsWith("-")){
                    Price.lte(s[0]);
                }
                if(searchVo.getPrice().endsWith("-")){
                    Price.gte(s[0]);
                }
            }
        }

            // 高亮关键字
            if (!StringUtils.isEmpty(searchVo.getKeyWords())){
                HighlightBuilder highlightBuilder = new HighlightBuilder();
                highlightBuilder.field("title");
                highlightBuilder.preTags("<b style='color:red'>");
                highlightBuilder.postTags("</b>");
                searchSourceBuilder.highlighter(highlightBuilder);
            }

            // 分页 参数形式
            searchSourceBuilder.from((int) ((searchVo.getPageNumber()-1)*searchVo.getSizeNumber()));
            searchSourceBuilder.size((int) (searchVo.getSizeNumber()*pageSize));
            // 最后封装检索条件
            searchSourceBuilder.query(boolQuery);
            //  将检索条件对象传入检索对象
            searchRequest.source(searchSourceBuilder);



        //3. 聚合分析
        //3.1 品牌聚合
        TermsAggregationBuilder brand_agg = AggregationBuilders.terms("brand_id_agg").field("brandId").size(50);
        //3.1.1品牌子聚合
        brand_agg.subAggregation(AggregationBuilders.terms("brand_title_agg").field("brandTitle").size(1));
        searchSourceBuilder.aggregation(brand_agg);


        //3.2 分类聚合
        TermsAggregationBuilder catelogAgg = AggregationBuilders.terms("cateLog_id_Agg").field("oneSortId").size(50);
        catelogAgg.subAggregation(AggregationBuilders.terms("cateLog_title_Agg").field("oneSortTitle").size(1));
        searchSourceBuilder.aggregation(catelogAgg);


        //3.3.1嵌入聚合
        NestedAggregationBuilder nested_attrList_agg = AggregationBuilders.nested("attrList_agg","attrList");

        //3.3.1.1聚合出所有的AttrID（显示Attr分组的聚合）
        TermsAggregationBuilder attrGroupId= AggregationBuilders.terms("attrGroupId_agg").field("attrList.attrGroupId");
        TermsAggregationBuilder attrGroupTitle = AggregationBuilders.terms("attrGroupTitle_agg").field("attrList.attrGroupTitle").size(50);

        // 3.3.1.2
        //对名字再次分装
        // attrGroupTitle.subAggregation(AggregationBuilders.terms("attrId_agg").field("attrList.id").size(50));
        attrGroupTitle.subAggregation(AggregationBuilders.terms("attrTitle_agg").field("attrList.attrTitle").size(50));

        attrGroupId.subAggregation(attrGroupTitle);

        //3.3.3 将嵌入式聚合放入 searchSourceBuilder中
        nested_attrList_agg.subAggregation(attrGroupId);
        searchSourceBuilder.aggregation(nested_attrList_agg);


        // 查看构建的 searchSourceBuilder 语句
        String string = searchSourceBuilder.toString();
        System.out.println(string);

        searchRequest.source(searchSourceBuilder);

        return searchRequest;
    }
}