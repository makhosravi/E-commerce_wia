package com.od.makh.myapplication.Utils;

public class CheckResponse
{
    public static final int SUCCESS_LOGIN = 1;
    public static final int SUCCESS_REGISTER = 2;
    public static final int SERVER_ERROR = 3;
    public static final int JSON_EXCEPTION = 4;
    public static String getMessage(int state)
    {
        if (state == SUCCESS_LOGIN){
            return "اطلاعات شما همگام سازی شد";
        }
        else if (state == SUCCESS_REGISTER){
            return "اطلاعات شما با موفقیت ثبت شد";
        }
        else if (state == SERVER_ERROR){
            return "خطا در سرور";
        }
        else if (state == JSON_EXCEPTION){
            return "خطا در پردازش داده ها";
        }
        else
            return "خطا ناشناخته";
    }
}
