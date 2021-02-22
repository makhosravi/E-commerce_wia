package com.od.makh.myapplication.Model;

import io.realm.RealmObject;

public class Image extends RealmObject
{
    /**
     * image1 : http://www.servertest.ir/divar_wiadev/uploads/img44_30_222050.png
     * image2 : null
     * iamge3 : null
     * image4 : null
     */

    private String image1;
    private String image2;
    private String image3;
    private String image4;

    public Image()
    {

    }

    public String getImage1()
    {
        return image1;
    }

    public void setImage1(String image1)
    {
        this.image1 = image1;
    }

    public String getImage2()
    {
        return image2;
    }

    public void setImage2(String image2)
    {
        this.image2 = image2;
    }

    public String getImage3()
    {
        return image3;
    }

    public void setImage3(String image3)
    {
        this.image3 = image3;
    }

    public String getImage4()
    {
        return image4;
    }

    public void setImage4(String image4)
    {
        this.image4 = image4;
    }
}
