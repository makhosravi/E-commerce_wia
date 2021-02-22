package com.od.makh.myapplication.Model;

public class BuildingPostDetail
{

    /**
     * id : 7
     * userId : 34
     * categoryId : 121
     * city : اردبیل
     * area : 160
     * price :
     * orderType : ارائه
     * advertiserType : مشاور املاک
     * numOfRoom : سه
     * trust : 70000000
     * rent : 3500000
     * officialDoc :
     * adTitle : اجاره آپارتمان
     * adDate : 2019-11-07T14:59:05
     * adExplain : ویو خوبی دارد
     * lifeTime : 10
     * isMarked : false
     * isEmergency : فوری
     */

    private String id;
    private String userId;
    private String categoryId;
    private String city;
    private String area;
    private String price;
    private String orderType;
    private String advertiserType;
    private String numOfRoom;
    private String trust;
    private String rent;
    private String officialDoc;
    private String adTitle;
    private String adDate;
    private String adExplain;
    private String lifeTime;
    private String isChecked;
    private String isEmergency;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(String categoryId)
    {
        this.categoryId = categoryId;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getArea()
    {
        return area;
    }

    public void setArea(String area)
    {
        this.area = area;
    }

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getOrderType()
    {
        return orderType;
    }

    public void setOrderType(String orderType)
    {
        this.orderType = orderType;
    }

    public String getAdvertiserType()
    {
        return advertiserType;
    }

    public void setAdvertiserType(String advertiserType)
    {
        this.advertiserType = advertiserType;
    }

    public String getNumOfRoom()
    {
        return numOfRoom;
    }

    public void setNumOfRoom(String numOfRoom)
    {
        this.numOfRoom = numOfRoom;
    }

    public String getTrust()
    {
        return trust;
    }

    public void setTrust(String trust)
    {
        this.trust = trust;
    }

    public String getRent()
    {
        return rent;
    }

    public void setRent(String rent)
    {
        this.rent = rent;
    }

    public String getOfficialDoc()
    {
        return officialDoc;
    }

    public void setOfficialDoc(String officialDoc)
    {
        this.officialDoc = officialDoc;
    }

    public String getAdTitle()
    {
        return adTitle;
    }

    public void setAdTitle(String adTitle)
    {
        this.adTitle = adTitle;
    }

    public String getAdDate()
    {
        return adDate;
    }

    public void setAdDate(String adDate)
    {
        this.adDate = adDate;
    }

    public String getAdExplain()
    {
        return adExplain;
    }

    public void setAdExplain(String adExplain)
    {
        this.adExplain = adExplain;
    }

    public String getLifeTime()
    {
        return lifeTime;
    }

    public void setLifeTime(String lifeTime)
    {
        this.lifeTime = lifeTime;
    }

    public Boolean isMarked()
    {
        if (isChecked.equalsIgnoreCase("false"))
            return false;
        else
            return true;
    }

    public void setIsChecked(String isChecked)
    {
        this.isChecked = isChecked;
    }

    public String getIsEmergency()
    {
        return isEmergency;
    }

    public void setIsEmergency(String isEmergency)
    {
        this.isEmergency = isEmergency;
    }
}
