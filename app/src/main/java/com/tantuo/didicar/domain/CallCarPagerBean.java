package com.tantuo.didicar.domain;

import java.util.List;

/**
 * Author by TanTuo, 微信：18601949127,
 * Email:1991201740@qq.com
 * 作用：滴滴地图界面解析出来的Bean
 */
public class CallCarPagerBean {


    /**
     * retcode : 200
     * data : [{"id":10000,"title":"我要打车","type":1,"children":[{"id":10007,"title":"快车","type":1,"url":"/static/api/news/10007/list_1.json"},{"id":10006,"title":"公交","type":1,"url":"/static/api/news/10006/list_5.json"},{"id":10008,"title":"步行","type":1,"url":"/static/api/news/10008/list_7.json"},{"id":10014,"title":"安全攻坚","type":1,"url":"/static/api/news/10014/list_3.json"},{"id":10010,"title":"单车","type":1,"url":"/static/api/news/10010/list_3.json"},{"id":10091,"title":"代驾","type":1,"url":"/static/api/news/10091/list_1.json"},{"id":10012,"title":"关爱出行","type":1,"url":"/static/api/news/10012/list_3.json"},{"id":10095,"title":"顺风车","type":1,"url":"/static/api/news/10095/list_1.json"},{"id":10009,"title":"礼橙专车","type":1,"url":"/static/api/news/10009/list_3.json"},{"id":10011,"title":"代驾","type":1,"url":"/static/api/news/10011/list_3.json"},{"id":10093,"title":"车生活","type":1,"url":"/static/api/news/10093/list_1.json"},{"id":10192,"title":"豪华车","type":1,"url":"/static/api/news/10192/list_1.json"}]},{"id":10002,"title":"订单","type":10,"url":"/static/api/news/10002/list_1.json","url1":"/static/api/news/10002/list1_1.json"},{"id":10003,"title":"安全","type":2,"url":"/static/api/news/10003/list_1.json"},{"id":10004,"title":"客服","type":3,"excurl":"/static/api/news/comment/exc_1.json","dayurl":"/static/api/news/comment/day_1.json","weekurl":"/static/api/news/comment/week_1.json"},{"id":10005,"title":"设置","type":4,"url":"/static/api/news/vote/vote_1.json"}]
     * extend : [10007,10006,10008,10014,10091,10010,10192,10009,10095,10093,10012,10011]
     */

    private int retcode;
    private List<DataBean> data;
    private List<Integer> extend;

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public List<Integer> getExtend() {
        return extend;
    }

    public void setExtend(List<Integer> extend) {
        this.extend = extend;
    }

    public static class DataBean {
        /**
         * id : 10000
         * title : 我要打车
         * type : 1
         * children : [{"id":10007,"title":"快车","type":1,"url":"/static/api/news/10007/list_1.json"},{"id":10006,"title":"公交","type":1,"url":"/static/api/news/10006/list_5.json"},{"id":10008,"title":"步行","type":1,"url":"/static/api/news/10008/list_7.json"},{"id":10014,"title":"安全攻坚","type":1,"url":"/static/api/news/10014/list_3.json"},{"id":10010,"title":"单车","type":1,"url":"/static/api/news/10010/list_3.json"},{"id":10091,"title":"代驾","type":1,"url":"/static/api/news/10091/list_1.json"},{"id":10012,"title":"关爱出行","type":1,"url":"/static/api/news/10012/list_3.json"},{"id":10095,"title":"顺风车","type":1,"url":"/static/api/news/10095/list_1.json"},{"id":10009,"title":"礼橙专车","type":1,"url":"/static/api/news/10009/list_3.json"},{"id":10011,"title":"代驾","type":1,"url":"/static/api/news/10011/list_3.json"},{"id":10093,"title":"车生活","type":1,"url":"/static/api/news/10093/list_1.json"},{"id":10192,"title":"豪华车","type":1,"url":"/static/api/news/10192/list_1.json"}]
         * url : /static/api/news/10002/list_1.json
         * url1 : /static/api/news/10002/list1_1.json
         * excurl : /static/api/news/comment/exc_1.json
         * dayurl : /static/api/news/comment/day_1.json
         * weekurl : /static/api/news/comment/week_1.json
         */

        private int id;
        private String title;
        private int type;
        private String url;
        private String url1;
        private String excurl;
        private String dayurl;
        private String weekurl;
        private List<ChildrenBean> children;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrl1() {
            return url1;
        }

        public void setUrl1(String url1) {
            this.url1 = url1;
        }

        public String getExcurl() {
            return excurl;
        }

        public void setExcurl(String excurl) {
            this.excurl = excurl;
        }

        public String getDayurl() {
            return dayurl;
        }

        public void setDayurl(String dayurl) {
            this.dayurl = dayurl;
        }

        public String getWeekurl() {
            return weekurl;
        }

        public void setWeekurl(String weekurl) {
            this.weekurl = weekurl;
        }

        public List<ChildrenBean> getChildren() {
            return children;
        }

        public void setChildren(List<ChildrenBean> children) {
            this.children = children;
        }

        public static class ChildrenBean {
            /**
             * id : 10007
             * title : 快车
             * type : 1
             * url : /static/api/news/10007/list_1.json
             */

            private int id;
            private String title;
            private int type;
            private String url;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
