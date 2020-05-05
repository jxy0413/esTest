package cn.es.service;

/**
 * @Auther jxy
 * @Date 2020-04-30
 */
import cn.es.bean.Ecodata;
import cn.es.mapper.EcodataMapper;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.net.InetAddress;

/**
 * @Auther jxy
 * @Date 2020-04-30
 */
@Service
public class EcodataService {
    @Autowired
    private EcodataMapper ecodataMapper;

    public Ecodata get(Long datanumId,TransportClient client) throws Exception{
        //获取settings设置对;
        Ecodata ecodata = ecodataMapper.get(datanumId);
        String s = JSONObject.toJSONString(ecodata);
        IndexRequestBuilder indexRequestBuilder = client.prepareIndex("test", "ecodata", datanumId.toString()).setSource(s, XContentType.JSON);
        indexRequestBuilder.get();
        return ecodata;
    }


    public void get()throws Exception{
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();

        TransportAddress worker01 = new TransportAddress(InetAddress.getByName("202.204.124.135"), 9300);

        TransportAddress worker02 = new TransportAddress(InetAddress.getByName("202.204.124.137"), 9300);

        TransportAddress worker03 = new TransportAddress(InetAddress.getByName("202.204.124.138"), 9300);

        TransportClient client =new PreBuiltTransportClient(settings)
                .addTransportAddress(worker01).addTransportAddress(worker02).addTransportAddress(worker03);
        for(Long i=101L;i<5000000L;i++){
            System.out.println(i);
            get(i,client);

        }
    }
}
