package com.pdy.WebService;

public class SCIBeaconsModel {
	   /// <summary>
    /// IbeaconID
    /// </summary>
    public int IBeaconId;
    /// <summary>
    /// UUID
    /// </summary>
    public String UUID;
    /// <summary>
    /// 大分类
    /// </summary>
    public String Major;
    /// <summary>
    /// 小分类
    /// </summary>
    public String Minor;
    /// <summary>
    /// 动作名称
    /// </summary>
    public String ActionName;
    /// <summary>
    /// 操作类型ID
    /// </summary>
    public int ActionId;
    /// <summary>
    /// 真实操作ID
    /// </summary>
    public int TrueActionId;
    /// <summary>
    /// 继电器第几路
    /// </summary>
    //public int RelaysNum;
    
    
    /// <summary>
    /// 附加触发条件
    /// </summary>
    public int AdditionCondition;
    /// <summary>
    /// 触发距离
    /// </summary>
    public int Distance;
    /// <summary>
    /// 忽略时间
    /// 如：第一次经过该Ibeacon30分钟内有效，30分中内再次经过的时候不会触发
    /// </summary>
    public int IgnoreTime;

    /// <summary>
    /// 手机移动状态
    /// </summary>
    public int[] MobileStatusIds;
    /// <summary>
    /// 进入时离Ibeacon的距离倍数
    /// </summary>
    public int EnterMultiple;
    
	public long lastTime;
	public String BluetoothName;
	public String ServerUUID;
	public String CharUUID;
	public String OpenType;
	public int actionTypeId;
	public int enterMultiple;
	public int iBeaconId;
	public int lbsRegionalId;
    
}
