package com.ash.whatever.bean;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * 新闻内容实体类，用于存储网络解析后的数据
 * Created by Corey on 2016/6/21.
 */
public class NewsBean {

    /**
     * code : 200
     * msg : success
     * newslist : [{"ctime":"2016-06-21 13:30","title":"吴建民乘车同行者：车祸发生时吴建民已经睡着","description":"网易社会","picUrl":"http://s.cimg.163.com/catchpic/E/EB/EBF5930EB6C08B9713EA5FEDE062FB09.jpg.119x83.jpg","url":"http://news.163.com/16/0621/13/BQ3BDAF900011229.html#f=slist"},{"ctime":"2016-06-21 13:34","title":"盲井案细节:嫌犯骗婚杀人 有人消失后回村盖房","description":"网易社会","picUrl":"http://s.cimg.163.com/catchpic/5/52/521D4174C816124049FB75667BD9E2FE.jpg.119x83.jpg","url":"http://news.163.com/16/0621/13/BQ3BL1BI0001124J.html#f=slist"},{"ctime":"2016-06-21 13:50","title":"广东枪案嫌犯疑有预谋 曾被威胁不还钱杀全家","description":"网易社会","picUrl":"http://s.cimg.163.com/catchpic/9/94/94B8DD772ACFA6F12C5B501EA8E789ED.jpg.119x83.jpg","url":"http://news.163.com/16/0621/13/BQ3CHI2L00011229.html#f=slist"},{"ctime":"2016-06-21 14:26","title":"江苏一小学2名学生患白血病 校方称跑道检测合格","description":"网易社会","picUrl":"http://s.cimg.163.com/catchpic/E/EF/EF441003271DF1B669CD798DC47CE2AC.jpg.119x83.jpg","url":"http://news.163.com/16/0621/14/BQ3EJDUI00011229.html#f=slist"},{"ctime":"2016-06-21 12:39","title":"湖北幼儿园校车与皮卡相撞侧翻 十余名儿童受伤","description":"网易社会","picUrl":"http://s.cimg.163.com/catchpic/B/B0/B0C98F9C648D19668C9B91DA99E3557E.JPG.119x83.jpg","url":"http://news.163.com/16/0621/12/BQ38EQA10001124J.html#f=slist"},{"ctime":"2016-06-21 12:52","title":"北京人艺表演艺术家韩善续病逝 曾出演西游记","description":"网易社会","picUrl":"http://s.cimg.163.com/catchpic/6/66/66EF0F018D8AE47B99665DEED6656ADC.jpg.119x83.jpg","url":"http://news.163.com/16/0621/12/BQ396VAI0001124J.html#f=slist"},{"ctime":"2016-06-21 13:07","title":"河北14岁初中生与同学发生争执 被对方捅死","description":"网易社会","picUrl":"http://s.cimg.163.com/cnews/2016/6/21/20160621125158f4b1a.jpg.119x83.jpg","url":"http://news.163.com/16/0621/13/BQ3A2GD900011229.html#f=slist"},{"ctime":"2016-06-21 13:10","title":"湖南男子涉嫌酒驾 当交警面大口喝酒即兴RAP","description":"网易社会","picUrl":"http://s.cimg.163.com/cnews/2016/6/21/201606211307030f53d.jpg.119x83.jpg","url":"http://news.163.com/16/0621/13/BQ3A7TLS00011229.html#f=slist"},{"ctime":"2016-06-21 13:12","title":"核心期刊不给学生联合署名，复旦导师怒撤论文：但求问","description":"网易社会","picUrl":"http://s.cimg.163.com/catchpic/E/EB/EBF5930EB6C08B9713EA5FEDE062FB09.jpg.119x83.jpg","url":"http://news.163.com/16/0621/13/BQ3AB2SB00014AED.html#f=slist"},{"ctime":"2016-06-21 11:31","title":"内蒙古科技大学一男生校园坠亡：排除他杀，被传论文答","description":"网易社会","picUrl":"","url":"http://news.163.com/16/0621/11/BQ34J0G800014AED.html#f=slist"},{"ctime":"2016-06-21 11:43","title":"男子进看守所4天死亡:患病且长时间被限体位","description":"网易社会","picUrl":"","url":"http://news.163.com/16/0621/11/BQ3599CO00011229.html#f=slist"},{"ctime":"2016-06-21 11:47","title":"男子低头看手机摔下路面身亡 家属获赔13.5万","description":"网易社会","picUrl":"http://s.cimg.163.com/catchpic/1/1D/1DA4304F80FB88156AF3297ADBDBC7A8.jpg.119x83.jpg","url":"http://news.163.com/16/0621/11/BQ35G6M100011229.html#f=slist"},{"ctime":"2016-06-21 11:51","title":"湖南高院官员回应上班开房被停职 :真相总会有的","description":"网易社会","picUrl":"http://s.cimg.163.com/cnews/2016/6/21/20160621102457b82c4_550.jpg.119x83.jpg","url":"http://news.163.com/16/0621/11/BQ35OBPH00014SEH.html#f=slist"},{"ctime":"2016-06-21 11:53","title":"重庆警方破获特大跨省制贩毒案 缴毒90余公斤","description":"网易社会","picUrl":"http://s.cimg.163.com/catchpic/B/B6/B6093C8A285DDCFF60F3D346F6E59BA9.jpg.119x83.jpg","url":"http://news.163.com/16/0621/11/BQ35RJV300014JB6.html#f=slist"},{"ctime":"2016-06-21 12:06","title":"6岁女童补6颗牙全出事 医生：已私了 病志已销毁","description":"网易社会","picUrl":"http://s.cimg.163.com/catchpic/C/C3/C3B58CE22B50B9915FCA897B4C9F0C62.jpg.119x83.jpg","url":"http://news.163.com/16/0621/12/BQ36I9SN00011229.html#f=slist"},{"ctime":"2016-06-21 12:20","title":"父亲多次对6岁女儿注射毒品 称为了让她不学坏","description":"网易社会","picUrl":"http://s.cimg.163.com/cnews/2016/6/21/2016062111420132104.jpg.119x83.jpg","url":"http://news.163.com/16/0621/12/BQ37CB5C00011229.html#f=slist"},{"ctime":"2016-06-21 12:21","title":"南昌万达广场三轮车司机为拒交警扣车 引燃自己","description":"网易社会","picUrl":"http://s.cimg.163.com/catchpic/B/B0/B0C98F9C648D19668C9B91DA99E3557E.JPG.119x83.jpg","url":"http://news.163.com/16/0621/12/BQ37FC2H00011229.html#f=slist"},{"ctime":"2016-06-21 10:38","title":"官方辟谣：动车车身印\u201c玉林狗肉节欢迎你\u201d属恶意炒作","description":"网易社会","picUrl":"http://s.cimg.163.com/catchpic/B/B6/B6093C8A285DDCFF60F3D346F6E59BA9.jpg.119x83.jpg","url":"http://news.163.com/16/0621/10/BQ31I7DP00014SEH.html#f=slist"},{"ctime":"2016-06-21 09:39","title":"少女被男子骗到西安逼迫卖淫 后将闺蜜骗入局","description":"网易社会","picUrl":"http://s.cimg.163.com/catchpic/F/F7/F7F020D24C806E701699A32849567653.jpg.119x83.jpg","url":"http://news.163.com/16/0621/09/BQ2U6APO00011229.html#f=slist"},{"ctime":"2016-06-21 09:49","title":"湖南高院法官被曝与多名女子开房，院方回应今日已展开","description":"网易社会","picUrl":"http://s.cimg.163.com/catchpic/4/43/43E59720F82A54F489C31598039AD7C1.jpg.119x83.jpg","url":"http://news.163.com/16/0621/09/BQ2UNU8C00014AED.html#f=slist"}]
     */

    private int code;
    private String msg;
    /**
     * ctime : 2016-06-21 13:30
     * title : 吴建民乘车同行者：车祸发生时吴建民已经睡着
     * description : 网易社会
     * picUrl : http://s.cimg.163.com/catchpic/E/EB/EBF5930EB6C08B9713EA5FEDE062FB09.jpg.119x83.jpg
     * url : http://news.163.com/16/0621/13/BQ3BDAF900011229.html#f=slist
     */

    private List<NewslistBean> newslist;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<NewslistBean> getNewslist() {
        return newslist;
    }

    public void setNewslist(List<NewslistBean> newslist) {
        this.newslist = newslist;
    }

    public static class NewslistBean extends DataSupport{
        private int id;
        private String ctime;
        private String title;
        private String description;
        private String picUrl;
        private String url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "NewslistBean{" +
                    "ctime='" + ctime + '\'' +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", picUrl='" + picUrl + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }
}
