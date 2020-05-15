package cn.es.service;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.stereotype.Service;

import java.net.InetAddress;

/**
 * @Auther jxy
 * @Date 2020-05-13
 */
@Service
public class EsService {
    public void queryEs(String stationId,String datatypeId)throws Exception{
        TransportClient client =null;
        //获取settings设置对象
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();

        TransportAddress worker01 = new TransportAddress(InetAddress.getByName("202.204.124.135"), 9300);

        TransportAddress worker02 = new TransportAddress(InetAddress.getByName("202.204.124.137"), 9300);

        TransportAddress worker03 = new TransportAddress(InetAddress.getByName("202.204.124.138"), 9300);

        client =new PreBuiltTransportClient(settings)
                .addTransportAddress(worker01).addTransportAddress(worker02).addTransportAddress(worker03);

        long time1 = System.currentTimeMillis();
        SearchResponse searchResponse = client.prepareSearch("fdcp-dev").setTypes("ecodata").
                setQuery(QueryBuilders.matchAllQuery()).
                setQuery(QueryBuilders.boolQuery().
                        must(QueryBuilders.matchQuery("station_id", stationId)).
                        must(QueryBuilders.matchQuery("datatype_id", datatypeId))).get();
        SearchHits hits = searchResponse.getHits();
        SearchHit[] hits1 = hits.getHits();
        for(SearchHit hit:hits1){
            String sourceAsString = hit.getSourceAsString();
            System.out.println(sourceAsString);
        }
        long time2 = System.currentTimeMillis();
        Long time =  ((time2 - time1));
        System.out.println("执行了："+time+"毫秒！");

    }
}
