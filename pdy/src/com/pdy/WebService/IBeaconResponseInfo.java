package com.pdy.WebService;

public class IBeaconResponseInfo {
    /// <summary>
    /// IBeaconID
    /// </summary>
    public int IBeaconId ;
    /// <summary>
    /// IBeacon操作ID
    /// </summary>
    public int IBeaconActionId ;

   
    /// <summary>
    /// 继电器第几路
    /// </summary>
    //public int RelaysNum;
    
    /// <summary>
    /// 用户名
    /// </summary>
    public String UserName ;
    /// <summary>
    /// 功率
    /// </summary>
    public int Power ;
    /// <summary>
    /// 电池电量
    /// </summary>
    public int BatteryLevel ;
    /// <summary>
    /// 当前距离
    /// </summary>
    public int CurrentDistance ;
    /// <summary>
    /// IBeacon滴答数
    /// </summary>
    public int TickTack ;
    /// <summary>
    /// IBeacon数据发送时间戳
    /// </summary>
    public String IBSentTimeStamp ;
    /// <summary>
    /// IBeacon数据产生时间戳
    /// </summary>
    public String IBTimeStamp ;
    /// <summary>
    /// IBeanconUUID
    /// </summary>
    public String UUID ;
    /// <summary>
    /// 用于签名时间戳
    /// </summary>
    public String TimeStamp ;
    /// <summary>
    /// 签名
    /// </summary>
    public String Sign ;
    /// <summary>
    /// 是否需要用户信息
    /// </summary>
    public Boolean NeedUser ;
    /// <summary>
    /// 是真实的action 还是Information
    /// </summary>
    public Boolean IsTrueAction ;
    /// <summary>
    /// 上班状态
    /// </summary>
    public Boolean IsAtWork ;
    /// <summary>
    /// 响应描述
    /// </summary>
    public String RecordRemark ;

}
