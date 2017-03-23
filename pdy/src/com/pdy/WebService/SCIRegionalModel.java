package com.pdy.WebService;

public class SCIRegionalModel {
	public int RegionalId;
    /// <summary>
    /// 范围X坐标
    /// </summary>
    public double RegionalX;
    /// <summary>
    /// 范围Y坐标
    /// </summary>
    public double RegionalY;
    /// <summary>
    /// 范围半径
    /// </summary>
    public double RegionalR;
    /// <summary>
    /// 1为进入范围，0为离开范围
    /// </summary>
    public int ActionType;
    /// <summary>
    /// ActionID
    /// </summary>
    public int RegionalActionId;
    public String RegionalActionName;
    public String RegionalActionContent;
    /// <summary>
    /// 是否需要询问
    /// </summary>
    public boolean NeedAskUser;

    
    public int InCount;
    
    public int OutCount;
}
