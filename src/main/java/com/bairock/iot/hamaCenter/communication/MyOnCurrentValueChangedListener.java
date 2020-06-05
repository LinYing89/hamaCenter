package com.bairock.iot.hamaCenter.communication;

import com.bairock.iot.hamalib.device.CtrlModel;
import com.bairock.iot.hamalib.device.Device;
import com.bairock.iot.hamalib.device.devcollect.CollectProperty;
import com.bairock.iot.hamalib.device.devcollect.DevCollect;
import com.bairock.iot.hamalib.order.DeviceOrder;
import com.bairock.iot.hamalib.order.OrderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bairock.iot.hamaCenter.utils.Util;
import com.bairock.iot.hamaCenter.data.webData.WebDevValue;
import com.bairock.iot.hamaCenter.service.DeviceBroadcastService;

@Component
public class MyOnCurrentValueChangedListener implements CollectProperty.OnCurrentValueChangedListener {

	@Autowired
	private DeviceBroadcastService deviceService;

	@Override
	public void onCurrentValueChanged(DevCollect dev, Float value) {

		Device superParent = dev.findSuperParent();
		if (dev instanceof DevCollect) {
			// 通知网页
			WebDevValue webDevValue = new WebDevValue(dev.getLongCoding(), dev.getCollectProperty().createFormatValue());
			deviceService.broadcastValueChanged(superParent.getUsername(), superParent.getDevGroupName(), webDevValue);

			// 远程设备才发往本地, 本地设备状态在服务器收到之前已经到位了
			if (superParent.getCtrlModel() == CtrlModel.REMOTE) {
				// 发送到pad
				DeviceOrder devOrder = new DeviceOrder(OrderType.VALUE, dev.getId(), dev.getLongCoding(),
						String.valueOf(value));
				String strOrder = Util.orderBaseToString(devOrder);
				PadChannelBridgeHelper.getIns().sendOrderSynable(superParent.getUsername(),
						superParent.getDevGroupName(), strOrder);
			}
		}
	}

}
