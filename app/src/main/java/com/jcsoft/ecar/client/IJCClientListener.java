package com.jcsoft.ecar.client;

import com.jcsoft.ecar.bean.CarLockStatus;
import com.jcsoft.ecar.bean.DevInfo;
import com.jcsoft.ecar.bean.DevTrackInfo;
import com.jcsoft.ecar.bean.JCShortMessageInfo;
import com.jcsoft.ecar.bean.VFStatus;

import java.util.List;

public interface IJCClientListener 
{
	void onLoginResult(int result);
	
	void onGetAllDevInfos(List<DevInfo> infos);
	
	void onGetPosition(int devId, DevInfo info);
	
	void onGetTrack(DevTrackInfo info);
	
	void onOpenVF(int devId, int result);
	
	void onCloseVF(int devId, int result);
	
	void onLockCar(int devId, int result);
	
	void onUnlockCar(int devId, int result);
	
	void onGetSysParam(String key, String value);
	
	void onReceivedMessage(JCShortMessageInfo message);

	void onConnectFailed();

	void onConnected();

	void onRealLocate(int i);
	
	void onVFStatusChanged(VFStatus status);
	
	void onCarLockStatusChanged(CarLockStatus status);

	void onModifyPassword(int result);
	
	void onKicked(int reason);
}
