package cn.es;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther jxy
 * @Date 2020-04-30
 */
public class EsDemoTest {
    TransportClient client =null;
    /**
     * 获取客户端连接对象
     */
    @Before
    public void testClient()throws Exception{
        //获取settings设置对象
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();

        TransportAddress worker01 = new TransportAddress(InetAddress.getByName("202.204.124.135"), 9300);

        TransportAddress worker02 = new TransportAddress(InetAddress.getByName("202.204.124.137"), 9300);

        TransportAddress worker03 = new TransportAddress(InetAddress.getByName("202.204.124.138"), 9300);

        client =new PreBuiltTransportClient(settings)
                .addTransportAddress(worker01).addTransportAddress(worker02).addTransportAddress(worker03);
    }

    @After
    public void closeClient(){
        client.close();
    }

    @Test
    public void createIndex(){
        String json = "{\n" +
                "   \"user\":\"jiaxiangyu\",\n" +
                "   \"postDate\":\"2013-10-1\",\n" +
                "   \"message\":\"traving out es\"\n" +
                "}";
        IndexRequestBuilder indexRequestBuilder = client.prepareIndex("myindex1", "article", "21").setSource(json, XContentType.JSON);
        //调用get真正执行
        IndexResponse indexResponse = indexRequestBuilder.get();
    }

    @Test
    public void createIndex1(){
        Map<String,String> map = new HashMap<>();
        map.put("name","zhangsan");
        map.put("age","28");
        map.put("sex","0");
        IndexRequestBuilder indexRequestBuilder = client.prepareIndex("myindex1", "article", "2").setSource(map);
        indexRequestBuilder.get();
    }

    @Test
    public void createIndex3() throws Exception{
        IndexRequestBuilder indexRequestBuilder = client.prepareIndex("myindex1", "article", "3").
                setSource(new XContentFactory().jsonBuilder().startObject().field("name","lisi").endObject());
        indexRequestBuilder.get();
    }

    /**
     * 将JavaBean转换成json 最常用
     */
    @Test
    public void createIndex4(){
        Person person = new Person();
        person.setAddress("北京");
        person.setAge(20);
        person.setEmail("901213@qq.com");
        person.setPhone("13013425242");
        person.setSay("hello");
        String s = JSONObject.toJSONString(person);
        IndexRequestBuilder indexRequestBuilder = client.prepareIndex("myindex1", "article", "4").setSource(s, XContentType.JSON);
        indexRequestBuilder.get();
    }

    @Test
    public void addBatch(){
        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        Person person = new Person();
        person.setId(5);
        person.setAddress("上海");
        person.setAge(20);
        person.setEmail("901213@qq.com");
        person.setPhone("13013425242");
        person.setSay("hello");

        Person person1 = new Person();
        person1.setId(3);
        person1.setAddress("官洲");
        person1.setAge(20);
        person1.setEmail("901213@qq.com");
        person1.setPhone("13013425242");
        person1.setSay("hello");

        String s = JSONObject.toJSONString(person);
        String s1 = JSONObject.toJSONString(person1);

        IndexRequestBuilder indexRequestBuilder = client.prepareIndex("myindex1", "article", "5").setSource(s, XContentType.JSON);
        IndexRequestBuilder indexRequestBuilder1 = client.prepareIndex("myindex1", "article", "6").setSource(s1, XContentType.JSON);

        BulkRequestBuilder add = bulkRequestBuilder.add(indexRequestBuilder).add(indexRequestBuilder1);
        //获取
        add.get();
    }

    /**
     * 更新索引
     */
    @Test
    public void updateIndex(){
        HashMap<String,String>map = new HashMap<>();
        map.put("phone","159139910013");
        UpdateRequestBuilder updateRequestBuilder = client.prepareUpdate("myindex1", "article", "5").setDoc(map);
        updateRequestBuilder.get();
    }


    /**
     * 删除索引 id
     */
    @Test
    public void deleteIndexById(){
        DeleteResponse deleteResponse = client.prepareDelete("myindex1", "article", "5").get();
    }

    /**
     * 删除整个索引
     */
    @Test
    public void deleteIndex(){
        client.admin().indices().prepareDelete("myindex1").execute().actionGet();
    }

    /**
     * 查询索引 根据id
     */
    @Test
    public void queryIndex(){
        GetResponse documentFields = client.prepareGet("test", "ecodata", "13").get();
        String index = documentFields.getIndex();
        String type = documentFields.getType();
        String id = documentFields.getId();
        System.out.println(index);
        System.out.println(type);
        System.out.println(id);
        Map<String, Object> source = documentFields.getSource();

        for(String s:source.keySet()){
            System.out.println(source.get(s));
        }
    }

    /**
     * 查询所有数据
     */
    @Test
    public void queryIndexAll(){
        SearchResponse searchResponse = client.prepareSearch("school").setTypes("student").setQuery(new MatchAllQueryBuilder()).get();
        SearchHits hits = searchResponse.getHits();
        SearchHit[] hits1 = hits.getHits();
        for(SearchHit hit:hits1){
            String sourceAsString = hit.getSourceAsString();
            System.out.println(sourceAsString);
        }
    }

    /**
     * 查询 RangeQuery 范围
     * 年龄在20到30的人
     */
    @Test
    public void rangeQuery(){
        SearchResponse searchResponse = client.prepareSearch("school").setTypes("student").setQuery(new RangeQueryBuilder("age").lt(30).gt(20)).get();
        SearchHits hits = searchResponse.getHits();
        SearchHit[] hits1 = hits.getHits();
        for(SearchHit hit:hits1){
            String sourceAsString = hit.getSourceAsString();
            System.out.println(sourceAsString);
        }
    }

    /**
     * 词条查询
     */
    @Test
    public void termQuery(){
        SearchResponse searchResponse = client.prepareSearch("school").setTypes("student").setQuery(new TermQueryBuilder("about", "li")).get();
        SearchHits hits = searchResponse.getHits();
        SearchHit[] hits1 = hits.getHits();
        for(SearchHit hit:hits1){
            String sourceAsString = hit.getSourceAsString();
            System.out.println(sourceAsString);
        }
    }

    /**
     * 模糊查询
     */
    @Test
    public void fuzzyQuery(){
        SearchResponse searchResponse = client.prepareSearch("school").setTypes("student").setQuery(QueryBuilders.fuzzyQuery("about", "li")).get();
        SearchHits hits = searchResponse.getHits();
        SearchHit[] hits1 = hits.getHits();
        for(SearchHit hit:hits1){
            String sourceAsString = hit.getSourceAsString();
            System.out.println(sourceAsString);
        }
    }

    /**
     * 分页查询
     */
    @Test
    public void getPageIndex(){
        int pageSize = 5;
        int pageNum = 2;
        //计算初试页
        int startNum = (pageNum-1) * pageSize;
        SearchResponse searchResponse = client.prepareSearch("school").setTypes("student").setQuery(QueryBuilders.matchAllQuery()).addSort("age", SortOrder.ASC).setFrom(startNum).setSize(pageSize).get();
        SearchHits hits = searchResponse.getHits();
        SearchHit[] hits1 = hits.getHits();
        for(SearchHit hit:hits1){
            String sourceAsString = hit.getSourceAsString();
            System.out.println(sourceAsString);
        }
    }

    /**
     * 高亮查询
     */
    @Test
    public void highLight(){
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("school").setTypes("student").setQuery(QueryBuilders.termQuery("sex", "boy"));
        //通过HighlightBuilder 来显示
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("sex").preTags("<font style='color:red'>").preTags("</font>");
        SearchResponse searchResponse = searchRequestBuilder.highlighter(highlightBuilder).get();
        SearchHits hits = searchResponse.getHits();
        SearchHit[] hits1 = hits.getHits();
        for(SearchHit hit:hits1){
            System.out.println(hit.getSourceAsString());
            Text[] sexes = hit.getHighlightFields().get("sex").getFragments();
            for(Text say:sexes){
                System.out.println(say);
            }

        }
    }
}
