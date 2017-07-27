package com.lsjr.zizisteward.bean;

import java.util.List;

public class Commodity {


    /**
     * FamousShop : [{"id":755,"sprice":2000,"spic":"/images?uuid=eee88b48-e757-4250-9b5f-85a0d3fbba8d","sname":"红帮裁缝西装定制 2016 Pitti Uomo风潮男士修身藏青色单西"},{"id":754,"sprice":6000,"spic":"/images?uuid=10d2dfd8-52c1-460b-8d07-b3ac229f4975","sname":"红帮裁缝 justin o'shea同款英国进口羊毛 短毛品质 百搭驼色风衣"},{"id":751,"sprice":4000,"spic":"/images?uuid=d2c0c279-f336-4716-9c92-ee974802263f","sname":"红帮裁缝 定制西装 男 西服 高端商务时尚休闲 单排平驳领 灰色百搭"},{"id":752,"sprice":4000,"spic":"/images?uuid=ad19678c-fccc-4e68-9a1d-1b1e17bfff67","sname":"红帮裁缝 定制西服 男 英国SCABAL 四季面料  复古双排枪驳领西服"},{"id":753,"sprice":22000,"spic":"/images?uuid=79592165-5f67-41b8-a9de-15c87d33a5f7","sname":"HORSSENS高级定制 纯手工丝绒礼服"},{"id":750,"sprice":16800,"spic":"/images?uuid=4f29d793-4de3-49bb-9c00-be4598fa7c46","sname":"HORSSENS高级定制 纯手工真丝西装"},{"id":587,"sprice":8888,"spic":"/images?uuid=2f797b6e-dd73-4708-81ce-501d8a63359a","sname":"HORSSENS高级定制 彩色斑点粗花呢四粒扣猎装"},{"id":586,"sprice":9800,"spic":"/images?uuid=701157f7-9cd6-4772-9025-ed9af06c7b38","sname":"HORSSENS高级定制 英格兰格子两粒扣拼接翻领西服"}]
     * error : 1
     * FamousType : [{"classify_type":0,"description":"","entityId":116,"id":116,"is_hobby":0,"persistent":false,"tgrade":-2,"tname":"服饰","tpath":1,"tpid":77,"tshow":true,"type_icon":"","type_icons":"","type_img":""},{"classify_type":0,"description":"","entityId":119,"id":119,"is_hobby":0,"persistent":false,"tgrade":-2,"tname":"鞋靴","tpath":2,"tpid":77,"tshow":true,"type_icon":"","type_icons":"","type_img":""},{"classify_type":0,"description":"","entityId":82,"id":82,"is_hobby":0,"persistent":false,"tgrade":-2,"tname":"箱包","tpath":3,"tpid":77,"tshow":true,"type_icon":"","type_icons":"","type_img":""},{"classify_type":0,"description":"","entityId":83,"id":83,"is_hobby":0,"persistent":false,"tgrade":-2,"tname":"配饰","tpath":6,"tpid":77,"tshow":true,"type_icon":"","type_icons":"","type_img":""},{"classify_type":0,"description":"","entityId":150,"id":150,"is_hobby":0,"persistent":false,"tgrade":-2,"tname":"家居","tpath":14,"tpid":77,"tshow":true,"type_icon":"","type_icons":"","type_img":""},{"classify_type":0,"description":"","entityId":121,"id":121,"is_hobby":0,"persistent":false,"tgrade":-2,"tname":"室内陈设","tpath":15,"tpid":77,"tshow":true,"type_icon":"","type_icons":"","type_img":""},{"classify_type":0,"description":"","entityId":131,"id":131,"is_hobby":0,"persistent":false,"tgrade":-2,"tname":"进口家具","tpath":16,"tpid":77,"tshow":true,"type_icon":"","type_icons":"","type_img":""},{"classify_type":0,"description":"","entityId":123,"id":123,"is_hobby":0,"persistent":false,"tgrade":-2,"tname":"器物定制","tpath":17,"tpid":77,"tshow":true,"type_icon":"","type_icons":"","type_img":""},{"classify_type":0,"description":"","entityId":126,"id":126,"is_hobby":0,"persistent":false,"tgrade":-2,"tname":"收藏品","tpath":18,"tpid":77,"tshow":true,"type_icon":"","type_icons":"","type_img":""},{"classify_type":0,"description":"","entityId":127,"id":127,"is_hobby":0,"persistent":false,"tgrade":-2,"tname":"茶道","tpath":31,"tpid":77,"tshow":true,"type_icon":"","type_icons":"","type_img":""},{"classify_type":0,"description":"","entityId":108,"id":108,"is_hobby":0,"persistent":false,"tgrade":-2,"tname":"酒庄","tpath":32,"tpid":77,"tshow":true,"type_icon":"","type_icons":"","type_img":""},{"classify_type":0,"description":"","entityId":124,"id":124,"is_hobby":0,"persistent":false,"tgrade":-2,"tname":"游艇","tpath":38,"tpid":77,"tshow":true,"type_icon":"","type_icons":"","type_img":""}]
     * msg : 查看名品分类列表！
     */

    private String error;
    private String msg;
    private List<FamousShopBean> FamousShop;
    private List<FamousTypeBean> FamousType;

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

    public List<FamousShopBean> getFamousShop() {
        return FamousShop;
    }

    public void setFamousShop(List<FamousShopBean> FamousShop) {
        this.FamousShop = FamousShop;
    }

    public List<FamousTypeBean> getFamousType() {
        return FamousType;
    }

    public void setFamousType(List<FamousTypeBean> FamousType) {
        this.FamousType = FamousType;
    }

    public static class FamousShopBean {
        /**
         * id : 755
         * sprice : 2000
         * spic : /images?uuid=eee88b48-e757-4250-9b5f-85a0d3fbba8d
         * sname : 红帮裁缝西装定制 2016 Pitti Uomo风潮男士修身藏青色单西
         */

        private int id;
        private int sprice;
        private String spic;
        private String sname;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSprice() {
            return sprice;
        }

        public void setSprice(int sprice) {
            this.sprice = sprice;
        }

        public String getSpic() {
            return spic;
        }

        public void setSpic(String spic) {
            this.spic = spic;
        }

        public String getSname() {
            return sname;
        }

        public void setSname(String sname) {
            this.sname = sname;
        }
    }

    public static class FamousTypeBean {
        /**
         * classify_type : 0
         * description :
         * entityId : 116
         * id : 116
         * is_hobby : 0
         * persistent : false
         * tgrade : -2
         * tname : 服饰
         * tpath : 1
         * tpid : 77
         * tshow : true
         * type_icon :
         * type_icons :
         * type_img :
         */

        private int classify_type;
        private String description;
        private int entityId;
        private int id;
        private int is_hobby;
        private boolean persistent;
        private int tgrade;
        private String tname;
        private int tpath;
        private int tpid;
        private boolean tshow;
        private String type_icon;
        private String type_icons;
        private String type_img;

        public int getClassify_type() {
            return classify_type;
        }

        public void setClassify_type(int classify_type) {
            this.classify_type = classify_type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getEntityId() {
            return entityId;
        }

        public void setEntityId(int entityId) {
            this.entityId = entityId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIs_hobby() {
            return is_hobby;
        }

        public void setIs_hobby(int is_hobby) {
            this.is_hobby = is_hobby;
        }

        public boolean isPersistent() {
            return persistent;
        }

        public void setPersistent(boolean persistent) {
            this.persistent = persistent;
        }

        public int getTgrade() {
            return tgrade;
        }

        public void setTgrade(int tgrade) {
            this.tgrade = tgrade;
        }

        public String getTname() {
            return tname;
        }

        public void setTname(String tname) {
            this.tname = tname;
        }

        public int getTpath() {
            return tpath;
        }

        public void setTpath(int tpath) {
            this.tpath = tpath;
        }

        public int getTpid() {
            return tpid;
        }

        public void setTpid(int tpid) {
            this.tpid = tpid;
        }

        public boolean isTshow() {
            return tshow;
        }

        public void setTshow(boolean tshow) {
            this.tshow = tshow;
        }

        public String getType_icon() {
            return type_icon;
        }

        public void setType_icon(String type_icon) {
            this.type_icon = type_icon;
        }

        public String getType_icons() {
            return type_icons;
        }

        public void setType_icons(String type_icons) {
            this.type_icons = type_icons;
        }

        public String getType_img() {
            return type_img;
        }

        public void setType_img(String type_img) {
            this.type_img = type_img;
        }
    }
}

