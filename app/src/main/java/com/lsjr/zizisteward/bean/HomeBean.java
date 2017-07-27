package com.lsjr.zizisteward.bean;

import java.util.List;

/**
 * Created by admin on 2017/5/17.
 */

public class HomeBean {


    /**
     * error : 1
     * advertisements : [{"content":"","details_editor":false,"entityId":1,"file_format":"","file_size":"","id":1,"image_filename":"/images?uuid=804c3706-4056-4547-a053-26b5c8889919","is_link_enabled":false,"is_use":false,"location":"","no":"","persistent":false,"resolution":"","shop_id":"","target":2,"time":"","url":"/banner/indexbanner"},{"content":"","details_editor":false,"entityId":9,"file_format":"","file_size":"","id":9,"image_filename":"/images?uuid=524f96a6-bcae-457b-ac24-e4d926f10ce9","is_link_enabled":false,"is_use":false,"location":"","no":"","persistent":false,"resolution":"","shop_id":"","target":2,"time":"","url":"/banner/indexbannertwo?id=9"}]
     * diligent_recommend : [{"ad_areas_id":0,"article_number":"","audit":0,"bname":"","cost_price":0,"entityId":610,"feature_item":"","hotel_level":0,"id":610,"is_approve":0,"is_particularly_recommend":0,"mname":"","persistent":false,"putaway":0,"samount":0,"sbin":0,"sbrand":0,"scolour":"","scount":0,"sell_points":"","shop_address":"","shop_type":0,"shot":0,"simg":"","sinfo":"","sisrec":0,"size":"","skeyword":"座椅|爱情故事","smarktime":"","sname":"Claretta Miniforms座椅","snew":0,"spic":"","spicfirst":"/images?uuid=906537e4-5058-4ab0-9104-c71562f67a2d","spid":0,"sprice":6400,"stime":"","tdapid":0,"themeId":0,"theme_name":"","tname":"","tpid":0},{"ad_areas_id":0,"article_number":"","audit":0,"bname":"","cost_price":0,"entityId":588,"feature_item":"","hotel_level":0,"id":588,"is_approve":0,"is_particularly_recommend":0,"mname":"","persistent":false,"putaway":0,"samount":0,"sbin":0,"sbrand":0,"scolour":"","scount":0,"sell_points":"","shop_address":"","shop_type":0,"shot":0,"simg":"","sinfo":"","sisrec":0,"size":"","skeyword":"皮椅","smarktime":"","sname":"Dumbo Miniforms皮质餐椅","snew":0,"spic":"","spicfirst":"/images?uuid=71521aca-e89f-4da9-9b8e-be5aded17264","spid":0,"sprice":3800,"stime":"","tdapid":0,"themeId":0,"theme_name":"","tname":"","tpid":0},{"ad_areas_id":0,"article_number":"","audit":0,"bname":"","cost_price":0,"entityId":500,"feature_item":"","hotel_level":0,"id":500,"is_approve":0,"is_particularly_recommend":0,"mname":"","persistent":false,"putaway":0,"samount":0,"sbin":0,"sbrand":0,"scolour":"","scount":0,"sell_points":"","shop_address":"","shop_type":0,"shot":0,"simg":"","sinfo":"","sisrec":0,"size":"","skeyword":"古驰手提包","smarktime":"","sname":"Dionysus 真皮竹节手提包","snew":0,"spic":"","spicfirst":"/images?uuid=4c8c7f44-e8da-44cf-bf0c-581213908936","spid":0,"sprice":18675,"stime":"","tdapid":0,"themeId":0,"theme_name":"","tname":"","tpid":0},{"ad_areas_id":0,"article_number":"","audit":0,"bname":"","cost_price":0,"entityId":509,"feature_item":"","hotel_level":0,"id":509,"is_approve":0,"is_particularly_recommend":0,"mname":"","persistent":false,"putaway":0,"samount":0,"sbin":0,"sbrand":0,"scolour":"","scount":0,"sell_points":"","shop_address":"","shop_type":0,"shot":0,"simg":"","sinfo":"","sisrec":0,"size":"","skeyword":"古驰手提包","smarktime":"","sname":"Gucci/古驰 Lilith真皮手提包","snew":0,"spic":"","spicfirst":"/images?uuid=b1da7d67-b812-4019-946d-7e43becfb4c6","spid":0,"sprice":26250,"stime":"","tdapid":0,"themeId":0,"theme_name":"","tname":"","tpid":0}]
     * msg : 首页查询成功
     * shouImg : {"diligentFood":"/images?uuid=569d8899-e209-4849-b155-6507b58f3614","diligentActivity":"/images?uuid=dccac206-07d3-4576-8536-e22f4315b826"}
     * homePageMapData : {"diligentFood":[{"ad_areas_id":0,"areas_name":"","article_number":"","audit":0,"bname":"","city_id":0,"citys_name":"","cost_price":0,"entityId":797,"feature_item":"","hotel_level":0,"id":797,"is_approve":0,"mname":"","persistent":false,"putaway":0,"samount":0,"sbrand":0,"scolour":"","scount":0,"sell_points":"","shop_address":"","shop_type":0,"shot":0,"simg":"","sinfo":"","sisrec":0,"size":"","skeyword":"餐饮|饭店|中餐|美食","smarktime":"","sname":"武汉金悦食府","snew":0,"spic":"","spicfirst":"/images?uuid=9a27e0c7-86d6-4505-b35b-1715f4d204ea","spid":0,"sprice":300,"stime":"","themeId":0,"theme_name":"","tname":"","tpid":0},{"ad_areas_id":0,"areas_name":"","article_number":"","audit":0,"bname":"","city_id":0,"citys_name":"","cost_price":0,"entityId":748,"feature_item":"","hotel_level":0,"id":748,"is_approve":0,"mname":"","persistent":false,"putaway":0,"samount":0,"sbrand":0,"scolour":"","scount":0,"sell_points":"","shop_address":"","shop_type":0,"shot":0,"simg":"","sinfo":"","sisrec":0,"size":"","skeyword":"茶道第一会","smarktime":"","sname":"陆羽会·茗茶第一会","snew":0,"spic":"","spicfirst":"/images?uuid=c136654a-2ad8-4b69-a24f-2f5a460e2cc9","spid":0,"sprice":300,"stime":"","themeId":0,"theme_name":"","tname":"","tpid":0},{"ad_areas_id":0,"areas_name":"","article_number":"","audit":0,"bname":"","city_id":0,"citys_name":"","cost_price":0,"entityId":512,"feature_item":"","hotel_level":0,"id":512,"is_approve":0,"mname":"","persistent":false,"putaway":0,"samount":0,"sbrand":0,"scolour":"","scount":0,"sell_points":"","shop_address":"","shop_type":0,"shot":0,"simg":"","sinfo":"","sisrec":0,"size":"","skeyword":"特色料理","smarktime":"","sname":"万达瑞华 · \u201c和\u201d日本餐厅","snew":0,"spic":"","spicfirst":"/images?uuid=0be04c26-e8d5-4f7b-be10-25b8c9158767","spid":0,"sprice":737,"stime":"","themeId":0,"theme_name":"","tname":"","tpid":0},{"ad_areas_id":0,"areas_name":"","article_number":"","audit":0,"bname":"","city_id":0,"citys_name":"","cost_price":0,"entityId":713,"feature_item":"","hotel_level":0,"id":713,"is_approve":0,"mname":"","persistent":false,"putaway":0,"samount":0,"sbrand":0,"scolour":"","scount":0,"sell_points":"","shop_address":"","shop_type":0,"shot":0,"simg":"","sinfo":"","sisrec":0,"size":"","skeyword":"藏龙岛心 汤逊湖畔","smarktime":"","sname":"滨湖美景酒店中餐厅","snew":0,"spic":"","spicfirst":"/images?uuid=f31ad5cc-47a3-4d90-aba2-339549b9fe43","spid":0,"sprice":150,"stime":"","themeId":0,"theme_name":"","tname":"","tpid":0}],"diligentPierre":[{"ad_areas_id":0,"article_number":"","audit":0,"bname":"","cost_price":0,"entityId":610,"feature_item":"","hotel_level":0,"id":610,"is_approve":0,"is_particularly_recommend":0,"mname":"","persistent":false,"putaway":0,"samount":0,"sbin":0,"sbrand":0,"scolour":"","scount":0,"sell_points":"","shop_address":"","shop_type":0,"shot":0,"simg":"","sinfo":"","sisrec":0,"size":"","skeyword":"座椅|爱情故事","smarktime":"","sname":"Claretta Miniforms座椅","snew":0,"spic":"","spicfirst":"/images?uuid=906537e4-5058-4ab0-9104-c71562f67a2d","spid":0,"sprice":6400,"stime":"","tdapid":0,"themeId":0,"theme_name":"","tname":"","tpid":0},{"ad_areas_id":0,"article_number":"","audit":0,"bname":"","cost_price":0,"entityId":588,"feature_item":"","hotel_level":0,"id":588,"is_approve":0,"is_particularly_recommend":0,"mname":"","persistent":false,"putaway":0,"samount":0,"sbin":0,"sbrand":0,"scolour":"","scount":0,"sell_points":"","shop_address":"","shop_type":0,"shot":0,"simg":"","sinfo":"","sisrec":0,"size":"","skeyword":"皮椅","smarktime":"","sname":"Dumbo Miniforms皮质餐椅","snew":0,"spic":"","spicfirst":"/images?uuid=71521aca-e89f-4da9-9b8e-be5aded17264","spid":0,"sprice":3800,"stime":"","tdapid":0,"themeId":0,"theme_name":"","tname":"","tpid":0},{"ad_areas_id":0,"article_number":"","audit":0,"bname":"","cost_price":0,"entityId":500,"feature_item":"","hotel_level":0,"id":500,"is_approve":0,"is_particularly_recommend":0,"mname":"","persistent":false,"putaway":0,"samount":0,"sbin":0,"sbrand":0,"scolour":"","scount":0,"sell_points":"","shop_address":"","shop_type":0,"shot":0,"simg":"","sinfo":"","sisrec":0,"size":"","skeyword":"古驰手提包","smarktime":"","sname":"Dionysus 真皮竹节手提包","snew":0,"spic":"","spicfirst":"/images?uuid=4c8c7f44-e8da-44cf-bf0c-581213908936","spid":0,"sprice":18675,"stime":"","tdapid":0,"themeId":0,"theme_name":"","tname":"","tpid":0},{"ad_areas_id":0,"article_number":"","audit":0,"bname":"","cost_price":0,"entityId":509,"feature_item":"","hotel_level":0,"id":509,"is_approve":0,"is_particularly_recommend":0,"mname":"","persistent":false,"putaway":0,"samount":0,"sbin":0,"sbrand":0,"scolour":"","scount":0,"sell_points":"","shop_address":"","shop_type":0,"shot":0,"simg":"","sinfo":"","sisrec":0,"size":"","skeyword":"古驰手提包","smarktime":"","sname":"Gucci/古驰 Lilith真皮手提包","snew":0,"spic":"","spicfirst":"/images?uuid=b1da7d67-b812-4019-946d-7e43becfb4c6","spid":0,"sprice":26250,"stime":"","tdapid":0,"themeId":0,"theme_name":"","tname":"","tpid":0}],"diligentActivity":[{"ad_areas_id":0,"areas_name":"","article_number":"","audit":0,"bname":"","city_id":0,"citys_name":"","cost_price":0,"entityId":607,"feature_item":"","hotel_level":0,"id":607,"is_approve":0,"mname":"","persistent":false,"putaway":0,"samount":0,"sbrand":0,"scolour":"","scount":0,"sell_points":"","shop_address":"","shop_type":0,"shot":0,"simg":"","sinfo":"","sisrec":0,"size":"","skeyword":"孜孜管家","smarktime":"","sname":"产品上线发布会","snew":0,"spic":"","spicfirst":"/images?uuid=dcd068e2-aa73-4e93-bbb0-9bab6b7da63a","spid":0,"sprice":200,"stime":"","themeId":0,"theme_name":"","tname":"","tpid":0},{"ad_areas_id":0,"areas_name":"","article_number":"","audit":0,"bname":"","city_id":0,"citys_name":"","cost_price":0,"entityId":557,"feature_item":"","hotel_level":0,"id":557,"is_approve":0,"mname":"","persistent":false,"putaway":0,"samount":0,"sbrand":0,"scolour":"","scount":0,"sell_points":"","shop_address":"","shop_type":0,"shot":0,"simg":"","sinfo":"","sisrec":0,"size":"","skeyword":"孜孜管家","smarktime":"","sname":"招商大会隆重举行","snew":0,"spic":"","spicfirst":"/images?uuid=aef9eb8a-9d41-46cf-a24d-594d47216a3e","spid":0,"sprice":200,"stime":"","themeId":0,"theme_name":"","tname":"","tpid":0}]}
     */

    private String error;
    private String msg;
    private ShouImgBean shouImg;
    private HomePageMapDataBean homePageMapData;
    private List<AdvertisementsBean> advertisements;
    private List<DiligentRecommendBean> diligent_recommend;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ShouImgBean getShouImg() {
        return shouImg;
    }

    public void setShouImg(ShouImgBean shouImg) {
        this.shouImg = shouImg;
    }

    public HomePageMapDataBean getHomePageMapData() {
        return homePageMapData;
    }

    public void setHomePageMapData(HomePageMapDataBean homePageMapData) {
        this.homePageMapData = homePageMapData;
    }

    public List<AdvertisementsBean> getAdvertisements() {
        return advertisements;
    }

    public void setAdvertisements(List<AdvertisementsBean> advertisements) {
        this.advertisements = advertisements;
    }

    public List<DiligentRecommendBean> getDiligent_recommend() {
        return diligent_recommend;
    }

    public void setDiligent_recommend(List<DiligentRecommendBean> diligent_recommend) {
        this.diligent_recommend = diligent_recommend;
    }

    public static class ShouImgBean {
        /**
         * diligentFood : /images?uuid=569d8899-e209-4849-b155-6507b58f3614
         * diligentActivity : /images?uuid=dccac206-07d3-4576-8536-e22f4315b826
         */

        private String diligentFood;
        private String diligentActivity;

        public String getDiligentFood() {
            return diligentFood;
        }

        public void setDiligentFood(String diligentFood) {
            this.diligentFood = diligentFood;
        }

        public String getDiligentActivity() {
            return diligentActivity;
        }

        public void setDiligentActivity(String diligentActivity) {
            this.diligentActivity = diligentActivity;
        }
    }

    public static class HomePageMapDataBean {
        private List<DiligentFoodBean> diligentFood;
        private List<DiligentPierreBean> diligentPierre;
        private List<DiligentActivityBean> diligentActivity;

        public List<DiligentFoodBean> getDiligentFood() {
            return diligentFood;
        }

        public void setDiligentFood(List<DiligentFoodBean> diligentFood) {
            this.diligentFood = diligentFood;
        }

        public List<DiligentPierreBean> getDiligentPierre() {
            return diligentPierre;
        }

        public void setDiligentPierre(List<DiligentPierreBean> diligentPierre) {
            this.diligentPierre = diligentPierre;
        }

        public List<DiligentActivityBean> getDiligentActivity() {
            return diligentActivity;
        }

        public void setDiligentActivity(List<DiligentActivityBean> diligentActivity) {
            this.diligentActivity = diligentActivity;
        }

        public static class DiligentFoodBean {
            /**
             * ad_areas_id : 0
             * areas_name :
             * article_number :
             * audit : 0
             * bname :
             * city_id : 0
             * citys_name :
             * cost_price : 0
             * entityId : 797
             * feature_item :
             * hotel_level : 0
             * id : 797
             * is_approve : 0
             * mname :
             * persistent : false
             * putaway : 0
             * samount : 0
             * sbrand : 0
             * scolour :
             * scount : 0
             * sell_points :
             * shop_address :
             * shop_type : 0
             * shot : 0
             * simg :
             * sinfo :
             * sisrec : 0
             * size :
             * skeyword : 餐饮|饭店|中餐|美食
             * smarktime :
             * sname : 武汉金悦食府
             * snew : 0
             * spic :
             * spicfirst : /images?uuid=9a27e0c7-86d6-4505-b35b-1715f4d204ea
             * spid : 0
             * sprice : 300
             * stime :
             * themeId : 0
             * theme_name :
             * tname :
             * tpid : 0
             */

            private int ad_areas_id;
            private String areas_name;
            private String article_number;
            private int audit;
            private String bname;
            private int city_id;
            private String citys_name;
            private int cost_price;
            private int entityId;
            private String feature_item;
            private int hotel_level;
            private int id;
            private int is_approve;
            private String mname;
            private boolean persistent;
            private int putaway;
            private int samount;
            private int sbrand;
            private String scolour;
            private int scount;
            private String sell_points;
            private String shop_address;
            private int shop_type;
            private int shot;
            private String simg;
            private String sinfo;
            private int sisrec;
            private String size;
            private String skeyword;
            private String smarktime;
            private String sname;
            private int snew;
            private String spic;
            private String spicfirst;
            private int spid;
            private int sprice;
            private String stime;
            private int themeId;
            private String theme_name;
            private String tname;
            private int tpid;

            public int getAd_areas_id() {
                return ad_areas_id;
            }

            public void setAd_areas_id(int ad_areas_id) {
                this.ad_areas_id = ad_areas_id;
            }

            public String getAreas_name() {
                return areas_name;
            }

            public void setAreas_name(String areas_name) {
                this.areas_name = areas_name;
            }

            public String getArticle_number() {
                return article_number;
            }

            public void setArticle_number(String article_number) {
                this.article_number = article_number;
            }

            public int getAudit() {
                return audit;
            }

            public void setAudit(int audit) {
                this.audit = audit;
            }

            public String getBname() {
                return bname;
            }

            public void setBname(String bname) {
                this.bname = bname;
            }

            public int getCity_id() {
                return city_id;
            }

            public void setCity_id(int city_id) {
                this.city_id = city_id;
            }

            public String getCitys_name() {
                return citys_name;
            }

            public void setCitys_name(String citys_name) {
                this.citys_name = citys_name;
            }

            public int getCost_price() {
                return cost_price;
            }

            public void setCost_price(int cost_price) {
                this.cost_price = cost_price;
            }

            public int getEntityId() {
                return entityId;
            }

            public void setEntityId(int entityId) {
                this.entityId = entityId;
            }

            public String getFeature_item() {
                return feature_item;
            }

            public void setFeature_item(String feature_item) {
                this.feature_item = feature_item;
            }

            public int getHotel_level() {
                return hotel_level;
            }

            public void setHotel_level(int hotel_level) {
                this.hotel_level = hotel_level;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getIs_approve() {
                return is_approve;
            }

            public void setIs_approve(int is_approve) {
                this.is_approve = is_approve;
            }

            public String getMname() {
                return mname;
            }

            public void setMname(String mname) {
                this.mname = mname;
            }

            public boolean isPersistent() {
                return persistent;
            }

            public void setPersistent(boolean persistent) {
                this.persistent = persistent;
            }

            public int getPutaway() {
                return putaway;
            }

            public void setPutaway(int putaway) {
                this.putaway = putaway;
            }

            public int getSamount() {
                return samount;
            }

            public void setSamount(int samount) {
                this.samount = samount;
            }

            public int getSbrand() {
                return sbrand;
            }

            public void setSbrand(int sbrand) {
                this.sbrand = sbrand;
            }

            public String getScolour() {
                return scolour;
            }

            public void setScolour(String scolour) {
                this.scolour = scolour;
            }

            public int getScount() {
                return scount;
            }

            public void setScount(int scount) {
                this.scount = scount;
            }

            public String getSell_points() {
                return sell_points;
            }

            public void setSell_points(String sell_points) {
                this.sell_points = sell_points;
            }

            public String getShop_address() {
                return shop_address;
            }

            public void setShop_address(String shop_address) {
                this.shop_address = shop_address;
            }

            public int getShop_type() {
                return shop_type;
            }

            public void setShop_type(int shop_type) {
                this.shop_type = shop_type;
            }

            public int getShot() {
                return shot;
            }

            public void setShot(int shot) {
                this.shot = shot;
            }

            public String getSimg() {
                return simg;
            }

            public void setSimg(String simg) {
                this.simg = simg;
            }

            public String getSinfo() {
                return sinfo;
            }

            public void setSinfo(String sinfo) {
                this.sinfo = sinfo;
            }

            public int getSisrec() {
                return sisrec;
            }

            public void setSisrec(int sisrec) {
                this.sisrec = sisrec;
            }

            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
            }

            public String getSkeyword() {
                return skeyword;
            }

            public void setSkeyword(String skeyword) {
                this.skeyword = skeyword;
            }

            public String getSmarktime() {
                return smarktime;
            }

            public void setSmarktime(String smarktime) {
                this.smarktime = smarktime;
            }

            public String getSname() {
                return sname;
            }

            public void setSname(String sname) {
                this.sname = sname;
            }

            public int getSnew() {
                return snew;
            }

            public void setSnew(int snew) {
                this.snew = snew;
            }

            public String getSpic() {
                return spic;
            }

            public void setSpic(String spic) {
                this.spic = spic;
            }

            public String getSpicfirst() {
                return spicfirst;
            }

            public void setSpicfirst(String spicfirst) {
                this.spicfirst = spicfirst;
            }

            public int getSpid() {
                return spid;
            }

            public void setSpid(int spid) {
                this.spid = spid;
            }

            public int getSprice() {
                return sprice;
            }

            public void setSprice(int sprice) {
                this.sprice = sprice;
            }

            public String getStime() {
                return stime;
            }

            public void setStime(String stime) {
                this.stime = stime;
            }

            public int getThemeId() {
                return themeId;
            }

            public void setThemeId(int themeId) {
                this.themeId = themeId;
            }

            public String getTheme_name() {
                return theme_name;
            }

            public void setTheme_name(String theme_name) {
                this.theme_name = theme_name;
            }

            public String getTname() {
                return tname;
            }

            public void setTname(String tname) {
                this.tname = tname;
            }

            public int getTpid() {
                return tpid;
            }

            public void setTpid(int tpid) {
                this.tpid = tpid;
            }
        }

        public static class DiligentPierreBean {
            /**
             * ad_areas_id : 0
             * article_number :
             * audit : 0
             * bname :
             * cost_price : 0
             * entityId : 610
             * feature_item :
             * hotel_level : 0
             * id : 610
             * is_approve : 0
             * is_particularly_recommend : 0
             * mname :
             * persistent : false
             * putaway : 0
             * samount : 0
             * sbin : 0
             * sbrand : 0
             * scolour :
             * scount : 0
             * sell_points :
             * shop_address :
             * shop_type : 0
             * shot : 0
             * simg :
             * sinfo :
             * sisrec : 0
             * size :
             * skeyword : 座椅|爱情故事
             * smarktime :
             * sname : Claretta Miniforms座椅
             * snew : 0
             * spic :
             * spicfirst : /images?uuid=906537e4-5058-4ab0-9104-c71562f67a2d
             * spid : 0
             * sprice : 6400
             * stime :
             * tdapid : 0
             * themeId : 0
             * theme_name :
             * tname :
             * tpid : 0
             */

            private int ad_areas_id;
            private String article_number;
            private int audit;
            private String bname;
            private int cost_price;
            private int entityId;
            private String feature_item;
            private int hotel_level;
            private int id;
            private int is_approve;
            private int is_particularly_recommend;
            private String mname;
            private boolean persistent;
            private int putaway;
            private int samount;
            private int sbin;
            private int sbrand;
            private String scolour;
            private int scount;
            private String sell_points;
            private String shop_address;
            private int shop_type;
            private int shot;
            private String simg;
            private String sinfo;
            private int sisrec;
            private String size;
            private String skeyword;
            private String smarktime;
            private String sname;
            private int snew;
            private String spic;
            private String spicfirst;
            private int spid;
            private int sprice;
            private String stime;
            private int tdapid;
            private int themeId;
            private String theme_name;
            private String tname;
            private int tpid;

            public int getAd_areas_id() {
                return ad_areas_id;
            }

            public void setAd_areas_id(int ad_areas_id) {
                this.ad_areas_id = ad_areas_id;
            }

            public String getArticle_number() {
                return article_number;
            }

            public void setArticle_number(String article_number) {
                this.article_number = article_number;
            }

            public int getAudit() {
                return audit;
            }

            public void setAudit(int audit) {
                this.audit = audit;
            }

            public String getBname() {
                return bname;
            }

            public void setBname(String bname) {
                this.bname = bname;
            }

            public int getCost_price() {
                return cost_price;
            }

            public void setCost_price(int cost_price) {
                this.cost_price = cost_price;
            }

            public int getEntityId() {
                return entityId;
            }

            public void setEntityId(int entityId) {
                this.entityId = entityId;
            }

            public String getFeature_item() {
                return feature_item;
            }

            public void setFeature_item(String feature_item) {
                this.feature_item = feature_item;
            }

            public int getHotel_level() {
                return hotel_level;
            }

            public void setHotel_level(int hotel_level) {
                this.hotel_level = hotel_level;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getIs_approve() {
                return is_approve;
            }

            public void setIs_approve(int is_approve) {
                this.is_approve = is_approve;
            }

            public int getIs_particularly_recommend() {
                return is_particularly_recommend;
            }

            public void setIs_particularly_recommend(int is_particularly_recommend) {
                this.is_particularly_recommend = is_particularly_recommend;
            }

            public String getMname() {
                return mname;
            }

            public void setMname(String mname) {
                this.mname = mname;
            }

            public boolean isPersistent() {
                return persistent;
            }

            public void setPersistent(boolean persistent) {
                this.persistent = persistent;
            }

            public int getPutaway() {
                return putaway;
            }

            public void setPutaway(int putaway) {
                this.putaway = putaway;
            }

            public int getSamount() {
                return samount;
            }

            public void setSamount(int samount) {
                this.samount = samount;
            }

            public int getSbin() {
                return sbin;
            }

            public void setSbin(int sbin) {
                this.sbin = sbin;
            }

            public int getSbrand() {
                return sbrand;
            }

            public void setSbrand(int sbrand) {
                this.sbrand = sbrand;
            }

            public String getScolour() {
                return scolour;
            }

            public void setScolour(String scolour) {
                this.scolour = scolour;
            }

            public int getScount() {
                return scount;
            }

            public void setScount(int scount) {
                this.scount = scount;
            }

            public String getSell_points() {
                return sell_points;
            }

            public void setSell_points(String sell_points) {
                this.sell_points = sell_points;
            }

            public String getShop_address() {
                return shop_address;
            }

            public void setShop_address(String shop_address) {
                this.shop_address = shop_address;
            }

            public int getShop_type() {
                return shop_type;
            }

            public void setShop_type(int shop_type) {
                this.shop_type = shop_type;
            }

            public int getShot() {
                return shot;
            }

            public void setShot(int shot) {
                this.shot = shot;
            }

            public String getSimg() {
                return simg;
            }

            public void setSimg(String simg) {
                this.simg = simg;
            }

            public String getSinfo() {
                return sinfo;
            }

            public void setSinfo(String sinfo) {
                this.sinfo = sinfo;
            }

            public int getSisrec() {
                return sisrec;
            }

            public void setSisrec(int sisrec) {
                this.sisrec = sisrec;
            }

            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
            }

            public String getSkeyword() {
                return skeyword;
            }

            public void setSkeyword(String skeyword) {
                this.skeyword = skeyword;
            }

            public String getSmarktime() {
                return smarktime;
            }

            public void setSmarktime(String smarktime) {
                this.smarktime = smarktime;
            }

            public String getSname() {
                return sname;
            }

            public void setSname(String sname) {
                this.sname = sname;
            }

            public int getSnew() {
                return snew;
            }

            public void setSnew(int snew) {
                this.snew = snew;
            }

            public String getSpic() {
                return spic;
            }

            public void setSpic(String spic) {
                this.spic = spic;
            }

            public String getSpicfirst() {
                return spicfirst;
            }

            public void setSpicfirst(String spicfirst) {
                this.spicfirst = spicfirst;
            }

            public int getSpid() {
                return spid;
            }

            public void setSpid(int spid) {
                this.spid = spid;
            }

            public int getSprice() {
                return sprice;
            }

            public void setSprice(int sprice) {
                this.sprice = sprice;
            }

            public String getStime() {
                return stime;
            }

            public void setStime(String stime) {
                this.stime = stime;
            }

            public int getTdapid() {
                return tdapid;
            }

            public void setTdapid(int tdapid) {
                this.tdapid = tdapid;
            }

            public int getThemeId() {
                return themeId;
            }

            public void setThemeId(int themeId) {
                this.themeId = themeId;
            }

            public String getTheme_name() {
                return theme_name;
            }

            public void setTheme_name(String theme_name) {
                this.theme_name = theme_name;
            }

            public String getTname() {
                return tname;
            }

            public void setTname(String tname) {
                this.tname = tname;
            }

            public int getTpid() {
                return tpid;
            }

            public void setTpid(int tpid) {
                this.tpid = tpid;
            }
        }

        public static class DiligentActivityBean {
            /**
             * ad_areas_id : 0
             * areas_name :
             * article_number :
             * audit : 0
             * bname :
             * city_id : 0
             * citys_name :
             * cost_price : 0
             * entityId : 607
             * feature_item :
             * hotel_level : 0
             * id : 607
             * is_approve : 0
             * mname :
             * persistent : false
             * putaway : 0
             * samount : 0
             * sbrand : 0
             * scolour :
             * scount : 0
             * sell_points :
             * shop_address :
             * shop_type : 0
             * shot : 0
             * simg :
             * sinfo :
             * sisrec : 0
             * size :
             * skeyword : 孜孜管家
             * smarktime :
             * sname : 产品上线发布会
             * snew : 0
             * spic :
             * spicfirst : /images?uuid=dcd068e2-aa73-4e93-bbb0-9bab6b7da63a
             * spid : 0
             * sprice : 200
             * stime :
             * themeId : 0
             * theme_name :
             * tname :
             * tpid : 0
             */

            private int ad_areas_id;
            private String areas_name;
            private String article_number;
            private int audit;
            private String bname;
            private int city_id;
            private String citys_name;
            private int cost_price;
            private int entityId;
            private String feature_item;
            private int hotel_level;
            private int id;
            private int is_approve;
            private String mname;
            private boolean persistent;
            private int putaway;
            private int samount;
            private int sbrand;
            private String scolour;
            private int scount;
            private String sell_points;
            private String shop_address;
            private int shop_type;
            private int shot;
            private String simg;
            private String sinfo;
            private int sisrec;
            private String size;
            private String skeyword;
            private String smarktime;
            private String sname;
            private int snew;
            private String spic;
            private String spicfirst;
            private int spid;
            private int sprice;
            private String stime;
            private int themeId;
            private String theme_name;
            private String tname;
            private int tpid;

            public int getAd_areas_id() {
                return ad_areas_id;
            }

            public void setAd_areas_id(int ad_areas_id) {
                this.ad_areas_id = ad_areas_id;
            }

            public String getAreas_name() {
                return areas_name;
            }

            public void setAreas_name(String areas_name) {
                this.areas_name = areas_name;
            }

            public String getArticle_number() {
                return article_number;
            }

            public void setArticle_number(String article_number) {
                this.article_number = article_number;
            }

            public int getAudit() {
                return audit;
            }

            public void setAudit(int audit) {
                this.audit = audit;
            }

            public String getBname() {
                return bname;
            }

            public void setBname(String bname) {
                this.bname = bname;
            }

            public int getCity_id() {
                return city_id;
            }

            public void setCity_id(int city_id) {
                this.city_id = city_id;
            }

            public String getCitys_name() {
                return citys_name;
            }

            public void setCitys_name(String citys_name) {
                this.citys_name = citys_name;
            }

            public int getCost_price() {
                return cost_price;
            }

            public void setCost_price(int cost_price) {
                this.cost_price = cost_price;
            }

            public int getEntityId() {
                return entityId;
            }

            public void setEntityId(int entityId) {
                this.entityId = entityId;
            }

            public String getFeature_item() {
                return feature_item;
            }

            public void setFeature_item(String feature_item) {
                this.feature_item = feature_item;
            }

            public int getHotel_level() {
                return hotel_level;
            }

            public void setHotel_level(int hotel_level) {
                this.hotel_level = hotel_level;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getIs_approve() {
                return is_approve;
            }

            public void setIs_approve(int is_approve) {
                this.is_approve = is_approve;
            }

            public String getMname() {
                return mname;
            }

            public void setMname(String mname) {
                this.mname = mname;
            }

            public boolean isPersistent() {
                return persistent;
            }

            public void setPersistent(boolean persistent) {
                this.persistent = persistent;
            }

            public int getPutaway() {
                return putaway;
            }

            public void setPutaway(int putaway) {
                this.putaway = putaway;
            }

            public int getSamount() {
                return samount;
            }

            public void setSamount(int samount) {
                this.samount = samount;
            }

            public int getSbrand() {
                return sbrand;
            }

            public void setSbrand(int sbrand) {
                this.sbrand = sbrand;
            }

            public String getScolour() {
                return scolour;
            }

            public void setScolour(String scolour) {
                this.scolour = scolour;
            }

            public int getScount() {
                return scount;
            }

            public void setScount(int scount) {
                this.scount = scount;
            }

            public String getSell_points() {
                return sell_points;
            }

            public void setSell_points(String sell_points) {
                this.sell_points = sell_points;
            }

            public String getShop_address() {
                return shop_address;
            }

            public void setShop_address(String shop_address) {
                this.shop_address = shop_address;
            }

            public int getShop_type() {
                return shop_type;
            }

            public void setShop_type(int shop_type) {
                this.shop_type = shop_type;
            }

            public int getShot() {
                return shot;
            }

            public void setShot(int shot) {
                this.shot = shot;
            }

            public String getSimg() {
                return simg;
            }

            public void setSimg(String simg) {
                this.simg = simg;
            }

            public String getSinfo() {
                return sinfo;
            }

            public void setSinfo(String sinfo) {
                this.sinfo = sinfo;
            }

            public int getSisrec() {
                return sisrec;
            }

            public void setSisrec(int sisrec) {
                this.sisrec = sisrec;
            }

            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
            }

            public String getSkeyword() {
                return skeyword;
            }

            public void setSkeyword(String skeyword) {
                this.skeyword = skeyword;
            }

            public String getSmarktime() {
                return smarktime;
            }

            public void setSmarktime(String smarktime) {
                this.smarktime = smarktime;
            }

            public String getSname() {
                return sname;
            }

            public void setSname(String sname) {
                this.sname = sname;
            }

            public int getSnew() {
                return snew;
            }

            public void setSnew(int snew) {
                this.snew = snew;
            }

            public String getSpic() {
                return spic;
            }

            public void setSpic(String spic) {
                this.spic = spic;
            }

            public String getSpicfirst() {
                return spicfirst;
            }

            public void setSpicfirst(String spicfirst) {
                this.spicfirst = spicfirst;
            }

            public int getSpid() {
                return spid;
            }

            public void setSpid(int spid) {
                this.spid = spid;
            }

            public int getSprice() {
                return sprice;
            }

            public void setSprice(int sprice) {
                this.sprice = sprice;
            }

            public String getStime() {
                return stime;
            }

            public void setStime(String stime) {
                this.stime = stime;
            }

            public int getThemeId() {
                return themeId;
            }

            public void setThemeId(int themeId) {
                this.themeId = themeId;
            }

            public String getTheme_name() {
                return theme_name;
            }

            public void setTheme_name(String theme_name) {
                this.theme_name = theme_name;
            }

            public String getTname() {
                return tname;
            }

            public void setTname(String tname) {
                this.tname = tname;
            }

            public int getTpid() {
                return tpid;
            }

            public void setTpid(int tpid) {
                this.tpid = tpid;
            }
        }
    }

    public static class AdvertisementsBean {
        /**
         * content :
         * details_editor : false
         * entityId : 1
         * file_format :
         * file_size :
         * id : 1
         * image_filename : /images?uuid=804c3706-4056-4547-a053-26b5c8889919
         * is_link_enabled : false
         * is_use : false
         * location :
         * no :
         * persistent : false
         * resolution :
         * shop_id :
         * target : 2
         * time :
         * url : /banner/indexbanner
         */

        private String content;
        private boolean details_editor;
        private int entityId;
        private String file_format;
        private String file_size;
        private int id;
        private String image_filename;
        private boolean is_link_enabled;
        private boolean is_use;
        private String location;
        private String no;
        private boolean persistent;
        private String resolution;
        private String shop_id;
        private int target;
        private String time;
        private String url;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public boolean isDetails_editor() {
            return details_editor;
        }

        public void setDetails_editor(boolean details_editor) {
            this.details_editor = details_editor;
        }

        public int getEntityId() {
            return entityId;
        }

        public void setEntityId(int entityId) {
            this.entityId = entityId;
        }

        public String getFile_format() {
            return file_format;
        }

        public void setFile_format(String file_format) {
            this.file_format = file_format;
        }

        public String getFile_size() {
            return file_size;
        }

        public void setFile_size(String file_size) {
            this.file_size = file_size;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImage_filename() {
            return image_filename;
        }

        public void setImage_filename(String image_filename) {
            this.image_filename = image_filename;
        }

        public boolean isIs_link_enabled() {
            return is_link_enabled;
        }

        public void setIs_link_enabled(boolean is_link_enabled) {
            this.is_link_enabled = is_link_enabled;
        }

        public boolean isIs_use() {
            return is_use;
        }

        public void setIs_use(boolean is_use) {
            this.is_use = is_use;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public boolean isPersistent() {
            return persistent;
        }

        public void setPersistent(boolean persistent) {
            this.persistent = persistent;
        }

        public String getResolution() {
            return resolution;
        }

        public void setResolution(String resolution) {
            this.resolution = resolution;
        }

        public String getShop_id() {
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
        }

        public int getTarget() {
            return target;
        }

        public void setTarget(int target) {
            this.target = target;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class DiligentRecommendBean {
        /**
         * ad_areas_id : 0
         * article_number :
         * audit : 0
         * bname :
         * cost_price : 0
         * entityId : 610
         * feature_item :
         * hotel_level : 0
         * id : 610
         * is_approve : 0
         * is_particularly_recommend : 0
         * mname :
         * persistent : false
         * putaway : 0
         * samount : 0
         * sbin : 0
         * sbrand : 0
         * scolour :
         * scount : 0
         * sell_points :
         * shop_address :
         * shop_type : 0
         * shot : 0
         * simg :
         * sinfo :
         * sisrec : 0
         * size :
         * skeyword : 座椅|爱情故事
         * smarktime :
         * sname : Claretta Miniforms座椅
         * snew : 0
         * spic :
         * spicfirst : /images?uuid=906537e4-5058-4ab0-9104-c71562f67a2d
         * spid : 0
         * sprice : 6400
         * stime :
         * tdapid : 0
         * themeId : 0
         * theme_name :
         * tname :
         * tpid : 0
         */

        private int ad_areas_id;
        private String article_number;
        private int audit;
        private String bname;
        private int cost_price;
        private int entityId;
        private String feature_item;
        private int hotel_level;
        private int id;
        private int is_approve;
        private int is_particularly_recommend;
        private String mname;
        private boolean persistent;
        private int putaway;
        private int samount;
        private int sbin;
        private int sbrand;
        private String scolour;
        private int scount;
        private String sell_points;
        private String shop_address;
        private int shop_type;
        private int shot;
        private String simg;
        private String sinfo;
        private int sisrec;
        private String size;
        private String skeyword;
        private String smarktime;
        private String sname;
        private int snew;
        private String spic;
        private String spicfirst;
        private int spid;
        private int sprice;
        private String stime;
        private int tdapid;
        private int themeId;
        private String theme_name;
        private String tname;
        private int tpid;

        public int getAd_areas_id() {
            return ad_areas_id;
        }

        public void setAd_areas_id(int ad_areas_id) {
            this.ad_areas_id = ad_areas_id;
        }

        public String getArticle_number() {
            return article_number;
        }

        public void setArticle_number(String article_number) {
            this.article_number = article_number;
        }

        public int getAudit() {
            return audit;
        }

        public void setAudit(int audit) {
            this.audit = audit;
        }

        public String getBname() {
            return bname;
        }

        public void setBname(String bname) {
            this.bname = bname;
        }

        public int getCost_price() {
            return cost_price;
        }

        public void setCost_price(int cost_price) {
            this.cost_price = cost_price;
        }

        public int getEntityId() {
            return entityId;
        }

        public void setEntityId(int entityId) {
            this.entityId = entityId;
        }

        public String getFeature_item() {
            return feature_item;
        }

        public void setFeature_item(String feature_item) {
            this.feature_item = feature_item;
        }

        public int getHotel_level() {
            return hotel_level;
        }

        public void setHotel_level(int hotel_level) {
            this.hotel_level = hotel_level;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIs_approve() {
            return is_approve;
        }

        public void setIs_approve(int is_approve) {
            this.is_approve = is_approve;
        }

        public int getIs_particularly_recommend() {
            return is_particularly_recommend;
        }

        public void setIs_particularly_recommend(int is_particularly_recommend) {
            this.is_particularly_recommend = is_particularly_recommend;
        }

        public String getMname() {
            return mname;
        }

        public void setMname(String mname) {
            this.mname = mname;
        }

        public boolean isPersistent() {
            return persistent;
        }

        public void setPersistent(boolean persistent) {
            this.persistent = persistent;
        }

        public int getPutaway() {
            return putaway;
        }

        public void setPutaway(int putaway) {
            this.putaway = putaway;
        }

        public int getSamount() {
            return samount;
        }

        public void setSamount(int samount) {
            this.samount = samount;
        }

        public int getSbin() {
            return sbin;
        }

        public void setSbin(int sbin) {
            this.sbin = sbin;
        }

        public int getSbrand() {
            return sbrand;
        }

        public void setSbrand(int sbrand) {
            this.sbrand = sbrand;
        }

        public String getScolour() {
            return scolour;
        }

        public void setScolour(String scolour) {
            this.scolour = scolour;
        }

        public int getScount() {
            return scount;
        }

        public void setScount(int scount) {
            this.scount = scount;
        }

        public String getSell_points() {
            return sell_points;
        }

        public void setSell_points(String sell_points) {
            this.sell_points = sell_points;
        }

        public String getShop_address() {
            return shop_address;
        }

        public void setShop_address(String shop_address) {
            this.shop_address = shop_address;
        }

        public int getShop_type() {
            return shop_type;
        }

        public void setShop_type(int shop_type) {
            this.shop_type = shop_type;
        }

        public int getShot() {
            return shot;
        }

        public void setShot(int shot) {
            this.shot = shot;
        }

        public String getSimg() {
            return simg;
        }

        public void setSimg(String simg) {
            this.simg = simg;
        }

        public String getSinfo() {
            return sinfo;
        }

        public void setSinfo(String sinfo) {
            this.sinfo = sinfo;
        }

        public int getSisrec() {
            return sisrec;
        }

        public void setSisrec(int sisrec) {
            this.sisrec = sisrec;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getSkeyword() {
            return skeyword;
        }

        public void setSkeyword(String skeyword) {
            this.skeyword = skeyword;
        }

        public String getSmarktime() {
            return smarktime;
        }

        public void setSmarktime(String smarktime) {
            this.smarktime = smarktime;
        }

        public String getSname() {
            return sname;
        }

        public void setSname(String sname) {
            this.sname = sname;
        }

        public int getSnew() {
            return snew;
        }

        public void setSnew(int snew) {
            this.snew = snew;
        }

        public String getSpic() {
            return spic;
        }

        public void setSpic(String spic) {
            this.spic = spic;
        }

        public String getSpicfirst() {
            return spicfirst;
        }

        public void setSpicfirst(String spicfirst) {
            this.spicfirst = spicfirst;
        }

        public int getSpid() {
            return spid;
        }

        public void setSpid(int spid) {
            this.spid = spid;
        }

        public int getSprice() {
            return sprice;
        }

        public void setSprice(int sprice) {
            this.sprice = sprice;
        }

        public String getStime() {
            return stime;
        }

        public void setStime(String stime) {
            this.stime = stime;
        }

        public int getTdapid() {
            return tdapid;
        }

        public void setTdapid(int tdapid) {
            this.tdapid = tdapid;
        }

        public int getThemeId() {
            return themeId;
        }

        public void setThemeId(int themeId) {
            this.themeId = themeId;
        }

        public String getTheme_name() {
            return theme_name;
        }

        public void setTheme_name(String theme_name) {
            this.theme_name = theme_name;
        }

        public String getTname() {
            return tname;
        }

        public void setTname(String tname) {
            this.tname = tname;
        }

        public int getTpid() {
            return tpid;
        }

        public void setTpid(int tpid) {
            this.tpid = tpid;
        }
    }
}
