package cn.es.util;

import cn.es.bean.Article;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther jxy
 * @Date 2020-05-13
 */
public class ExcelUtil {
    public static void main(String[] args)throws Exception {
        System.out.println(getArticleList());
    }

    public static List<Article> getArticleList()throws Exception{
        FileInputStream fileInputStream = new FileInputStream("/Users/jiaxiangyu/Desktop/学习公众号/elasticsearch/es.xlsx");
        //获取第一个sheet页
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);
        //获取第一个sheet页
        XSSFSheet sheetAt = xssfWorkbook.getSheetAt(0);
        //获取sheet最后
        int lastRowNum = sheetAt.getLastRowNum();
        List<Article> list =new ArrayList<>();
        //获取每一条数据
        for(int i=1;i<=lastRowNum;i++){
            //遍历数据
            XSSFRow row = sheetAt.getRow(i);
            String titile = row.getCell(0).toString();
            String source = row.getCell(1).toString();
            String times = row.getCell(2).toString();
            String readCounts = row.getCell(3).toString();
            String content = row.getCell(4).toString();
            Article article =new Article();

            article.setId(i+"");
            article.setTitle(titile);
            article.setFrom(source);
            article.setTimes(times);
            article.setReadCounts(readCounts);
            article.setContent(content);

            list.add(article);
        }
        fileInputStream.close();
        return list;
    }




}
