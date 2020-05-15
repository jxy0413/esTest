package cn.es.util;

import cn.es.bean.Article;
import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.Transport;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther jxy
 * @Date 2020-05-14
 */
public class Es2Hbase {
    private static String HBASE_TABLENAME ="eshbase";
    private static String HBASE_COLUMNENAME ="f1";

    public static void main(String[] args) throws Exception{
        //第一步 解析excel数据
        List<Article> articleList = ExcelUtil.getArticleList();
        Table hbaseTable = getHbaseTable();
        //第二步 将excel数据保存到hbase和es里面去
        TransportClient client = getClient();
//        //将数据保存到es里面去 通过批量保存的方式
//        saveToEs(articleList,client);
//
//        client.close();
        //saveToHbase(articleList,hbaseTable);
        //第三步  搜索关键字 es中的标题
        //搜索es当中title关键字  谷歌
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title", "谷歌");
        SearchResponse searchResponse = client.prepareSearch("articles").setTypes("article").setQuery(termQueryBuilder).get();

        SearchHits hits = searchResponse.getHits();
        //获取搜索结果的每条数据
        SearchHit[] hits1 = hits.getHits();

        for(SearchHit searchHit:hits){
            String id = searchHit.getId();
            System.out.println(id);
            String sourceAsString = searchHit.getSourceAsString();
            System.out.println(sourceAsString);
        }

        //获取到hbase 中 es id为1的值
        Get get = new Get("1".getBytes());
        Result result = hbaseTable.get(get);
        Cell[] cells = result.rawCells();
        for(Cell cell:cells){
            String cellStr = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
            System.out.println(cellStr);
        }
        hbaseTable.close();

    }


    public static void saveToHbase(List<Article>articleList,Table hbaseTable)throws IOException{
        List<Put> listPut = new ArrayList<>();
        for(Article article:articleList){
            Put put = new Put(article.getId().getBytes());
            put.addColumn(HBASE_COLUMNENAME.getBytes(),"title".getBytes(),article.getTitle().getBytes());
            put.addColumn(HBASE_COLUMNENAME.getBytes(),"from".getBytes(),article.getFrom().getBytes());
            put.addColumn(HBASE_COLUMNENAME.getBytes(),"times".getBytes(),article.getTimes().getBytes());
            put.addColumn(HBASE_COLUMNENAME.getBytes(),"readCounts".getBytes(),article.getReadCounts().getBytes());
            put.addColumn(HBASE_COLUMNENAME.getBytes(),"content".getBytes(),article.getContent().getBytes());
            listPut.add(put);
        }
        hbaseTable.put(listPut);
    }



    public static TransportClient getClient ()throws UnknownHostException {
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();

        TransportAddress worker01 = new TransportAddress(InetAddress.getByName("202.204.124.135"), 9300);

        TransportAddress worker02 = new TransportAddress(InetAddress.getByName("202.204.124.137"), 9300);

        TransportAddress worker03 = new TransportAddress(InetAddress.getByName("202.204.124.138"), 9300);

        TransportClient client =new PreBuiltTransportClient(settings)
                .addTransportAddress(worker01).addTransportAddress(worker02).addTransportAddress(worker03);

        return client;
    }

    public static void saveToEs(List<Article>articleList,TransportClient client){
        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        for(Article article:articleList){
            JSONObject jsonObject = new JSONObject();
            String jsonArticle = jsonObject.toJSONString(article);
            //将对象转为
            IndexRequestBuilder indexRequestBuilder = client.prepareIndex("articles", "article", article.getId()).setSource(jsonArticle, XContentType.JSON);
            bulkRequestBuilder.add(indexRequestBuilder);
        }
        //触发真正执行
        bulkRequestBuilder.get();
    }


    public static Table getHbaseTable()throws IOException {
        //将数据保存到hbase里面去，获取hbase的客户端连接
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum","101.37.35.14");
        conf.set("hbase.zookeeper.property.clientPort", "2182");
        Connection connection = ConnectionFactory.createConnection(conf);
        System.out.println(connection);
        System.out.println(connection.getAdmin());
        System.out.println(connection.getAdmin().getClusterStatus());
        //将数据保存到hbase的eshbase这张表去
        Admin admin = connection.getAdmin();
        //如果hbase表不存在 就进行创建
        if(!admin.tableExists(TableName.valueOf(HBASE_TABLENAME))){
            //表不存在 我们就创建
            HTableDescriptor tableDescriptors = new HTableDescriptor(TableName.valueOf(HBASE_TABLENAME));
            //为我们的表指定列名
            HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(HBASE_COLUMNENAME);
            tableDescriptors.addFamily(hColumnDescriptor);
            admin.createTable(tableDescriptors);
        }

        Table table = connection.getTable(TableName.valueOf(HBASE_TABLENAME));
        System.out.println(table);
        return table;
    }
}
