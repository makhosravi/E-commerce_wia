package com.od.makh.myapplication.Model;

import io.realm.RealmObject;

public class Post extends RealmObject
{

    /**
     * id : 12
     * price : 300500000
     * image : {"image1":"http://www.servertest.ir/divar_wiadev/uploads/img44_30_222050.png","image2":null,"iamge3":null,"image4":null}
     * adTitle : test2
     * adDate : 2019-09-06T22:20:50
     * isEmergency : فوری
     */

    private String id;
    private String userId;
    private String price;
    private Image image;
    private String adTitle;
    private String adDate;
    private String isEmergency;

    public Post()
    {

    }

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

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public Image getImage()
    {
        return image;
    }

    public void setImage(Image image)
    {
        this.image = image;
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

    public String getIsEmergency()
    {
        return isEmergency;
    }

    public void setIsEmergency(String isEmergency)
    {
        this.isEmergency = isEmergency;
    }


}
