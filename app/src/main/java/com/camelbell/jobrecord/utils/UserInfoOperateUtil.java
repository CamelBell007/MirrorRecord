package com.camelbell.jobrecord.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class UserInfoOperateUtil
{
    /**
     * 
     * 存入离线评论内容
     * @param context
     * @param content 评论内容
     * @param submit 是否点击提交过 0 未点击；1 点击过
     */
    public static void setOffLineComment(Context context,String content,int submit)
    {
        // 实例化SharedPreferences对象
        SharedPreferences mySharedPreferences = context.getSharedPreferences(
                "offline", Activity.MODE_PRIVATE);
        // 实例化SharedPreferences.Editor对象
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        // 用putString的方法保存数据
        editor.putString("commentContent", content);
        editor.putInt("commentSubmit", submit);
        // 提交当前数据
        editor.commit();
    }
    /**
     * 获取离线评论内容
     * @param context
     */
    public static String getOffLineComment(Context context)
    {
        // 实例化SharedPreferences对象
        SharedPreferences mySharedPreferences = context.getSharedPreferences(
                "offline", Activity.MODE_PRIVATE);
        if (mySharedPreferences != null)
        {
            String content = mySharedPreferences.getString("commentContent", "");
            int submit = mySharedPreferences.getInt("commentSubmit",0);
            return submit + "=" + content;
        }
        return null;
    }
    
    /**
     * 清除离线内容
     * @param context
     */
    public static void clearOffLine(Context context){
    	SharedPreferences sharedPreferences = context.getSharedPreferences(
                "offline", Activity.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit(); 
    }
	
    /**
     * 
     * 获取评论用户信息
     * 
     * @author wangchuanjian
     * @param context
     * @return
     */
    public static String getUserInfo(Context context)
    {
        // 读取SharedPreferences数据前要实例化出一个SharedPreferences对象
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "yohoUserInfo", Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        if (sharedPreferences != null)
        {
            String yohoAccount = sharedPreferences.getString("yohoAccount", "");
            String yohoPassword = sharedPreferences.getString("yohoPassword",
                    "");
            return yohoAccount + "=" + yohoPassword;
        }
        return null;
    }

    /**
     * 
     * 存入登录用户信息
     * @author wangchuanjian
     * @param context
     * @param yohoAccount
     * @param yohoPassword
     */
    public static void setUserInfo(Context context,String yohoAccount,String yohoPassword)
    {
        // 实例化SharedPreferences对象
        SharedPreferences mySharedPreferences = context.getSharedPreferences(
                "yohoUserInfo", Activity.MODE_PRIVATE);
        // 实例化SharedPreferences.Editor对象
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        // 用putString的方法保存数据
        editor.putString("yohoAccount", yohoAccount);
        editor.putString("yohoPassword", yohoPassword);
        // 提交当前数据
        editor.commit();

    }
    
    
    /**
     * 
     * 获取第三方用户信息
     * 
     * @author wangchuanjian
     * @param context
     * @return
     */
    public static String getPlatformUserInfo(Context context)
    {
        // 读取SharedPreferences数据前要实例化出一个SharedPreferences对象
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "yohoUserInfo", Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        if (sharedPreferences != null)
        {
            String token = sharedPreferences.getString("token", "");
            String OpenId = sharedPreferences.getString("openId",
                    "");
            String type = sharedPreferences.getString("type",
                    "");
            return token + "=" + OpenId+"#"+type;
        }
        return null;
    }
    
    /**
     * 获取第三方用户头像
     * @param context
     * @return
     */
    public static String getPlatformUserIcon(Context context)
    {
    	// 读取SharedPreferences数据前要实例化出一个SharedPreferences对象
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "yohoUserInfo", Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        if (sharedPreferences != null)
        {
            return sharedPreferences.getString("avater", "");
        }
		return null;
    }

    /**
     * 获取第三方用户昵称
     * @param context
     * @return
     */
    public static String getPlatformUserName(Context context)
    {
    	// 读取SharedPreferences数据前要实例化出一个SharedPreferences对象
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "yohoUserInfo", Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        if (sharedPreferences != null)
        {
            return sharedPreferences.getString("nickname", "");
        }
		return null;
    }
    /**
     * 
     * 存入登录用户信息
     * @author wangchuanjian
     * @param context
     * @param token
     * @param openId
     */
    public static void setPlatformUserInfo(Context context,String token,String openId,String type,String avater,String nickname)
    {
        // 实例化SharedPreferences对象
        SharedPreferences mySharedPreferences = context.getSharedPreferences(
                "yohoUserInfo", Activity.MODE_PRIVATE);
        // 实例化SharedPreferences.Editor对象
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        // 用putString的方法保存数据
        editor.putString("token", token);
        editor.putString("openId", openId);
        editor.putString("type", type);
        editor.putString("avater", avater);
        editor.putString("nickname", nickname);
        // 提交当前数据
        editor.commit();
        

    }
    
    /**
     * 
     * 删掉用户信息
     * @author wangchuanjian
     * @param context
     * @return
     */
    public static void delPlatformUserInfo(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "yohoUserInfo", Activity.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit(); 
    }
    
    
    /**
     * 
     * 存入登录状态
     * @author wangchuanjian
     */
    public static void setLoginState(Context context,int state)
    {
        // 实例化SharedPreferences对象
        SharedPreferences mySharedPreferences = context.getSharedPreferences(
                "state", Activity.MODE_PRIVATE);
        // 实例化SharedPreferences.Editor对象
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        // 用putString的方法保存数据
        editor.putInt("state", state);
        // 提交当前数据
        editor.commit();

    }
    
    /**
     * 
     * 获取登录状态
     * 
     * @author wangchuanjian
     * @return
     */
    public static int getLoginState(Context context)
    {
        // 读取SharedPreferences数据前要实例化出一个SharedPreferences对象
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "state", Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        if (sharedPreferences != null)
        {
            int state = sharedPreferences.getInt("state", 0);
            return state;
        }
        return 0;
    }
    
    /**
     * 
     * 删掉用户信息
     * @author wangchuanjian
     * @param context
     * @return
     */
    public static void delLoginState(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "state", Activity.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit(); 
    }
    
    
    /**
     * 
     * 存入关注用户信息
     * @author wangchuanjian
     * @param context
     * @param platform
     */
    public static void setPlatformFollowInfo(Context context,String platform)
    {
        // 实例化SharedPreferences对象
        SharedPreferences mySharedPreferences = context.getSharedPreferences(
                "followUser", Activity.MODE_PRIVATE);
        // 实例化SharedPreferences.Editor对象
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        // 用putString的方法保存数据
        editor.putString("platform", platform);
        // 提交当前数据
        editor.commit();
        

    }
    
    /**
     * 
     * 获取关注平台信息
     * 
     * @author wangchuanjian
     * @param context
     * @return
     */
    public static String getPlatformFollowInfo(Context context)
    {
        // 读取SharedPreferences数据前要实例化出一个SharedPreferences对象
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "followUser", Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        if (sharedPreferences != null)
        {
            String platform = sharedPreferences.getString("platform", "");
            return platform;
        }
        return null;
    }
}
