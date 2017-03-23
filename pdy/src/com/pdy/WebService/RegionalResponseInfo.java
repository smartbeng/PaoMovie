package com.pdy.WebService;

public class RegionalResponseInfo {
	   /// <summary>
    /// RegionalId
    /// </summary>
    public int RegionalId ;
    /// <summary>
    /// Regional操作ID
    /// </summary>
    public int RegionalActionId ;
    /// <summary>
    /// 用户名
    /// </summary>
    public String UserName ;
    /// <summary>
    /// 滴答数
    /// </summary>
    public int TickTack ;
    /// <summary>
    /// 数据发送时间戳
    /// </summary>
    public String IBSentTimeStamp ;
    /// <summary>
    /// 数据产生时间戳
    /// </summary>
    public String IBTimeStamp ;
    /// <summary>
    /// 用于签名时间戳
    /// </summary>
    public String TimeStamp ;
    /// <summary>
    /// 签名
    /// </summary>
    public String Sign ;
    /// <summary>
    /// 是真实的action 还是Information
    /// </summary>
    public Boolean IsTrueAction ;
    /// <summary>
    /// 上班状态
    /// </summary>
    public Boolean IsAtWork ;
    
    public int ActionType;
    
    public String ActionTimeStamp ;
}
