package com.jcsoft.ecar.client;

public class GetPositionRespProcessor implements IProcessor
{
	private static GetPositionRespProcessor _instance = null;
	
	public boolean init()
	{
		ProcessorManager.instance().registerProcessor(GetPositionRespProcessor.instance());
		return true;
	}
			
	public static GetPositionRespProcessor instance()
	{
		if (_instance == null)
		{
			_instance = new GetPositionRespProcessor();
		}
		return _instance;
	}
	
	@Override
	public int process(JCProtocol protocol, IJCClientListener listener)
	{
		if (protocol.getDataType() != EnumDataType.DATA_GetPositionResp)
		{
			return -1;
		}		
		
		GetPositionResp resp = (GetPositionResp)protocol;
		listener.onGetPosition(resp.getDevId(), resp.getDevInfo());
		return 0;
	}

}
